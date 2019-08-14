package com.tokopedia.travel.homepage.presentation.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.recyclerview.VerticalRecyclerView
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.applink.RouteManager
import com.tokopedia.travel.homepage.R
import com.tokopedia.travel.homepage.data.TravelHomepageItemModel
import com.tokopedia.travel.homepage.di.TravelHomepageComponent
import com.tokopedia.travel.homepage.presentation.adapter.factory.TravelHomepageAdapterTypeFactory
import com.tokopedia.travel.homepage.presentation.adapter.factory.TravelHomepageTypeFactory
import com.tokopedia.travel.homepage.presentation.listener.OnItemBindListener
import com.tokopedia.travel.homepage.presentation.listener.OnItemClickListener
import com.tokopedia.travel.homepage.presentation.viewmodel.TravelHomepageViewModel
import javax.inject.Inject

/**
 * @author by furqan on 06/08/2019
 */
class TravelHomepageFragment: BaseListFragment<TravelHomepageItemModel, TravelHomepageTypeFactory>(), OnItemBindListener, OnItemClickListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var travelHomepageViewModel: TravelHomepageViewModel

    override fun getAdapterTypeFactory(): TravelHomepageTypeFactory = TravelHomepageAdapterTypeFactory(this, this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.run {
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            travelHomepageViewModel = viewModelProvider.get(TravelHomepageViewModel::class.java)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (getRecyclerView(view) as VerticalRecyclerView).clearItemDecoration()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        travelHomepageViewModel.getIntialList()
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

    override fun onBannerVHItemBind() {
        travelHomepageViewModel.getBanner(GraphqlHelper.loadRawString(resources, R.raw.query_travel_homepage_banner))
    }

    override fun onCategoryVHBind() {
        travelHomepageViewModel.getCategories(GraphqlHelper.loadRawString(resources, R.raw.query_travel_homepage_category_list))
    }

    override fun onDestinationVHBind() {
        travelHomepageViewModel.getDestination(GraphqlHelper.loadRawString(resources, R.raw.query_travel_homepage_destination))
    }

    override fun onOrderListVHBind() {
        travelHomepageViewModel.getOrderList(GraphqlHelper.loadRawString(resources, R.raw.query_travel_homepage_order_list))
    }

    override fun onRecentSearchVHBind() {
        travelHomepageViewModel.getRecentSearch(GraphqlHelper.loadRawString(resources, R.raw.query_travel_homepage_recent_search))
    }

    override fun onRecommendationVHBind() {
        travelHomepageViewModel.getRecommendation(GraphqlHelper.loadRawString(resources, R.raw.query_travel_homepage_recommendation))
    }

    override fun onItemClick(appUrl: String) {
        RouteManager.route(context, appUrl)
    }

    companion object {
        fun getInstance(): TravelHomepageFragment = TravelHomepageFragment()
    }
}