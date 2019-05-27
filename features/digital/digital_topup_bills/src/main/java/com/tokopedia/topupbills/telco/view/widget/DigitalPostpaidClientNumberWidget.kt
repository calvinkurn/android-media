package com.tokopedia.topupbills.telco.view.widget

import android.content.Context
import android.support.annotation.AttrRes
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import com.tokopedia.topupbills.R
import com.tokopedia.topupbills.getColorFromResources
import com.tokopedia.topupbills.telco.view.listener.ClientNumberPostpaidListener

/**
 * Created by nabillasabbaha on 26/04/19.
 */
class DigitalPostpaidClientNumberWidget : DigitalClientNumberWidget {

    private lateinit var btnEnquiry: Button
    private lateinit var enquiryLayout: RelativeLayout
    private lateinit var operatorName: TextView
    private lateinit var userName: TextView
    private lateinit var totalBills: TextView
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
        enquiryLayout = view.findViewById(R.id.enquiry_layout)
        operatorName = view.findViewById(R.id.operator_name)
        userName = view.findViewById(R.id.user_name)
        totalBills = view.findViewById(R.id.total_bills)

        btnEnquiry.setOnClickListener {
            if (btnEnquiry.isEnabled) {
                postpaidListener.enquiryNumber()
            }
        }
    }

    fun resetClientNumberPostpaid() {
        btnEnquiry.isEnabled = false
        btnEnquiry.visibility = View.VISIBLE
        enquiryLayout.visibility = View.GONE
        btnEnquiry.setBackgroundResource(R.drawable.grey_button_rounded)
        btnEnquiry.setTextColor(context.resources.getColorFromResources(context, R.color.digital_text_enquiry_non_active))
    }

    fun setButtonEnquiryEnable() {
        btnEnquiry.isEnabled = true
        btnEnquiry.setBackgroundResource(R.drawable.bg_button_orange)
        btnEnquiry.setTextColor(context.resources.getColorFromResources(context, R.color.white))
    }

    fun showEnquiryResultPostpaid() {
        btnEnquiry.visibility = View.GONE
        enquiryLayout.visibility = View.VISIBLE
    }

    fun setPostpaidListener(listener: ClientNumberPostpaidListener) {
        this.postpaidListener = listener
    }
}
