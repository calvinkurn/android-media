package com.tokopedia.play.domain

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.play.data.ProductTagging
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

/**
 * Created by mzennis on 2020-03-06.
 */
class GetProductTagItemsUseCase @Inject constructor(private val graphqlRepository: GraphqlRepository) :
        UseCase<ProductTagging>() {

    var params: HashMap<String, Any> = HashMap()

    override suspend fun executeOnBackground(): ProductTagging {
        require(params.isNotEmpty()) { "please input the channel id" }

        val gqlRequest = GraphqlRequest(query, ProductTagging.Response::class.java, params)
        val gqlResponse = graphqlRepository.getReseponse(listOf(gqlRequest), GraphqlCacheStrategy
                .Builder(CacheType.ALWAYS_CLOUD).build())
        val response = gqlResponse.getData<ProductTagging.Response>(ProductTagging.Response::class.java)
        if (response?.playGetTagsItem != null) {
            return response.playGetTagsItem
        } else {
            throw MessageErrorException("server error")
        }
    }

    companion object {

        private const val REQ = "playTagsItemReq"
        private const val CHANNEL_ID = "channelID"

        private val query = getQuery()

        private fun getQuery() : String {
            val playTagsItemReq = "\$playTagsItemReq"

            return """
           query($playTagsItemReq: PlayGetTagsItemReq!){
              playGetTagsItem(req: $playTagsItemReq){
                products{
                  id: ID
                  name: Name
                  image_url: ImageUrl
                  shop_id: ShopID
                  original_price: OriginalPrice
                  original_price_formatted: OriginalPriceFormatted
                  discount: Discount
                  price: Price
                  price_formatted: PriceFormatted
                  quantity: Quantity
                  is_variant: IsVariant
                  is_available: IsAvailable
                  order: Order
                  app_link: AppLink
                  web_link: WebLink
                  min_quantity: MinQuantity
                  is_free_shipping: IsFreeShipping
                }
                vouchers{
                  voucher_id: ID
                  voucher_name: Name
                  shop_id: ShopID
                  title: Title
                  subtitle: Subtitle
                  voucher_type: VoucherType
                  voucher_image: VoucherImage
                  voucher_image_square: VoucherImageSquare
                  voucher_quota: VoucherQuota
                  voucher_finish_time: VoucherFinishTime
                }
              }
            }
            """.trimIndent()
        }

        fun createParam(channelId: String): HashMap<String, Any> {
            return hashMapOf(
                    REQ to hashMapOf(
                            CHANNEL_ID to channelId
                    )
            )
        }
    }
}