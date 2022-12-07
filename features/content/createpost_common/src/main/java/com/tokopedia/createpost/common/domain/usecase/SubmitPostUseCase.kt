package com.tokopedia.createpost.common.domain.usecase

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
import javax.inject.Inject
import kotlin.collections.ArrayList

/**
 * Revamped By : Jonathan Darwin on October 13, 2022
 */
@GqlQuery(SubmitPostUseCase.QUERY_NAME, SubmitPostUseCase.QUERY)
open class SubmitPostUseCase @Inject constructor(
    private val uploadMultipleMediaUseCase: UploadMultipleMediaUseCase,
    graphqlRepository: GraphqlRepository,
) : GraphqlUseCase<SubmitPostData>(graphqlRepository) {

    var postUpdateProgressManager: PostUpdateProgressManager? = null

    init {
        setGraphqlQuery(SubmitPostUseCaseQuery())
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

        uploadMultipleMediaUseCase.state.collectLatest { state ->
            if(state.images is UploadMediaDataModel.Media.Success && state.videos is UploadMediaDataModel.Media.Success) {
                val newMedia = state.images.mediumList + state.videos.mediumList

                /** If all media is alr processed */
                if(mediumList.size == newMedia.size) {
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
                                PARAM_TYPE to INPUT_TYPE_CONTENT,
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

                    _state.update { SubmitPostResult.Success(result) }
                }
            }
            else if (state.images is UploadMediaDataModel.Media.Fail) {
                _state.update { SubmitPostResult.Fail(state.images.throwable) }
            }
            else if (state.videos is UploadMediaDataModel.Media.Fail) {
                _state.update { SubmitPostResult.Fail(state.videos.throwable) }
            }
        }
    }

    /**
     * The code below will be used when we have migrated
     * both image & video uploader to uploadpedia
     */
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
                    PARAM_TYPE to INPUT_TYPE_CONTENT,
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
            val tags = mapTagList(mediaList[index].tags, mediaList[index].products)
            mediumList.add(SubmitPostMedium(pair.first, index, tags, pair.second))
        }
        return mediumList
    }

    private fun mapTagList(tags: List<FeedXMediaTagging>, productItem: List<RelatedProductItem>): List<MediaTag> {
        val tagList : MutableList<MediaTag> = arrayListOf()

        tags.forEach {
            val position: MutableList<Double> = arrayListOf()
            position.add(it.posX.toDouble())
            position.add(it.posY.toDouble())

            val id = productItem.getOrNull(it.tagIndex)?.id
            if(id != null) {
                val tag = MediaTag(
                    type = TAGS_TYPE_PRODUCT,
                    content = id,
                    position = position
                )
                tagList.add(tag)
            }
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


        const val QUERY_NAME = "SubmitPostUseCaseQuery"
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
