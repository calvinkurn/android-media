package com.tokopedia.product_bundle.common.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.product_bundle.common.data.model.request.Bundle
import com.tokopedia.product_bundle.common.data.model.request.ProductData
import com.tokopedia.product_bundle.common.data.model.request.RequestData
import com.tokopedia.product_bundle.common.data.model.response.GetBundleInfoResponse
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class GetBundleInfoUseCase @Inject constructor(
    @ApplicationContext repository: GraphqlRepository
) : GraphqlUseCase<GetBundleInfoResponse>(repository) {

    companion object {
        private const val PARAM_BUNDLES = "bundles"
        private const val PARAM_SQUAD = "squad"
        private const val PARAM_USE_CASE = "usecase"
        private const val PARAM_REQUEST_DATA = "requestData"
        private const val PARAM_PRODUCT_DATA = "productData"
        private val query =
            """ 
            query getBundleInfo( ${'$'}bundles: [Bundle], ${'$'}squad: String, ${'$'}usecase: String, ${'$'}requestData: RequestData, ${'$'}productData: ProductData) {
                GetBundleInfo(bundles: ${'$'}bundles, squad: ${'$'}squad, usecase: ${'$'}usecase, requestData: ${'$'}requestData, productData: ${'$'}productData) {
                    error {
                        messages
                        reason
                        errorCode
                    }
                    bundleInfo {
                        bundleID
                        groupID
                        name
                        type
                        status
                        shopID
                        startTimeUnix
                        stopTimeUnix
                        warehouseID
                        quota
                        originalQuota
                        maxOrder
                        preorder {
                            status
                            statusNum
                            processType
                            processTypeNum
                            startTime
                            endTime
                            orderLimit
                            maxOrder
                            processDay
                            processTime
                        }
                        bundleItem {
                            productID
                            name
                            picURL
                            status
                            bundlePrice
                            stock
                            minOrder
                            productStatus
                            originalPrice
                            selection {
                                productVariantID
                                variantID
                                variantUnitID
                                position
                                name
                                identifier
                                option {
                                    productVariantOptionID
                                    unitValueID
                                    value
                                    hex
                                }
                            }
                            children {
                                productID
                                name
                                picURL
                                minOrder
                                bundlePrice
                                originalPrice
                                stock
                                optionID
                                isBuyable
                            }
                        }
                    }
                }
            }
            """.trimIndent()
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
        requestParams.putObject(PARAM_BUNDLES, emptyList<Bundle>())
        requestParams.putString(PARAM_SQUAD, squad)
        requestParams.putString(PARAM_USE_CASE, usecase)
        requestParams.putObject(PARAM_REQUEST_DATA, requestData)
        requestParams.putObject(PARAM_PRODUCT_DATA, productData)
        setRequestParams(requestParams.parameters)
    }
}