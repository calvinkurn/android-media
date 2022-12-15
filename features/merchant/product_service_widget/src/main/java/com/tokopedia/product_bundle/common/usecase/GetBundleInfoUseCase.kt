package com.tokopedia.product_bundle.common.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.product_bundle.common.data.model.request.Bundle
import com.tokopedia.product_bundle.common.data.model.request.ProductData
import com.tokopedia.product_bundle.common.data.model.request.RequestData
import com.tokopedia.product_bundle.common.data.model.response.GetBundleInfoResponse
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class GetBundleInfoUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository
) : GraphqlUseCase<GetBundleInfoResponse>(repository) {

    companion object {
        private const val ERROR_INVALID_BUNDLE = "ERROR_INVALID_BUNDLE"
        private const val SORT_BY_BENEFIT = "benefit"
        private const val SORT_BY_PREORDER = "preorder"
        private const val PARAM_BUNDLES = "bundles"
        private const val PARAM_SQUAD = "squad"
        private const val PARAM_USE_CASE = "usecase"
        private const val PARAM_REQUEST_DATA = "requestData"
        private const val PARAM_PRODUCT_DATA = "productData"
        private const val PARAM_TYPE = "type"
        private const val PARAM_SORT_BY = "sortBy"
        private val query =
            """ 
            query getBundleInfo( ${'$'}bundles: [Bundle], ${'$'}squad: String, ${'$'}usecase: String, ${'$'}requestData: RequestData, ${'$'}productData: ProductData, ${'$'}type: Int, ${'$'}sortBy: [String]) {
                GetBundleInfo(bundles: ${'$'}bundles, squad: ${'$'}squad, usecase: ${'$'}usecase, requestData: ${'$'}requestData, productData: ${'$'}productData, type: ${'$'}type, sortBy: ${'$'}sortBy) {
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
                        bundleStats {
                            SoldItem
                        }
                        shopInformation {
                            ShopName
                            ShopType
                            ShopBadge
                            ShopID
                        }
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

    private var params: HashMap<String, Any> = hashMapOf()

    init {
        setGraphqlQuery(query)
        setTypeClass(GetBundleInfoResponse::class.java)
    }

    fun setParams(
        squad: String, usecase: String,
        requestData: RequestData,
        productData: ProductData,
        bundleIdList: List<Bundle>,
        type: Int? = null,
        activateSorting: Boolean = false
    ) {
        val requestParams = RequestParams.create()
        requestParams.putObject(PARAM_BUNDLES, bundleIdList)
        requestParams.putString(PARAM_SQUAD, squad)
        requestParams.putString(PARAM_USE_CASE, usecase)
        requestParams.putObject(PARAM_REQUEST_DATA, requestData)
        requestParams.putObject(PARAM_PRODUCT_DATA, productData)
        requestParams.putObject(PARAM_SORT_BY,
            if (activateSorting) listOf(SORT_BY_BENEFIT, SORT_BY_PREORDER) else emptyList())
        type?.let { requestParams.putInt(PARAM_TYPE, it) }
        params = requestParams.parameters
    }

    override suspend fun executeOnBackground(): GetBundleInfoResponse {
        val gqlRequest = GraphqlRequest(query, GetBundleInfoResponse::class.java, params)
        val gqlResponse: GraphqlResponse = repository.response(listOf(gqlRequest))

        val response = gqlResponse.getData<GetBundleInfoResponse>(GetBundleInfoResponse::class.java)
        val errorMessage = response?.getBundleInfo?.error?.messages.orEmpty()

        return when {
            errorMessage == ERROR_INVALID_BUNDLE -> {
                response
            }
            errorMessage.isBlank() -> {
                response
            }
            else -> {
                throw MessageErrorException(errorMessage)
            }
        }
    }
}
