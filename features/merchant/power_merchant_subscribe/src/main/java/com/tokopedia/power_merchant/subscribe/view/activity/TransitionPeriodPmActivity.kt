package com.tokopedia.power_merchant.subscribe.view.activity

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.power_merchant.subscribe.view.fragment.PowerMerchantSubscribeFragment
import com.tokopedia.power_merchant.subscribe.view.fragment.TransitionPeriodPmFragment

class TransitionPeriodPmActivity: BaseSimpleActivity(), HasComponent<BaseAppComponent> {

    companion object {
        fun newInstance(context: Context): Intent {
            return Intent(context, TransitionPeriodPmActivity::class.java)
        }
    }

    override fun getComponent(): BaseAppComponent {
        return (application as BaseMainApplication).baseAppComponent
    }

    override fun getNewFragment(): Fragment {
        return TransitionPeriodPmFragment.newInstance()
    }
}