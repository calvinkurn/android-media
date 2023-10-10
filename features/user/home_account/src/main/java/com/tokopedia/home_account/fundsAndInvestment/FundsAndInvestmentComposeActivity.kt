package com.tokopedia.home_account.fundsAndInvestment

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.home_account.di.ActivityComponentFactory
import com.tokopedia.home_account.di.HomeAccountUserComponents
import com.tokopedia.kotlin.extensions.view.hide

class FundsAndInvestmentComposeActivity : BaseSimpleActivity(),
    HasComponent<HomeAccountUserComponents> {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toolbar.hide()
    }
    override fun getNewFragment(): Fragment =
        FundsAndInvestmentComposeFragment.newInstance()

    override fun getComponent(): HomeAccountUserComponents =
        ActivityComponentFactory.instance.createHomeAccountComponent(this, application)

}
