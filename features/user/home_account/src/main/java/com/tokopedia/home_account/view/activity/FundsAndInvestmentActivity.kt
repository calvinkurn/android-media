package com.tokopedia.home_account.view.activity

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.home_account.di.*
import com.tokopedia.home_account.view.fragment.FundsAndInvestmentFragment
import com.tokopedia.home_account.view.listener.onAppBarCollapseListener
import com.tokopedia.sessioncommon.di.SessionModule

class FundsAndInvestmentActivity : BaseSimpleActivity(), HasComponent<HomeAccountUserComponents>,
    onAppBarCollapseListener {

    override fun getNewFragment(): Fragment {
        return FundsAndInvestmentFragment.newInstance(intent?.extras)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toolbar.visibility = View.GONE
        updateTitle("")
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

    override fun showToolbarElevation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.elevation =
                resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.unify_space_4)
        }
    }

    override fun hideToolbarElevation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.elevation =
                resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.unify_space_0)
        }
    }
}