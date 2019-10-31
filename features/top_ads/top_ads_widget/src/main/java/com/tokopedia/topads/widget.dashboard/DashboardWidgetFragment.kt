package com.tokopedia.topads.widget.dashboard

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
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
import com.tokopedia.topads.widget.R
import com.tokopedia.topads.widget.dashboard.data.TopAdsDepositResponse
import com.tokopedia.topads.widget.dashboard.data.TopAdsStatisticResponse
import com.tokopedia.topads.widget.dashboard.di.DaggerDashboardWidgetComponent
import com.tokopedia.topads.widget.dashboard.di.DashboardWidgetComponent
import com.tokopedia.topads.widget.dashboard.viewmodel.DashboardWidgetViewModel
import kotlinx.android.synthetic.main.fragment_widget_dashboard_topads.*
import javax.inject.Inject

/**
 * Author errysuprayogi on 25,October,2019
 */
class DashboardWidgetFragment : BaseDaggerFragment() {


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: DashboardWidgetViewModel

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
        viewModel.getTopAdsDeposit(this::onSuccessGetDeposit, this::onError)
        viewModel.getTopAdsStatisctic(this::onSuccessGetStatistic, this::onError)
        setupView()
    }

    private fun onSuccessGetStatistic(data: TopAdsStatisticResponse.Data) {
        data.topadsDashboardStatistics?.let {
            txt_impresion_count.setText(it.data.summary.adsImpressionSumFmt)
            txt_click_count.setText(it.data.summary.adsClickSumFmt)
            txt_profit_count.setText(it.data.summary.adsAllGrossProfitFmt)
            txt_spend_count.setText(it.data.summary.adsCostSumFmt)
            toggleStatisticView(true)
        }
    }

    private fun onError(t: Throwable) {
        t.printStackTrace()
    }

    private fun onSuccessGetDeposit(data: TopAdsDepositResponse.Data) {
        data.topadsDashboardDeposits?.let {
            txt_saldo_topads.setText(it.data.amountFmt)
        }
    }

    private fun setupView() {
        context?.let {
            val txt = SpannableString(getString(R.string.wording_empty_ads))
            txt.setSpan(ForegroundColorSpan(ContextCompat.getColor(it, R.color.tkpd_main_green)), 74, txt.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            txt.setSpan(StyleSpan(Typeface.BOLD), 74, txt.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            txt.setSpan(object : ClickableSpan(){
                override fun onClick(widget: View) {
                    RouteManager.route(context, ApplinkConst.SellerApp.TOPADS_AUTOADS)
                }
            }, 74, txt.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            txt_empty_wording.setText(txt)
            txt_empty_wording.movementMethod = LinkMovementMethod.getInstance()
        }
        btn_expander.setOnClickListener {
            toggleStatisticView(statistic_container.visibility != View.VISIBLE)
        }
        btn_go_to_dashboard_topads.setOnClickListener {
            RouteManager.route(context, ApplinkConst.SellerApp.TOPADS_DASHBOARD)
        }
    }

    private fun toggleEmptyView(isVisible: Boolean) {
        statistic_container.visibility = View.GONE
        if (isVisible) {
            empty_state_container.visibility = View.VISIBLE
            btn_expander.setImageResource(R.drawable.topads_widget_ic_up)
        } else {
            empty_state_container.visibility = View.GONE
            btn_expander.setImageResource(R.drawable.topads_widget_ic_down)
        }
    }

    private fun toggleStatisticView(isVisible: Boolean) {
        empty_state_container.visibility = View.GONE
        if (isVisible) {
            statistic_container.visibility = View.VISIBLE
            btn_expander.setImageResource(R.drawable.topads_widget_ic_up)
        } else {
            statistic_container.visibility = View.GONE
            btn_expander.setImageResource(R.drawable.topads_widget_ic_down)
        }
    }
}
