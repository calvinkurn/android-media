package com.tokopedia.search.result.presentation.view.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager.widget.PagerAdapter

interface SearchViewPagerAdapter {

    fun asViewPagerAdapter(): PagerAdapter

    fun getRegisteredFragmentAtPosition(position: Int): Fragment?

    fun getFirstPageFragment(): Fragment?

    fun getSecondPageFragment(): Fragment?
}
