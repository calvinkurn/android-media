package com.tokopedia.travel.homepage.presentation.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import kotlinx.android.synthetic.main.travel_homepage_fragment.*
import javax.inject.Inject

/**
 * @author by furqan on 06/08/2019
 */
class TravelHomepageFragment : BaseListFragment<TravelHomepageItemModel, TravelHomepageTypeFactory>(), OnItemBindListener, OnItemClickListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var travelHomepageViewModel: TravelHomepageViewModel

    private var startToTransitionOffset = 0
    private var searchBarTransitionRange = 0

    override fun getAdapterTypeFactory(): TravelHomepageTypeFactory = TravelHomepageAdapterTypeFactory(this, this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.run {
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            travelHomepageViewModel = viewModelProvider.get(TravelHomepageViewModel::class.java)
        }

        searchBarTransitionRange = resources.getDimensionPixelSize(R.dimen.toolbar_transition_range)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.travel_homepage_fragment, container, false);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        travel_homepage_toolbar.setNavigationOnClickListener { activity?.onBackPressed() }

        (getRecyclerView(view) as VerticalRecyclerView).clearItemDecoration()
        getRecyclerView(view).addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                calculateToolbarView(getRecyclerView(view).computeVerticalScrollOffset())
            }
        })
    }

    private fun calculateToolbarView(offset: Int) {

        val endTransitionOffset = startToTransitionOffset + searchBarTransitionRange
        val maxTransitionOffset = endTransitionOffset - startToTransitionOffset

        //mapping alpha to be rendered per pixel for x height
        var offsetAlpha = 255f / maxTransitionOffset * (offset - startToTransitionOffset)
        //2.5 is maximum
        if (offsetAlpha < 0) {
            offsetAlpha = 0f
        }

        if (offsetAlpha >= 255) {
            travel_homepage_toolbar.toOnScrolledMode()
        } else {
            travel_homepage_toolbar.toInitialMode()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        travelHomepageViewModel.getIntialList()
        travelHomepageViewModel.travelItemList.observe(this, Observer {

            clearAllData()
            it?.run { renderList(this) }
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