package com.tokopedia.play.domain

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.play.data.Product
import com.tokopedia.play.data.ProductTagging
import com.tokopedia.play.data.Voucher
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
            return if (response.playGetTagsItem.listOfProducts.isEmpty()
                    || response.playGetTagsItem.listOfVouchers.isEmpty()) { // TODO ("testing")
                mockProductTagging()
            } else response.playGetTagsItem
        } else {
            throw MessageErrorException("server error")
        }
    }

    private fun mockProductTagging(): ProductTagging {
        return ProductTagging(
                listOfProducts = listOf(
                        Product(id = 689413405,
                                appLink = "tokopedia://product/689413405",
                                discount = 50,
                                image = "https://ecs7.tokopedia.net/img/cache/700/product-1/2016/11/14/5480226/5480226_7d5d6943-c478-4eb6-b2ca-6ba739c9f524_600_600.jpg",
                                isAvailable = true,
                                isVariant = true,
                                minimumQuantity = 1,
                                name = "Ramayana - Kemeja Pria Blue Camouflage RAF (07901447)",
                                order = 0,
                                originalPrice = 400000,
                                originalPriceFormatted = "Rp 400.000",
                                price = 200000,
                                priceFormatted = "Rp 200.000",
                                quantity = 0,
                                shopId = "479057",
                                webLink = "https://staging.tokopedia.com/ramayana-qc/ramayana-kemeja-pria-blue-camouflage-raf-07901447"),
                        Product(id = 689413405,
                                appLink = "tokopedia://product/689413405",
                                discount = 0,
                                image = "https://ecs7.tokopedia.net/img/cache/700/product-1/2016/11/14/5480226/5480226_7d5d6943-c478-4eb6-b2ca-6ba739c9f524_600_600.jpg",
                                isAvailable = true,
                                isVariant = true,
                                minimumQuantity = 1,
                                name = "Ramayana - Kemeja Pria Blue Camouflage RAF (07901447)",
                                order = 0,
                                originalPrice = 400000,
                                originalPriceFormatted = "Rp 400.000",
                                price = 0,
                                priceFormatted = "",
                                quantity = 0,
                                shopId = "479057",
                                webLink = "https://staging.tokopedia.com/ramayana-qc/ramayana-kemeja-pria-blue-camouflage-raf-07901447"),
                        Product(id = 728073530,
                                appLink = "tokopedia://product/728073530",
                                discount = 50,
                                image = "https://ecs7.tokopedia.net/img/cache/700/product-1/2016/11/14/5480226/5480226_7d5d6943-c478-4eb6-b2ca-6ba739c9f524_600_600.jpg",
                                isAvailable = true,
                                isVariant = true,
                                minimumQuantity = 1,
                                name = "Ramayana - Kemeja Pria Blue Camouflage RAF (07901447)",
                                order = 0,
                                originalPrice = 400000,
                                originalPriceFormatted = "Rp 400.000",
                                price = 200000,
                                priceFormatted = "Rp 200.000",
                                quantity = 0,
                                shopId = "479057",
                                webLink = "https://staging.tokopedia.com/ramayana-qc/ramayana-kemeja-pria-blue-camouflage-raf-07901447"),
                        Product(id = 728073530,
                                appLink = "tokopedia://product/728073530",
                                discount = 0,
                                image = "https://ecs7.tokopedia.net/img/cache/700/product-1/2016/11/14/5480226/5480226_7d5d6943-c478-4eb6-b2ca-6ba739c9f524_600_600.jpg",
                                isAvailable = true,
                                isVariant = true,
                                minimumQuantity = 1,
                                name = "Ramayana - Kemeja Pria Blue Camouflage RAF (07901447)",
                                order = 0,
                                originalPrice = 400000,
                                originalPriceFormatted = "Rp 400.000",
                                price = 0,
                                priceFormatted = "",
                                quantity = 0,
                                shopId = "479057",
                                webLink = "https://staging.tokopedia.com/ramayana-qc/ramayana-kemeja-pria-blue-camouflage-raf-07901447")
                ),
                listOfVouchers = listOf(
                        Voucher(
                                title = "Free Ongkir",
                                subtitle = "min. pembelian 100rb",
                                voucherType = 1
                        ),
                        Voucher(
                                title = "Cashback 2rb",
                                subtitle = "min. pembelian 100rb",
                                voucherType = 2
                        ),
                        Voucher(
                                title = "Cashback 2rb",
                                subtitle = "min. pembelian 100rb",
                                voucherType = 3
                        ),
                        Voucher(
                                title = "Cashback 2rb",
                                subtitle = "min. pembelian 100rb",
                                voucherType = 2
                        )
                )
        )
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
                  shopID: ShopID
                  original_price: OriginalPrice
                  original_price_formatted: OriginalPriceFormatted
                  discount: Discount
                  price: Price
                  price_formatted: PriceFormatted
                  quantity: Quantity
                  is_variant: IsVariant
                  is_available: IsAvailable
                  order: Order
                  applink: AppLink
                  web_link: WebLink
                  min_quantity: MinQuantity
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