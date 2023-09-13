package com.tokopedia.affiliate.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.affiliate.TRANSACTION_ID
import com.tokopedia.affiliate.di.AffiliateComponent
import com.tokopedia.affiliate.di.DaggerAffiliateComponent
import com.tokopedia.affiliate.ui.fragment.withdrawal.AffiliateSaldoWithdrawalDetailFragment
import com.tokopedia.affiliate_toko.R
import com.tokopedia.header.HeaderUnify
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class AffiliateSaldoWithdrawalDetailActivity :
    BaseSimpleActivity(),
    HasComponent<AffiliateComponent> {
    private val affiliateComponent: AffiliateComponent by lazy(LazyThreadSafetyMode.NONE) { initInject() }

    @Inject
    @JvmField
    var userSession: UserSessionInterface? = null

    override fun getLayoutRes() = R.layout.affiliate_activity_transaction_detail

    override fun getParentViewResourceID(): Int = R.id.saldo_transaction_parent_view

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        affiliateComponent.injectWithdrawalDetailActivity(this)
        setupToolbar()
    }

    private fun initInject() =
        DaggerAffiliateComponent
            .builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .build()

    override fun getComponent(): AffiliateComponent = affiliateComponent

    private fun setupToolbar() {
        findViewById<HeaderUnify>(R.id.saldoTransactionHeader).apply {
            isShowBackButton = true
            toolbar = this
            setSupportActionBar(toolbar)
            supportActionBar?.let {
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
                supportActionBar?.setDisplayShowTitleEnabled(true)
            }
            this.title = TITLE
        }
    }

    override fun getNewFragment() = AffiliateSaldoWithdrawalDetailFragment.newInstance(
        intent.getStringExtra(TRANSACTION_ID) ?: ""
    )

    override fun getTagFragment() = TAG

    companion object {
        fun newInstance(context: Context, withdrawalId: String): Intent {
            val intent = Intent(context, AffiliateSaldoWithdrawalDetailActivity::class.java)
            intent.putExtra(TRANSACTION_ID, withdrawalId)
            return intent
        }

        private const val TITLE = "Detail Penarikan Saldo"
        private const val TAG = "DETAIL_FRAGMENT"
    }
}
