package com.tokopedia.saldodetails.view.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceCallback
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.saldodetails.R
import com.tokopedia.saldodetails.adapter.SaldoInfoVIewPagerAdapter
import com.tokopedia.saldodetails.commom.analytics.SaldoDetailsConstants
import com.tokopedia.saldodetails.contract.SaldoHoldInfoContract
import com.tokopedia.saldodetails.di.SaldoDetailsComponent
import com.tokopedia.saldodetails.di.SaldoDetailsComponentInstance
import com.tokopedia.saldodetails.presenter.SaldoHoldInfoPresenter
import com.tokopedia.saldodetails.response.model.saldoholdinfo.response.SaldoHoldDepositHistory
import com.tokopedia.saldodetails.response.model.saldoholdinfo.response.SaldoHoldInfoItem
import com.tokopedia.saldodetails.utils.CurrencyUtils
import com.tokopedia.saldodetails.view.fragment.SaldoHoldInfoFragment
import com.tokopedia.saldodetails.view.ui.SaldoHistoryTabItem
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.TabsUnify
import kotlinx.android.synthetic.main.saldo_hold_info_tabview.*
import kotlinx.android.synthetic.main.saldo_info_help_bottomsheet.view.*
import javax.inject.Inject

class SaldoHoldInfoActivity : BaseSimpleActivity(), HasComponent<SaldoDetailsComponent>, SaldoHoldInfoContract.View {

    var isTickerShow: Boolean? = false
    var tickerMessage: String? = null
    var sellerListSize: Int? = 0
    var buyerListSize: Int? = 0
    var viewPager: ViewPager? = null
    var arrayListSeller: ArrayList<SaldoHoldInfoItem>? = null
    var arrayListBuyer: ArrayList<SaldoHoldInfoItem>? = null
    var sellerAmount: Long = 0
    var buyerAmount: Long = 0
    var item: ArrayList<SaldoHistoryTabItem>? = null
    lateinit var tabLayout: TabsUnify
    val SALDO_SELLER_AMOUNT = "SALDO_SELLER_AMOUNT"
    val SALDO_BUYER_AMOUNT = "SALDO_BUYER_AMOUNT"
    val SAVE_INSTANCE_CACHEMANAGER_ID = "SAVE_INSTANCE_CACHEMANAGER_ID"
    val KEY_TYPE = "KEY_TYPE"
    val VALUE_SELLER_TYPE = 0
    val VALUE_BUYER_TYPE = 1

    companion object {
        val TAG: String = SaldoHoldInfoItem::class.java.simpleName
        val SALDOHOLD_FINTECH_PLT_PREPARE_METRICS = "saldoholdfintech_plt_prepare_metrics"
        val SALDOHOLD_FINTECH_PLT_NETWORK_METRICS = "saldoholdfintech_plt_network_metrics"
        val SALDOHOLD_FINTECH_PLT_RENDER_METRICS = "saldoholdfintech_plt_render_metrics"
    }

    private val performanceInterface by lazy { PageLoadTimePerformanceCallback(SALDOHOLD_FINTECH_PLT_PREPARE_METRICS, SALDOHOLD_FINTECH_PLT_NETWORK_METRICS, SALDOHOLD_FINTECH_PLT_RENDER_METRICS) as PageLoadTimePerformanceInterface }

    @Inject
    lateinit var saldoInfoPresenter: SaldoHoldInfoPresenter


    override fun onCreate(savedInstanceState: Bundle?) {
        performanceInterface.startMonitoring()
        performanceInterface.stopPreparePagePerformanceMonitoring()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.saldo_hold_info_tabview)
        SaldoDetailsComponentInstance.getComponent(this).inject(this)
        tabLayout = findViewById(R.id.tabs_saldo_info_type)
        viewPager = findViewById(R.id.view_pager_saldo_info_type)
        saldoInfoPresenter.attachView(this)
        clearFragmentManger()

        btn_bantuan.setOnClickListener {
            initBottomSheet()
        }
        performanceInterface.stopPreparePagePerformanceMonitoring()
        performanceInterface.startNetworkRequestPerformanceMonitoring()
        saldoInfoPresenter.getSaldoHoldInfo()
    }

    private fun clearFragmentManger() {
        supportFragmentManager.fragments.forEach {
            supportFragmentManager.beginTransaction().remove(it).commit()
        }

    }

    override fun getComponent(): SaldoDetailsComponent {
        return SaldoDetailsComponentInstance.getComponent(this)
    }

    override fun getNewFragment(): Fragment? = null

    override fun renderSaldoHoldInfo(saldoHoldDepositHistory: SaldoHoldDepositHistory?) {
        performanceInterface.stopNetworkRequestPerformanceMonitoring()
        performanceInterface.startRenderPerformanceMonitoring()
        saldoHoldDepositHistory?.let {

            tv_valueTotalSaldoHold.text = it.totalFmt
            sellerListSize = it.sellerData?.size
            buyerListSize = it.buyerData?.size
            arrayListSeller = it.sellerData as ArrayList<SaldoHoldInfoItem>
            arrayListBuyer = it.buyerData as ArrayList<SaldoHoldInfoItem>

            sellerListSize?.let {
                for (i in 0 until it) {
                    sellerAmount = CurrencyUtils.convertToCurrencyLongFromString(arrayListSeller?.get(i)?.amountFmt
                            ?: "0").let { it1 -> sellerAmount.plus(it1) }
                }
            }
            buyerListSize?.let {
                for (i in 0 until it) {
                    buyerAmount = CurrencyUtils.convertToCurrencyLongFromString(arrayListBuyer?.get(i)?.amountFmt
                            ?: "0").let { it1 -> buyerAmount.plus(it1) }
                }
            }
            isTickerShow = it.tickerMessageIsshow
            tickerMessage = it.tickerMessageId

            if (isTickerShow != null && isTickerShow == true) {
                saldo_hold_info_ticker.visibility = View.VISIBLE
                saldo_hold_info_ticker.setTextDescription(tickerMessage.toString())
            } else
                saldo_hold_info_ticker.visibility = View.GONE

        }

        setUpViewPager(sellerListSize, buyerListSize)
        initViewPagerAdapter()
        showLayout()
        performanceInterface.stopRenderPerformanceMonitoring()
    }

    private fun setUpViewPager(sellerListSize: Int?, buyerListSize: Int?) {
        item = ArrayList()
        if (sellerListSize == 0 && buyerListSize != 0) {

            val bundleBuyer = Bundle()
            val saveInstanceCacheManagerBuyer = SaveInstanceCacheManager(this, true)
            saveInstanceCacheManagerBuyer.apply {
                put(KEY_TYPE, VALUE_BUYER_TYPE)
                put(TAG, arrayListBuyer)
                buyerAmount.let {
                    put(SALDO_BUYER_AMOUNT, it)
                }
                put(SaldoHoldInfoFragment.TRANSACTION_TYPE, SaldoHoldInfoFragment.FOR_BUYER)
                this.id?.let {
                    bundleBuyer.putString(SAVE_INSTANCE_CACHEMANAGER_ID, id)
                }
            }

            val saldoHistoryTabItemBuyer = SaldoHistoryTabItem()
            saldoHistoryTabItemBuyer.fragment = SaldoHoldInfoFragment.createInstance(bundleBuyer)
            saldoHistoryTabItemBuyer.title = resources.getString(R.string.saldo_total_balance_buyer) + "(" + buyerListSize + ")"
            item?.add(saldoHistoryTabItemBuyer)

        } else if (buyerListSize == 0 && sellerListSize != 0) {

            val bundleSeller = Bundle()
            val saveInstanceCacheManagerSeller = SaveInstanceCacheManager(this, true)
            saveInstanceCacheManagerSeller.apply {
                put(KEY_TYPE, VALUE_SELLER_TYPE)
                put(TAG, arrayListSeller)
                sellerAmount.let {
                    put(SALDO_SELLER_AMOUNT, it)
                }
                put(SaldoHoldInfoFragment.TRANSACTION_TYPE, SaldoHoldInfoFragment.FOR_SELLER)
                this.id?.let {
                    bundleSeller.putString(SAVE_INSTANCE_CACHEMANAGER_ID, id)
                }
            }

            val saldoHistoryTabItemSeller = SaldoHistoryTabItem()
            saldoHistoryTabItemSeller.fragment = SaldoHoldInfoFragment.createInstance(bundleSeller)
            saldoHistoryTabItemSeller.title = resources.getString(R.string.saldo_total_balance_seller) + "(" + sellerListSize + ")"
            item?.add(saldoHistoryTabItemSeller)

        } else if (buyerListSize != 0 && sellerListSize != 0) {

            val bundlBuyer = Bundle()
            val saveInstanceCacheManagerBuyer = SaveInstanceCacheManager(this, true)
            saveInstanceCacheManagerBuyer.apply {
                put(KEY_TYPE, VALUE_BUYER_TYPE)
                put(TAG, arrayListBuyer)
                buyerAmount?.let {
                    put(SALDO_BUYER_AMOUNT, it)
                }
                this.id?.let {
                    bundlBuyer.putString(SAVE_INSTANCE_CACHEMANAGER_ID, id)
                }
            }

            val saldoHistoryTabItemBuyer = SaldoHistoryTabItem()
            saldoHistoryTabItemBuyer.fragment = SaldoHoldInfoFragment.createInstance(bundlBuyer)
            saldoHistoryTabItemBuyer.title = resources.getString(R.string.saldo_total_balance_buyer) + "(" + buyerListSize + ")"

            val bundleSeller = Bundle()

            val saveInstanceCacheManagerSeller = SaveInstanceCacheManager(this, true)
            saveInstanceCacheManagerSeller.apply {
                put(KEY_TYPE, VALUE_SELLER_TYPE)
                put(TAG, arrayListSeller)
                sellerAmount?.let {
                    put(SALDO_SELLER_AMOUNT, it)
                    bundleSeller.putInt("SELLER_ll_containerTYPE", 0)

                }
                this.id?.let {
                    bundleSeller.putString(SAVE_INSTANCE_CACHEMANAGER_ID, id)
                }
            }

            val saldoHistoryTabItemSeller = SaldoHistoryTabItem()
            saldoHistoryTabItemSeller.fragment = SaldoHoldInfoFragment.createInstance(bundleSeller)
            saldoHistoryTabItemSeller.title = resources.getString(R.string.saldo_total_balance_seller) + "(" + sellerListSize + ")"

            item?.add(0, saldoHistoryTabItemSeller)
            item?.add(1, saldoHistoryTabItemBuyer)

        }
        if (buyerListSize == 0 || sellerListSize == 0) {
            tabLayout.visibility = View.GONE
        }
    }

    private fun initViewPagerAdapter() {
        val adapter = item?.let { SaldoInfoVIewPagerAdapter(supportFragmentManager, it) }
        viewPager?.adapter = adapter
        viewPager?.let { tabLayout.setupWithViewPager(it) }
    }

    private fun initBottomSheet() {
        val bottomSheet = BottomSheetUnify()
        val view = LayoutInflater.from(this).inflate(R.layout.saldo_info_help_bottomsheet, null)
        view.apply {
            btn1.setOnClickListener {
                RouteManager.route(this@SaldoHoldInfoActivity, String.format("%s?url=%s",
                        ApplinkConst.WEBVIEW, SaldoDetailsConstants.SALDO_HOLD_HELP_URL))
                bottomSheet.dismiss()
            }
            btn2.setOnClickListener {
                RouteManager.route(this@SaldoHoldInfoActivity, String.format("%s?url=%s",
                        ApplinkConst.WEBVIEW, SaldoDetailsConstants.SALDO_HOLD_HELP_URL_TWO))
                bottomSheet.dismiss()
            }
        }
        supportFragmentManager.let {
            bottomSheet.apply {
                setChild(view)
                setTitle(this@SaldoHoldInfoActivity.getString(com.tokopedia.saldodetails.R.string.saldo_info_btn_text))
                show(it, "SaldoHoldBottomSheet")
            }
        }
    }

    override fun showErrorView() {
        performanceInterface.stopNetworkRequestPerformanceMonitoring()
        container_cl.visibility = View.GONE
        viewflipper_container.displayedChild = 1
        globalerror.setType(5)
        globalerror.setActionClickListener {
            saldoInfoPresenter.getSaldoHoldInfo()
        }
    }

    private fun showLayout() {
        container_cl.visibility = View.VISIBLE
        viewflipper_container.displayedChild = 0
    }

    override fun onDestroy() {
        super.onDestroy()
        saldoInfoPresenter.detachView()
        performanceInterface.stopMonitoring()

    }
}