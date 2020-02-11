package com.tokopedia.topads.widget.dashboard.view

import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds
import com.tokopedia.topads.widget.R
import com.tokopedia.topads.widget.dashboard.analytic.TopAdsWidgetTracker
import com.tokopedia.topads.widget.dashboard.data.TopAdsDepositResponse
import com.tokopedia.topads.widget.dashboard.data.TopAdsStatisticResponse
import com.tokopedia.topads.widget.dashboard.di.DaggerDashboardWidgetComponent
import com.tokopedia.topads.widget.dashboard.di.DashboardWidgetComponent
import com.tokopedia.topads.widget.dashboard.viewmodel.DashboardWidgetViewModel
import kotlinx.android.synthetic.main.layout_ads_saldo.*
import kotlinx.android.synthetic.main.layout_ads_statistic.*
import kotlinx.android.synthetic.main.layout_error_state.*
import javax.inject.Inject

/**
 * Author errysuprayogi on 25,October,2019
 */
class DashboardWidgetFragment : BaseDaggerFragment() {


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: DashboardWidgetViewModel
    internal var tracker: TopAdsWidgetTracker? = null

    override fun initInjector() {
        context?.let {
            getComponent(it).inject(this)
        }
    }

    private fun getComponent(context: Context): DashboardWidgetComponent = DaggerDashboardWidgetComponent.builder()
            .baseAppComponent((context.applicationContext as BaseMainApplication).baseAppComponent).build()

    override fun getScreenName(): String? {
        return DashboardWidgetFragment::class.simpleName
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_widget_dashboard_topads, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(DashboardWidgetViewModel::class.java)
        setupView()
        tracker = TopAdsWidgetTracker()
    }

    override fun onResume() {
        loadData()
    }

    private fun onSuccessGetStatistic(data: TopAdsStatisticResponse.Data) {
        data.topadsDashboardStatistics?.let {
            txt_impresion_count.setText(it.data.summary.adsImpressionSumFmt)
            txt_click_count.setText(it.data.summary.adsClickSumFmt)
            txt_profit_count.setText(it.data.summary.adsAllGrossProfitFmt)
            txt_spend_count.setText(it.data.summary.adsCostSumFmt)
            toggleStatisticView(true)
        }
        data.topAdsGetAutoAds?.let {
            if (it.data.status == 500 || it.data.status == 600) {
                showActiveAds()
            } else {
                showInactiveAds()
            }
        }
        data.topAdsGetShopInfo?.let {
            when(it.data.category){
                2 -> showNoTopAds()
            }
        }
    }

    private fun showActiveAds() {
        container_topads_inactive.visibility = View.GONE
        txt_empty_wording.visibility = View.GONE
        btn_go_to_dashboard_topads.visibility = View.VISIBLE
        detail_statistic_container.visibility = View.VISIBLE
        btn_expander.visibility = View.VISIBLE
    }

    private fun showNoTopAds() {
        detail_statistic_container.visibility = View.GONE
        container_topads_inactive.visibility = View.GONE
        btn_go_to_dashboard_topads.visibility = View.GONE
        txt_empty_wording.visibility = View.VISIBLE
        btn_expander.visibility = View.INVISIBLE
    }

    private fun showInactiveAds() {
        btn_go_to_dashboard_topads.visibility = View.GONE
        txt_empty_wording.visibility = View.GONE
        container_topads_inactive.visibility = View.VISIBLE
        detail_statistic_container.visibility = View.VISIBLE
        btn_expander.visibility = View.VISIBLE
    }

    private fun onError(t: Throwable) {
        t.printStackTrace()
        layout_ads_saldo.visibility = View.GONE
        layout_ads_statistic.visibility = View.GONE
        layout_error_state.visibility = View.VISIBLE
    }

    private fun onSuccessGetDeposit(data: TopAdsDepositResponse.Data) {
        data.topadsDashboardDeposits?.let {
            txt_saldo_topads.setText(it.data.amountFmt)
        }
    }

    private fun setupView() {
        context?.let {
            val txt = SpannableString(getString(R.string.wording_empty_ads))
            txt.setSpan(ForegroundColorSpan(ContextCompat.getColor(it, R.color.tkpd_main_green)), 73, txt.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            txt.setSpan(StyleSpan(Typeface.BOLD), 73, txt.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            txt_empty_wording.setText(txt)
            txt_empty_wording.setOnClickListener {
                RouteManager.route(context, ApplinkConst.SellerApp.TOPADS_AUTOADS)
                tracker?.eventTopAdsMulaiBeriklan()
            }
        }
        btn_expander.setOnClickListener {
            toggleStatisticView(statistic_container.visibility != View.VISIBLE)
        }
        btn_go_to_dashboard_topads.setOnClickListener {
            RouteManager.route(context, ApplinkConst.SellerApp.TOPADS_DASHBOARD)
            tracker?.eventTopAdsLihatDashboard()
        }
        btn_retry.setOnClickListener {
            loadData()
        }
        layout_ads_saldo.setOnClickListener {
            RouteManager.route(context, ApplinkConstInternalTopAds.TOPADS_HISTORY_CREDIT)
            tracker?.eventTopAdsCreditHistory()
        }
        btn_start_active_topads.setOnClickListener {
            RouteManager.route(context, ApplinkConst.SellerApp.TOPADS_DASHBOARD)
            tracker?.eventTopAdsAktifkan()
        }
    }

    private fun loadData() {
        layout_error_state.visibility = View.GONE
        layout_ads_statistic.visibility = View.VISIBLE
        layout_ads_saldo.visibility = View.VISIBLE
        viewModel.getTopAdsDeposit(this::onSuccessGetDeposit, this::onError)
        viewModel.getTopAdsStatisctic(this::onSuccessGetStatistic, this::onError)
    }

    private fun toggleStatisticView(isVisible: Boolean) {
        if (isVisible) {
            statistic_container.visibility = View.VISIBLE
            btn_expander.setImageResource(R.drawable.topads_widget_ic_up)
        } else {
            statistic_container.visibility = View.GONE
            btn_expander.setImageResource(R.drawable.topads_widget_ic_down)
        }
    }
}
