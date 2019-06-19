package com.tokopedia.checkout.view.feature.shipment

import android.app.Activity
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.tokopedia.checkout.R
import com.tokopedia.design.base.BaseDialog
import com.tokopedia.design.component.ButtonCompat

/**
 * Created by Irfan Khoirul on 2019-06-19.
 */

class PromoNotEligibleDialog(activity: Activity) : BaseDialog(activity) {

    lateinit var tvTitle: TextView
    lateinit var tvInfo: TextView
    lateinit var rvPromoList: RecyclerView
    lateinit var btnContinue: ButtonCompat
    lateinit var btnCancel: ButtonCompat

    override fun layoutResId(): Int {
        return R.layout.dialog_promo_not_eligible
    }

    override fun initView(dialogView: View) {
        tvTitle = dialogView.findViewById(R.id.tv_title)
        tvInfo = dialogView.findViewById(R.id.tv_info)
        rvPromoList = dialogView.findViewById(R.id.rv_promo_list)
        btnContinue = dialogView.findViewById(R.id.btn_continue)
        btnCancel = dialogView.findViewById(R.id.btn_cancel)
    }

    override fun initListener(dialog: AlertDialog?) {
        dialog?.setCancelable(false)
        dialog?.setCanceledOnTouchOutside(false)
    }

}