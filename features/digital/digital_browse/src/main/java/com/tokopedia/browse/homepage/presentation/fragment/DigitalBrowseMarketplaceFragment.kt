package com.tokopedia.browse.homepage.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.applink.RouteManager
import com.tokopedia.browse.R
import com.tokopedia.browse.common.DigitalBrowseRouter
import com.tokopedia.browse.common.data.DigitalBrowsePopularAnalyticsModel
import com.tokopedia.browse.common.util.DigitalBrowseAnalytics
import com.tokopedia.browse.homepage.di.DigitalBrowseHomeComponent
import com.tokopedia.browse.homepage.presentation.adapter.DigitalBrowseMarketplaceAdapter
import com.tokopedia.browse.homepage.presentation.adapter.DigitalBrowseMarketplaceAdapterTypeFactory
import com.tokopedia.browse.homepage.presentation.adapter.viewholder.DigitalBrowseCategoryViewHolder
import com.tokopedia.browse.homepage.presentation.adapter.viewholder.DigitalBrowsePopularViewHolder
import com.tokopedia.browse.homepage.presentation.contract.DigitalBrowseMarketplaceContract
import com.tokopedia.browse.homepage.presentation.model.DigitalBrowseMarketplaceViewModel
import com.tokopedia.browse.homepage.presentation.model.DigitalBrowsePopularBrandsViewModel
import com.tokopedia.browse.homepage.presentation.model.DigitalBrowseRowViewModel
import com.tokopedia.browse.homepage.presentation.presenter.DigitalBrowseMarketplacePresenter
import com.tokopedia.design.component.TextViewCompat
import java.util.*
import javax.inject.Inject

/**
 * @author by furqan on 30/08/18.
 */

class DigitalBrowseMarketplaceFragment : BaseDaggerFragment(), DigitalBrowseMarketplaceContract.View,
        DigitalBrowseCategoryViewHolder.CategoryListener, DigitalBrowsePopularViewHolder.PopularBrandListener {

    @Inject
    internal var presenter: DigitalBrowseMarketplacePresenter? = null
    @Inject
    internal var digitalBrowseAnalytics: DigitalBrowseAnalytics? = null

    private var containerPopularBrand: LinearLayout? = null
    private var tvAllPopularBrand: TextViewCompat? = null
    private var rvPopularBrand: RecyclerView? = null
    private var rvCategory: RecyclerView? = null

    private var categoryAdapter: DigitalBrowseMarketplaceAdapter? = null
    private var popularAdapter: DigitalBrowseMarketplaceAdapter? = null

    private var digitalBrowseMarketplaceViewModel: DigitalBrowseMarketplaceViewModel? = null

    override val categoryItemCount: Int
        get() = categoryAdapter!!.itemCount

    override val fragmentContext: Context?
        get() = context

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_digital_browse_marketplace, container, false)

        containerPopularBrand = view.findViewById(R.id.container_popular_title)
        tvAllPopularBrand = view.findViewById(R.id.tv_all_popular)
        rvPopularBrand = view.findViewById(R.id.rv_popular_brand)
        rvCategory = view.findViewById(R.id.rv_category)

        tvAllPopularBrand!!.setOnClickListener {
            digitalBrowseAnalytics!!.eventClickViewAllOnBelanjaPage()
            RouteManager.route(getContext(), OFFICIAL_STORES)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter!!.attachView(this)
        presenter!!.onInit()

        initializeCategoryView()
        initializePopularView()

        if (savedInstanceState != null) {
            digitalBrowseMarketplaceViewModel = savedInstanceState.getParcelable(KEY_MARKETPLACE_DATA)

            if (digitalBrowseMarketplaceViewModel != null) {
                renderCategory(digitalBrowseMarketplaceViewModel!!.rowViewModelList)
                renderPopularBrands(digitalBrowseMarketplaceViewModel!!.popularBrandsList)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putParcelable(KEY_MARKETPLACE_DATA, digitalBrowseMarketplaceViewModel)
    }

    override fun initInjector() {
        getComponent(DigitalBrowseHomeComponent::class.java).inject(this)
    }

    override fun getScreenName(): String? {
        return null
    }

    override fun renderData(marketplaceData: DigitalBrowseMarketplaceViewModel) {
        this.digitalBrowseMarketplaceViewModel = marketplaceData

        renderCategory(marketplaceData.rowViewModelList)
        renderPopularBrands(marketplaceData.popularBrandsList)
    }

    override fun showGetDataError(e: Throwable) {
        categoryAdapter!!.hideLoading()
        NetworkErrorHelper.showEmptyState(activity, activity!!.window.decorView.rootView,
                ErrorHandler.getErrorMessage(getContext(), e)
        ) {
            categoryAdapter!!.showLoading()
            presenter!!.getMarketplaceDataCloud()
        }
    }

    override fun sendPopularImpressionAnalytics(analyticsModelList: List<DigitalBrowsePopularAnalyticsModel>) {
        digitalBrowseAnalytics!!.eventPromoImpressionPopularBrand(analyticsModelList)
    }

    private fun initializeCategoryView() {
        tvAllPopularBrand!!.visibility = View.GONE
        containerPopularBrand!!.visibility = View.GONE
        rvPopularBrand!!.visibility = View.GONE

        val digitalBrowseMarketplaceAdapterTypeFactory = DigitalBrowseMarketplaceAdapterTypeFactory(this, this)
        categoryAdapter = DigitalBrowseMarketplaceAdapter(digitalBrowseMarketplaceAdapterTypeFactory, ArrayList<Visitable<*>>())
        popularAdapter = DigitalBrowseMarketplaceAdapter(digitalBrowseMarketplaceAdapterTypeFactory, ArrayList<Visitable<*>>())

        val layoutManager = GridLayoutManager(getContext(), COLUMN_NUMBER)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (categoryAdapter!!.isLoadingObject(position)) {
                    4
                } else {
                    1
                }
            }
        }

        rvCategory!!.layoutManager = layoutManager
        rvCategory!!.setHasFixedSize(true)
        rvCategory!!.adapter = categoryAdapter

        categoryAdapter!!.showLoading()
    }

    private fun initializePopularView() {
        hidePopularBrand()
        val layoutManager = LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false)

        rvPopularBrand!!.layoutManager = layoutManager
        rvPopularBrand!!.setHasFixedSize(false)
        rvPopularBrand!!.adapter = popularAdapter
    }

    private fun renderCategory(digitalBrowseRowViewModels: List<DigitalBrowseRowViewModel>?) {
        categoryAdapter!!.hideLoading()
        categoryAdapter!!.clearAllElements()
        categoryAdapter!!.addElement(digitalBrowseRowViewModels)
    }

    private fun renderPopularBrands(digitalBrowsePopularBrandsViewModels: List<DigitalBrowsePopularBrandsViewModel>?) {
        showPopularBrand()

        popularAdapter!!.clearAllElements()
        popularAdapter!!.addElement(digitalBrowsePopularBrandsViewModels)
    }

    private fun showPopularBrand() {
        containerPopularBrand!!.visibility = View.VISIBLE
        tvAllPopularBrand!!.visibility = View.VISIBLE
        rvPopularBrand!!.visibility = View.VISIBLE
    }

    private fun hidePopularBrand() {
        containerPopularBrand!!.visibility = View.GONE
        tvAllPopularBrand!!.visibility = View.GONE
        rvPopularBrand!!.visibility = View.GONE
    }

    override fun onPopularItemClicked(viewModel: DigitalBrowsePopularBrandsViewModel, position: Int) {
        digitalBrowseAnalytics!!.eventPromoClickPopularBrand(
                presenter!!.getPopularAnalyticsModel(viewModel, position))

        if (viewModel.url != null && RouteManager.isSupportApplink(getContext(), viewModel.url)) {
            RouteManager.route(getContext(), viewModel.url)
        } else {
            if (activity!!.application is DigitalBrowseRouter) {
                (activity!!.application as DigitalBrowseRouter)
                        .goToWebview(activity!!, viewModel.url!!)
            }
        }
    }

    override fun onCategoryItemClicked(viewModel: DigitalBrowseRowViewModel, itemPosition: Int) {

        digitalBrowseAnalytics!!.eventClickOnCategoryBelanja(viewModel.name!!, itemPosition + 1)

        if (viewModel.appLinks != null && RouteManager.isSupportApplink(getContext(), viewModel.appLinks)) {
            RouteManager.route(getContext(), viewModel.appLinks)
        } else if (RouteManager.isSupportApplink(getContext(), viewModel.url)) {
            RouteManager.route(getContext(), viewModel.url)
        } else if (activity!!.application is DigitalBrowseRouter) {
            (activity!!.application as DigitalBrowseRouter)
                    .goToWebview(activity!!, viewModel.url!!)
        }
    }

    override fun sendImpressionAnalytics(iconName: String?, iconPosition: Int) {
        digitalBrowseAnalytics!!.eventImpressionHomePage(iconName!!, iconPosition)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter!!.onDestroyView()
    }

    companion object {

        private val COLUMN_NUMBER = 4
        private val OFFICIAL_STORES = "tokopedia://official-stores"
        private val KEY_MARKETPLACE_DATA = "KEY_MARKETPLACE_DATA"


        val fragmentInstance: Fragment
            get() = DigitalBrowseMarketplaceFragment()
    }
}
