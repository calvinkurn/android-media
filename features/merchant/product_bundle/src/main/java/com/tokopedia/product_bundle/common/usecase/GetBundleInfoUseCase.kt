package com.tokopedia.product_bundle.common.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.product_bundle.common.data.model.request.ProductData
import com.tokopedia.product_bundle.common.data.model.request.RequestData
import com.tokopedia.product_bundle.common.data.model.response.GetBundleInfoResponse
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class GetBundleInfoUseCase @Inject constructor(
    repository: GraphqlRepository
) : GraphqlUseCase<GetBundleInfoResponse>(repository) {

    companion object {
        private const val PARAM_SQUAD = "squad"
        private const val PARAM_USE_CASE = "usecase"
        private const val PARAM_REQUEST_DATA = "requestData"
        private const val PARAM_PRODUCT_DATA = "productData"
        private val query =
            """ 
            query GetBundleByProductIdUseCase(
                ${'$'}squad: String, 
                ${'$'}usecase: String, 
                ${'$'}requestData: RequestData, 
                ${'$'}productData: ProductData
            ) {
                getBundleByProductIdUseCase(
                    squad: ${'$'}squad, 
                    usecase: ${'$'}usecase,
                    requestData: ${'$'}requestData,
                    productData: ${'$'}productData
                ) {                  
                    error{
                      message
                      reason
                      code
                    }
                    data[
                        {
                          bundleID
                          groupID
                          name
                          type
                          status
                          shopID
                          startTimeUnix
                          stopTimeUnix
                          quota
                          originalQuota
                          maxOrder
                          preorder{
                            processType
                            processTypeNum
                            startTime
                            endTime
                            orderLimit
                            maxOrder
                            processDay
                            processTime
                          }
                          bundleItem{
                            productID
                            name
                            picURL
                            status
                            originalPrice
                            bundlePrice
                            stock
                            minOrder
                            selection{
                              productVariantID
                              variantID
                              variantUnitID
                              position
                              name
                              identifier
                              option{
                                productVariantOptionID
                                unitValueID
                                value
                                hex
                              }
                            }
                            childern{
                              productID
                              name
                              picURL
                              minOrder
                              originalPrice
                              bundlePrice
                              stock
                              optionID
                            }
                          }
                        }
                    ]
            }
            """.trimIndent()
        var requestParams = mapOf<String, Any>()
    }

    init {
        setGraphqlQuery(query)
        setTypeClass(GetBundleInfoResponse::class.java)
    }

    fun setParams(
        squad: String, usecase: String,
        requestData: RequestData,
        productData: ProductData
    ) {
        val requestParams = RequestParams.create()
        requestParams.putString(PARAM_SQUAD, squad)
        requestParams.putString(PARAM_USE_CASE, usecase)
        requestParams.putObject(PARAM_REQUEST_DATA, requestData)
        requestParams.putObject(PARAM_PRODUCT_DATA, productData)
        setRequestParams(requestParams.parameters)
    }
}