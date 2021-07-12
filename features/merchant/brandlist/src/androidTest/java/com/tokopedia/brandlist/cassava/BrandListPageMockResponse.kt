package com.tokopedia.brandlist.cassava

import android.content.Context
import com.tokopedia.brandlist.test.R
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationMockHelper.getRawString

class BrandListPageMockResponse: MockModelConfig() {
    companion object {
        const val KEY_QUERY_ALL_BRAND = "OfficialStoreAllBrands"
        const val KEY_QUERY_CATEGORIES = "OfficialStoreCategories"
        const val KEY_QUERY_BRAND_RECOMMENDATION = "OfficialStoreBrandsRecommendation"
        const val KEY_QUERY_FEATURED_SHOP = "query OfficialStoreFeaturedShop"
    }
    override fun createMockModel(context: Context): MockModelConfig {
        addMockResponse(
                KEY_QUERY_CATEGORIES,
                getRawString(context, R.raw.response_mock_data_official_store_categories),
                FIND_BY_CONTAINS)
        addMockResponse(
                KEY_QUERY_BRAND_RECOMMENDATION,
                getRawString(context, R.raw.response_mock_data_official_store_brand_recommendation),
                FIND_BY_CONTAINS)
        addMockResponse(
                KEY_QUERY_FEATURED_SHOP,
                getRawString(context, R.raw.response_mock_data_official_store_featured_shop_2),
                FIND_BY_QUERY_NAME)
        addMockResponse(
                KEY_QUERY_ALL_BRAND,
                getRawString(context, R.raw.response_mock_data_official_store_all_brand),
                FIND_BY_CONTAINS)

        return this
    }
}