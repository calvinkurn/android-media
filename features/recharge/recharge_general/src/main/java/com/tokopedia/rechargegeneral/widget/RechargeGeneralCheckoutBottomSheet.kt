package com.tokopedia.rechargegeneral.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.common.topupbills.data.TopupBillsEnquiry
import com.tokopedia.common.topupbills.widget.TopupBillsCheckoutWidget
import com.tokopedia.promocheckout.common.view.widget.TickerPromoStackingCheckoutView
import com.tokopedia.rechargegeneral.R
import com.tokopedia.unifycomponents.BaseCustomView
import kotlinx.android.synthetic.main.view_recharge_general_widget_checkout_view_bottom_sheet.view.*
import org.jetbrains.annotations.NotNull

/**
 * Created by resakemal on 12/12/19.
 */
class RechargeGeneralCheckoutBottomSheet @JvmOverloads constructor(@NotNull context: Context,
                                                                   attrs: AttributeSet? = null,
                                                                   defStyleAttr: Int = 0,
                                                                   var listener: CheckoutListener? = null)
    : BaseCustomView(context, attrs, defStyleAttr), TopupBillsCheckoutWidget.ActionListener {

    init {
        View.inflate(context, R.layout.view_recharge_general_widget_checkout_view_bottom_sheet, this)

        enquiry_data_view.title = "Data"

        checkout_view.setVisibilityLayout(true)
        checkout_view.listener = this
    }

    fun getPromoTicker(): TickerPromoStackingCheckoutView {
        return checkout_view.getPromoTicker()
    }

    fun setPayload(enquiryData: TopupBillsEnquiry) {
        enquiryData.attributes?.let { attributes ->
            enquiry_data_view.enquiryData = enquiryData
            checkout_view.setTotalPrice(attributes.price)
        }
    }

    override fun onClickNextBuyButton() {
        listener?.onClickCheckout(enquiry_data_view.enquiryData)
    }

    interface CheckoutListener {
        fun onClickCheckout(data: TopupBillsEnquiry)
    }

}