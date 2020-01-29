package com.tokopedia.brandlist.brandlist_search.presentation.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.brandlist.brandlist_search.presentation.fragment.BrandlistSearchFragment

class BrandlistSearchActivity: BaseSimpleActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
    }

    override fun getNewFragment(): Fragment? {
        return BrandlistSearchFragment.createInstance()
    }

}