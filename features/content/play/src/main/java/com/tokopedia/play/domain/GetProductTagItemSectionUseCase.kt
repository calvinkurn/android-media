package com.tokopedia.play.domain

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.play.data.ProductSection
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

/**
 * @author by astidhiyaa on 27/01/22
 */
@GqlQuery(GetProductTagItemSectionUseCase.QUERY_NAME, GetProductTagItemSectionUseCase.QUERY)
class GetProductTagItemSectionUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
) : GraphqlUseCase<ProductSection.Response>(graphqlRepository) {

    init {
        setGraphqlQuery(GetProductTagItemSectionUseCaseQuery())
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(ProductSection.Response::class.java)
    }

    companion object {
        private const val CHANNEL_ID = "channelID"
        private const val REQUEST_PARAM = "req"
        const val QUERY_NAME = "GetProductTagItemSectionUseCaseQuery"
        const val QUERY = """
            query getProductSheet(${'$'}req: PlayGetTagsItemSectionReq){
              playGetTagsItemSection(req: ${'$'}req){
                 sections {
                  type
                  title
                  countdown {
                    copy
                  }
                  background {
                    gradient
                    image_url: imageUrl
                  }
                  start_time: startTime
                  end_time: endTime
                  server_time: serverTime
                  source_id: sourceID
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
                  voucher_code: VoucherCode
                  is_highlighted: IsHighlighted
                  is_copyable: IsVoucherCopyable
                  is_private: IsPrivate
                }
                config { 
                    peek_product_count
                    title_bottomsheet
                }
              }
            }
        """

        fun createParam(channelId: String): RequestParams {
            val params = mapOf(
                CHANNEL_ID to channelId
            )
            return RequestParams.create().apply {
                putObject(REQUEST_PARAM, params)
            }
        }
    }
}