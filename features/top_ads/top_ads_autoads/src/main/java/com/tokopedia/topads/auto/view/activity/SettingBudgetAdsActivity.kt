package com.tokopedia.topads.auto.view.activity

import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment

import com.tokopedia.topads.auto.base.AutoAdsBaseActivity
import com.tokopedia.topads.auto.view.fragment.DailyBudgetFragment
import com.tokopedia.topads.auto.view.fragment.SettingBudgetAdsFragment

/**
 * Author errysuprayogi on 09,May,2019
 */
class SettingBudgetAdsActivity : AutoAdsBaseActivity() {

    override fun getNewFragment(): Fragment? {
        val budget = intent.getIntExtra(DailyBudgetFragment.KEY_DAILY_BUDGET, 0)
        val status = intent.getIntExtra(DailyBudgetFragment.KEY_AUTOADS_STATUS, 0)
        return SettingBudgetAdsFragment.newInstance(status, budget)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.elevation = 0f
        }
    }
}
