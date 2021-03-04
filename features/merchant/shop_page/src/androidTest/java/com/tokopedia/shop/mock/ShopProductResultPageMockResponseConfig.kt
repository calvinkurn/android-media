package com.tokopedia.shop.mock

import android.content.Context
import com.tokopedia.shop.test.R
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationMockHelper.getRawString

class ShopProductResultPageMockResponseConfig(
        var productCampaignType: String = TYPE_NORMAL_PRODUCT
) : MockModelConfig() {
    companion object {
        const val KEY_QUERY_GET_SHOP_INFO = "shopInfo"
        const val KEY_QUERY_GET_SHOP_SORT = "getShopSort"
        const val KEY_QUERY_GET_SHOP_SHOWCASE_BY_ID = "shopShowcasesByShopID"
        const val KEY_QUERY_GET_SHOP_PRODUCT = "getShopProduct"

        const val TYPE_NORMAL_PRODUCT = "normal"
        const val TYPE_UPCOMING_PRODUCT = "upcoming"
        const val TYPE_ONGOING_PRODUCT = "ongoing"

    }

    override fun createMockModel(context: Context): MockModelConfig {
        addMockResponse(
                KEY_QUERY_GET_SHOP_INFO,
                getRawString(context, R.raw.response_mock_data_shop_product_result_shop_info),
                FIND_BY_CONTAINS
        )

        addMockResponse(
                KEY_QUERY_GET_SHOP_SORT,
                getRawString(context, R.raw.response_mock_data_shop_product_result_shop_sort),
                FIND_BY_CONTAINS
        )

        addMockResponse(
                KEY_QUERY_GET_SHOP_SHOWCASE_BY_ID,
                getRawString(context, R.raw.response_mock_data_shop_product_result_shop_showcase_by_id),
                FIND_BY_CONTAINS
        )

        addProductListMockResponse(context)
        return this
    }

    private fun addProductListMockResponse(context: Context) {
        val rawString = when (productCampaignType) {
            TYPE_UPCOMING_PRODUCT -> {
                R.raw.response_mock_data_shop_upcoming_product_result_get_shop_product
            }
            TYPE_ONGOING_PRODUCT -> {
                R.raw.response_mock_data_shop_ongoing_product_result_get_shop_product
            }
            else -> {
                R.raw.response_mock_data_shop_product_result_get_shop_product
            }
        }
        addMockResponse(
                KEY_QUERY_GET_SHOP_PRODUCT,
                getRawString(context, rawString),
                FIND_BY_CONTAINS
        )
    }
}