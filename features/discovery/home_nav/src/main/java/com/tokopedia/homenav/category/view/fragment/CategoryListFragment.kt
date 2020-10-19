package com.tokopedia.homenav.category.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.homenav.R

class CategoryListFragment: BaseDaggerFragment() {
    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_nav_category, container, false)
    }

}