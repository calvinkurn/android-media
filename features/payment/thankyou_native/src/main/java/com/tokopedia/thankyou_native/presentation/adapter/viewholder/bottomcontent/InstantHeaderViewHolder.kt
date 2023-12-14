package com.tokopedia.thankyou_native.presentation.adapter.viewholder.bottomcontent

import android.view.View
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.databinding.ThankInstantHeaderBinding
import com.tokopedia.thankyou_native.presentation.adapter.model.InstantHeaderUiModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.thankyou_native.presentation.views.listener.HeaderListener
import com.tokopedia.utils.view.binding.viewBinding

class InstantHeaderViewHolder(
    view: View,
    private val listener: HeaderListener
): AbstractViewHolder<InstantHeaderUiModel>(view) {

    private val binding: ThankInstantHeaderBinding? by viewBinding()

    override fun bind(data: InstantHeaderUiModel?) {
        if (data == null) return

        binding?.headerIcon?.setImageUrl(data.gatewayImage)
        binding?.headerTitle?.text = data.title
        binding?.headerDescription?.text = data.description
        binding?.headerTips?.description = data.note
        binding?.primaryButton?.shouldShowWithAction(!data.shouldHidePrimaryButton) {
            binding?.primaryButton?.text = data.primaryButtonText
        }
        binding?.secondaryButton?.shouldShowWithAction(!data.shouldHideSecondaryButton) {
            binding?.secondaryButton?.text = data.secondaryButtonText
        }

        binding?.cardContainer?.setOnClickListener {
            listener.onSeeDetailInvoice()
        }
    }

    companion object {
        val LAYOUT_ID = R.layout.thank_instant_header
    }
}
