package com.tokopedia.topupbills.telco.view.widget

import android.content.Context
import android.support.annotation.AttrRes
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.tokopedia.topupbills.R
import com.tokopedia.topupbills.getColorFromResources
import com.tokopedia.topupbills.telco.data.TelcoEnquiryData
import com.tokopedia.topupbills.telco.view.listener.ClientNumberPostpaidListener

/**
 * Created by nabillasabbaha on 26/04/19.
 */
class DigitalPostpaidClientNumberWidget : DigitalClientNumberWidget {

    private lateinit var btnEnquiry: Button
    private lateinit var enquiryResult: LinearLayout
    private lateinit var titleEnquiryResult: TextView
    private lateinit var postpaidListener: ClientNumberPostpaidListener

    constructor(context: Context) : super(context) {
        initV()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initV()
    }

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initV()
    }

    override fun getLayout(): Int {
        return R.layout.view_digital_input_number_telco
    }

    fun initV() {
        btnEnquiry = view.findViewById(R.id.button_next_enquiry)
        titleEnquiryResult = view.findViewById(R.id.title_enquiry_result)
        enquiryResult = view.findViewById(R.id.enquiry_result)

        btnEnquiry.setOnClickListener {
            if (btnEnquiry.isEnabled) {
                postpaidListener.enquiryNumber()
            }
        }
    }

    fun resetClientNumberPostpaid() {
        btnEnquiry.isEnabled = false
        enquiryResult.removeAllViews()
        btnEnquiry.visibility = View.VISIBLE
        titleEnquiryResult.visibility = View.GONE
        btnEnquiry.setBackgroundResource(R.drawable.grey_button_rounded)
        btnEnquiry.setTextColor(context.resources.getColorFromResources(context, R.color.digital_text_enquiry_non_active))
    }

    fun setButtonEnquiryEnable() {
        btnEnquiry.isEnabled = true
        btnEnquiry.setBackgroundResource(R.drawable.bg_button_orange)
        btnEnquiry.setTextColor(context.resources.getColorFromResources(context, R.color.white))
    }

    fun showEnquiryResultPostpaid(telcoEnquiryData: TelcoEnquiryData) {
        enquiryResult.removeAllViews()
        for(item in telcoEnquiryData.enquiry.attributes.mainInfoList) {
            if (!TextUtils.isEmpty(item.value)) {
                val billsResult = DigitalTelcoBillsResultWidget(context)
                billsResult.setLabel(item.label)
                billsResult.setValue(item.value)
                enquiryResult.addView(billsResult)
            }
        }
        btnEnquiry.visibility = View.GONE
        titleEnquiryResult.visibility = View.VISIBLE
    }

    fun setPostpaidListener(listener: ClientNumberPostpaidListener) {
        this.postpaidListener = listener
    }
}
