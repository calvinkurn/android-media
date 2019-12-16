package com.tokopedia.rechargegeneral.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.common.topupbills.data.TopupBillsEnquiry
import com.tokopedia.common.topupbills.widget.TopupBillsCheckoutWidget
import com.tokopedia.design.base.BaseCustomView
import com.tokopedia.rechargegeneral.R
import kotlinx.android.synthetic.main.view_widget_checkout_view_bottom_sheet.view.*
import org.jetbrains.annotations.NotNull

/**
 * Created by resakemal on 12/12/19.
 */
class RechargeGeneralCheckoutBottomSheet @JvmOverloads constructor(@NotNull context: Context, attrs: AttributeSet? = null,
                                                                   defStyleAttr: Int = 0)
    : BaseCustomView(context, attrs, defStyleAttr), TopupBillsCheckoutWidget.ActionListener {

    lateinit var listener: CheckoutListener

    init {
        View.inflate(context, R.layout.view_widget_checkout_view_bottom_sheet, this)

        enquiry_data_view.title = "Data"

        checkout_view.setVisibilityLayout(true)
        checkout_view.setListener(this)
    }

    fun setPayload(enquiryData: TopupBillsEnquiry) {
        enquiry_data_view.enquiryData = enquiryData
        checkout_view.setTotalPrice(enquiryData.attributes.price)
    }

    override fun onClickNextBuyButton() {
        if (::listener.isInitialized) listener.onClickCheckout()
    }

    interface CheckoutListener {
        fun onClickCheckout()
    }

}