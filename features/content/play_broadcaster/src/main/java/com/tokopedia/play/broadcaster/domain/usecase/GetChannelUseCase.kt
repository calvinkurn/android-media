package com.tokopedia.play.broadcaster.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.play.broadcaster.domain.model.GetChannelResponse
import com.tokopedia.play.broadcaster.util.error.DefaultErrorThrowable
import javax.inject.Inject


/**
 * Created by mzennis on 26/06/20.
 */
class GetChannelUseCase @Inject constructor(
        private val graphqlRepository: GraphqlRepository
) : BaseUseCase<GetChannelResponse.Channel>() {

    //TODO("Add coverURL field in basic")
    private val query = """
            query GetChannel(${'$'}channelId: String){
              broadcasterGetChannels(
                req : {
                  filter: {
                    channelIDs: [${'$'}channelId]
                  }
                }
              ) {
                channels {
                  basic {
                    channelID
                    title
                    slug
                    description
                    activeMediaID
                    startTime
                    endTime
                    enableChat
                    status {
                      ID
                      text
                    }
                  },
                  author {
                    ID
                    name
                    thumbnailURL
                    badge
                  },
                  medias {
                    ID
                    channelID
                    coverURL
                    source
                    ingestURL
                    livestreamID
                  },
                  productTags {
                    ID
                    channelID
                    productID
                    weight
                    productName
                    description
                    originalPriceFmt
                    originalPrice
                    priceFmt
                    price
                    discount
                    quantity
                    isVariant
                    isAvailable
                    order
                    applink
                    weblink
                    minQuantity
                    isFreeShipping
                  },
                  pinMessages {
                    ID
                    channelID
                    message
                    appLink
                    webLink
                    imageURL
                    weight
                  },
                  quickReplies {
                    ID
                    channelID
                    message
                    weight
                  },
                  publicVouchers {
                    ID
                    ShopID
                    Name
                    Title
                    Subtitle
                    Type
                    Image
                    ImageSquare
                    Quota
                    FinishTime
                    Code
                    QuotaAvailable
                    UsedQuota
                    TnC
                  }
                }
              }
            }
        """

    var params: Map<String, Any> = emptyMap()

    override suspend fun executeOnBackground(): GetChannelResponse.Channel {
        val gqlResponse = configureGqlResponse(graphqlRepository, query, GetChannelResponse::class.java, params, GraphqlCacheStrategy
                .Builder(CacheType.ALWAYS_CLOUD).build())
        val response = gqlResponse.getData<GetChannelResponse>(GetChannelResponse::class.java)
        try { response?.broadcasterGetChannels?.channels?.let { return it.first() } }
        catch (e: Exception) { }
        throw DefaultErrorThrowable()
    }

    companion object {

        private const val PARAM_CHANNEL_ID = "channelId"

        fun createParams(channelId: String): Map<String, Any> = mapOf(
                PARAM_CHANNEL_ID to channelId
        )
    }

}