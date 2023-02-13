package com.tokopedia.affiliate.ui.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.affiliate.di.AffiliateComponent
import com.tokopedia.affiliate.di.DaggerAffiliateComponent
import com.tokopedia.affiliate.ui.fragment.AffiliatePromoSearchFragment

class AffiliatePromoSearchActivity : BaseSimpleActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInject()
        supportActionBar?.hide()
    }

    override fun getNewFragment(): Fragment {
        return AffiliatePromoSearchFragment.newInstance()
    }

    private fun initInject() {
        getComponent().injectPromoSearchActivity(this)
    }

    private fun getComponent(): AffiliateComponent =
        DaggerAffiliateComponent
            .builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .build()
}
