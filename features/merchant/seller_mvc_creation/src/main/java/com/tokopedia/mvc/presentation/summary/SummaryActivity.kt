package com.tokopedia.mvc.presentation.summary

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.mvc.R
import com.tokopedia.mvc.presentation.summary.fragment.SummaryFragment

class SummaryActivity: BaseSimpleActivity() {

    override fun getLayoutRes() = R.layout.smvc_activity_common
    override fun getNewFragment() = SummaryFragment()
    override fun getParentViewResourceID() = R.id.container
}
