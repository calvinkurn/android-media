package com.tokopedia.topads.auto.view.activity

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.topads.auto.base.AutoAdsBaseActivity
import com.tokopedia.topads.auto.view.fragment.EditAutoAdsBudgetFragment

/**
 * Author errysuprayogi on 09,May,2019
 */
class EditBudgetAutoAdsActivity : AutoAdsBaseActivity() {

    override fun getNewFragment(): Fragment? {
        return EditAutoAdsBudgetFragment.newInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.elevation = 0f
        }
    }
}
