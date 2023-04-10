package com.tokopedia.product.detail.postatc.view.component.fallback

import com.tokopedia.product.detail.databinding.ItemPostAtcFallbackBinding
import com.tokopedia.product.detail.postatc.base.PostAtcListener
import com.tokopedia.product.detail.postatc.base.PostAtcViewHolder

class FallbackViewHolder(
    private val binding: ItemPostAtcFallbackBinding,
    private val listener: PostAtcListener
) : PostAtcViewHolder<FallbackUiModel>(binding.root) {
    override fun bind(element: FallbackUiModel) = with(binding) {
        postAtcFallbackButton.setOnClickListener {
            listener.onClickLihatKeranjang(
                element.cartId,
                getComponentTrackData(element)
            )
        }
    }
}
