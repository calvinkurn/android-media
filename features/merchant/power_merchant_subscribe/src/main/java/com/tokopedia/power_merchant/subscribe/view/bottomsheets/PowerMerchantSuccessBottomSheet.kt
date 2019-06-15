package com.tokopedia.power_merchant.subscribe.view.bottomsheets

import android.content.Context
import android.view.View
import android.widget.Button
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.design.component.BottomSheets
import com.tokopedia.power_merchant.subscribe.IMG_URL_BS_SUCCESS
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.view.contract.PmSubscribeContract
import kotlinx.android.synthetic.main.bottom_sheets_pm_success.view.*

class PowerMerchantSuccessBottomSheet : BottomSheets(){

    lateinit var buttonSubmit: Button
    lateinit var listener : PmSubscribeContract.View

    override fun getLayoutResourceId(): Int {
        return R.layout.bottom_sheets_pm_success
    }

    override fun initView(view: View) {
        ImageHandler.LoadImage(view.img_btm_sheets, IMG_URL_BS_SUCCESS)
        buttonSubmit = view.findViewById(R.id.button_checknow)
        view.txt_success_header_bs.text = ""
        view.txt_success_desc_bs.text = ""

        buttonSubmit.setOnClickListener {
            listener.refreshData()
        }

    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        listener = context as PmSubscribeContract.View

    }

}