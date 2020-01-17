package com.tokopedia.saldodetails.view.activity

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.design.bottomsheet.CloseableBottomSheetDialog
import com.tokopedia.design.component.Tabs
import com.tokopedia.saldodetails.R
import com.tokopedia.saldodetails.adapter.SaldoInfoVIewPagerAdapter
import com.tokopedia.saldodetails.commom.analytics.SaldoDetailsConstants
import com.tokopedia.saldodetails.contract.SaldoHoldInfoContract
import com.tokopedia.saldodetails.di.SaldoDetailsComponent
import com.tokopedia.saldodetails.di.SaldoDetailsComponentInstance
import com.tokopedia.saldodetails.presenter.SaldoHoldInfoPresenter
import com.tokopedia.saldodetails.response.model.saldoholdinfo.response.BuyerDataItem
import com.tokopedia.saldodetails.response.model.saldoholdinfo.response.SaldoHoldDepositHistory
import com.tokopedia.saldodetails.response.model.saldoholdinfo.response.SellerDataItem
import com.tokopedia.saldodetails.view.fragment.SaldoHoldInfoFragment
import com.tokopedia.saldodetails.view.ui.SaldoHistoryTabItem
import kotlinx.android.synthetic.main.saldo_hold_info_tabview.*
import kotlinx.android.synthetic.main.saldo_info_help_bottomsheet.view.*
import kotlinx.android.synthetic.main.saldo_info_toolbar.*
import javax.inject.Inject

class SaldoHoldInfoActivity : BaseSimpleActivity(), HasComponent<SaldoDetailsComponent>, SaldoHoldInfoContract.View {

    var isTickerShow: Boolean? = false
    var tickerMessage: String? = null
    var sellerListSize:Int? = 0
    var buyerListSize :Int?= 0
    var viewPager: ViewPager? = null
    var resultList: ArrayList<Any>? = null
    var sellerAmount: Double? = 0.0
    var buyerAmount: Double? = 0.0
    lateinit var allTransactionList: ArrayList<Any>
    var item: ArrayList<SaldoHistoryTabItem>? = null
    lateinit var tabLayout: Tabs
    lateinit var helpdialog: CloseableBottomSheetDialog
    val SALDO_SELLER_AMOUNT = "SALDO_SELLER_AMOUNT"
    val SALDO_BUYER_AMOUNT = "SALDO_BUYER_AMOUNT"
    val RESULT_LIST = "RESULT_LIST"

    @Inject
    lateinit var saldoInfoPresenter: SaldoHoldInfoPresenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.saldo_hold_info_tabview)
        SaldoDetailsComponentInstance.getComponent(application).inject(this)
        tabLayout = findViewById(R.id.tabs_saldo_info_type)
        viewPager = findViewById<ViewPager>(R.id.view_pager_saldo_info_type)
        saldoInfoPresenter.attachView(this)
        saldoInfoPresenter.getSaldoHoldInfo()
        top_bar_close_button.setOnClickListener {
            onBackPressed()
        }

        btn_bantuan.setOnClickListener {
            initBottomSheet()
        }
    }

    override fun getComponent(): SaldoDetailsComponent {
        return SaldoDetailsComponentInstance.getComponent(application)
    }

    override fun getNewFragment(): Fragment? = null

    override fun renderSaldoHoldInfo(saldoHoldDepositHistory: SaldoHoldDepositHistory?) {
        resultList = ArrayList()

        saldoHoldDepositHistory?.let {

            tv_valueTotalSaldoHold.text = it.totalFmt
            sellerListSize = it.sellerData?.size
            buyerListSize = it.buyerData?.size
            val arrayListSeller = it.sellerData as ArrayList<SellerDataItem>
            val arrayListBuyer = it.buyerData as ArrayList<BuyerDataItem>

            for (i in 0 until arrayListSeller.size) {
                sellerAmount = arrayListSeller[i].amountFmt?.removeRP()?.let { it1 -> sellerAmount?.plus(it1) }
            }

            for (i in 0 until arrayListBuyer.size) {
                buyerAmount = arrayListBuyer[i].amountFmt?.removeRP()?.let { it1 -> buyerAmount?.plus(it1) }
            }

            resultList = combinedTransactionList(it.sellerData, it.buyerData)
            isTickerShow = it.tickerMessageIsshow
            tickerMessage = it.tickerMessageId
        }

        setUpViewPager(sellerListSize, buyerListSize)
        initViewPagerAdapter()
        showLayout()

    }

    fun combinedTransactionList(arrayList: ArrayList<SellerDataItem>, arrayList1: ArrayList<BuyerDataItem>): ArrayList<Any> {
        allTransactionList = ArrayList()
        allTransactionList.clear()
        allTransactionList.addAll(arrayList)
        allTransactionList.addAll(arrayList1)

        return allTransactionList
    }

    private fun setUpViewPager(sellerListSize: Int?, buyerListSize: Int?) {
        item = ArrayList()
        val bundle = Bundle()
        bundle.putParcelableArrayList(RESULT_LIST, resultList as ArrayList<out Parcelable>)
        buyerAmount?.let { bundle.putDouble(SALDO_BUYER_AMOUNT, buyerAmount!!) }
        sellerAmount?.let { bundle.putDouble(SALDO_SELLER_AMOUNT, sellerAmount!!) }


        if (sellerListSize == 0 && buyerListSize != 0) {
            val saldoHistoryTabItemBuyer = SaldoHistoryTabItem()
            saldoHistoryTabItemBuyer.fragment = SaldoHoldInfoFragment.createInstance(bundle)
            saldoHistoryTabItemBuyer.title = resources.getString(R.string.saldo_total_balance_buyer) + "(" + buyerListSize + ")"
            item?.add(saldoHistoryTabItemBuyer)

        } else if (buyerListSize == 0 && sellerListSize != 0) {
            val saldoHistoryTabItemSeller = SaldoHistoryTabItem()
            saldoHistoryTabItemSeller.fragment = SaldoHoldInfoFragment.createInstance(bundle)
            saldoHistoryTabItemSeller.title = resources.getString(R.string.saldo_total_balance_seller) + "(" + sellerListSize + ")"
            item?.add(saldoHistoryTabItemSeller)

        } else if (buyerListSize != 0 && sellerListSize != 0) {
            val saldoHistoryTabItemBuyer = SaldoHistoryTabItem()
            saldoHistoryTabItemBuyer.fragment = SaldoHoldInfoFragment.createInstance(bundle)
            saldoHistoryTabItemBuyer.title = resources.getString(R.string.saldo_total_balance_buyer) + "(" + buyerListSize + ")"

            val saldoHistoryTabItemSeller = SaldoHistoryTabItem()
            saldoHistoryTabItemSeller.fragment = SaldoHoldInfoFragment.createInstance(bundle)
            saldoHistoryTabItemSeller.title = resources.getString(R.string.saldo_total_balance_seller) + "(" + sellerListSize + ")"

            item?.add(0, saldoHistoryTabItemBuyer)
            item?.add(1, saldoHistoryTabItemSeller)
        }

        if (buyerListSize == 0 || sellerListSize == 0) {
            tabLayout.visibility = View.GONE
        }
    }

    private fun initViewPagerAdapter() {
        val adapter = item?.let { SaldoInfoVIewPagerAdapter(supportFragmentManager, it) }
        viewPager?.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)
    }

    private fun initBottomSheet() {
        helpdialog = CloseableBottomSheetDialog.createInstanceRounded(this)
        val view = LayoutInflater.from(this).inflate(R.layout.saldo_info_help_bottomsheet, null)
        helpdialog.setContentView(view)
        view.btn1.setOnClickListener {
            RouteManager.route(this, String.format("%s?url=%s",
                    ApplinkConst.WEBVIEW, SaldoDetailsConstants.SALDO_HOLD_HELP_URL))
            helpdialog.cancel()
        }
        view.btn2.setOnClickListener {
            RouteManager.route(this, String.format("%s?url=%s",
                    ApplinkConst.WEBVIEW, SaldoDetailsConstants.SALDO_HOLD_HELP_URL_TWO))
            helpdialog.cancel()
        }

        helpdialog.show()

    }

    override fun showErrorView() {
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
    }

}

private fun String?.removeRP(): Double? {
    return this?.substring(2)?.trim()?.toDouble()?.times(1000)
}

