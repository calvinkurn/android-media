package com.tokopedia.play.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.play.data.ProductTagging
import javax.inject.Inject

/**
 * Created by mzennis on 2020-03-06.
 */
class GetProductTagItemsUseCase @Inject constructor(
        graphqlRepository: GraphqlRepository
) : GraphqlUseCase<ProductTagging.Response>(graphqlRepository) {

    init {
        setGraphqlQuery(query)
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(ProductTagging.Response::class.java)
    }

    companion object {

        private const val CHANNEL_ID = "channelId"

        private val query = """
            query(${'$'}channelId: String){
              playGetTagsItem(req: {channelID:${'$'}channelId}){
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
                  voucher_finish_time: VoucherFinishTime,
                  voucher_code: VoucherCode,
                  is_copyable: IsVoucherCopyable,
                  is_highlighted: IsHighlighted,
                  is_private: IsPrivate
                }
                config {
                  peek_product_count
                }
              }
            }
        """.trimIndent()

        fun createParam(channelId: String): HashMap<String, Any> {
            return hashMapOf(
                    CHANNEL_ID to channelId
            )
        }
    }
}