package com.tokopedia.topupbills.telco.postpaid.widget

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.AttrRes
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.common.topupbills.data.MultiCheckoutButtons
import com.tokopedia.common.topupbills.data.constant.showMultiCheckoutButton
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.topupbills.R
import com.tokopedia.topupbills.telco.postpaid.listener.ClientNumberPostpaidListener
import com.tokopedia.topupbills.telco.common.widget.DigitalClientNumberWidget
import com.tokopedia.unifycomponents.UnifyButton

/**
 * Created by nabillasabbaha on 26/04/19.
 */
class DigitalPostpaidClientNumberWidget : DigitalClientNumberWidget {

    private lateinit var btnMain: UnifyButton
    private lateinit var btnSecondary: UnifyButton
    private lateinit var postpaidListener: ClientNumberPostpaidListener

    constructor(context: Context) : super(context) {
        initV()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initV()
    }

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initV()
    }

    override fun getLayout(): Int {
        return R.layout.view_telco_input_number_postpaid
    }

    fun initV() {
        btnMain = view.findViewById(R.id.telco_main_btn)
        btnSecondary = view.findViewById(R.id.telco_secondary_btn)
    }

    fun resetClientNumberPostpaid() {
        btnMain.show()
    }

    fun resetEnquiryResult() {
        btnMain.show()
    }

    fun setButtonEnquiry(enable: Boolean) {
        btnMain.isClickable = enable
    }

    fun setLoadingButtonEnquiry(loading: Boolean) {
        btnMain.isLoading = loading
    }

    fun setPostpaidListener(listener: ClientNumberPostpaidListener) {
        this.postpaidListener = listener
    }

    fun showMulticheckoutButtonSupport(multiCheckoutButtons: List<MultiCheckoutButtons>) {
        showMultiCheckoutButton(multiCheckoutButtons, context, btnMain, btnSecondary,
            CoachMark2(context),
            {
                postpaidListener.mainButtonClick()
            } , {
                postpaidListener.secondaryButtonClick()
            }, {
                postpaidListener.onCloseCoachMark()
            }
        )
    }
}
