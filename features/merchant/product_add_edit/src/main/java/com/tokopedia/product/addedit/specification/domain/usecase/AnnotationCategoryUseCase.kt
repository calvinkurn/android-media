package com.tokopedia.product.addedit.specification.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.product.addedit.specification.domain.model.AnnotationCategoryResponse
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class AnnotationCategoryUseCase @Inject constructor(
        repository: GraphqlRepository): GraphqlUseCase<AnnotationCategoryResponse>(repository) {

    companion object {
        const val PARAM_PRODUCT_ID = "productID"
        const val PARAM_CATEGORY_ID = "categoryID"
        const val PARAM_VENDOR_NAME = "vendorName"
        const val PARAM_EXCLUDE_SENSITIVE = "excludeSensitive"
        const val DEFAULT_VALUE_VENDOR_NAME = "merchant"
        const val DEFAULT_VALUE_EXCLUDE_SENSITIVE = "true"
        private val query =
                """
                query getAnotationCategoryV2(${'$'}categoryID: String!, ${'$'}productID: String, ${'$'}vendorName: String, ${'$'}excludeSensitive: String) {
                    drogonAnnotationCategoryV2(categoryID: ${'$'}categoryID, productID: ${'$'}productID, vendorName: ${'$'}vendorName, excludeSensitive: ${'$'}excludeSensitive){
                        header {
                          processTime
                          messages
                          reason
                          errorCode
                        }
                        categoryID
                        productID
                        vendorName
                        data {
                            variant
                            sortOrder
                            values {
                                id
                                name
                                selected
                                data
                                isAgg
                            }
                        }
                    }
                }
                """.trimIndent()
    }

    private val requestParams = RequestParams.create()

    init {
        setGraphqlQuery(query)
        setTypeClass(AnnotationCategoryResponse::class.java)
    }

    fun setParamsCategoryId(categoryId: String) {
        requestParams.putString(PARAM_CATEGORY_ID, categoryId)
        requestParams.putString(PARAM_VENDOR_NAME, DEFAULT_VALUE_VENDOR_NAME)
        requestParams.putString(PARAM_EXCLUDE_SENSITIVE, DEFAULT_VALUE_EXCLUDE_SENSITIVE)
        setRequestParams(requestParams.parameters)
    }

    fun setParamsProductId(productId: String) {
        requestParams.putString(PARAM_PRODUCT_ID, productId)
        requestParams.putString(PARAM_VENDOR_NAME, DEFAULT_VALUE_VENDOR_NAME)
        requestParams.putString(PARAM_EXCLUDE_SENSITIVE, DEFAULT_VALUE_EXCLUDE_SENSITIVE)
        setRequestParams(requestParams.parameters)
    }
}