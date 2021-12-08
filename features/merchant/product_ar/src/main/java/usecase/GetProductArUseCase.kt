package usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.localizationchooseaddress.common.ChosenAddressRequestHelper
import com.tokopedia.product.detail.common.ProductDetailCommonConstant
import com.tokopedia.product.detail.common.data.model.rates.UserLocationRequest
import com.tokopedia.usecase.RequestParams
import model.ProductArResponse
import javax.inject.Inject

class GetProductArUseCase @Inject constructor(graphqlRepository: GraphqlRepository) : GraphqlUseCase<ProductArResponse>(graphqlRepository) {

    companion object {
        const val QUERY = """
            query pdpGetARData(${'$'}productID : String, ${'$'}shopID : String,  ${'$'}userLocation: pdpUserLocation) {
                  pdpGetARData(productID: ${'$'}productID, shopID: ${'$'}shopID,  userLocation: ${'$'}userLocation) {
                    provider
                    options {
                      psku
                      name
                      productID
                      type
                      providerData
                      price
                      campaignInfo {
                        campaignID
                        campaignType
                        campaignTypeName
                        discountPercentage
                        originalPrice
                        discountPrice
                        stock
                        stockSoldPercentage
                        minOrder
                      }
                      stock
                      stockCopy
                      button {
                        text
                      }
                    }
                    optionBgImage
                  }
                }
        """

        fun createParams(productId: String,
                         shopId: String,
                         userLocationRequest: ChosenAddressRequestHelper): RequestParams =
                RequestParams.create().apply {
                    putString(ProductDetailCommonConstant.PARAM_PRODUCT_ID, productId)
                    putString(ProductDetailCommonConstant.PARAM_SHOP_ID, shopId)

                    val localizationChooseAddress = userLocationRequest.getChosenAddress()
                    putObject(ProductDetailCommonConstant.PARAM_USER_LOCATION, UserLocationRequest(
                            districtID = localizationChooseAddress?.districtId ?: "",
                            addressID = localizationChooseAddress?.addressId ?: "",
                            postalCode = localizationChooseAddress?.postalCode ?: "",
                            latlon = localizationChooseAddress?.geolocation ?: ""
                    ))
                }
    }

    init {
        setGraphqlQuery(QUERY)
        setCacheStrategy(GraphqlCacheStrategy
                .Builder(CacheType.CACHE_FIRST).build())
        setTypeClass(ProductArResponse::class.java)
    }


    private var requestParams: RequestParams = RequestParams.EMPTY

    @GqlQuery("pdpGetARData", QUERY)
    override suspend fun executeOnBackground(requestParams: RequestParams): ProductArResponse {
        this.requestParams = requestParams
        return ProductArResponse()
    }

    @GqlQuery("pdpGetARData", QUERY)
    override suspend fun executeOnBackground(): ProductArResponse {

        return ProductArResponse()
    }
}