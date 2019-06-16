package com.tokopedia.power_merchant.subscribe.view.bottomsheets

import android.content.Context
import android.view.View
import android.widget.Button
import com.tokopedia.design.component.BottomSheets
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.view.contract.PmSubscribeContract

class PowerMerchantCancelBottomSheet : BottomSheets() {
    lateinit var buttonCancel: Button
    lateinit var listener: PmSubscribeContract.View

    override fun getLayoutResourceId(): Int {
        return R.layout.bottom_sheet_pm_cancel
    }

    override fun initView(view: View) {
        buttonCancel = view.findViewById(R.id.button_cancel_bs)
        buttonCancel.setOnClickListener {
            listener.cancelMembership()
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        listener = context as PmSubscribeContract.View
    }
}