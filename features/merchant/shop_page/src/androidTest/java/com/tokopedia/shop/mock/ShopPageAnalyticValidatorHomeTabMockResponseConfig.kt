package com.tokopedia.shop.mock

import android.content.Context
import com.tokopedia.shop.test.R
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationMockHelper.getRawString

class ShopPageAnalyticValidatorHomeTabMockResponseConfig : MockModelConfig() {
    companion object {
        const val KEY_QUERY_GET_SHOP_TICKER = "get_ticker"
        const val KEY_QUERY_GET_SHOP_PAGE_HEADER_LAYOUT = "getShopPageGetHeaderLayout"
        const val KEY_QUERY_GET_SHOP_OPERATIONAL_HOUR_STATUS = "getShopOperationalHourStatus"
        const val KEY_QUERY_SHOP_SHOWCASES_BY_ID = "shopShowcasesByShopID"
        const val KEY_QUERY_SHOP_SORT_DATA = "getShopSort"
        const val KEY_QUERY_SHOP_PAGE_GET_LAYOUT = "get_shop_page_home_layout"
        const val KEY_QUERY_MEMBERSHIP_STAMP_PROGRESS = "membershipStampProgress"
        const val KEY_QUERY_GET_SHOP_PRODUCT = "GetShopProduct"
        const val KEY_QUERY_GET_IS_SHOP_OFFICIAL = "getIsOfficial"
        const val KEY_QUERY_SHOP_BADGE = "getShopBadge"
        const val KEY_QUERY_SHOP_INFO_HEADER_CONTENT_DATA = "getShopInfoHeaderContentData"
        const val KEY_QUERY_SHOP_INFO_FAVORITE = "getShopInfoFavorite"
        const val KEY_QUERY_CHECK_WISHLIST = "CheckWishList"
        const val KEY_QUERY_GET_CAMPAIGN_NOTIFY_ME = "get_campaign_notify_me"
        const val KEY_QUERY_MUTATION_ADD_TO_CART = "add_to_cart_v2"
        const val KEY_GET_MERCHANT_CAMPAIGN_TNC = "get_merchant_campaign_tnc"
        const val KEY_MUTATION_CHECK_CAMPAIGN_NOTIFY_ME = "check_campaign_notify_me"
        const val KEY_QUERY_GET_PUBLIC_MERCHANT_VOUCHER = "GetPublicMerchantVoucherList"

    }

    override fun createMockModel(context: Context): MockModelConfig {
        addMockResponse(
                KEY_QUERY_GET_SHOP_TICKER,
                getRawString(context, R.raw.response_mock_data_get_shop_ticker),
                FIND_BY_CONTAINS
        )

        addMockResponse(
                KEY_QUERY_GET_SHOP_PAGE_HEADER_LAYOUT,
                getRawString(context, R.raw.response_mock_data_shop_page_header_layout_buyer_view),
                FIND_BY_CONTAINS
        )

        addMockResponse(
                KEY_QUERY_GET_IS_SHOP_OFFICIAL,
                getRawString(context, R.raw.response_mock_data_shop_page_p1_data_with_home_tab),
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
                KEY_QUERY_SHOP_SORT_DATA,
                getRawString(context, R.raw.response_mock_data_shop_product_result_shop_sort),
                FIND_BY_CONTAINS
        )

        addMockResponse(
                KEY_QUERY_SHOP_PAGE_GET_LAYOUT,
                getRawString(context, R.raw.response_mock_data_shop_page_analytic_tracker_get_layout),
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
                KEY_QUERY_GET_CAMPAIGN_NOTIFY_ME,
                getRawString(context, R.raw.response_mock_data_campaign_notify_me),
                FIND_BY_CONTAINS)

        addMockResponse(
                KEY_QUERY_MUTATION_ADD_TO_CART,
                getRawString(context, R.raw.response_mock_data_add_to_cart),
                FIND_BY_CONTAINS)

        addMockResponse(
                KEY_GET_MERCHANT_CAMPAIGN_TNC,
                getRawString(context, R.raw.response_mock_data_get_merchant_campaign_tnc),
                FIND_BY_CONTAINS)

        addMockResponse(
                KEY_MUTATION_CHECK_CAMPAIGN_NOTIFY_ME,
                getRawString(context, R.raw.response_mock_data_mutation_check_campaign_notify_me),
                FIND_BY_CONTAINS)

        addMockResponse(
                KEY_QUERY_GET_PUBLIC_MERCHANT_VOUCHER,
                getRawString(context, R.raw.response_mock_data_shop_public_merchant_voucher_list),
                FIND_BY_CONTAINS)

        return this
    }
}