package com.tokopedia.power_merchant.subscribe.view.bottomsheets

import android.view.View
import android.widget.Button
import com.tokopedia.design.component.BottomSheets
import com.tokopedia.power_merchant.subscribe.R

class PowerMerchantCancelBottomSheet : BottomSheets() {
    lateinit var buttonCancel: Button
    private var listener: BottomSheetCancelListener? = null

    interface BottomSheetCancelListener{
        fun onclickButton()
    }

    override fun getLayoutResourceId(): Int {
        return R.layout.bottom_sheet_pm_cancel
    }

    fun setListener(listener: BottomSheetCancelListener) {
        this.listener = listener
    }

    override fun title(): String {
        super.title()
        return ""
    }

    override fun initView(view: View) {
        buttonCancel = view.findViewById(R.id.button_cancel_bs)
        buttonCancel.setOnClickListener {
            listener?.onclickButton()
        }
        updateHeight()
    }

}