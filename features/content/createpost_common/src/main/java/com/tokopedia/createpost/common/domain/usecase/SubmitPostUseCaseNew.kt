package com.tokopedia.createpost.common.domain.usecase

import android.util.Log
import com.tokopedia.affiliatecommon.data.pojo.submitpost.request.MediaTag
import com.tokopedia.affiliatecommon.data.pojo.submitpost.request.SubmitPostMedium
import com.tokopedia.affiliatecommon.data.pojo.submitpost.response.SubmitPostData
import com.tokopedia.createpost.common.TYPE_CONTENT_SHOP
import com.tokopedia.createpost.common.view.util.PostUpdateProgressManager
import com.tokopedia.createpost.common.view.viewmodel.MediaModel
import com.tokopedia.createpost.common.view.viewmodel.RelatedProductItem
import com.tokopedia.createpost.common.data.feedrevamp.FeedXMediaTagging
import com.tokopedia.createpost.common.domain.entity.SubmitPostResult
import com.tokopedia.createpost.common.domain.entity.UploadMediaDataModel
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import kotlinx.coroutines.flow.*
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

/**
 * Use this usecase after migrating both image & video uploader
 */
@GqlQuery(SubmitPostUseCaseNew.QUERY_NAME, SubmitPostUseCaseNew.QUERY)
open class SubmitPostUseCaseNew @Inject constructor(
    private val uploadMultipleMediaUseCase: UploadMultipleMediaUseCaseNew,
    graphqlRepository: GraphqlRepository,
) : GraphqlUseCase<SubmitPostData>(graphqlRepository) {

    var postUpdateProgressManager: PostUpdateProgressManager? = null

    init {
        setGraphqlQuery(SubmitPostUseCaseNewQuery())
        setCacheStrategy(
            GraphqlCacheStrategy
                .Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(SubmitPostData::class.java)
    }

    private val _state = MutableStateFlow<SubmitPostResult>(SubmitPostResult.Unknown)
    val state: Flow<SubmitPostResult>
        get() = _state

    suspend fun execute(
        id: String?,
        type: String,
        token: String,
        authorId: String,
        caption: String,
        media: List<Pair<String, String>>,
        relatedIdList: List<String>,
        mediaList: List<MediaModel>,
        mediaWidth: Int,
        mediaHeight: Int
    ) {
        uploadMultipleMediaUseCase.postUpdateProgressManager = postUpdateProgressManager

        val mediumList = getMediumList(media, mediaList)

        uploadMultipleMediaUseCase.execute(mediumList)

        uploadMultipleMediaUseCase.state.collectLatest {
            Log.d("<LOG>", "state changed : $it")
            if(it.images is UploadMediaDataModel.Media.Success && it.videos is UploadMediaDataModel.Media.Success) {
                val newMedia = it.images.mediumList + it.videos.mediumList

                if(mediumList.size == newMedia.size) {
                    Log.d("<LOG>", "rearrangeMedia")
                    /** Rearrange Media */
                    val arrangedMedia = rearrangeMedia(newMedia)

                    /** Submit Post */
                    postUpdateProgressManager?.onSubmitPost()

                    setRequestParams(
                        mapOf(
                            PARAM_INPUT to mapOf(
                                PARAM_ACTION to if(id.isNullOrEmpty()) ACTION_CREATE else ACTION_UPDATE,
                                PARAM_ID to if(id.isNullOrEmpty()) null else id,
                                PARAM_AD_ID to null,
                                PARAM_TYPE to getInputType(type),
                                PARAM_TOKEN to token,
                                PARAM_AUTHOR_ID to authorId,
                                PARAM_AUTHOR_TYPE to type,
                                PARAM_CAPTION to caption,
                                PARAM_MEDIA_WIDTH to mediaWidth,
                                PARAM_MEDIA_HEIGHT to mediaHeight,
                                PARAM_MEDIA to arrangedMedia,
                            )
                        )
                    )

                    val result = super.executeOnBackground()

                    Log.d("<LOG>", "submitPost : $result")
                    _state.update { SubmitPostResult.Success(result) }
                }
            }
        }
    }

    suspend fun executeOnBackground(
        id: String?,
        type: String,
        token: String,
        authorId: String,
        caption: String,
        media: List<Pair<String, String>>,
        relatedIdList: List<String>,
        mediaList: List<MediaModel>,
        mediaWidth: Int,
        mediaHeight: Int
    ): SubmitPostData {
        uploadMultipleMediaUseCase.postUpdateProgressManager = postUpdateProgressManager

        /** Upload All Media */
        val newMediumList = uploadMultipleMediaUseCase.executeOnBackground(getMediumList(media, mediaList))

        /** Rearrange Media */
        val arrangedMedia = rearrangeMedia(newMediumList)

        /** Submit Post */
        postUpdateProgressManager?.onSubmitPost()

        setRequestParams(
            mapOf(
                PARAM_INPUT to mapOf(
                    PARAM_ACTION to if(id.isNullOrEmpty()) ACTION_CREATE else ACTION_UPDATE,
                    PARAM_ID to if(id.isNullOrEmpty()) null else id,
                    PARAM_AD_ID to null,
                    PARAM_TYPE to getInputType(type),
                    PARAM_TOKEN to token,
                    PARAM_AUTHOR_ID to authorId,
                    PARAM_AUTHOR_TYPE to type,
                    PARAM_CAPTION to caption,
                    PARAM_MEDIA_WIDTH to mediaWidth,
                    PARAM_MEDIA_HEIGHT to mediaHeight,
                    PARAM_MEDIA to arrangedMedia,
                )
            )
        )

        return super.executeOnBackground()
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

    private fun rearrangeMedia(mediaList: List<SubmitPostMedium>): List<SubmitPostMedium> {
        val rearrangedList: MutableList<SubmitPostMedium> = ArrayList(mediaList)
        mediaList.forEach { media ->
            rearrangedList[media.order] = media
        }
        return rearrangedList
    }

    private fun getInputType(type: String) =
        if (type == TYPE_CONTENT_SHOP) INPUT_TYPE_CONTENT else type


    companion object {
        private const val PARAM_TYPE = "type"
        private const val PARAM_TOKEN = "token"
        private const val PARAM_AUTHOR_ID = "authorID"
        private const val PARAM_AUTHOR_TYPE = "authorType"
        private const val PARAM_CAPTION = "caption"
        private const val PARAM_ACTION = "action"
        private const val PARAM_ID = "ID"
        private const val PARAM_AD_ID = "adID"
        private const val PARAM_MEDIA = "media"
        private const val ACTION_CREATE = "create"
        private const val PARAM_MEDIA_WIDTH = "mediaRatioW"
        private const val PARAM_MEDIA_HEIGHT = "mediaRatioH"
        private const val PARAM_INPUT = "input"

        private const val INPUT_TYPE_CONTENT = "content"
        private const val TAGS_TYPE_PRODUCT = "product"

        private const val ACTION_UPDATE = "update"


        const val QUERY_NAME = "SubmitPostUseCaseNewQuery"
        const val QUERY = """          
            mutation SubmitPost(${'$'}input:ContentSubmitInput!) {
              feed_content_submit(
                Input:${'$'}input
              ){
                success
                redirectURI
                error
                meta {
                  followers
                  content {
                    activityID
                    title
                    description
                    url
                    instagram {
                      backgroundURL
                      profileURL
                    }
                  }
                }
              }
            }
        """
    }
}
