package com.tokopedia.tokomart.categorylist.presentation.activity

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.tokomart.categorylist.presentation.fragment.TokoMartCategoryListFragment

class TokoMartCategoryListActivity: BaseSimpleActivity() {

    override fun getNewFragment(): Fragment = TokoMartCategoryListFragment.newInstance()
}