package com.tokopedia.product_bundle.common.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.product_bundle.common.data.model.request.RequestData
import com.tokopedia.product_bundle.common.data.model.response.GetBundleByProductIdResponse
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class GetBundleByProductIdUseCase @Inject constructor(
    repository: GraphqlRepository
) : GraphqlUseCase<GetBundleByProductIdResponse>(repository) {

    companion object {
        private const val PARAM_PRODUCT_ID = "productID"
        private const val PARAM_SQUAD = "squad"
        private const val PARAM_USE_CASE = "usecase"
        private const val PARAM_REQUEST_DATA = "requestData"
        private val query =
            """ 
            query GetBundleByProductIdUseCase(${'$'}productID: String, ${'$'}squad: String, ${'$'}usecase: String, ${'$'}requestData: RequestData) {
                getBundleByProductIdUseCase(productID: ${'$'}productID, squad: ${'$'}squad, ) {                  
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
        setTypeClass(GetBundleByProductIdResponse::class.java)
    }

    fun setParams(productId: String, squad: String, usecase: String, requestData: RequestData) {
        val requestParams = RequestParams.create()
        requestParams.putString(PARAM_PRODUCT_ID, productId)
        requestParams.putString(PARAM_SQUAD, squad)
        requestParams.putString(PARAM_USE_CASE, usecase)
        requestParams.putObject(PARAM_REQUEST_DATA, requestData)
        setRequestParams(requestParams.parameters)
    }
}