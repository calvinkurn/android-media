package com.tokopedia.affiliate.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.affiliate.di.DaggerAffiliateComponent
import com.tokopedia.affiliate.ui.fragment.education.AffiliateEducationSeeAllFragment

class AffiliateEducationSeeAllActivity :
    BaseSimpleActivity() {

    companion object {
        const val PARAM_PAGE_TYPE = "param_page_type"
        const val PARAM_CATEGORY_ID = "param_category_id"
        const val SEARCH_KEYWORD = "search_keyword"
        fun createIntent(
            context: Context,
            pageType: String?,
            categoryId: String?,
            searchKeyword: String?
        ): Intent {
            val intent = Intent(context, AffiliateEducationSeeAllActivity::class.java)
            intent.putExtra(PARAM_PAGE_TYPE, pageType)
            intent.putExtra(PARAM_CATEGORY_ID, categoryId)
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
        return getAffiliateEducationSeeAllFragment()
    }

    private fun getAffiliateEducationSeeAllFragment(): Fragment {
        return AffiliateEducationSeeAllFragment.newInstance(
            intent?.getStringExtra(PARAM_PAGE_TYPE),
            intent?.getStringExtra(PARAM_CATEGORY_ID),
            intent?.getStringExtra(SEARCH_KEYWORD),
        )
    }

    private fun initInject() {
        DaggerAffiliateComponent
            .builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .build()
            .injectEducationSeeMoreActivity(this)
    }
}
