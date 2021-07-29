package com.tokopedia.saldodetails.view.activity.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.config.GlobalConfig
import com.tokopedia.saldodetails.di.SaldoDetailsComponent
import com.tokopedia.saldodetails.di.SaldoDetailsComponentInstance
import com.tokopedia.saldodetails.view.fragment.detail.SaldoSalesDetailFragment
import com.tokopedia.user.session.UserSession
import javax.inject.Inject

class SaldoSalesDetailActivity : BaseSimpleActivity(), HasComponent<SaldoDetailsComponent> {

    @Inject
    lateinit var userSession: UserSession
    private val saldoComponent by lazy(LazyThreadSafetyMode.NONE) {
        SaldoDetailsComponentInstance.getComponent(
            this
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSecureWindowFlag()
        saldoComponent.inject(this)
    }

    private fun setSecureWindowFlag() {
        if (GlobalConfig.APPLICATION_TYPE == GlobalConfig.CONSUMER_APPLICATION || GlobalConfig.APPLICATION_TYPE == GlobalConfig.SELLER_APPLICATION) {
            runOnUiThread {
                window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
            }
        }
    }

    override fun getComponent() = saldoComponent
    override fun getNewFragment() = SaldoSalesDetailFragment.getInstance()
    override fun getTagFragment() = TAG

    companion object {
        fun newInstance(context: Context): Intent {
            return Intent(context, SaldoSalesDetailActivity::class.java)
        }
        private val TAG = "DETAIL_FRAGMENT"

    }
}
