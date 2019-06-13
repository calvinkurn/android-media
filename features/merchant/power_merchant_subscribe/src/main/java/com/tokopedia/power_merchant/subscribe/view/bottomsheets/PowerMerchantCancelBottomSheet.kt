package com.tokopedia.power_merchant.subscribe.view.bottomsheets

import android.view.View
import com.tokopedia.design.component.BottomSheets
import com.tokopedia.power_merchant.subscribe.R
import kotlinx.android.synthetic.main.bottom_sheet_pm_cancel.*

class PowerMerchantCancelBottomSheet: BottomSheets() {
    override fun getLayoutResourceId(): Int {
        return R.layout.bottom_sheet_pm_cancel
    }

    override fun initView(view: View?) {
        button_cancel.setOnClickListener {

        }
    }
}