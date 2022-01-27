package com.tokopedia.play.domain

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.play.data.ProductSection
import javax.inject.Inject

/**
 * @author by astidhiyaa on 27/01/22
 */
@GqlQuery(GetProductTagItemSectionUseCase.QUERY_NAME, GetProductTagItemSectionUseCase.QUERY)
class GetProductTagItemSectionUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
) : GraphqlUseCase<ProductSection.Response>(graphqlRepository) {

    init {
        setGraphqlQuery(GetProductTagItemSectionUseCaseQuery.GQL_QUERY)
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(ProductSection.Response::class.java)
    }

    companion object {
        private const val CHANNEL_ID = "channelId"
        const val QUERY_NAME = "GetProductTagItemSectionUseCaseQuery"
        const val QUERY = """
            query getProductSheet(${'$'}channelId: String){
              playGetTagsItemSection(req: {channelID:${'$'}channelId}){
                 sections: {
                  type
                  title
                  countdown {
                    copy
                  }
                  background {
                    gradient
                    imageUrl
                  }
                  startTime
                  endTime
                  serverTime
                  products{
                    ID
                    Name
                    ImageUrl
                    ShopID
                    OriginalPrice
                    OriginalPriceFormatted
                    Discount
                    Price
                    PriceFormatted
                    Quantity
                    QuantityRender {
                      show
                      copy
                      color
                    }
                    IsVariant
                    IsAvailable
                    Order
                    AppLink
                    WebLink
                    MinQuantity
                    IsFreeShipping
                  }
                }
                vouchers{
                  ID
                  Name
                  ShopID
                  Title
                  Subtitle
                  VoucherType
                  VoucherImage
                  VoucherImageSquare
                  VoucherQuota
                  VoucherFinishTime
                  VoucherCode
                  IsHighlighted
                  IsVoucherCopyable
                  IsPrivate
                }
                config { 
                    peek_product_count
                    title_bottomsheet
                }
              }
            }
        """

        fun createParam(channelId: String): HashMap<String, Any> = hashMapOf(
            CHANNEL_ID to channelId
        )
    }
}