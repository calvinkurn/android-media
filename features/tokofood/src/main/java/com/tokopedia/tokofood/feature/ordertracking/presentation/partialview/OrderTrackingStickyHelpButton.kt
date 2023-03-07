package com.tokopedia.tokofood.feature.ordertracking.presentation.partialview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View.OnClickListener
import androidx.core.content.ContextCompat
import com.tokopedia.tokofood.databinding.TokofoodPartialOrderDetailStickyHelpButtonBinding
import com.tokopedia.tokofood.feature.ordertracking.presentation.navigator.OrderTrackingNavigator
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.ActionButtonsUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.MerchantDataUiModel
import com.tokopedia.unifycomponents.BaseCustomView

class OrderTrackingStickyHelpButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseCustomView(context, attrs, defStyleAttr) {

    private var binding: TokofoodPartialOrderDetailStickyHelpButtonBinding? = null

    init {
        binding = TokofoodPartialOrderDetailStickyHelpButtonBinding.inflate(
            LayoutInflater.from(context),
            this,
            true
        )
        setBackgroundColor()
    }

    private var navigator: OrderTrackingNavigator? = null

    private fun setBackgroundColor() {
        binding?.orderTrackingStickyHelpButton?.setBackgroundColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_Background))
    }

    fun setupHelpButton(orderId: String, primaryButton: ActionButtonsUiModel.ActionButton, merchantData: MerchantDataUiModel?) {
        binding?.btnOrderTrackingSecondaryHelp?.run {
            text = primaryButton.label
            if (primaryButton.appUrl.isNotBlank()) {
                setOnClickListener(createPrimaryButtonClickListener(orderId, primaryButton.appUrl, merchantData))
            }
        }
    }

    fun setOrderTrackingNavigator(navigator: OrderTrackingNavigator) {
        this.navigator = navigator
    }

    private fun createPrimaryButtonClickListener(orderId: String, appUrl: String, merchantData: MerchantDataUiModel?): OnClickListener {
        return OnClickListener {
            navigator?.goToHelpPage(orderId, appUrl, merchantData?.merchantId.orEmpty())
        }
    }

    override fun onDetachedFromWindow() {
        binding = null
        super.onDetachedFromWindow()
    }
}
