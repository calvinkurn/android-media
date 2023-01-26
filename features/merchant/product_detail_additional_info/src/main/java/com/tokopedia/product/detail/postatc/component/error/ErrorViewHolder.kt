package com.tokopedia.product.detail.postatc.component.error

import com.tokopedia.product.detail.databinding.ItemErrorBinding
import com.tokopedia.product.detail.postatc.base.PostAtcListener
import com.tokopedia.product.detail.postatc.base.PostAtcViewHolder

class ErrorViewHolder(
    private val binding: ItemErrorBinding,
    private val listener: PostAtcListener
) : PostAtcViewHolder<ErrorUiModel>(binding.root) {
    override fun bind(element: ErrorUiModel) {
        binding.apply {
            postAtcError.setType(element.errorType)
            postAtcError.setActionClickListener {
                listener.refreshPage()
            }
        }
    }
}
