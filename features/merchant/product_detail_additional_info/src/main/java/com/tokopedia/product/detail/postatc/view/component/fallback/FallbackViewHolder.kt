package com.tokopedia.product.detail.postatc.view.component.fallback

import com.tokopedia.product.detail.databinding.ItemPostAtcFallbackBinding
import com.tokopedia.product.detail.postatc.base.PostAtcCallback
import com.tokopedia.product.detail.postatc.base.PostAtcViewHolder

class FallbackViewHolder(
    private val binding: ItemPostAtcFallbackBinding,
    private val callback: PostAtcCallback
) : PostAtcViewHolder<FallbackUiModel>(binding.root) {
    override fun bind(element: FallbackUiModel) = with(binding) {
        postAtcFallbackButton.setOnClickListener {
            callback.onClickLihatKeranjang(
                element.cartId,
                getComponentTrackData(element)
            )
        }
    }
}
