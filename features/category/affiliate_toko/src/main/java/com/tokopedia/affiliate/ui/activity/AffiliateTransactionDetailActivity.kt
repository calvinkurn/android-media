package com.tokopedia.affiliate.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.affiliate.di.AffiliateComponent
import com.tokopedia.affiliate.di.DaggerAffiliateComponent
import com.tokopedia.affiliate.ui.fragment.AffiliateTransactionDetailFragment

class AffiliateTransactionDetailActivity :
    BaseSimpleActivity(), HasComponent<AffiliateComponent> {
    private val affiliateComponent: AffiliateComponent by lazy(LazyThreadSafetyMode.NONE) { initInject() }

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
        affiliateComponent.injectTransactionDetailActivity(this)
        supportActionBar?.hide()
    }

    override fun getNewFragment(): Fragment? {
        return getAffiliateTransactionFragment()
    }

    private fun getAffiliateTransactionFragment(): Fragment? {
        intent?.getStringExtra(PARAM_TRANSACTION)?.let { transactionID ->
            return AffiliateTransactionDetailFragment.newInstance(transactionID)
        }
        return null
    }

    private fun initInject(): AffiliateComponent =
        DaggerAffiliateComponent
            .builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .build()

    override fun getComponent(): AffiliateComponent = affiliateComponent
}
