package com.tokopedia.affiliate.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.affiliate.di.AffiliateComponent
import com.tokopedia.affiliate.di.DaggerAffiliateComponent
import com.tokopedia.affiliate.ui.fragment.AffiliateTransactionDetailFragment
import com.tokopedia.affiliate.viewmodel.AffiliateTransactionDetailViewModel
import com.tokopedia.basemvvm.viewcontrollers.BaseViewModelActivity
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import javax.inject.Inject

class AffiliateTransactionDetailActivity : BaseViewModelActivity<AffiliateTransactionDetailViewModel>() {

    private lateinit var affiliateVM: AffiliateTransactionDetailViewModel

    @Inject
    lateinit var viewModelProvider: ViewModelProvider.Factory

    companion object {
        private const val PARAM_TRANSACTION = "PARAM_TRANSACTION"

        fun createIntent(
                context: Context,
                transactionId: String?
        ): Intent {
            val intent = Intent(context, AffiliateTransactionDetailActivity::class.java)
            intent.putExtra(PARAM_TRANSACTION, transactionId)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
    }

    override fun getNewFragment(): Fragment? {
        return getAffiliateTransactionFragment()
    }

    private fun getAffiliateTransactionFragment():Fragment? {
        intent?.getStringExtra(PARAM_TRANSACTION)?.let { transactionID ->
            return AffiliateTransactionDetailFragment.newInstance(transactionID)
        }
        return null
    }

    override fun getViewModelType(): Class<AffiliateTransactionDetailViewModel> {
        return AffiliateTransactionDetailViewModel::class.java
    }

    override fun setViewModel(viewModel: BaseViewModel) {
        affiliateVM = viewModel as AffiliateTransactionDetailViewModel
    }

    override fun getVMFactory(): ViewModelProvider.Factory {
        return viewModelProvider
    }

    override fun initInject() {
        getComponent().injectTransactionDetailActivity(this)
    }

    private fun getComponent(): AffiliateComponent =
        DaggerAffiliateComponent
            .builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .build()

}