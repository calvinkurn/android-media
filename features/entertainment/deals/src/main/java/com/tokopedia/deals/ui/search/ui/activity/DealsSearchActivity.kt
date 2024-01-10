package com.tokopedia.deals.ui.search.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.deals.di.DealsComponentFactory
import com.tokopedia.deals.ui.search.di.component.DaggerDealsSearchComponent
import com.tokopedia.deals.ui.search.di.component.DealsSearchComponent
import com.tokopedia.deals.ui.search.ui.fragment.DealsSearchFragment

class DealsSearchActivity : BaseSimpleActivity(), HasComponent<DealsSearchComponent> {

    var childCategoryIds: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        childCategoryIds = intent.getStringExtra(com.tokopedia.deals.ui.search.DealsSearchConstants.CHILD_CATEGORY_IDS)
        super.onCreate(savedInstanceState)
        toolbar?.visibility = View.GONE
    }

    override fun getNewFragment(): Fragment? {
        return DealsSearchFragment.createInstance(childCategoryIds)
    }

    override fun getComponent(): DealsSearchComponent {
        return DaggerDealsSearchComponent.builder()
            .dealsComponent(DealsComponentFactory.instance.getDealsComponent(application, this))
            .build()
    }

    override fun getTagFragment(): String {
        return DealsSearchFragment.TAG
    }

    companion object {
        fun getCallingIntent(context: Context): Intent = Intent(context, DealsSearchActivity::class.java)
    }
}
