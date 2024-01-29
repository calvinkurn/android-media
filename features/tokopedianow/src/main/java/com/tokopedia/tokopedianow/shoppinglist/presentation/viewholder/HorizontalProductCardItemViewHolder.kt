package com.tokopedia.tokopedianow.shoppinglist.presentation.viewholder

import android.graphics.Paint
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowHorizontalProductCardItemBinding
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.HorizontalProductCardItemUiModel
import com.tokopedia.utils.view.binding.viewBinding

class HorizontalProductCardItemViewHolder(
    itemView: View
): AbstractViewHolder<HorizontalProductCardItemUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_horizontal_product_card_item
    }

    private var binding: ItemTokopedianowHorizontalProductCardItemBinding? by viewBinding()

    override fun bind(data: HorizontalProductCardItemUiModel) {
        binding?.apply {
            iuProduct.loadImage(data.image)
            tpPrice.text = data.price
            tpName.text = data.name
            tpWeight.text = data.weight
            tpPercentage.text = data.percentage
            tpSlashPrice.text = data.slashPrice
            tpSlashPrice.paintFlags = tpSlashPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }
    }
}
