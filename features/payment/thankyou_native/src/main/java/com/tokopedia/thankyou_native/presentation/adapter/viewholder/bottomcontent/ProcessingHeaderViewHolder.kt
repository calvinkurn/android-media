package com.tokopedia.thankyou_native.presentation.adapter.viewholder.bottomcontent

import android.view.View
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.databinding.ThankProcessingHeaderBinding
import com.tokopedia.thankyou_native.presentation.adapter.model.ProcessingHeaderUiModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.thankyou_native.presentation.views.listener.HeaderListener
import com.tokopedia.utils.view.binding.viewBinding

class ProcessingHeaderViewHolder(
    view: View,
    private val listener: HeaderListener
): AbstractViewHolder<ProcessingHeaderUiModel>(view) {

    private val binding: ThankProcessingHeaderBinding? by viewBinding()

    override fun bind(data: ProcessingHeaderUiModel?) {
        if (data == null) return

        binding?.headerTitle?.text = data.title
        binding?.headerDescription?.text = data.description
        binding?.methodLabel?.text = data.methodLabel
        binding?.method?.text = data.method
        binding?.methodImage?.setImageUrl(data.methodImage)
        binding?.amountLabel?.text = data.amountLabel
        binding?.amount?.text = data.amount
        binding?.installment?.text = data.installment
        binding?.info?.setText(data.note)
        binding?.primaryButton?.shouldShowWithAction(!data.shouldHidePrimaryButton) {
            binding?.primaryButton?.text = data.primaryButtonText
        }
        binding?.secondaryButton?.shouldShowWithAction(!data.shouldHideSecondaryButton) {
            binding?.secondaryButton?.text = data.secondaryButtonText
        }

        binding?.primaryButton?.setOnClickListener {
            listener.onButtonClick(data.primaryButtonApplink, data.primaryButtonType, true, data.primaryButtonText)
        }
        binding?.secondaryButton?.setOnClickListener {
            listener.onButtonClick(data.secondaryButtonApplink, data.secondaryButtonType, false, data.secondaryButtonText)
        }
        binding?.seeDetailBtn?.setOnClickListener {
            listener.onSeeDetailInvoice()
        }
    }

    companion object {
        val LAYOUT_ID = R.layout.thank_processing_header
    }
}

