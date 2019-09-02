package com.tokopedia.affiliate.feature.dashboard.view.fragment

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.CardView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.affiliate.R
import com.tokopedia.affiliate.analytics.AffiliateAnalytics
import com.tokopedia.affiliate.analytics.AffiliateEventTracking
import com.tokopedia.affiliate.common.di.DaggerAffiliateComponent
import com.tokopedia.affiliate.feature.dashboard.di.DaggerDashboardComponent
import com.tokopedia.affiliate.feature.dashboard.view.activity.CommissionDetailActivity
import com.tokopedia.affiliate.feature.dashboard.view.adapter.DashboardAdapter
import com.tokopedia.affiliate.feature.dashboard.view.adapter.factory.DashboardItemTypeFactoryImpl
import com.tokopedia.affiliate.feature.dashboard.view.listener.DashboardContract
import com.tokopedia.affiliate.feature.dashboard.view.presenter.DashboardPresenter
import com.tokopedia.affiliate.feature.dashboard.view.viewmodel.DashboardFloatingButtonViewModel
import com.tokopedia.affiliate.feature.dashboard.view.viewmodel.DashboardHeaderViewModel
import com.tokopedia.affiliate.feature.dashboard.view.viewmodel.DashboardItemViewModel
import com.tokopedia.affiliate.feature.dashboard.view.viewmodel.EmptyDashboardViewModel
import com.tokopedia.affiliate.feature.explore.view.activity.ExploreActivity
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey.APP_ENABLE_SALDO_SPLIT
import com.tokopedia.user.session.UserSession
import java.util.ArrayList
import javax.inject.Inject

/**
 * Created by jegul on 2019-09-02.
 */
class NewDashboardFragment : BaseDaggerFragment(), DashboardContract.View, SwipeRefreshLayout.OnRefreshListener {

    companion object {

        private const val TEXT_TYPE_PROFILE_SEEN = 1
        private const val TEXT_TYPE_PRODUCT_CLICKED = 2
        private const val TEXT_TYPE_PRODUCT_BOUGHT = 3
        private const val TEXT_RECOMMENDATION_LEFT = 4
        private const val ITEM_COUNT = 5

        fun getInstance(bundle: Bundle): NewDashboardFragment {
            val fragment = NewDashboardFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var tvRecommendationCount: TextView
    private lateinit var rvHistory: RecyclerView
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var cvRecommendation: CardView

    private lateinit var adapter: DashboardAdapter
    private lateinit var swipeToRefresh: SwipeToRefresh
    private var isCanLoadMore = false
    private var cursor = ""

    @Inject
    lateinit var presenter: DashboardPresenter

    @Inject
    lateinit var userSession: UserSession

    @Inject
    lateinit var affiliateAnalytics: AffiliateAnalytics

    override fun initInjector() {
        val affiliateComponent = DaggerAffiliateComponent.builder()
                .baseAppComponent((activity!!.applicationContext as BaseMainApplication).baseAppComponent).build() as DaggerAffiliateComponent

        DaggerDashboardComponent.builder()
                .affiliateComponent(affiliateComponent)
                .build().inject(this)
    }

    override fun getScreenName(): String {
        return AffiliateEventTracking.Screen.BYME_EXPLORE
    }

    override fun onStart() {
        super.onStart()
        affiliateAnalytics.analyticTracker.sendScreenAuthenticated(screenName)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_af_dashboard, container, false)
        rvHistory = view.findViewById(R.id.rv_history)
        cvRecommendation = view.findViewById(R.id.item_recommendation_count)
        tvRecommendationCount = view.findViewById(R.id.tv_recommendation_count)
        swipeToRefresh = view.findViewById(R.id.swipe_refresh_layout)
        presenter.attachView(this)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cvRecommendation.visibility = View.GONE
        initView()
        initViewListener()
        if (!userSession.isLoggedIn)
            activity!!.finish()
        else
            presenter.checkAffiliate()
    }

    override fun onRefresh() {
        loadFirstData(true)
    }

    override fun showLoading() {
        adapter.addElement(LoadingModel())
    }

    override fun hideLoading() {
        adapter.hideLoading()
    }

    private fun initView() {
        initDefaultValue()
        swipeToRefresh.setOnRefreshListener(this)
        adapter = DashboardAdapter(DashboardItemTypeFactoryImpl(this), ArrayList())
        layoutManager = LinearLayoutManager(activity)
        rvHistory.layoutManager = layoutManager
        rvHistory.adapter = adapter
        rvHistory.addOnScrollListener(onScrollListener())
    }

    private fun loadFirstData(isPullToRefresh: Boolean) {
        isCanLoadMore = true
        presenter.loadDashboardItem(isPullToRefresh)
    }

    private fun initDefaultValue() {
        tvRecommendationCount.text = countTextBuilder(TEXT_RECOMMENDATION_LEFT, 0)
    }

    private fun initViewListener() {
        cvRecommendation.setOnClickListener { view ->
            val intent = ExploreActivity.getInstance(activity)
            startActivity(intent)
        }
    }

    private fun onScrollListener(): RecyclerView.OnScrollListener {
        return object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val totalItemCount = layoutManager.itemCount
                val lastVisibleItemPos = layoutManager.findLastVisibleItemPosition()
                if (isCanLoadMore
                        && !TextUtils.isEmpty(cursor)
                        && totalItemCount <= lastVisibleItemPos + ITEM_COUNT) {
                    isCanLoadMore = false
                    adapter.addElement(LoadingMoreModel())
                    presenter.loadMoreDashboardItem(cursor)
                }
            }
        }
    }

    private fun countTextBuilder(textType: Int, count: Int): String {
        val defaultText = resources.getString(
                if (textType == TEXT_TYPE_PROFILE_SEEN)
                    R.string.title_profil_dilihat
                else
                    if (textType == TEXT_TYPE_PRODUCT_CLICKED)
                        R.string.title_klik_produk
                    else
                        R.string.title_produk_dibeli)
        return "$count $defaultText"
    }

    override fun onSuccessGetDashboardItem(header: DashboardHeaderViewModel,
                                           itemList: List<DashboardItemViewModel>,
                                           cursor: String,
                                           floatingModel: DashboardFloatingButtonViewModel) {
        adapter.clearAllElements()
        if (swipeToRefresh.isRefreshing) swipeToRefresh!!.isRefreshing = false
        adapter.addElement(header)

        if (itemList.isEmpty()) {
            val emptyDashboardViewModel = EmptyDashboardViewModel(floatingModel.count)
            adapter.addElement(emptyDashboardViewModel)
            adapter.notifyDataSetChanged()
        } else {
            adapter.addElement(itemList)
            adapter.notifyDataSetChanged()
            var paddingBottom = convertDpToPixel(activity!!.resources.getDimensionPixelOffset(R.dimen.dp_8))
            if (floatingModel.count != 0) {
                cvRecommendation.visibility = View.VISIBLE
                tvRecommendationCount.text = MethodChecker.fromHtml(floatingModel.text)
                paddingBottom = convertDpToPixel(activity!!.resources.getDimensionPixelOffset(R.dimen.dp_36))
            }
            rvHistory.setPadding(
                    rvHistory.paddingLeft,
                    rvHistory.paddingTop,
                    rvHistory.paddingRight,
                    paddingBottom)
        }

        if (TextUtils.isEmpty(cursor) || cursor == "1") {
            isCanLoadMore = false
            this.cursor = ""
        } else {
            isCanLoadMore = true
            this.cursor = cursor
        }
    }

    fun convertDpToPixel(dp: Int): Int {
        val metrics = activity!!.resources.displayMetrics
        return dp * (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)
    }

    override fun onErrorGetDashboardItem(error: String) {
        adapter.clearAllElements()
        cvRecommendation.visibility = View.GONE
        NetworkErrorHelper.showEmptyState(activity,
                view,
                error
        ) { presenter.loadDashboardItem(false) }
    }

    override fun onSuccessLoadMoreDashboardItem(itemList: List<DashboardItemViewModel>, cursor: String) {
        adapter.hideLoading()
        adapter.addElement(itemList)
        adapter.notifyDataSetChanged()
        if (cursor.isEmpty() || itemList.isEmpty()) {
            isCanLoadMore = false
            this.cursor = ""
        } else {
            isCanLoadMore = true
            this.cursor = cursor
        }

    }

    override fun onErrorLoadMoreDashboardItem(error: String) {
        NetworkErrorHelper.createSnackbarWithAction(
                activity,
                error
        ) { presenter.loadMoreDashboardItem(cursor) }
    }

    override fun onSuccessCheckAffiliate(isAffiliate: Boolean) {
        if (isAffiliate)
            loadFirstData(false)
        else
            activity!!.finish()
    }

    override fun onErrorCheckAffiliate(error: String) {
        NetworkErrorHelper.showEmptyState(activity,
                view,
                error
        ) { presenter.checkAffiliate() }
    }

    override fun goToAffiliateExplore() {
        RouteManager.route(context, ApplinkConst.AFFILIATE_EXPLORE)
    }

    override fun onItemClicked(model: DashboardItemViewModel) {
        val bundle = Bundle()
        bundle.putString(CommissionDetailActivity.PARAM_AFF_ID, model.id)
        startActivity(CommissionDetailActivity.newInstance(activity!!, bundle))
    }

    override fun goToDeposit() {
        val remoteConfig = FirebaseRemoteConfigImpl(context)
        if (remoteConfig.getBoolean(APP_ENABLE_SALDO_SPLIT, false)) {
            if (userSession.hasShownSaldoIntroScreen()) {
                openApplink(ApplinkConst.DEPOSIT)
            } else {
                userSession.setSaldoIntroPageStatus(true)
                openApplink(ApplinkConst.SALDO_INTRO)
            }
        } else {
            RouteManager.route(context, String.format("%s?url=%s", ApplinkConst.WEBVIEW,
                    ApplinkConst.WebViewUrl.SALDO_DETAIL))
        }
        affiliateAnalytics.onAfterClickSaldo()
    }

    private fun openApplink(applink: String) {
        RouteManager.route(context, applink)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }
}