package com.tokopedia.seller.search.feature.initialsearch.view.activity

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.seller.search.R
import com.tokopedia.seller.search.feature.initialsearch.view.fragment.InitialSearchFragment

class InitialSellerSearchActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment = InitialSearchFragment()

    override fun getLayoutRes(): Int {
        return R.layout.activity_initial_seller_search
    }
}
