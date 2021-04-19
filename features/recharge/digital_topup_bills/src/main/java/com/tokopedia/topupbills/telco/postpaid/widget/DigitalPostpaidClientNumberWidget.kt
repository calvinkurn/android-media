package com.tokopedia.topupbills.telco.postpaid.widget

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.AttrRes
import com.tokopedia.common.topupbills.data.TelcoEnquiryData
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.topupbills.R
import com.tokopedia.topupbills.telco.postpaid.listener.ClientNumberPostpaidListener
import com.tokopedia.topupbills.telco.prepaid.widget.DigitalClientNumberWidget
import com.tokopedia.unifycomponents.UnifyButton

/**
 * Created by nabillasabbaha on 26/04/19.
 */
class DigitalPostpaidClientNumberWidget : DigitalClientNumberWidget {

    private lateinit var btnEnquiry: UnifyButton
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
        return R.layout.view_telco_input_number_postpaid
    }

    fun initV() {
        btnEnquiry = view.findViewById(R.id.telco_enquiry_btn)
        titleEnquiryResult = view.findViewById(R.id.telco_title_enquiry_result)
        enquiryResult = view.findViewById(R.id.telco_enquiry_result)

        btnEnquiry.setOnClickListener {
            if (btnEnquiry.isEnabled) {
                postpaidListener.enquiryNumber()
            }
        }
    }

    fun resetClientNumberPostpaid() {
        enquiryResult.removeAllViews()
        btnEnquiry.show()
        titleEnquiryResult.gone()
        enquiryResult.gone()
        setButtonEnquiry(false)
    }

    fun resetEnquiryResult() {
        btnEnquiry.show()
        titleEnquiryResult.gone()
        enquiryResult.gone()
    }

    fun setButtonEnquiry(enable: Boolean) {
        btnEnquiry.isEnabled = enable
    }

    fun setLoadingButtonEnquiry(loading: Boolean) {
        btnEnquiry.isLoading = loading
    }

    fun showEnquiryResultPostpaid(telcoEnquiryData: TelcoEnquiryData) {
        enquiryResult.removeAllViews()
        if (telcoEnquiryData.enquiry.attributes.mainInfoList != null) {
            for (item in telcoEnquiryData.enquiry.attributes.mainInfoList) {
                if (!TextUtils.isEmpty(item.value)) {
                    val billsResult = DigitalTelcoBillsResultWidget(context)
                    billsResult.setLabel(item.label)
                    billsResult.setValue(item.value)
                    enquiryResult.addView(billsResult)
                }
            }
        }
        btnEnquiry.gone()
        titleEnquiryResult.show()
        enquiryResult.show()
    }

    fun setPostpaidListener(listener: ClientNumberPostpaidListener) {
        this.postpaidListener = listener
    }
}
