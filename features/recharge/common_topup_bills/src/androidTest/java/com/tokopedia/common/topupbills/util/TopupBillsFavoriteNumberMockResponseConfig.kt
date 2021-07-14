package com.tokopedia.common.topupbills.util

import android.content.Context
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationMockHelper
import com.tokopedia.common.topupbills.test.R


class TopupBillsFavoriteNumberMockResponseConfig: MockModelConfig() {

    companion object {
        const val KEY_QUERY_MODIFY_FAV_NUMBER = "updateFavoriteDetail"
        const val KEY_QUERY_FETCH_FAV_NUMBER = "rechargeFetchFavoriteNumber"

    }

    override fun createMockModel(context: Context): MockModelConfig {
        addMockResponse(
            KEY_QUERY_FETCH_FAV_NUMBER,
            InstrumentationMockHelper.getRawString(context, R.raw.response_mock_data_favorite_number_fetch),
            FIND_BY_CONTAINS
        )
        addMockResponse(
            KEY_QUERY_MODIFY_FAV_NUMBER,
            InstrumentationMockHelper.getRawString(context, R.raw.response_mock_data_favorite_number_update),
            FIND_BY_CONTAINS
        )
        return this
    }
}