package com.tokopedia.macrobenchmark_util.env.mock.config

import android.content.Context
import android.content.Intent
import com.tokopedia.macrobenchmark_util.R
import com.tokopedia.macrobenchmark_util.env.mock.InstrumentationMockHelper.getRawString
import com.tokopedia.macrobenchmark_util.env.mock.MockModelConfig

open class HomeMockResponseConfig() : MockModelConfig() {
    companion object {
        const val KEY_QUERY_DYNAMIC_POSITION = "dynamicPosition"
        const val KEY_QUERY_DYNAMIC_POSITION_ICON = "homeIcon"
        const val KEY_QUERY_DYNAMIC_POSITION_TICKER = "homeTicker"
        const val KEY_QUERY_DYNAMIC_HOME_CHANNEL_ATF_1 = "\"param\": \"channel_ids=65312\""
        const val KEY_QUERY_DYNAMIC_HOME_CHANNEL_ATF_2 = "\"param\": \"channel_ids=45397\""
        const val KEY_QUERY_GET_STATE_CHOSEN_ADDRESS = "KeroAddrGetStateChosenAddress"
    }

    override fun createMockModel(context: Context, intent: Intent): MockModelConfig {
        addMockResponse(
            KEY_QUERY_DYNAMIC_HOME_CHANNEL_ATF_1,
            intent.getStringExtra(KEY_QUERY_DYNAMIC_HOME_CHANNEL_ATF_1)?:"",
            FIND_BY_CONTAINS
        )

        addMockResponse(
            KEY_QUERY_DYNAMIC_HOME_CHANNEL_ATF_2,
            intent.getStringExtra(KEY_QUERY_DYNAMIC_HOME_CHANNEL_ATF_2)?:"",
            FIND_BY_CONTAINS
        )

        addMockResponse(
            KEY_QUERY_DYNAMIC_POSITION,
            intent.getStringExtra(KEY_QUERY_DYNAMIC_POSITION)?:"",
            FIND_BY_CONTAINS
        )

        addMockResponse(
            KEY_QUERY_DYNAMIC_POSITION_ICON,
            intent.getStringExtra(KEY_QUERY_DYNAMIC_POSITION_ICON)?:"",
            FIND_BY_CONTAINS
        )

        addMockResponse(
            KEY_QUERY_DYNAMIC_POSITION_TICKER,
            intent.getStringExtra(KEY_QUERY_DYNAMIC_POSITION_TICKER)?:"",
            FIND_BY_CONTAINS
        )

        addMockResponse(
            KEY_QUERY_GET_STATE_CHOSEN_ADDRESS,
            intent.getStringExtra(KEY_QUERY_GET_STATE_CHOSEN_ADDRESS)?:"",
            FIND_BY_CONTAINS
        )
        updateMock(context)
        return this
    }

    open fun updateMock(context: Context) {}
}