package com.tokopedia.topads.sdk.domain.interactor

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.topads.sdk.domain.model.TopadsIsAdsQuery
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

/**
 * Created by yfsx on 3/29/21.
 */

class GetTopadsIsAdsUseCase @Inject constructor(
        private val graphqlUseCase: GraphqlUseCase<TopadsIsAdsQuery>)
    : UseCase<TopadsIsAdsQuery>() {

    init {
        graphqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        graphqlUseCase.setTypeClass(TopadsIsAdsQuery::class.java)
    }

    override suspend fun executeOnBackground(): TopadsIsAdsQuery {
        graphqlUseCase.clearCache()
        if (params.parameters.get(PARAM_URL_PARAM).toString().contains(PARAM_TXSC)) {
            graphqlUseCase.setGraphqlQuery(query)
            graphqlUseCase.setRequestParams(params.parameters)
            return graphqlUseCase.executeOnBackground()
        }
        return TopadsIsAdsQuery()
    }

    companion object {
        const val PARAM_PRODUCT_ID = "product_id"
        const val PARAM_PRODUCT_KEY = "product_key"
        const val PARAM_SHOP_DOMAIN = "shop_domain"
        const val PARAM_SRC = "src"
        const val PARAM_DEVICE = "device"
        const val PARAM_Q = "q"
        const val PARAM_URL_PARAM = "url_param"
        const val PARAM_PAGE_NAME = "page_name"

        const val DEFAULT_DEVICE = "android"
        const val DEFAULT_SRC = "recom_landing_page"
        const val DEFAULT_PAGE_NAME_GOOGLE = "im_google"
        const val DEFAULT_PAGE_NAME_FACEBOOK = "im_facebook"
        const val DEFAULT_PAGE_NAME_TIKTOK = "im_tiktok"
        const val DEFAULT_Q = "recom"


        const val PARAM_TXSC = "txsc"
    }

    private var params: RequestParams = RequestParams.create()

    //region query
    private val query by lazy {
        val productId = "\$product_id"
        val productKey = "\$product_key"
        val shopDomain = "\$shop_domain"
        val src = "\$src"
        val device = "\$device"
        val q = "\$q"
        val url_param = "\$url_param"
        val page_name = "\$page_name"

        """
            query GetTopAdsIsAds($productId: String!, $productKey: String!, $shopDomain: String!, $src: String!, $device: String!, $q: String!, $url_param: String!, $page_name: String!) {
              topAdsGetDynamicSlotting(product_id: $productId, product_key: $productKey, shop_domain: $shopDomain, src: $src, device: $device, q: $q, url_param: $url_param, page_name: $page_name) {
                data {
                  product_click_url
                  product {
                    image {
                      m_url
                    }
                  }
                  is_charge
                }
                status {
                  error_code
                  message
                }
                header {
                  total_data
                  process_time
                }
              }
            }
        """.trimIndent()
    }
    //endregion

    fun setParams(productId: String = "",
                  productKey: String = "",
                  shopDomain: String = "",
                  src: String = DEFAULT_SRC,
                  device: String = DEFAULT_DEVICE,
                  q: String = DEFAULT_Q,
                  urlParam: String = "",
                  pageName: String = "") {
        params.parameters.clear()
        params.putString(PARAM_PRODUCT_ID, productId)
        params.putString(PARAM_PRODUCT_KEY, productKey)
        params.putString(PARAM_SHOP_DOMAIN, shopDomain)
        params.putString(PARAM_SRC, src)
        params.putString(PARAM_DEVICE, device)
        params.putString(PARAM_Q, q)
        params.putString(PARAM_URL_PARAM, urlParam)
        params.putString(PARAM_PAGE_NAME, pageName)
    }
}