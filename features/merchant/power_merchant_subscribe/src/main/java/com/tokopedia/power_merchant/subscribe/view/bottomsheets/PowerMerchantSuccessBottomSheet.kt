package com.tokopedia.power_merchant.subscribe.view.bottomsheets

import android.view.View
import android.widget.Button
import com.tokopedia.design.component.BottomSheets
import com.tokopedia.power_merchant.subscribe.R
import kotlinx.android.synthetic.main.bottom_sheets_pm_success.*

class PowerMerchantSuccessBottomSheet: BottomSheets() {

    lateinit var buttonSubmit: Button

    override fun getLayoutResourceId(): Int {
        return R.layout.bottom_sheets_pm_success
    }

    override fun initView(view: View) {
        buttonSubmit = view.findViewById(R.id.button_checknow)
        buttonSubmit.setOnClickListener {

        }


    }

}