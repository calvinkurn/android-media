package com.tokopedia.creation.common.upload.domain.usecase.post

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.createpost.common.domain.entity.SubmitPostData
import com.tokopedia.createpost.common.domain.entity.SubmitPostResult
import com.tokopedia.createpost.common.domain.entity.request.MediaTag
import com.tokopedia.createpost.common.domain.entity.request.SubmitPostRequest
import com.tokopedia.createpost.common.domain.usecase.UploadMultipleMediaUseCase
import com.tokopedia.creation.common.upload.model.CreationUploadData
import com.tokopedia.creation.common.upload.util.getFileAbsolutePath
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import kotlinx.coroutines.flow.*
import javax.inject.Inject

/**
 * Revamped By : Jonathan Darwin on October 13, 2022
 */
@GqlQuery(SubmitPostUseCase.QUERY_NAME, SubmitPostUseCase.QUERY)
open class SubmitPostUseCase @Inject constructor(
    private val uploadMultipleMediaUseCase: UploadMultipleMediaUseCase,
    @ApplicationContext graphqlRepository: GraphqlRepository,
    private val deleteMediaPostCacheUseCase: DeleteMediaPostCacheUseCase,
) : GraphqlUseCase<SubmitPostData>(graphqlRepository) {

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
        data: CreationUploadData.Post,
        onSuccessUploadPerMedia: suspend () -> Unit,
    ): SubmitPostData {

        val uploadedMedia = if (data.creationId.isEmpty()) {
            /** Map Media Data to Request */
            val mediaList = mapMediaToRequest(data.mediaList)

            /** Upload Media to Uploadpedia */
            val response = uploadMultipleMediaUseCase.execute(mediaList, onSuccessUploadPerMedia)

            /** Rearrange Media Order */
            rearrangeMedia(response)
        } else {
            emptyList()
        }

        /** Submit Post */
        setRequestParams(
            mapOf(
                PARAM_INPUT to mapOf(
                    PARAM_ACTION to if (data.creationId.isEmpty()) ACTION_CREATE else ACTION_UPDATE,
                    PARAM_ID to data.creationId.ifEmpty { null },
                    PARAM_AD_ID to null,
                    PARAM_TYPE to INPUT_TYPE_CONTENT,
                    PARAM_TOKEN to data.token,
                    PARAM_AUTHOR_ID to data.authorId,
                    PARAM_AUTHOR_TYPE to data.authorType,
                    PARAM_CAPTION to data.caption,
                    PARAM_MEDIA_WIDTH to data.mediaWidth,
                    PARAM_MEDIA_HEIGHT to data.mediaHeight,
                    PARAM_MEDIA to uploadedMedia,
                )
            )
        )

        val result = super.executeOnBackground()

        if (result.feedContentSubmit.success != SubmitPostData.SUCCESS) {
            throw Exception(result.feedContentSubmit.error)
        }

        /** Delete Media Cache */
        deleteMediaPostCacheUseCase(data.mediaList.map { it.path }.toSet())

        return result
    }

    private fun mapMediaToRequest(mediaList: List<CreationUploadData.Post.Media>): List<SubmitPostRequest> {
        return mediaList.mapIndexed { index, media ->
            SubmitPostRequest(
                order = index,
                mediaURL = getFileAbsolutePath(media.path).orEmpty(),
                type = media.type,
                tags = media.productIds.map { productId ->
                    MediaTag(
                        type = TAGS_TYPE_PRODUCT,
                        content = productId,
                        position = listOf(0.0, 0.0)
                    )
                },
            )
        }
    }

    private fun rearrangeMedia(mediaList: List<SubmitPostRequest>): List<SubmitPostRequest> {
        return mediaList.sortedBy { it.order }
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
