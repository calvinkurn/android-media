package com.tokopedia.homenav.mock

import android.content.Context
import com.tokopedia.homenav.test.R
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationMockHelper.getRawString

internal open class MainNavMockResponseConfig : MockModelConfig() {
    companion object {
        const val KEY_CONTAINS_MAINNAV_MEMBERSHIP = "getMembership"
        const val KEY_CONTAINS_MAINNAV_ORDER_LIST = "GetOrderHistory"
        const val KEY_CONTAINS_MAINNAV_PAYMENT_LIST = "PaymentQuery"
        const val KEY_CONTAINS_MAINNAV_PROFILE = "getProfile"
        const val KEY_CONTAINS_MAINNAV_SALDO = "getHomeNavSaldo"
        const val KEY_CONTAINS_MAINNAV_TOKOPOINTS_FILTERED = "GetHomeNavTokopoints "
        const val KEY_CONTAINS_MAINNAV_WALLET_OVO = "mainNavOvoWallet"
        const val KEY_CONTAINS_MAINNAV_WALLET_APP = "walletAppGetBalance"
        const val KEY_CONTAINS_MAINNAV_WALLET_ELIGIBILITY = "walletappGetWalletEligible"
        const val KEY_CONTAINS_MAINNAV_BU_LIST = "businessUnitList"
        const val KEY_CONTAINS_MAINNAV_SHOP_INFO = "getShopInfo"
        const val KEY_CONTAINS_MAINNAV_GET_WISHLIST = "GetWishlist"
        const val KEY_CONTAINS_MAINNAV_PRODUCT_REV_WAIT_FOR_FEEDBACK = "productrevWaitForFeedback"
        const val KEY_CONTAINS_MAINNAV_PRODUCT_REV_AFFILIATE = "affiliateUserDetail"
        const val KEY_CONTAINS_MAINNAV_TOKOPEDIA_PLUS = "tokopediaPlusWidget"
    }

    override fun createMockModel(context: Context): MockModelConfig {
        addMockResponse(
            KEY_CONTAINS_MAINNAV_MEMBERSHIP,
            getRawString(context, R.raw.response_success_mock_mainnav_membership),
            FIND_BY_CONTAINS
        )

        addMockResponse(
            KEY_CONTAINS_MAINNAV_ORDER_LIST,
            getRawString(context, R.raw.response_success_mock_mainnav_get_order_history),
            FIND_BY_CONTAINS
        )

        addMockResponse(
            KEY_CONTAINS_MAINNAV_PAYMENT_LIST,
            getRawString(context, R.raw.response_success_mock_mainnav_payment_query),
            FIND_BY_CONTAINS
        )

        addMockResponse(
            KEY_CONTAINS_MAINNAV_PROFILE,
            getRawString(context, R.raw.response_success_mock_mainnav_profile),
            FIND_BY_CONTAINS
        )

        addMockResponse(
            KEY_CONTAINS_MAINNAV_SALDO,
            getRawString(context, R.raw.response_success_mock_mainnav_saldo),
            FIND_BY_CONTAINS
        )

        addMockResponse(
            KEY_CONTAINS_MAINNAV_TOKOPOINTS_FILTERED,
            getRawString(context, R.raw.response_success_mock_mainnav_tokopoints_filtered),
            FIND_BY_CONTAINS
        )

        addMockResponse(
            KEY_CONTAINS_MAINNAV_WALLET_OVO,
            getRawString(context, R.raw.response_success_mock_mainnav_wallet_ovo),
            FIND_BY_CONTAINS
        )

        addMockResponse(
            KEY_CONTAINS_MAINNAV_WALLET_APP,
            getRawString(context, R.raw.response_success_mock_mainnav_walletapp),
            FIND_BY_CONTAINS
        )

        addMockResponse(
            KEY_CONTAINS_MAINNAV_WALLET_ELIGIBILITY,
            getRawString(context, R.raw.response_success_mock_mainnav_walletapp_eligibility),
            FIND_BY_CONTAINS
        )

        addMockResponse(
            KEY_CONTAINS_MAINNAV_BU_LIST,
            getRawString(context, R.raw.response_success_mock_mainnav_bulist),
            FIND_BY_CONTAINS
        )

        addMockResponse(
            KEY_CONTAINS_MAINNAV_SHOP_INFO,
            getRawString(context, R.raw.response_success_mock_mainnav_shopinfo),
            FIND_BY_CONTAINS
        )

        addMockResponse(
            KEY_CONTAINS_MAINNAV_GET_WISHLIST,
            getRawString(context, R.raw.response_success_mock_mainnav_get_wishlist),
            FIND_BY_CONTAINS
        )

        addMockResponse(
            KEY_CONTAINS_MAINNAV_PRODUCT_REV_WAIT_FOR_FEEDBACK,
            getRawString(context, R.raw.response_success_mock_mainnav_review_order),
            FIND_BY_CONTAINS
        )

        addMockResponse(
            KEY_CONTAINS_MAINNAV_PRODUCT_REV_AFFILIATE,
            getRawString(context, R.raw.response_success_mock_mainnav_affiliate_user_detail),
            FIND_BY_CONTAINS
        )

        addMockResponse(
            KEY_CONTAINS_MAINNAV_TOKOPEDIA_PLUS,
            getRawString(context, R.raw.response_success_mock_mainnav_tokopedia_plus),
            FIND_BY_CONTAINS
        )

        updateMock(context)
        return this
    }

    open fun updateMock(context: Context) {}
}
