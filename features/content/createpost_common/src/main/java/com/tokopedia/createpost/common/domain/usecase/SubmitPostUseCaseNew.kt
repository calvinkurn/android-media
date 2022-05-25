package com.tokopedia.createpost.common.domain.usecase

import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.affiliatecommon.data.pojo.submitpost.request.ContentSubmitInput
import com.tokopedia.affiliatecommon.data.pojo.submitpost.request.MediaTag
import com.tokopedia.affiliatecommon.data.pojo.submitpost.request.SubmitPostMedium
import com.tokopedia.affiliatecommon.data.pojo.submitpost.response.SubmitPostData
import com.tokopedia.createpost.common.TYPE_CONTENT
import com.tokopedia.createpost.common.TYPE_CONTENT_SHOP
import com.tokopedia.createpost.common.di.ActivityContext
import com.tokopedia.createpost.common.view.util.PostUpdateProgressManager
import com.tokopedia.createpost.common.view.viewmodel.MediaModel
import com.tokopedia.createpost.common.view.viewmodel.RelatedProductItem
import com.tokopedia.createpost.common.data.feedrevamp.FeedXMediaTagging
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import rx.functions.Func1
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

open class SubmitPostUseCaseNew @Inject constructor(
    @ActivityContext private val context: Context,
    private val uploadMultipleImageUseCase: UploadMultipleImageUsecaseNew,
    private val graphqlUseCase: GraphqlUseCase) : UseCase<SubmitPostData>() {

    var postUpdateProgressManager: PostUpdateProgressManager? = null

    @Suppress("UNCHECKED_CAST")
    override fun createObservable(requestParams: RequestParams): Observable<SubmitPostData> {

        uploadMultipleImageUseCase.postUpdateProgressManager = postUpdateProgressManager
        val media = (requestParams.getObject(PARAM_MEDIA_LIST) as List<Pair<String, String>>?
            ?: emptyList())
        val mediaList = (requestParams.getObject(PARAM_MEDIA_MODEL_LIST) as List<MediaModel>?
            ?: emptyList())

        return uploadMultipleImageUseCase
            .createObservable(
                UploadMultipleImageUsecaseNew.createRequestParams(getMediumList(media, mediaList))
            )
            .map(rearrangeMedia())
            .flatMap(submitPostToGraphql(requestParams))
    }

    private fun getMediumList(media: List<Pair<String, String>>, mediaList: List<MediaModel>): List<SubmitPostMedium> {
        val mediumList = mutableListOf<SubmitPostMedium>()
        media.forEachIndexed { index, pair ->
            val tags = mapTagLIst(index, mediaList[index].tags, mediaList[index].products)
            mediumList.add(SubmitPostMedium(pair.first, index, tags, pair.second))
        }
        return mediumList
    }
    private fun mapTagLIst(index: Int, tags: List<FeedXMediaTagging>, productItem: List<RelatedProductItem>): List<MediaTag> {
        var tagList : MutableList<MediaTag> = arrayListOf()
        tags.forEach {
            val position: MutableList<Double> = arrayListOf()
            position.add(it.posX.toDouble())
            position.add(it.posY.toDouble())
            var tag = MediaTag(type = TAGS_TYPE_PRODUCT,
                content = productItem[it.tagIndex].id,
                position = position)
            tagList.add(tag)
        }
        return tagList
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
               postUpdateProgressManager?.onSubmitPost()

            val query = GraphqlHelper.loadRawString(
                context.resources,
                com.tokopedia.affiliatecommon.R.raw.mutation_af_submit_post
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
        input.mediaRatioW = requestParams.getInt(PARAM_MEDIA_WIDTH, 0)
        input.mediaRatioH = requestParams.getInt(PARAM_MEDIA_HEIGHT, 0)
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
        private const val PARAM_MEDIA_MODEL_LIST = "media_model_list"
        private const val ACTION_CREATE = "create"
        private const val CONTENT_SHOP = "content-shop"
        private const val AUTHOR_TYPE_VALUE = "content"
        private const val PARAM_MEDIA = "media"
        private const val PARAM_MEDIA_WIDTH = "media_width"
        private const val PARAM_MEDIA_HEIGHT = "media_height"



        private const val PARAM_INPUT = "input"

        private const val INPUT_TYPE_CONTENT = "content"
        private const val TAGS_TYPE_PRODUCT = "product"


        const val SUCCESS = 1

        private const val PARAM_MEDIA_LIST = "media_list"
        private const val PARAM_MEDIA_BYTE = "media_byte"
        private const val ACTION_UPDATE = "update"
        private const val PARAM_MEDIA_MODEL_TAGS = "media_tags"
        private const val PARAM_MEDIA_MODEL_PRODUCTS = "media_products"

        fun createRequestParams(
            id: String?,
            type: String,
            token: String,
            authorId: String,
            caption: String,
            media: List<Pair<String, String>>,
            relatedIdList: List<String>,
            mediaList: List<MediaModel>,
            mediaWidth:Int,
            mediaHeight:Int
        ): RequestParams {

            val requestParams = RequestParams.create()
            requestParams.putString(PARAM_TYPE, TYPE_CONTENT)
            requestParams.putString(PARAM_TOKEN, token)
            requestParams.putString(PARAM_AUTHOR_ID, authorId)
            requestParams.putString(PARAM_AUTHOR_TYPE, CONTENT_SHOP)
            requestParams.putString(PARAM_CAPTION, caption)
            requestParams.putObject(PARAM_MEDIA_MODEL_LIST, mediaList)
            requestParams.putInt(PARAM_MEDIA_WIDTH, mediaWidth)
            requestParams.putInt(PARAM_MEDIA_HEIGHT, mediaHeight)

            if (!id.isNullOrEmpty()) {
                requestParams.putString(PARAM_ID, id)
                requestParams.putString(PARAM_ACTION, ACTION_UPDATE)
            } else {
                requestParams.putString(PARAM_ACTION, ACTION_CREATE)
                requestParams.putObject(PARAM_MEDIA_LIST, media)
            }
            return requestParams
        }
    }
}
