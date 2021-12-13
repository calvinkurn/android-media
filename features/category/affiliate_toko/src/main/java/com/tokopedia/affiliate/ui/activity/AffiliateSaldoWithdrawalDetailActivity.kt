package com.tokopedia.affiliate.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.affiliate.TRANSACTION_ID
import com.tokopedia.affiliate.di.AffiliateComponent
import com.tokopedia.affiliate.di.DaggerAffiliateComponent
import com.tokopedia.affiliate.ui.fragment.withdrawal.AffiliateSaldoWithdrawalDetailFragment
import com.tokopedia.affiliate.viewmodel.AffiliateViewModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.basemvvm.viewcontrollers.BaseViewModelActivity
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.header.HeaderUnify
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class AffiliateSaldoWithdrawalDetailActivity :  BaseViewModelActivity<AffiliateViewModel>() {

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelProvider: ViewModelProvider.Factory

    private lateinit var affiliateVM: AffiliateViewModel

    override fun getLayoutRes() = R.layout.affiliate_activity_transaction_detail

    override fun getParentViewResourceID(): Int = R.id.saldo_transaction_parent_view

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupToolbar()
    }

    override fun getViewModelType(): Class<AffiliateViewModel> {
        return AffiliateViewModel::class.java
    }

    override fun getVMFactory(): ViewModelProvider.Factory {
        return viewModelProvider
    }

    override fun initInject() {
        getComponent().injectWithdrawalDetailActivity(this)
    }

    private fun getComponent(): AffiliateComponent =
            DaggerAffiliateComponent
                    .builder()
                    .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                    .build()

    override fun setViewModel(viewModel: BaseViewModel) {
        affiliateVM = viewModel as AffiliateViewModel
    }

    private fun setupToolbar(){
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
