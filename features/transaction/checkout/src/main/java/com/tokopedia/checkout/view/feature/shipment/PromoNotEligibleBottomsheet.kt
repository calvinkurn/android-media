package com.tokopedia.checkout.view.feature.shipment

import android.app.Dialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.tokopedia.checkout.R
import com.tokopedia.checkout.view.feature.shipment.adapter.PromoNotEligibleAdapter
import com.tokopedia.checkout.view.feature.shipment.viewmodel.NotEligiblePromoHolderdata
import com.tokopedia.design.component.BottomSheets
import com.tokopedia.design.component.ButtonCompat

/**
 * Created by Irfan Khoirul on 2019-06-19.
 */

class PromoNotEligibleBottomsheet : BottomSheets() {

    lateinit var tvInfo: TextView
    lateinit var rvPromoList: RecyclerView
    lateinit var btnContinue: ButtonCompat
    lateinit var actionListener: PromoNotEligibleActionListener
    lateinit var notEligiblePromoHolderDataList: ArrayList<NotEligiblePromoHolderdata>
    var checkoutType: Int = 0

    companion object {
        @JvmStatic
        fun createInstance(): PromoNotEligibleBottomsheet {
            return PromoNotEligibleBottomsheet()
        }
    }

    fun setListener(actionListener: PromoNotEligibleActionListener) {
        this.actionListener = actionListener
    }

    override fun getLayoutResourceId(): Int {
        return R.layout.dialog_promo_not_eligible
    }

    override fun initView(dialogView: View) {
        tvInfo = dialogView.findViewById(R.id.tv_info)
        rvPromoList = dialogView.findViewById(R.id.rv_promo_list)
        btnContinue = dialogView.findViewById(R.id.btn_continue)
        btnContinue.setOnClickListener {
            actionListener.onButtonContinueClicked(checkoutType)
        }

        val adapter = PromoNotEligibleAdapter()
        adapter.notEligiblePromoHolderDataList = notEligiblePromoHolderDataList
        val linearLayoutManager = LinearLayoutManager(activity)
        rvPromoList.layoutManager = linearLayoutManager
        rvPromoList.adapter = adapter
    }

    override fun setupDialog(dialog: Dialog?, style: Int) {
        super.setupDialog(dialog, style)
        actionListener.onShow()
    }

    override fun title(): String {
        return getString(R.string.label_continue_to_payment)
    }

}