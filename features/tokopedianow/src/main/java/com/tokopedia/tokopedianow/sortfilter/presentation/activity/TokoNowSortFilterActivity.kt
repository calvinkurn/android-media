package com.tokopedia.tokopedianow.sortfilter.presentation.activity

import androidx.fragment.app.Fragment
import com.tokopedia.tokopedianow.common.base.activity.BaseTokoNowActivity
import com.tokopedia.tokopedianow.sortfilter.presentation.fragment.TokoNowSortFilterFragment

class TokoNowSortFilterActivity: BaseTokoNowActivity() {
    companion object {
        const val SORT_VALUE = "sort_value"
        const val REQUEST_CODE_SORT_FILTER_BOTTOMSHEET = 1444
    }

    override fun getFragment(): Fragment {
        val sortValue = intent?.data?.getQueryParameter(SORT_VALUE).orEmpty()
        return TokoNowSortFilterFragment.newInstance(sortValue)
    }
}