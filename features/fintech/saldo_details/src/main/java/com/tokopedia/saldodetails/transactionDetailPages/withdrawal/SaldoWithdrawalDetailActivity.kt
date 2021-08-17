package com.tokopedia.saldodetails.transactionDetailPages.withdrawal

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.config.GlobalConfig
import com.tokopedia.saldodetails.commom.analytics.SaldoDetailsConstants.DetailScreenParams.Companion.WITHDRAWAL_ID
import com.tokopedia.saldodetails.commom.di.component.SaldoDetailsComponent
import com.tokopedia.saldodetails.commom.di.component.SaldoDetailsComponentInstance
import com.tokopedia.user.session.UserSession
import javax.inject.Inject

class SaldoWithdrawalDetailActivity : BaseSimpleActivity(), HasComponent<SaldoDetailsComponent> {

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
    override fun getNewFragment() = SaldoWithdrawalDetailFragment.newInstance(
        intent.getLongExtra(WITHDRAWAL_ID, 0)
    )

    override fun getTagFragment() = TAG

    companion object {
        fun newInstance(context: Context, withdrawalId: Long): Intent {
            val intent = Intent(context, SaldoWithdrawalDetailActivity::class.java)
            intent.putExtra(WITHDRAWAL_ID, withdrawalId)
            return intent
        }

        private const val TAG = "DETAIL_FRAGMENT"

    }
}
