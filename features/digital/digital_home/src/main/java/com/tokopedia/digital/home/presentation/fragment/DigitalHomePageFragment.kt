package com.tokopedia.digital.home.presentation.fragment

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
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.digital.home.APPLINK_HOME_FAV_LIST
import com.tokopedia.digital.home.APPLINK_HOME_MYBILLS
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.di.DigitalHomePageComponent
import com.tokopedia.digital.home.model.DigitalHomePageBannerModel
import com.tokopedia.digital.home.model.DigitalHomePageCategoryModel
import com.tokopedia.digital.home.model.DigitalHomePageItemModel
import com.tokopedia.digital.home.presentation.adapter.viewholder.DigitalHomePageTransactionViewHolder
import com.tokopedia.digital.home.presentation.viewmodel.DigitalHomePageViewModel
import com.tokopedia.digital.home.presentation.adapter.DigitalHomePageTypeFactory
import com.tokopedia.digital.home.presentation.listener.OnItemBindListener
import kotlinx.android.synthetic.main.layout_digital_home.*
import javax.inject.Inject

class DigitalHomePageFragment : BaseListFragment<DigitalHomePageItemModel, DigitalHomePageTypeFactory>(), OnItemBindListener, DigitalHomePageTransactionViewHolder.TransactionListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var viewModel : DigitalHomePageViewModel
    private var searchBarTransitionRange = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.layout_digital_home, container, false)
        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.run {
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            viewModel = viewModelProvider.get(DigitalHomePageViewModel::class.java)
        }

        searchBarTransitionRange = resources.getDimensionPixelSize(R.dimen.toolbar_transition_range)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        digital_homepage_toolbar.setNavigationOnClickListener { activity?.onBackPressed() }
        calculateToolbarView(0)

        (getRecyclerView(view) as VerticalRecyclerView).clearItemDecoration()
        getRecyclerView(view).addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                calculateToolbarView(getRecyclerView(view).computeVerticalScrollOffset())
            }
        })
    }

    private fun calculateToolbarView(offset: Int) {

        //mapping alpha to be rendered per pixel for x height
        var offsetAlpha = 255f / searchBarTransitionRange * (offset)
        //2.5 is maximum
        if (offsetAlpha < 0) {
            offsetAlpha = 0f
        }

        if (offsetAlpha >= 255) {
            activity?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            digital_homepage_toolbar.toOnScrolledMode()
        } else {
            activity?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
            digital_homepage_toolbar.toInitialMode()
        }
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun getSwipeRefreshLayoutResourceId(): Int {
        return R.id.swipe_refresh_layout
    }

    override fun hasInitialSwipeRefresh(): Boolean {
        return true
    }

    override fun initInjector() {
        getComponent(DigitalHomePageComponent::class.java).inject(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.digitalHomePageList.observe(this, Observer {
            clearAllData()
            it?.run { renderList(this) }
        })
    }

    override fun loadData(page: Int) {
        viewModel.getInitialList(swipeToRefresh?.isRefreshing?:false)
    }

    override fun onBannerItemDigitalBind(loadFromCloud: Boolean?) {
        viewModel.getBannerList(GraphqlHelper.loadRawString(resources, R.raw.query_digital_home_banner), loadFromCloud?:true)
    }

    override fun onCategoryItemDigitalBind(loadFromCloud: Boolean?) {
        viewModel.getCategoryList(GraphqlHelper.loadRawString(resources, R.raw.query_digital_home_category), loadFromCloud?:true)
    }

    override fun onPromoItemDigitalBind() {
        //nothing to do, api not ready yet
    }

    override fun onCategoryItemClicked(element: DigitalHomePageCategoryModel.Submenu?) {
        RouteManager.route(activity, element?.applink)
    }

    override fun onBannerItemClicked(element: DigitalHomePageBannerModel.Banner?) {
        RouteManager.route(activity, element?.applink)
    }

    override fun onBannerAllItemClicked() {
        RouteManager.route(activity, ApplinkConst.PROMO_LIST)
    }

    override fun getAdapterTypeFactory(): DigitalHomePageTypeFactory {
        return DigitalHomePageTypeFactory(this, this)
    }

    override fun onItemClicked(t: DigitalHomePageItemModel?) {
        // do nothing
    }

    override fun onClickFavNumber() {
        RouteManager.route(activity, APPLINK_HOME_FAV_LIST)
    }

    override fun onClickOrderList() {
        RouteManager.route(activity, ApplinkConst.DIGITAL_ORDER)
    }

    override fun onClickHelp() {
        RouteManager.route(activity, ApplinkConst.CONTACT_US_NATIVE)
    }

    override fun onClickMyBills() {
        RouteManager.route(activity, APPLINK_HOME_MYBILLS)
    }

    companion object{
        fun getInstance() = DigitalHomePageFragment()
    }
}
