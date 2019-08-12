package com.tokopedia.travel.homepage.presentation.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.travel.homepage.R
import com.tokopedia.travel.homepage.data.TravelHomepageItemModel
import com.tokopedia.travel.homepage.di.TravelHomepageComponent
import com.tokopedia.travel.homepage.presentation.adapter.factory.TravelHomepageAdapterTypeFactory
import com.tokopedia.travel.homepage.presentation.adapter.factory.TravelHomepageTypeFactory
import com.tokopedia.travel.homepage.presentation.viewmodel.TravelHomepageViewModel
import javax.inject.Inject

/**
 * @author by furqan on 06/08/2019
 */
class TravelHomepageFragment: BaseListFragment<TravelHomepageItemModel, TravelHomepageTypeFactory>() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var travelHomepageViewModel: TravelHomepageViewModel

    override fun getAdapterTypeFactory(): TravelHomepageTypeFactory = TravelHomepageAdapterTypeFactory()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.run {
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            travelHomepageViewModel = viewModelProvider.get(TravelHomepageViewModel::class.java)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        travelHomepageViewModel.getIntialList()
        travelHomepageViewModel.getBanner(GraphqlHelper.loadRawString(resources, R.raw.query_travel_homepage_banner))
        travelHomepageViewModel.travelItemList.observe(this, Observer {
            when (it?.second) {
                true -> {
                    clearAllData()
                    renderList(it.first)
                }
                false -> {

                }
            }
        })
    }

    override fun onItemClicked(t: TravelHomepageItemModel) {
        // do nothing
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