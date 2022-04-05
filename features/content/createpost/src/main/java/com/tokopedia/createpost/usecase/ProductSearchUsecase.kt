package com.tokopedia.createpost.usecase

import android.text.TextUtils
import com.tokopedia.createpost.data.non_seller_model.CatalogSearchProductResponse
import com.tokopedia.createpost.view.util.CategoryNavConstants
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.network.authentication.AuthHelper
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSession
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.util.ArrayList
import javax.inject.Inject

const val CATALOG_GQL_SEARCH_PRODUCT: String =  """
    query SearchProduct(${'$'}params: String!) {
        ace_search_product_v4(params: ${'$'}params) {
            header {
                totalData
                totalDataText
                defaultView
                responseCode
                errorMessage
                additionalParams
                keywordProcess
            }
            data {
                isQuerySafe
                autocompleteApplink
                redirection {
                    redirectApplink
                }
                products {
                    id
                    name
                    ads {
                        id
                        productClickUrl
                        productWishlistUrl
                        productViewUrl
                    }
                    shop {
                        id
                        name
                        city
                        url
                        isOfficial
                        isPowerBadge
                    }
                    freeOngkir {
                        isActive
                        imgUrl
                    }
                    imageUrl
                    imageUrl300
                    imageUrl700
                    price
                    priceInt
                    priceRange
                    categoryId
                    categoryName
                    categoryBreadcrumb
                    ratingAverage
                    priceInt
                    originalPrice
                    discountPercentage
                    warehouseIdDefault
                    boosterList
                    source_engine
                    minOrder
                    url
                    labelGroups {
                        title
                        position
                        type
                        url
                    }
                    labelGroupVariant {
                        title
                        type
                        type_variant
                        hex_color
                    }
                    badges {
                        title
                        imageUrl
                        show
                    }
                    wishlist
                }
            }
        }
    }
"""
@GqlQuery("ProductSearchQuery", CATALOG_GQL_SEARCH_PRODUCT)
class ProductSearchUsecase  @Inject
constructor(graphqlRepository: GraphqlRepository) :
    GraphqlUseCase<CatalogSearchProductResponse>(graphqlRepository) {

    fun getSearchProductDetails(
        onSuccess: (CatalogSearchProductResponse.SearchProduct) -> Unit,
        onError: (Throwable) -> Unit,
        amount: Double,
        productId: String
    ) {
        try {
            this.setTypeClass(CatalogSearchProductResponse::class.java)
            this.setRequestParams(getRequestParams(amount, productId))
            this.setGraphqlQuery(CATALOG_GQL_SEARCH_PRODUCT)
            this.execute(
                { result ->
                    onSuccess(result.searchProduct)
                }, { error ->
                    onError(error)
                }
            )
        } catch (throwable: Throwable) {
            onError(throwable)
        }
    }
    private fun getRequestParams(amount: Double, productId: String): MutableMap<String, Any?> {
        val paramProductListing :MutableMap<String, Any?> = mutableMapOf()
//        paramProductListing["params"] = createParametersForQuery(paramProductListing.parameters)
        return paramProductListing


    }
    private fun createParametersForQuery(parameters: Map<String, Any>): String {
        return generateUrlParamString(parameters)
    }
    private fun getProductListParams(start: Int, userSession: UserSession, id:String): RequestParams {
        val param = RequestParams.create()
        val searchProductRequestParams = RequestParams.create()
        searchProductRequestParams.apply {
            putString(CategoryNavConstants.START, (start * PAGING_ROW_COUNT).toString())
            putString(CategoryNavConstants.DEVICE, DEVICE)
            putString(CategoryNavConstants.UNIQUE_ID, getUniqueId(userSession))
            putString(CategoryNavConstants.ROWS, PAGING_ROW_COUNT.toString())
            putString(CategoryNavConstants.SOURCE, SOURCE)
            putString(CategoryNavConstants.CTG_ID, id)
//            viewModel.searchParametersMap.value?.let { safeSearchParams ->
//                putAllString(safeSearchParams)
//            }
        }
        param.putString(PRODUCT_PARAMS, createParametersForQuery(searchProductRequestParams.parameters))
        return param
    }
    private fun getUniqueId(userSession: UserSession): String {
           return AuthHelper.getMD5Hash(userSession.userId)
//        else
//            AuthHelper.getMD5Hash(gcmHandler.registrationId)
    }
    fun <T> generateUrlParamString(paramMap: Map<String, T>?): String {
        if (paramMap == null) {
            return ""
        }

        val paramList = ArrayList<String>()

        for (entry in paramMap.entries) {
            if (entry.value == null) continue

            addParamToList<T>(paramList, entry)
        }

        return TextUtils.join("&", paramList)
    }
    private fun <T> addParamToList(paramList: MutableList<String>, entry: Map.Entry<String, T>) {
        try {
            paramList.add(entry.key + "=" + URLEncoder.encode(entry.value.toString(), "UTF-8"))
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
    }

    companion object {
        private const val PAGING_ROW_COUNT = 20
        private const val DEVICE = "android"
        const val SOURCE = "catalog"
        const val PRODUCT_PARAMS = "product_params"
    }

}