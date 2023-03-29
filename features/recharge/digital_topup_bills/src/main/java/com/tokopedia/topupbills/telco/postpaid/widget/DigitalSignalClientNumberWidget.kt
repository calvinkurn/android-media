package com.tokopedia.topupbills.telco.postpaid.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.annotation.AttrRes
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.topupbills.R
import com.tokopedia.topupbills.telco.common.widget.DigitalClientNumberWidget
import com.tokopedia.topupbills.telco.postpaid.listener.ClientNumberPostpaidListener
import com.tokopedia.unifycomponents.UnifyButton

class DigitalSignalClientNumberWidget : DigitalClientNumberWidget {

    private lateinit var btnCheckout: UnifyButton
    private lateinit var prefixOperatorResult: LinearLayout
    private lateinit var postpaidListener: ClientNumberPostpaidListener

    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView()
    }

    override fun getLayout(): Int {
        return R.layout.view_telco_input_number_signal
    }

    fun initView() {
        btnCheckout = view.findViewById(R.id.signal_checkout_button)
        prefixOperatorResult = view.findViewById(R.id.signal_operator)

        btnCheckout.setOnClickListener {
            if (btnCheckout.isClickable) {
                postpaidListener.enquiryNumber()
            }
        }
    }

    fun resetClientNumberPostpaid() {
        prefixOperatorResult.removeAllViews()
        btnCheckout.show()
        prefixOperatorResult.gone()
    }

    fun setButtonEnquiry(enable: Boolean) {
        btnCheckout.isClickable = enable
    }

    fun showOperatorResult(text: Pair<String, String>) {
        prefixOperatorResult.removeAllViews()
        val operatorResultWidget = DigitalSignalOperatorResultWidget(context)
        operatorResultWidget.setLabel(text.first)
        operatorResultWidget.setValue(text.second)
        prefixOperatorResult.addView(operatorResultWidget)
        prefixOperatorResult.show()
    }

    fun setPostpaidListener(listener: ClientNumberPostpaidListener) {
        this.postpaidListener = listener
    }
}