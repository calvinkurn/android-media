package com.tokopedia.topads.auto.view.activity

import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.topads.auto.base.AutoAdsBaseActivity
import com.tokopedia.topads.auto.di.AutoAdsComponent
import com.tokopedia.topads.auto.view.fragment.budget.InitialBudgetFragment

/**
 * Author errysuprayogi on 07,May,2019
 */
class DailyBudgetActivity : AutoAdsBaseActivity() {

    override fun getNewFragment(): Fragment {
        return InitialBudgetFragment.newInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.elevation = 0f
        }
    }
}
