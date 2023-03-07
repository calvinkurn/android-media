package com.tokopedia.saldodetails.transactionDetailPages.withdrawal

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.saldodetails.R
import com.tokopedia.saldodetails.commom.analytics.SaldoDetailsConstants.DetailScreenParams.Companion.WITHDRAWAL_ID
import com.tokopedia.saldodetails.commom.di.component.SaldoDetailsComponent
import com.tokopedia.saldodetails.commom.di.component.SaldoDetailsComponentInstance
import com.tokopedia.user.session.UserSession
import kotlinx.android.synthetic.main.activity_saldodetails_transaction_detail.*
import javax.inject.Inject

class SaldoWithdrawalDetailActivity : BaseSimpleActivity(), HasComponent<SaldoDetailsComponent> {

    @Inject
    lateinit var userSession: UserSession

    override fun getLayoutRes() = R.layout.activity_saldodetails_transaction_detail
    override fun getParentViewResourceID(): Int = R.id.saldo_transaction_parent_view

    private val saldoComponent by lazy(LazyThreadSafetyMode.NONE) {
        SaldoDetailsComponentInstance.getComponent(
            this
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupToolbar()
        saldoComponent.inject(this)
    }

    private fun setupToolbar(){
        saldoTransactionHeader.isShowBackButton = true
        toolbar = saldoTransactionHeader
        setSupportActionBar(toolbar)
        supportActionBar?.let {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowTitleEnabled(true)
        }
        saldoTransactionHeader.title = TITLE
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

        private const val TITLE = "Detail Penarikan Saldo"
        private const val TAG = "DETAIL_FRAGMENT"

    }
}
