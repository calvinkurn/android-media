package com.tokopedia.common.topupbills.util

import android.content.Context
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationMockHelper
import com.tokopedia.common.topupbills.test.R


class TopupBillsFavoriteNumberMockResponseConfig(
    private val isMockFilledFavoriteNumber: Boolean,
    private val isMockUpdateFavoriteDetail: Boolean,
): MockModelConfig() {

    companion object {
        const val KEY_QUERY_MODIFY_FAV_NUMBER = "updateFavoriteDetail"
        const val KEY_QUERY_FETCH_FAV_NUMBER = "rechargeFetchFavoriteNumber"
    }

    override fun createMockModel(context: Context): MockModelConfig {
        if (isMockUpdateFavoriteDetail) {
            addMockUpdateFavoriteDetail(context)
        }
        if (isMockFilledFavoriteNumber) {
            addHappyMockModel(context)
        } else {
            addUnhappyMockModel(context)
        }
        return this
    }

    private fun addHappyMockModel(context: Context) {
        addMockResponse(
            KEY_QUERY_FETCH_FAV_NUMBER,
            InstrumentationMockHelper.getRawString(context, R.raw.response_mock_data_favorite_number_fetch),
            FIND_BY_CONTAINS
        )
    }

    private fun addUnhappyMockModel(context: Context) {
        addMockResponse(
            KEY_QUERY_FETCH_FAV_NUMBER,
            InstrumentationMockHelper.getRawString(context, R.raw.response_mock_data_favorite_number_fetch_zero),
            FIND_BY_CONTAINS
        )
    }

    private fun addMockUpdateFavoriteDetail(context: Context) {
        addMockResponse(
            KEY_QUERY_MODIFY_FAV_NUMBER,
            InstrumentationMockHelper.getRawString(context, R.raw.response_mock_data_favorite_number_update),
            FIND_BY_CONTAINS
        )
    }
}