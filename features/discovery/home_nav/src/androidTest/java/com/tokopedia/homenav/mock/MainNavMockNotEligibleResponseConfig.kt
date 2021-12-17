package com.tokopedia.homenav.mock

import android.content.Context
import com.tokopedia.test.application.util.InstrumentationMockHelper.getRawString
import com.tokopedia.homenav.test.R

internal open class MainNavMockNotEligibleResponseConfig : MainNavMockResponseConfig() {
    override fun updateMock(context: Context) {
        /**
         * Not eligible response
         */
        addMockResponse(
            KEY_CONTAINS_MAINNAV_WALLET_ELIGIBILITY,
            getRawString(context, R.raw.response_success_mock_mainnav_walletapp_eligibility_not_eligible),
            FIND_BY_CONTAINS
        )
    }
}