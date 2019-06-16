package com.tokopedia.power_merchant.subscribe.view.bottomsheets

import android.view.View
import android.widget.Button
import com.tokopedia.design.component.BottomSheets
import com.tokopedia.power_merchant.subscribe.R

class PowerMerchantCancelBottomSheet : BottomSheets() {
    lateinit var buttonCancel: Button

    override fun getLayoutResourceId(): Int {
        return R.layout.bottom_sheet_pm_cancel
    }

    override fun initView(view: View) {
        buttonCancel = view.findViewById(R.id.button_cancel_bs)
        buttonCancel.setOnClickListener {

        }
    }

    fun setCancelButtonPm(unit: () -> Unit) {
        this.dismiss()
        unit()
    }

}