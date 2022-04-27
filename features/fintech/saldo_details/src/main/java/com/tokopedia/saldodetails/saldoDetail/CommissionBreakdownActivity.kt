package com.tokopedia.saldodetails.saldoDetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.saldodetails.commom.di.component.SaldoDetailsComponent
import com.tokopedia.saldodetails.commom.di.component.SaldoDetailsComponentInstance

/**
 * For navigating to this class

 */


class CommissionBreakdownActivity : BaseSimpleActivity(), HasComponent<SaldoDetailsComponent> {

    private val saldoComponent by lazy(LazyThreadSafetyMode.NONE) {
        SaldoDetailsComponentInstance.getComponent(this)
    }

    override fun getNewFragment(): Fragment {
        return CommissionBreakdownFragment.createInstance()
    }

    override fun setupLayout(savedInstanceState: Bundle?) {
        super.setupLayout(savedInstanceState)
        initializeView()
    }
    private fun initializeView() {

    }

    override fun getTagFragment() = TAG
    override fun getScreenName() = null

    companion object {
        private val TAG = "DEPOSIT_FRAGMENT"

    }

    override fun getComponent() = saldoComponent
}
