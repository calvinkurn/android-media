package com.tokopedia.home_account.view.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.home_account.di.ActivityComponentFactory
import com.tokopedia.home_account.di.HomeAccountUserComponents
import com.tokopedia.home_account.view.fragment.FundsAndInvestmentFragment

open class FundsAndInvestmentActivity : BaseSimpleActivity(), HasComponent<HomeAccountUserComponents> {

    override fun getNewFragment(): Fragment {
        return FundsAndInvestmentFragment.newInstance(intent?.extras)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        updateTitle("")
        supportActionBar?.elevation = 0F
    }

    override fun getComponent(): HomeAccountUserComponents {
        return ActivityComponentFactory.instance.createHomeAccountComponent(this, application)
    }
}
