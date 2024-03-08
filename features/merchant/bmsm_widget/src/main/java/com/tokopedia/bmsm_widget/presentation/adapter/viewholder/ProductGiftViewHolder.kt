package com.tokopedia.bmsm_widget.presentation.adapter.viewholder

import android.widget.RelativeLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.tokopedia.bmsm_widget.databinding.ItemProductGiftBinding
import com.tokopedia.bmsm_widget.presentation.model.ProductGiftUiModel
import com.tokopedia.kotlin.extensions.view.getScreenWidth
import com.tokopedia.kotlin.extensions.view.pxToDp
import com.tokopedia.media.loader.loadImage

/**
 * Created by @ilhamsuaib on 06/12/23.
 */

class ProductGiftViewHolder(private val binding: ItemProductGiftBinding) :
    ViewHolder(binding.root) {

    fun bind(model: ProductGiftUiModel, itemWidthMatchParent: Boolean) {
        with(binding) {
            imgGiftImage.loadImage(model.imageUrl)
            tvGiftName.text = model.getProductNameWithQty()
            icGiftLock.isVisible = !model.isUnlocked
            setContainerWidth(itemWidthMatchParent)
        }
    }

    private fun setContainerWidth(itemWidthMatchParent: Boolean) {
        if (itemWidthMatchParent) {
            binding.giftItemContainer.layoutParams = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            )
        } else {
            val width = get42PercentOfScreenWidth()
            val widthInDp = itemView.context.pxToDp(width)
            binding.giftItemContainer.layoutParams = RelativeLayout.LayoutParams(
                widthInDp.toInt(),
                RelativeLayout.LayoutParams.WRAP_CONTENT
            )
        }
        binding.giftItemContainer.requestLayout()
    }

    private fun get42PercentOfScreenWidth(): Int {
        val inPercent = 42
        return getScreenWidth().times(inPercent).div(100)
    }
}