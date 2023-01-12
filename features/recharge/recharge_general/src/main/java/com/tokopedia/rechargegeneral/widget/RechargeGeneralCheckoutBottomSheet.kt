package com.tokopedia.rechargegeneral.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.tokopedia.common.topupbills.data.TopupBillsEnquiry
import com.tokopedia.common.topupbills.widget.TopupBillsCheckoutWidget
import com.tokopedia.promocheckout.common.view.widget.TickerPromoStackingCheckoutView
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
        binding = ViewRechargeGeneralWidgetCheckoutViewBottomSheetBinding.inflate(
            LayoutInflater.from(context),
            this,
            false
        )

        binding.enquiryDataView.title = "Data"
        binding.checkoutView.setVisibilityLayout(true)
        binding.checkoutView.listener = this
        addView(binding.root)
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
