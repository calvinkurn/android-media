package com.tokopedia.deals.search.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.deals.DealsComponentInstance
import com.tokopedia.deals.search.DealsSearchConstants
import com.tokopedia.deals.search.di.component.DaggerDealsSearchComponent
import com.tokopedia.deals.search.di.component.DealsSearchComponent
import com.tokopedia.deals.search.ui.fragment.DealsSearchFragment

class DealsSearchActivity : BaseSimpleActivity(), HasComponent<DealsSearchComponent> {

    var childCategoryIds: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        childCategoryIds = intent.getStringExtra(DealsSearchConstants.CHILD_CATEGORY_IDS)
        super.onCreate(savedInstanceState)
        toolbar?.visibility = View.GONE
    }

    override fun getNewFragment(): Fragment? {
        return DealsSearchFragment.createInstance(childCategoryIds)
    }

    override fun getComponent(): DealsSearchComponent {
        return DaggerDealsSearchComponent.builder()
                .dealsComponent(DealsComponentInstance.getDealsComponent(application, this))
                .build()
    }

    override fun getTagFragment(): String {
        return DealsSearchFragment.TAG
    }

    companion object {
        fun getCallingIntent(context: Context): Intent = Intent(context, DealsSearchActivity::class.java)
    }
}