package com.tokopedia.home_account.view.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.home_account.di.*
import com.tokopedia.home_account.view.fragment.FundsAndInvestmentFragment
import com.tokopedia.sessioncommon.di.SessionModule

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
        return DaggerHomeAccountUserComponents.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .homeAccountUserModules(HomeAccountUserModules(this))
            .homeAccountUserUsecaseModules(HomeAccountUserUsecaseModules())
            .homeAccountUserQueryModules(HomeAccountUserQueryModules())
            .sessionModule(SessionModule())
            .build()
    }
}