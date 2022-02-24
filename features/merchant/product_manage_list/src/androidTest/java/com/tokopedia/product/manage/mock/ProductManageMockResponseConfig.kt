package com.tokopedia.product.manage.mock

import android.content.Context
import com.tokopedia.product.manage.test.R
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationMockHelper

class ProductManageMockResponseConfig : MockModelConfig() {

    companion object {
        private const val KEY_QUERY_PRODUCT_LIST_META = "ProductListMeta"
        private const val KEY_QUERY_SHOP_INFO = "topAdsGetShopInfo"
        private const val KEY_QUERY_ADMIN_INFO = "getAdminInfo"
        private const val KEY_MUTATION_UPDATE_SHOP_ACTIVE = "updateShopActive"
        private const val KEY_QUERY_PRODUCT_LIST = "ProductList"
        private const val KEY_QUERY_GET_SHOP_INFO = "shopInfoByID"
    }

    override fun createMockModel(context: Context): MockModelConfig {
        addMockResponse(
            KEY_QUERY_PRODUCT_LIST_META,
            InstrumentationMockHelper.getRawString(
                context,
                R.raw.response_mock_data_product_manage_product_list_meta
            ),
            FIND_BY_CONTAINS
        )
        addMockResponse(
            KEY_QUERY_SHOP_INFO,
            InstrumentationMockHelper.getRawString(
                context,
                R.raw.response_mock_data_product_manage_shop_info
            ),
            FIND_BY_CONTAINS
        )
        addMockResponse(
            KEY_QUERY_ADMIN_INFO,
            InstrumentationMockHelper.getRawString(
                context,
                R.raw.response_mock_data_product_manage_admin_info
            ),
            FIND_BY_CONTAINS
        )
        addMockResponse(
            KEY_MUTATION_UPDATE_SHOP_ACTIVE,
            InstrumentationMockHelper.getRawString(
                context,
                R.raw.response_mock_data_product_manage_update_shop_active
            ),
            FIND_BY_CONTAINS
        )
        addMockResponse(
            KEY_QUERY_PRODUCT_LIST,
            InstrumentationMockHelper.getRawString(
                context,
                R.raw.response_mock_data_product_manage_product_list
            ),
            FIND_BY_CONTAINS
        )
        addMockResponse(
            KEY_QUERY_GET_SHOP_INFO,
            InstrumentationMockHelper.getRawString(
                context,
                R.raw.response_mock_data_product_manage_get_shop_info
            ),
            FIND_BY_CONTAINS
        )
        return this
    }
}