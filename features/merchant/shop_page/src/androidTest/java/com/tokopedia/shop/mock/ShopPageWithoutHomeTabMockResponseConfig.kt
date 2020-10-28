package com.tokopedia.shop.mock

import android.content.Context
import com.tokopedia.shop.test.R
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationMockHelper.getRawString

class ShopPageWithoutHomeTabMockResponseConfig : MockModelConfig() {
    companion object {
        const val KEY_QUERY_GET_SHOP_TICKER = "get_ticker"
        const val KEY_QUERY_GET_SHOP_OPERATIONAL_HOUR_STATUS = "getShopOperationalHourStatus"
        const val KEY_QUERY_SHOP_SHOWCASES_BY_ID = "shopShowcasesByShopID"
        const val KEY_QUERY_MEMBERSHIP_STAMP_PROGRESS = "membershipStampProgress"
        const val KEY_QUERY_GET_SHOP_PRODUCT = "GetShopProduct"
        const val KEY_QUERY_GET_IS_SHOP_OFFICIAL = "getIsOfficial"
        const val KEY_QUERY_SHOP_BADGE = "getShopBadge"
        const val KEY_QUERY_SHOP_INFO_HEADER_CONTENT_DATA = "getShopInfoHeaderContentData"
        const val KEY_QUERY_SHOP_INFO_FAVORITE = "getShopInfoFavorite"
        const val KEY_QUERY_CHECK_WISHLIST = "CheckWishList"
        const val KEY_QUERY_GET_PUBLIC_MERCHANT_VOUCHER = "GetPublicMerchantVoucherList"
        const val KEY_QUERY_GET_SHOP_FEATURED_PRODUCT = "getShopFeaturedProduct"
    }

    override fun createMockModel(context: Context): MockModelConfig {
        addMockResponse(
                KEY_QUERY_GET_SHOP_TICKER,
                getRawString(context, R.raw.response_mock_data_get_shop_ticker),
                FIND_BY_CONTAINS
        )

        addMockResponse(
                KEY_QUERY_GET_IS_SHOP_OFFICIAL,
                getRawString(context, R.raw.response_mock_data_shop_page_p1_data_without_home_tab),
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
                KEY_QUERY_SHOP_INFO_HEADER_CONTENT_DATA,
                getRawString(context, R.raw.response_mock_data_get_shop_info_header_content_data_without_home_tab),
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
                KEY_QUERY_MEMBERSHIP_STAMP_PROGRESS,
                getRawString(context, R.raw.response_mock_data_shop_membership_stamp),
                FIND_BY_CONTAINS)

        addMockResponse(
                KEY_QUERY_CHECK_WISHLIST,
                getRawString(context, R.raw.response_mock_data_get_check_wishlist),
                FIND_BY_CONTAINS)

        addMockResponse(
                KEY_QUERY_GET_PUBLIC_MERCHANT_VOUCHER,
                getRawString(context, R.raw.response_mock_data_shop_public_merchant_voucher_list),
                FIND_BY_CONTAINS)

        addMockResponse(
                KEY_QUERY_GET_SHOP_FEATURED_PRODUCT,
                getRawString(context, R.raw.response_mock_data_shop_featured_product),
                FIND_BY_CONTAINS)

        return this
    }
}