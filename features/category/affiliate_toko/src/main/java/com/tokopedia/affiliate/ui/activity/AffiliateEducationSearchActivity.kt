package com.tokopedia.affiliate.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.affiliate.di.AffiliateComponent
import com.tokopedia.affiliate.di.DaggerAffiliateComponent
import com.tokopedia.affiliate.ui.fragment.AffiliateEducationSearchFragment
import com.tokopedia.affiliate.ui.fragment.AffiliatePromoSearchFragment

class AffiliateEducationSearchActivity : BaseSimpleActivity() {

    companion object {
        private const val SEARCH_KEYWORD = "search_keyword"
        fun createIntent(
            context: Context,
            searchKeyword: String?
        ): Intent {
            val intent = Intent(context, AffiliateEducationSearchActivity::class.java)
            intent.putExtra(SEARCH_KEYWORD, searchKeyword)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInject()
        supportActionBar?.hide()
    }

    override fun getNewFragment(): Fragment {
        return AffiliateEducationSearchFragment.getFragmentInstance(intent?.getStringExtra(SEARCH_KEYWORD))
    }

    private fun initInject() {
        getComponent().injectEducationSearchActivity(this)
    }

    private fun getComponent(): AffiliateComponent =
        DaggerAffiliateComponent
            .builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .build()
}
