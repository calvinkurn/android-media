package com.tokopedia.shop.mock

import android.content.Context
import com.tokopedia.shop.test.R
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationMockHelper.getRawString

class ShopPageWithHomeTabMockResponseConfig : MockModelConfig() {
    companion object {
        const val KEY_QUERY_GET_SHOP_TICKER = "get_ticker"
        const val KEY_QUERY_GET_SHOP_OPERATIONAL_HOUR_STATUS = "getShopOperationalHourStatus"
        const val KEY_QUERY_SHOP_SHOWCASES_BY_ID = "shopShowcasesByShopID"
        const val KEY_QUERY_SHOP_PAGE_GET_LAYOUT = "get_shop_page_home_layout"
        const val KEY_QUERY_MEMBERSHIP_STAMP_PROGRESS = "membershipStampProgress"
        const val KEY_QUERY_GET_SHOP_PRODUCT = "GetShopProduct"
        const val KEY_QUERY_GET_IS_SHOP_OFFICIAL = "getIsOfficial"
        const val KEY_QUERY_SHOP_INFO_CORE_AND_ASSETS = "getShopInfoCoreAndAssets"
        const val KEY_QUERY_GET_SHOP_PAGE_HOME_TYPE = "shopPageGetHomeType"
        const val KEY_QUERY_IS_SHOP_POWER_MERCHANT = "goldGetPMOSStatus"
        const val KEY_QUERY_SHOP_INFO_TOP_CONTENT = "getShopInfoTopContent"
        const val KEY_QUERY_SHOP_BADGE = "getShopBadge"
        const val KEY_QUERY_SHOP_INFO_HEADER_CONTENT_DATA = "getShopInfoHeaderContentData"
        const val KEY_QUERY_SHOP_INFO_FAVORITE = "getShopInfoFavorite"
        const val KEY_QUERY_CHECK_WISHLIST = "CheckWishList"
        const val KEY_QUERY_WHITELIST = "WhitelistQuery"
    }

    override fun createMockModel(context: Context): MockModelConfig {
        addMockResponse(
                KEY_QUERY_GET_SHOP_TICKER,
                getRawString(context, R.raw.response_mock_data_get_shop_ticker),
                FIND_BY_CONTAINS
        )

        addMockResponse(
                KEY_QUERY_GET_IS_SHOP_OFFICIAL,
                getRawString(context, R.raw.response_mock_data_get_is_shop_os_data_with_home_tab),
                FIND_BY_CONTAINS
        )

        addMockResponse(
                KEY_QUERY_SHOP_INFO_CORE_AND_ASSETS,
                getRawString(context, R.raw.response_mock_data_get_shop_info_core_and_assets_with_home_tab),
                FIND_BY_CONTAINS
        )

        addMockResponse(
                KEY_QUERY_GET_SHOP_PAGE_HOME_TYPE,
                getRawString(context, R.raw.response_mock_data_get_shop_page_home_type_with_home_tab),
                FIND_BY_CONTAINS
        )

        addMockResponse(
                KEY_QUERY_IS_SHOP_POWER_MERCHANT,
                getRawString(context, R.raw.response_mock_data_get_is_shop_power_merchant),
                FIND_BY_CONTAINS
        )

        addMockResponse(
                KEY_QUERY_SHOP_INFO_TOP_CONTENT,
                getRawString(context, R.raw.response_mock_data_get_shop_info_top_content),
                FIND_BY_CONTAINS
        )

        addMockResponse(
                KEY_QUERY_SHOP_BADGE,
                getRawString(context, R.raw.response_mock_data_get_shop_badge),
                FIND_BY_CONTAINS
        )

        addMockResponse(
                KEY_QUERY_GET_SHOP_PRODUCT,
                getRawString(context, R.raw.response_mock_data_get_shop_product),
                FIND_BY_CONTAINS
        )

        addMockResponse(
                KEY_QUERY_WHITELIST,
                getRawString(context, R.raw.response_mock_data_whitelist),
                FIND_BY_CONTAINS
        )

        addMockResponse(
                KEY_QUERY_SHOP_INFO_HEADER_CONTENT_DATA,
                getRawString(context, R.raw.response_mock_data_get_shop_info_header_content_data_with_home_tab),
                FIND_BY_CONTAINS
        )

        addMockResponse(
                KEY_QUERY_SHOP_INFO_FAVORITE,
                getRawString(context, R.raw.response_mock_data_get_shop_info_favorite),
                FIND_BY_CONTAINS
        )

        addMockResponse(
                KEY_QUERY_GET_SHOP_OPERATIONAL_HOUR_STATUS,
                getRawString(context, R.raw.response_mock_data_shop_operational_hour),
                FIND_BY_CONTAINS)

        addMockResponse(
                KEY_QUERY_SHOP_SHOWCASES_BY_ID,
                getRawString(context, R.raw.response_mock_data_shop_showcase_by_shop_id),
                FIND_BY_CONTAINS)

        addMockResponse(
                KEY_QUERY_SHOP_PAGE_GET_LAYOUT,
                getRawString(context, R.raw.response_mock_data_shop_page_get_layout),
                FIND_BY_CONTAINS)

        addMockResponse(
                KEY_QUERY_MEMBERSHIP_STAMP_PROGRESS,
                getRawString(context, R.raw.response_mock_data_shop_membership_stamp),
                FIND_BY_CONTAINS)

        addMockResponse(
                KEY_QUERY_CHECK_WISHLIST,
                getRawString(context, R.raw.response_mock_data_get_check_wishlist),
                FIND_BY_CONTAINS)
        return this
    }
}