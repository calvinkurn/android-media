package com.tokopedia.officialstore.cassava

import android.content.Context
import com.tokopedia.instrumentation.test.R
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationMockHelper.getRawString

class OfficialStoreMockResponseConfig: MockModelConfig() {
    companion object {
        const val KEY_QUERY_BANNERS = "slides"
        const val KEY_QUERY_CATEGORIES = "OfficialStoreCategories"
        const val KEY_QUERY_BENEFITS = "OfficialStoreBenefits"
        const val KEY_QUERY_DYNAMIC_CHANNEL = "dynamicHomeChannel"
        const val KEY_QUERY_FEATURED_SHOP = "OfficialStoreFeaturedShop"
        const val KEY_QUERY_PRODUCT_RECOMMENDATION = "productRecommendationWidget"
    }
    override fun createMockModel(context: Context): MockModelConfig {
        addMockResponse(
                KEY_QUERY_BANNERS,
                getRawString(context, R.raw.response_mock_data_official_store_banners),
                FIND_BY_CONTAINS)
        addMockResponse(
                KEY_QUERY_CATEGORIES,
                getRawString(context, R.raw.response_mock_data_official_store_categories),
                FIND_BY_CONTAINS)
        addMockResponse(
                KEY_QUERY_BENEFITS,
                getRawString(context, R.raw.response_mock_data_official_store_benefits),
                FIND_BY_CONTAINS)
        addMockResponse(
                KEY_QUERY_DYNAMIC_CHANNEL,
                getRawString(context, R.raw.response_mock_data_official_store_dynamic_channels),
                FIND_BY_CONTAINS)
        addMockResponse(
                KEY_QUERY_FEATURED_SHOP,
                getRawString(context, R.raw.response_mock_data_official_store_featured_shop),
                FIND_BY_CONTAINS)
        addMockResponse(
                KEY_QUERY_PRODUCT_RECOMMENDATION,
                getRawString(context, R.raw.response_mock_data_official_store_product_recommendation),
                FIND_BY_CONTAINS)

        return this
    }
}