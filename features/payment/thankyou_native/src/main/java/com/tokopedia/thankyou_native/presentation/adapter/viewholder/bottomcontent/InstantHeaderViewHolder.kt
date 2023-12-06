package com.tokopedia.thankyou_native.presentation.adapter.viewholder.bottomcontent

import android.view.View
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.databinding.ThankInstantHeaderBinding
import com.tokopedia.thankyou_native.presentation.adapter.model.InstantHeaderUiModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.utils.view.binding.viewBinding

class InstantHeaderViewHolder(view: View): AbstractViewHolder<InstantHeaderUiModel>(view) {

    val binding: ThankInstantHeaderBinding? by viewBinding()

    override fun bind(data: InstantHeaderUiModel?) {
        if (data == null) return


    }

    companion object {
        val LAYOUT_ID = R.layout.thank_instant_header
    }
}
