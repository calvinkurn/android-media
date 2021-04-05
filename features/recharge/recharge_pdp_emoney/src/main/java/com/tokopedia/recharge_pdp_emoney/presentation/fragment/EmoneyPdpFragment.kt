package com.tokopedia.recharge_pdp_emoney.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.common.topupbills.data.TopupBillsPromo
import com.tokopedia.common.topupbills.data.TopupBillsRecommendation
import com.tokopedia.common.topupbills.utils.CommonTopupBillsGqlMutation
import com.tokopedia.common.topupbills.utils.CommonTopupBillsGqlQuery
import com.tokopedia.common.topupbills.view.viewmodel.TopupBillsViewModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.recharge_pdp_emoney.R
import com.tokopedia.recharge_pdp_emoney.di.EmoneyPdpComponent
import com.tokopedia.recharge_pdp_emoney.presentation.adapter.EmoneyPdpFragmentPagerAdapter
import com.tokopedia.recharge_pdp_emoney.presentation.viewmodel.EmoneyPdpViewModel
import com.tokopedia.recharge_pdp_emoney.utils.EmoneyPdpMapper
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.unifycomponents.ticker.TickerData
import com.tokopedia.unifycomponents.ticker.TickerPagerAdapter
import com.tokopedia.unifycomponents.ticker.TickerPagerCallback
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_emoney_pdp.*
import javax.inject.Inject

/**
 * @author by jessica on 29/03/21
 */

class EmoneyPdpFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelFragmentProvider by lazy { ViewModelProvider(requireActivity(), viewModelFactory) }
    private val topUpBillsViewModel by lazy { viewModelFragmentProvider.get(TopupBillsViewModel::class.java) }
    private val emoneyPdpViewModel by lazy { viewModelFragmentProvider.get(EmoneyPdpViewModel::class.java) }

    override fun getScreenName(): String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_emoney_pdp, container, false)
    }

    override fun initInjector() {
        getComponent(EmoneyPdpComponent::class.java).inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadData()

        // dummy
        // will replace this
        emoneyPdpHeaderView.titleText = "Cek saldo kamu di sini"
        emoneyPdpHeaderView.subtitleText = "Yuk, coba cek sisa saldo di kartu e-money kamu."
        emoneyPdpHeaderView.buttonCtaText = "Cek Saldo"
        emoneyPdpInputCardWidget.initView()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        topUpBillsViewModel.menuDetailData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    renderRecommendationsAndPromoList(it.data.recommendations, it.data.promos)
                    renderTicker(EmoneyPdpMapper.mapTopUpBillsTickersToTickersData(it.data.tickers))
                }
                is Fail -> {
                    emoneyPdpViewModel.setErrorMessage(it.throwable)
                }
            }
        })

        emoneyPdpViewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            renderErrorMessage()
        })
    }

    private fun loadData() {
        topUpBillsViewModel.getMenuDetail(CommonTopupBillsGqlQuery.catalogMenuDetail,
                topUpBillsViewModel.createMenuDetailParams(267))

        topUpBillsViewModel.getFavoriteNumbers(
                CommonTopupBillsGqlMutation.favoriteNumber,
                topUpBillsViewModel.createFavoriteNumbersParams(34))
    }

    private fun renderRecommendationsAndPromoList(recommendations: List<TopupBillsRecommendation>,
                                                  promoList: List<TopupBillsPromo>) {

        if (recommendations.isEmpty() && promoList.isEmpty()) {
            emoneyPdpViewPager.hide()
            return
        }

        if (recommendations.isNotEmpty()) emoneyPdpTab.addNewTab(getString(R.string.recharge_pdp_emoney_recents_tab))
        if (promoList.isNotEmpty()) emoneyPdpTab.addNewTab(getString(R.string.recharge_pdp_emoney_promo_tab))

        val adapter = EmoneyPdpFragmentPagerAdapter(this, recommendations, promoList)
        emoneyPdpViewPager.adapter = adapter

        emoneyPdpTab.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                tab.select()
                emoneyPdpViewPager.currentItem = tab.position
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {}
            override fun onTabReselected(p0: TabLayout.Tab?) {}
        })

        emoneyPdpViewPager?.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                val tab = emoneyPdpTab?.getUnifyTabLayout()?.getTabAt(position)
                tab?.select()
            }
        })
    }

    private fun renderTicker(tickers: List<TickerData>) {
        if (tickers.isEmpty()) {
            emoneyPdpTicker.hide()
            return
        }

        if (tickers.size == 1) setUpSingleTicker(tickers.first())
        else setUpMultipleTicker(tickers)
        emoneyPdpTicker.show()
    }

    private fun setUpSingleTicker(ticker: TickerData) {
        emoneyPdpTicker.tickerTitle = ticker.title
        emoneyPdpTicker.setHtmlDescription(ticker.description)
        emoneyPdpTicker.tickerType = ticker.type
        emoneyPdpTicker.setDescriptionClickEvent(object : TickerCallback {
            override fun onDescriptionViewClick(linkUrl: CharSequence) {
                RouteManager.route(context, "${ApplinkConst.WEBVIEW}?url=${linkUrl}")
            }

            override fun onDismiss() {}
        })
    }

    private fun setUpMultipleTicker(tickers: List<TickerData>) {
        context?.let { context ->
            val tickerAdapter = TickerPagerAdapter(context, tickers)
            tickerAdapter.setPagerDescriptionClickEvent(object : TickerPagerCallback {
                override fun onPageDescriptionViewClick(linkUrl: CharSequence, itemData: Any?) {
                    RouteManager.route(context, "${ApplinkConst.WEBVIEW}?url=${linkUrl}")
                }
            })
            emoneyPdpTicker.addPagerView(tickerAdapter, tickers)
        }
    }

    private fun renderErrorMessage() {

    }
}