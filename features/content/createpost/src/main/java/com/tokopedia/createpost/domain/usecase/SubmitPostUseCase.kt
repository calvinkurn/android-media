package com.tokopedia.createpost.domain.usecase

import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.affiliatecommon.data.pojo.submitpost.request.ContentSubmitInput
import com.tokopedia.affiliatecommon.data.pojo.submitpost.request.MediaTag
import com.tokopedia.affiliatecommon.data.pojo.submitpost.request.SubmitPostMedium
import com.tokopedia.affiliatecommon.data.pojo.submitpost.response.SubmitPostData
import com.tokopedia.createpost.TYPE_CONTENT_SHOP
import com.tokopedia.createpost.createpost.R
import com.tokopedia.createpost.di.ActivityContext
import com.tokopedia.createpost.view.util.SubmitPostNotificationManager
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import rx.functions.Func1
import java.util.*
import javax.inject.Inject

/**
 * @author by milhamj on 10/1/18.
 */
open class SubmitPostUseCase @Inject constructor(
        @ActivityContext private val context: Context,
        private val uploadMultipleImageUseCase: UploadMultipleImageUseCase,
        private val graphqlUseCase: GraphqlUseCase) : UseCase<SubmitPostData>() {

    var notificationManager: SubmitPostNotificationManager? = null

    @Suppress("UNCHECKED_CAST")
    override fun createObservable(requestParams: RequestParams): Observable<SubmitPostData> {
        val relatedIdList = requestParams.getObject(PARAM_TAGS) as List<String>
        val type = requestParams.getString(PARAM_TYPE, "")
        val tags = getListOfTag(relatedIdList, type)

        uploadMultipleImageUseCase.notificationManager = notificationManager

        val media = (requestParams.getObject(PARAM_MEDIA_LIST) as List<Pair<String, String>>?
                ?: emptyList())

        return uploadMultipleImageUseCase
                .createObservable(
                        UploadMultipleImageUseCase.createRequestParams(getMediumList(media, tags))
                )
                .map(rearrangeMedia())
                .flatMap(submitPostToGraphql(requestParams))
    }

    private fun getMediumList(media: List<Pair<String, String>>, tags: List<MediaTag>): List<SubmitPostMedium> {
        val mediumList = mutableListOf<SubmitPostMedium>()
        media.forEachIndexed { index, pair ->
            mediumList.add(SubmitPostMedium(pair.first, index, addProductTagsToFirstIndex(index, tags), pair.second))
        }
        return mediumList
    }

    private fun addProductTagsToFirstIndex(index: Int, tags: List<MediaTag>): List<MediaTag> {
        return if (index == 0) tags else arrayListOf()
    }

    private fun getListOfTag(relatedIdList: List<String>, type: String): List<MediaTag> {
        val tags = arrayListOf<MediaTag>()
        relatedIdList.forEach {
            tags.add(MediaTag(getTagType(type), it))
        }
        return tags
    }

    private fun getTagType(type: String): String {
        return if (type == TYPE_CONTENT_SHOP) TAGS_TYPE_PRODUCT else type
    }

    private fun rearrangeMedia(): Func1<List<SubmitPostMedium>, List<SubmitPostMedium>> {
        return Func1 {
            val rearrangedList: MutableList<SubmitPostMedium> = ArrayList(it)
            it.forEach { media ->
                rearrangedList[media.order] = media
            }
            rearrangedList
        }
    }

    private fun submitPostToGraphql(requestParams: RequestParams): Func1<List<SubmitPostMedium>, Observable<SubmitPostData>> {
        return Func1 { mediumList ->
            notificationManager?.onSubmitPost()

            val query = GraphqlHelper.loadRawString(
                    context.resources,
                    R.raw.mutation_af_submit_post
            )

            val variables = HashMap<String, Any>()
            variables[PARAM_INPUT] = getContentSubmitInput(requestParams, mediumList)

            val graphqlRequest = GraphqlRequest(
                    query,
                    SubmitPostData::class.java,
                    variables)

            graphqlUseCase.clearRequest()
            graphqlUseCase.addRequest(graphqlRequest)
            graphqlUseCase
                    .createObservable(RequestParams.create())
                    .map(mapGraphqlResponse())
        }
    }

    private fun mapGraphqlResponse(): Func1<GraphqlResponse, SubmitPostData> {
        return Func1 { graphqlResponse -> graphqlResponse.getData(SubmitPostData::class.java) }
    }

    private fun getInputType(type: String): String {
        return if (type == TYPE_CONTENT_SHOP) INPUT_TYPE_CONTENT else type
    }

    protected open fun getContentSubmitInput(requestParams: RequestParams,
                                             mediumList: List<SubmitPostMedium>): ContentSubmitInput {
        val input = ContentSubmitInput(
                action = requestParams.getString(PARAM_ACTION, null)
        )
        input.type = getInputType(requestParams.getString(PARAM_TYPE, ""))
        input.token = requestParams.getString(PARAM_TOKEN, "")
        input.authorID = requestParams.getString(PARAM_AUTHOR_ID, "")
        input.authorType = requestParams.getString(PARAM_AUTHOR_TYPE, "")
        input.caption = requestParams.getString(PARAM_CAPTION, "")
        input.media = mediumList
        input.activityId = requestParams.getString(PARAM_ID, null)

        return input
    }

    companion object {
        private const val PARAM_TYPE = "type"
        private const val PARAM_TOKEN = "token"
        private const val PARAM_AUTHOR_ID = "authorID"
        private const val PARAM_AUTHOR_TYPE = "authorType"
        private const val PARAM_CAPTION = "caption"
        private const val PARAM_TAGS = "tags"
        private const val PARAM_ACTION = "action"
        private const val PARAM_ID = "ID"

        private const val PARAM_INPUT = "input"

        private const val INPUT_TYPE_CONTENT = "content"
        private const val TAGS_TYPE_PRODUCT = "product"


        const val SUCCESS = 1

        private const val PARAM_MEDIA_LIST = "media_list"
        private const val ACTION_UPDATE = "update"

        fun createRequestParams(
                id: String?,
                type: String,
                token: String,
                authorId: String,
                caption: String,
                media: List<Pair<String, String>>,
                relatedIdList: List<String>
        ): RequestParams {

            val requestParams = RequestParams.create()
            requestParams.putString(PARAM_TYPE, type)
            requestParams.putString(PARAM_TOKEN, token)
            requestParams.putString(PARAM_AUTHOR_ID, authorId)
            requestParams.putString(PARAM_AUTHOR_TYPE, type)
            requestParams.putString(PARAM_CAPTION, caption)
            requestParams.putObject(PARAM_TAGS, relatedIdList)

            if (!id.isNullOrEmpty()) {
                //this is edit post
                requestParams.putString(PARAM_ID, id)
                requestParams.putString(PARAM_ACTION, ACTION_UPDATE)
            } else {
                requestParams.putObject(PARAM_MEDIA_LIST, media)
            }
            return requestParams
        }
    }
}
