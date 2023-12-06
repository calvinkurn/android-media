package com.tokopedia.thankyou_native.presentation.adapter.viewholder.bottomcontent

import android.view.View
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.databinding.ThankProcessingHeaderBinding
import com.tokopedia.thankyou_native.presentation.adapter.model.ProcessingHeaderUiModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.utils.view.binding.viewBinding

class ProcessingHeaderViewHolder(view: View): AbstractViewHolder<ProcessingHeaderUiModel>(view) {

    val binding: ThankProcessingHeaderBinding? by viewBinding()

    override fun bind(data: ProcessingHeaderUiModel?) {
        if (data == null) return


    }

    companion object {
        val LAYOUT_ID = R.layout.thank_processing_header
    }
}

