package com.tokopedia.rechargegeneral.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.common.topupbills.data.TopupBillsEnquiry
import com.tokopedia.common.topupbills.widget.TopupBillsCheckoutWidget
import com.tokopedia.promocheckout.common.view.widget.TickerPromoStackingCheckoutView
import com.tokopedia.rechargegeneral.R
import com.tokopedia.rechargegeneral.databinding.ViewRechargeGeneralWidgetCheckoutViewBottomSheetBinding
import com.tokopedia.unifycomponents.BaseCustomView
import org.jetbrains.annotations.NotNull

/**
 * Created by resakemal on 12/12/19.
 */
class RechargeGeneralCheckoutBottomSheet @JvmOverloads constructor(
    @NotNull context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    var listener: CheckoutListener? = null
) :
    BaseCustomView(context, attrs, defStyleAttr), TopupBillsCheckoutWidget.ActionListener {

    private var binding: ViewRechargeGeneralWidgetCheckoutViewBottomSheetBinding

    init {
        val view = View.inflate(context, R.layout.view_recharge_general_widget_checkout_view_bottom_sheet, this)
        binding = ViewRechargeGeneralWidgetCheckoutViewBottomSheetBinding.bind(view)

        binding.enquiryDataView.title = "Data"
        binding.checkoutView.setVisibilityLayout(true)
        binding.checkoutView.listener = this
    }

    fun getPromoTicker(): TickerPromoStackingCheckoutView {
        return binding.checkoutView.getPromoTicker()
    }

    fun setPayload(enquiryData: TopupBillsEnquiry) {
        enquiryData.attributes?.let { attributes ->
            binding.enquiryDataView.enquiryData = enquiryData
            binding.checkoutView.setTotalPrice(attributes.price)
        }
    }

    override fun onClickNextBuyButton() {
        listener?.onClickCheckout(binding.enquiryDataView.enquiryData)
    }

    interface CheckoutListener {
        fun onClickCheckout(data: TopupBillsEnquiry)
    }
}
