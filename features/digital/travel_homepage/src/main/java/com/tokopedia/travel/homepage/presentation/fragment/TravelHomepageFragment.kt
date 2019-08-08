package com.tokopedia.travel.homepage.presentation.fragment

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.travel.homepage.di.TravelHomepageComponent
import com.tokopedia.travel.homepage.presentation.adapter.factory.TravelHomepageAdapterTypeFactory
import com.tokopedia.travel.homepage.presentation.adapter.factory.TravelHomepageTypeFactory

/**
 * @author by furqan on 06/08/2019
 */
class TravelHomepageFragment: BaseListFragment<Visitable<TravelHomepageTypeFactory>, TravelHomepageTypeFactory>() {
    override fun getAdapterTypeFactory(): TravelHomepageTypeFactory = TravelHomepageAdapterTypeFactory()

    override fun onItemClicked(t: Visitable<TravelHomepageTypeFactory>?) {

    }

    override fun loadData(page: Int) {

    }

    override fun initInjector() {
        getComponent(TravelHomepageComponent::class.java).inject(this)
    }

    override fun getScreenName(): String = ""

    companion object {
        fun getInstance(): TravelHomepageFragment = TravelHomepageFragment()
    }
}