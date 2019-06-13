package com.tokopedia.checkout.view.feature.bottomsheetpromostacking

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.tokopedia.checkout.R
import com.tokopedia.checkout.view.feature.promostacking.adapter.ClashingAdapter
import com.tokopedia.design.component.BottomSheets
import com.tokopedia.design.component.ButtonCompat
import com.tokopedia.promocheckout.common.view.uimodel.ClashingInfoDetailUiModel
import com.tokopedia.promocheckout.common.view.uimodel.ClashingVoucherOptionUiModel
import android.support.v7.widget.SimpleItemAnimator
import com.google.gson.Gson
import com.tokopedia.promocheckout.common.view.uimodel.ClashingVoucherOrderUiModel
import com.tokopedia.transactionanalytics.CheckoutAnalyticsCart
import com.tokopedia.transactionanalytics.CheckoutAnalyticsCourierSelection

/**
 * Created by fwidjaja on 10/03/19.
 */

open class ClashBottomSheetFragment : BottomSheets(), ClashingAdapter.ActionListener {

    private var mTitle: String? = null
    private lateinit var actionListener: ActionListener
    private lateinit var tvClashingInfoTicker: TextView
    private lateinit var uiModel: ClashingInfoDetailUiModel
    private lateinit var source: String
    private lateinit var type: String
    private lateinit var rvClashingOption: RecyclerView
    private lateinit var btSubmit: ButtonCompat
    private lateinit var adapter: ClashingAdapter
    private var checkoutAnalyticsCart: CheckoutAnalyticsCart? = null
    private var checkoutAnalyticsCourierSelection: CheckoutAnalyticsCourierSelection? = null

    interface ActionListener {
        fun onSubmitNewPromoAfterClash(oldPromoList: ArrayList<String>, newPromoList: ArrayList<ClashingVoucherOrderUiModel>, type: String)
    }

    companion object {
        @JvmStatic
        fun newInstance(): ClashBottomSheetFragment {
            return ClashBottomSheetFragment()
        }
    }

    fun setData(uiModel: ClashingInfoDetailUiModel) {
        this.uiModel = uiModel
    }

    fun setSource(source: String) {
        this.source = source
    }

    fun setType(type: String) {
        this.type = type
    }

    fun setActionListener(actionListener: ActionListener) {
        this.actionListener = actionListener
    }

    fun setAnalyticsCart(checkoutAnalyticsCart: CheckoutAnalyticsCart) {
        this.checkoutAnalyticsCart = checkoutAnalyticsCart
    }

    fun setAnalyticsShipment(checkoutAnalyticsCourierSelection: CheckoutAnalyticsCourierSelection) {
        this.checkoutAnalyticsCourierSelection = checkoutAnalyticsCourierSelection
    }

    override fun initView(view: View) {
        btSubmit = view.findViewById(R.id.bt_submit)
        rvClashingOption = view.findViewById(R.id.rv_clashing_option)
        tvClashingInfoTicker = view.findViewById(R.id.tv_clashing_info_ticker)
        tvClashingInfoTicker.text = uiModel.clashMessage

        adapter = ClashingAdapter()
        adapter.setListener(this)
        adapter.data = uiModel.options
        rvClashingOption.adapter = adapter
        rvClashingOption.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        (rvClashingOption.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        adapter.notifyDataSetChanged()

        setButtonSubmitVisibility()
    }

    override fun configView(parentView: View?) {
        super.configView(parentView)
        parentView?.findViewById<View>(R.id.layout_title)?.setOnClickListener(null)
        parentView?.findViewById<View>(R.id.btn_close)?.setOnClickListener{ onCloseButtonClick() }
    }

    private fun setButtonSubmitVisibility() {
        var isDataSelected = false
        for (model: ClashingVoucherOptionUiModel in adapter.data) {
            if (model.isSelected) {
                isDataSelected = true
                break
            }
        }

        btSubmit.isEnabled = isDataSelected
        if (btSubmit.isEnabled) {
            btSubmit.setOnClickListener {
                for (index in adapter.data.indices) {
                    if (index == 0) {
                        val oldPromoList = ArrayList<String>()
                        var oldPromoListStr = ""
                        adapter.data[index + 1].voucherOrders.forEach { voucherOrder ->
                            oldPromoList.add(voucherOrder.code)
                            if (oldPromoListStr.isEmpty()) {
                                oldPromoListStr = voucherOrder.code
                            } else {
                                oldPromoListStr = oldPromoListStr + "," + voucherOrder.code
                            }
                        }
                        if (adapter.data[index].isSelected) {
                            val newPromoList = ArrayList<String>()
                            adapter.data[index].voucherOrders.forEach { voucherOrder ->
                                newPromoList.add(voucherOrder.code)
                            }
                            if (newPromoList.size > 0) {
                                if (source.equals("cart", ignoreCase = true)) {
                                    checkoutAnalyticsCart?.eventClickSubmitPromoKonflik(newPromoList[0])
                                } else {
                                    checkoutAnalyticsCourierSelection?.eventSubmitPromoConflict(newPromoList[0])
                                }
                            }
                            dismiss()
                            actionListener.onSubmitNewPromoAfterClash(oldPromoList, adapter.data[index].voucherOrders, type)
                        } else {
                            if (oldPromoList.size > 0) {
                                if (source.equals("cart", ignoreCase = true)) {
                                    // checkoutAnalyticsCart?.eventClickSubmitPromoKonflik(Gson().toJson(oldPromoList))
                                    checkoutAnalyticsCart?.eventClickSubmitPromoKonflik(oldPromoListStr)
                                } else {
                                    // checkoutAnalyticsCourierSelection?.eventSubmitPromoConflict(Gson().toJson(oldPromoList))
                                    checkoutAnalyticsCourierSelection?.eventSubmitPromoConflict(oldPromoListStr)
                                }
                            }
                            dismiss()
                        }
                    }
                }
            }
        }
    }

    override fun getLayoutResourceId(): Int {
        return R.layout.bottom_sheet_clash_voucher
    }

    override fun title(): String {
        return getString(R.string.clash_bottomsheet_title)
    }

    override fun onVoucherItemSelected(index: Int, isSelected: Boolean) {
        var reverseIndex = 0
        for (i in adapter.data.indices) {
            if (i != index) {
                adapter.data[i].isSelected = false
                reverseIndex = i
            }
        }

        if (rvClashingOption.isComputingLayout) {
            rvClashingOption.post {
                adapter.notifyItemChanged(reverseIndex)
            }
        } else {
            adapter.notifyItemChanged(reverseIndex)
        }

        for (model: ClashingVoucherOptionUiModel in adapter.data) {
            if (model.isSelected) {
                val vouchers = ArrayList<String>()
                for (voucherModel: ClashingVoucherOrderUiModel in model.voucherOrders) {
                    vouchers.add(voucherModel.code)
                }

                var listVouchers = ""
                for (voucherModel: ClashingVoucherOrderUiModel in model.voucherOrders) {
                    if (listVouchers.isEmpty()) {
                        listVouchers = voucherModel.code
                    } else {
                        listVouchers = listVouchers + "," + voucherModel.code
                    }
                }

                if (source.equals("cart", ignoreCase = true)) {
                    // checkoutAnalyticsCart?.eventSelectPromoPromoKonflik(Gson().toJson(vouchers))
                    checkoutAnalyticsCart?.eventSelectPromoPromoKonflik(listVouchers)
                } else {
                    // checkoutAnalyticsCourierSelection?.eventSelectPromoConflict(Gson().toJson(vouchers))
                    checkoutAnalyticsCourierSelection?.eventSelectPromoConflict(listVouchers)
                }
                break
            }
        }

        setButtonSubmitVisibility()
    }

}