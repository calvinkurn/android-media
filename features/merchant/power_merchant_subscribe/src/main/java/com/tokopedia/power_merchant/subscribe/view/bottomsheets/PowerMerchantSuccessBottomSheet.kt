package com.tokopedia.power_merchant.subscribe.view.bottomsheets

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.design.component.BottomSheets
import com.tokopedia.design.component.TextViewCompat
import com.tokopedia.power_merchant.subscribe.IMG_URL_BS_SUCCESS
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.view.contract.PmSubscribeContract
import kotlinx.android.synthetic.main.bottom_sheets_pm_success.view.*

class PowerMerchantSuccessBottomSheet : BottomSheets() {

    lateinit var buttonSubmit: Button
    private lateinit var listener: PmSubscribeContract.View
    lateinit var imgSuccessPm: ImageView
    lateinit var txtSuccessHeaderBs: TextViewCompat
    lateinit var txtSuccessDescBs: TextViewCompat
    var isTransitionPeriod:Boolean = false

    companion object {
        const val ARGUMENT_DATA_BOTTOM_SHEET_SUCCESS = "data_success"
        @JvmStatic
        fun newInstance(isTransitionPeriod: Boolean): PowerMerchantSuccessBottomSheet {
            val bundle = Bundle()
            bundle.putBoolean(ARGUMENT_DATA_BOTTOM_SHEET_SUCCESS, isTransitionPeriod)
            val fragment = PowerMerchantSuccessBottomSheet()
            fragment.arguments = bundle

            return fragment
        }
    }

    fun setListener(listener: PmSubscribeContract.View){
        this.listener = listener
    }
    private fun getArgumentsValue() {
        isTransitionPeriod = arguments?.getBoolean(ARGUMENT_DATA_BOTTOM_SHEET_SUCCESS,false) ?: false
    }

    override fun getLayoutResourceId(): Int {
        return R.layout.bottom_sheets_pm_success
    }

    override fun initView(view: View) {
        getArgumentsValue()
        imgSuccessPm = view.findViewById(R.id.img_btm_sheets)
        buttonSubmit = view.findViewById(R.id.button_checknow)
        txtSuccessHeaderBs = view.findViewById(R.id.txt_success_header_bs)
        txtSuccessDescBs = view.findViewById(R.id.txt_success_desc_bs)
        ImageHandler.LoadImage(view.img_btm_sheets, IMG_URL_BS_SUCCESS)
        if (isTransitionPeriod) {
            txtSuccessDescBs.text = getString(R.string.pm_label_bs_success_desc_transition)
            txtSuccessHeaderBs.text = getString(R.string.pm_label_bs_success_header_transition)
            buttonSubmit.text = getString(R.string.pm_label_bs_success_button_transition)
        }

        buttonSubmit.setOnClickListener {
            listener.refreshData()
        }
    }
}