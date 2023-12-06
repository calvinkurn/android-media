package com.tokopedia.thankyou_native.presentation.adapter.viewholder.bottomcontent

import android.view.View
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.databinding.ThankWaitingHeaderBinding
import com.tokopedia.thankyou_native.presentation.adapter.model.WaitingHeaderUiModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.utils.view.binding.viewBinding
import java.util.*

class WaitingHeaderViewHolder(view: View): AbstractViewHolder<WaitingHeaderUiModel>(view) {

    val binding: ThankWaitingHeaderBinding? by viewBinding()

    override fun bind(data: WaitingHeaderUiModel?) {
        if (data == null) return

        binding?.headerTitle?.text = data.title
        binding?.headerDescription?.text = data.description
        val cal = Calendar.getInstance()
        cal.add(Calendar.DATE, 1)
        binding?.headerTimer?.targetDate = cal
        binding?.accountIdLabel?.text = data.accountIdLabel
        binding?.accountId?.text = data.accountId
        binding?.accountImage?.setImageUrl(data.accountImage)
        binding?.amountLabel?.text = data.amountLabel
        binding?.amount?.text = data.amount.toString()
        binding?.info?.text = data.note
        binding?.primaryButton?.shouldShowWithAction(!data.shouldHidePrimaryButton) {
            binding?.primaryButton?.text = data.primaryButtonText
        }
        binding?.secondaryButton?.shouldShowWithAction(!data.shouldHideSecondaryButton) {
            binding?.secondaryButton?.text = data.secondaryButtonText
        }
    }

    companion object {
        val LAYOUT_ID = R.layout.thank_waiting_header
    }
}
