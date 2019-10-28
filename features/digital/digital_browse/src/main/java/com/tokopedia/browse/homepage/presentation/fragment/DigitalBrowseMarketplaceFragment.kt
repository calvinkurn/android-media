package com.tokopedia.browse.homepage.presentation.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.applink.RouteManager
import com.tokopedia.browse.R
import com.tokopedia.browse.common.DigitalBrowseRouter
import com.tokopedia.browse.common.util.DigitalBrowseAnalytics
import com.tokopedia.browse.homepage.di.DigitalBrowseHomeComponent
import com.tokopedia.browse.homepage.presentation.adapter.DigitalBrowseMarketplaceAdapter
import com.tokopedia.browse.homepage.presentation.adapter.DigitalBrowseMarketplaceAdapterTypeFactory
import com.tokopedia.browse.homepage.presentation.adapter.viewholder.DigitalBrowseCategoryViewHolder
import com.tokopedia.browse.homepage.presentation.contract.DigitalBrowseMarketplaceContract
import com.tokopedia.browse.homepage.presentation.model.DigitalBrowseMarketplaceViewModel
import com.tokopedia.browse.homepage.presentation.model.DigitalBrowseRowViewModel
import com.tokopedia.browse.homepage.presentation.presenter.DigitalBrowseMarketplacePresenter
import com.tokopedia.design.component.TextViewCompat
import java.util.*
import javax.inject.Inject

/**
 * @author by furqan on 30/08/18.
 */

class DigitalBrowseMarketplaceFragment : BaseDaggerFragment(), DigitalBrowseMarketplaceContract.View,
        DigitalBrowseCategoryViewHolder.CategoryListener {

    @Inject lateinit var presenter: DigitalBrowseMarketplacePresenter
    @Inject lateinit var digitalBrowseAnalytics: DigitalBrowseAnalytics

    private lateinit var rvCategory: RecyclerView

    private lateinit var categoryAdapter: DigitalBrowseMarketplaceAdapter
    private lateinit var popularAdapter: DigitalBrowseMarketplaceAdapter

    private var digitalBrowseMarketplaceViewModel: DigitalBrowseMarketplaceViewModel? = null

    override val fragmentContext: Context?
        get() = context

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_digital_browse_marketplace, container, false)

        rvCategory = view.findViewById(R.id.rv_category)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter.attachView(this)
        presenter.onInit()

        initializeCategoryView()

        digitalBrowseMarketplaceViewModel = savedInstanceState?.getParcelable(KEY_MARKETPLACE_DATA)

        if (digitalBrowseMarketplaceViewModel != null) {
            renderCategory(digitalBrowseMarketplaceViewModel!!.rowViewModelList)
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
    }

    override fun showGetDataError(e: Throwable) {
        categoryAdapter.hideLoading()
        NetworkErrorHelper.showEmptyState(activity, activity!!.window.decorView.rootView,
                ErrorHandler.getErrorMessage(context, e)
        ) {
            categoryAdapter.showLoading()
            presenter.getMarketplaceDataCloud()
        }
    }

    private fun initializeCategoryView() {
        val digitalBrowseMarketplaceAdapterTypeFactory = DigitalBrowseMarketplaceAdapterTypeFactory(this)
        categoryAdapter = DigitalBrowseMarketplaceAdapter(digitalBrowseMarketplaceAdapterTypeFactory, ArrayList())
        popularAdapter = DigitalBrowseMarketplaceAdapter(digitalBrowseMarketplaceAdapterTypeFactory, ArrayList())

        val layoutManager = GridLayoutManager(context, COLUMN_NUMBER)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (categoryAdapter.isLoadingObject(position)) {
                    4
                } else {
                    1
                }
            }
        }

        rvCategory.layoutManager = layoutManager
        rvCategory.setHasFixedSize(true)
        rvCategory.adapter = categoryAdapter

        categoryAdapter.showLoading()
    }

    private fun renderCategory(digitalBrowseRowViewModels: List<DigitalBrowseRowViewModel>?) {
        categoryAdapter.hideLoading()
        categoryAdapter.clearAllElements()
        categoryAdapter.addElement(digitalBrowseRowViewModels)
    }

    override fun onCategoryItemClicked(viewModel: DigitalBrowseRowViewModel, itemPosition: Int) {

        digitalBrowseAnalytics.eventClickOnCategoryBelanja(viewModel.name!!, itemPosition + 1)

        if (viewModel.appLinks != null && RouteManager.isSupportApplink(context, viewModel.appLinks)) {
            RouteManager.route(context, viewModel.appLinks)
        } else if (RouteManager.isSupportApplink(context, viewModel.url)) {
            RouteManager.route(context, viewModel.url)
        } else if (activity!!.application is DigitalBrowseRouter) {
            (activity!!.application as DigitalBrowseRouter)
                    .goToWebview(activity!!, viewModel.url!!)
        }
    }

    override fun sendImpressionAnalytics(iconName: String?, iconPosition: Int) {
        digitalBrowseAnalytics.eventImpressionHomePage(iconName!!, iconPosition)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroyView()
    }

    override fun getCategoryItemCount(): Int = categoryAdapter.itemCount

    companion object {

        private val COLUMN_NUMBER = 4
        private val KEY_MARKETPLACE_DATA = "KEY_MARKETPLACE_DATA"


        val fragmentInstance: Fragment
            get() = DigitalBrowseMarketplaceFragment()
    }
}
