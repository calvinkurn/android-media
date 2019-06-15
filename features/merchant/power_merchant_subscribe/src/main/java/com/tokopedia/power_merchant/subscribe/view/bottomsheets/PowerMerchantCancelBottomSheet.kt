package com.tokopedia.power_merchant.subscribe.view.bottomsheets

import android.view.View
import android.widget.Button
import com.tokopedia.design.component.BottomSheets
import com.tokopedia.power_merchant.subscribe.R
import kotlinx.android.synthetic.main.bottom_sheet_pm_cancel.*
import kotlinx.android.synthetic.main.partial_member_power_merchant.*

class PowerMerchantCancelBottomSheet() : BottomSheets() {
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
        unit()
    }

    interface PmSuccessBottomSheetListener {
        fun setPresenterAutoExtend()
    }
}