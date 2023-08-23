package com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.logisticaddaddress.R

class SearchPageActivity : BaseSimpleActivity() {

    override fun getLayoutRes() = R.layout.activity_search_address
    override fun getParentViewResourceID() = R.id.container_activity_search_address
    override fun getNewFragment(): Fragment = SearchPageFragment.newInstance(intent?.extras ?: Bundle())
}
