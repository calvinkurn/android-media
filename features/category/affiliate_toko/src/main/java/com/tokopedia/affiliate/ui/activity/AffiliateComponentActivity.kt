package com.tokopedia.affiliate.ui.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.affiliate.di.AffiliateComponent
import com.tokopedia.affiliate.di.DaggerAffiliateComponent
import com.tokopedia.affiliate.ui.fragment.AffiliatePromotionHistoryFragment

class AffiliateComponentActivity : BaseSimpleActivity(), HasComponent<AffiliateComponent> {
    private val affiliateComponent: AffiliateComponent by lazy(LazyThreadSafetyMode.NONE) { initInject() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        affiliateComponent.injectComponentActivity(this)
        supportActionBar?.hide()
    }

    private var isUserBlackListed = false
    private fun getBundle() {
        intent?.let {
            isUserBlackListed = it.getBooleanExtra("isUserBlackListed", false)
        }
    }

    override fun getNewFragment(): Fragment {
        getBundle()
        return AffiliatePromotionHistoryFragment.getFragmentInstance(isUserBlackListed)
    }

    fun initInject(): AffiliateComponent =
        DaggerAffiliateComponent
            .builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .build()

    override fun getComponent(): AffiliateComponent = affiliateComponent
}
