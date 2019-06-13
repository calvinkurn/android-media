package com.tokopedia.power_merchant.subscribe.view.bottomsheets

import android.view.View
import com.tokopedia.design.component.BottomSheets
import com.tokopedia.power_merchant.subscribe.R
import kotlinx.android.synthetic.main.bottom_sheets_pm_success.*

class PowerMerchantSuccessBottomSheet: BottomSheets() {


    override fun getLayoutResourceId(): Int {
        return R.layout.bottom_sheets_pm_success
    }

    override fun initView(view: View?) {
        button_checknow.setOnClickListener {

        }


    }

}