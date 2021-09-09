package com.tokopedia.tokopedianow.datefilter.presentation.activity

import androidx.fragment.app.Fragment
import com.tokopedia.tokopedianow.common.base.activity.BaseTokoNowActivity
import com.tokopedia.tokopedianow.datefilter.presentation.fragment.TokoNowDateFilterFragment

class TokoNowDateFilterActivity: BaseTokoNowActivity() {
    companion object {
        const val DATE_LABEL_POSITION = "date_label_position"
        const val START_DATE = "start_date"
        const val END_DATE = "end_date"
        const val REQUEST_CODE_DATE_FILTER_BOTTOMSHEET = 1455
    }

    override fun getFragment(): Fragment {
        val dateLabel = intent?.data?.getQueryParameter(DATE_LABEL_POSITION).orEmpty()
        val startDate = intent?.data?.getQueryParameter(START_DATE).orEmpty()
        val endDate = intent?.data?.getQueryParameter(END_DATE).orEmpty()
        return TokoNowDateFilterFragment.newInstance(dateLabel, startDate, endDate)
    }
}