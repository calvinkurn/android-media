package com.tokopedia.buy_more_get_more.minicart.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buy_more_get_more.R
import com.tokopedia.buy_more_get_more.minicart.presentation.adapter.BmgmMiniCartAdapter
import com.tokopedia.buy_more_get_more.minicart.presentation.model.BmgmMiniCartVisitable
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.ImageUnify

/**
 * Created by @ilhamsuaib on 31/07/23.
 */

class MiniCartGwpGiftPlaceholderViewHolder(
    itemView: View,
    private val listener: BmgmMiniCartAdapter.Listener
) : AbstractViewHolder<BmgmMiniCartVisitable.GwpGiftPlaceholder>(itemView) {

    companion object {
        val RES_LAYOUT = R.layout.item_mini_cart_gwp_gift_placeholder
    }

    private val imageView: ImageUnify by lazy {
        itemView.findViewById(R.id.imgGwpProductPlaceholder)
    }

    override fun bind(element: BmgmMiniCartVisitable.GwpGiftPlaceholder) {
        imageView.loadImage(element.productImage)
        itemView.setOnClickListener {
            listener.setOnItemClickedListener()
        }
    }
}