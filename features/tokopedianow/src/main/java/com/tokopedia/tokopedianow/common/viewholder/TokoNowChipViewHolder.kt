package com.tokopedia.tokopedianow.common.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.model.TokoNowChipUiModel
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowChipBinding
import com.tokopedia.utils.view.binding.viewBinding

class TokoNowChipViewHolder(
    itemView: View,
    private val listener: ChipListener
) : AbstractViewHolder<TokoNowChipUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_chip
    }

    private var binding: ItemTokopedianowChipBinding? by viewBinding()

    override fun bind(chip: TokoNowChipUiModel) {
        renderTitle(chip)
        renderImage(chip)
        setOnClickListener(chip)
    }

    private fun renderTitle(chip: TokoNowChipUiModel) {
        binding?.chip?.chipText = chip.text
    }

    private fun renderImage(chip: TokoNowChipUiModel) {
        binding?.chip?.chip_image_icon?.apply {
            visibility = if (chip.imageUrl.isNotEmpty()) {
                setImageUrl(chip.imageUrl)
                View.VISIBLE
            } else {
                View.GONE
            }
        }

    }

    private fun setOnClickListener(chip: TokoNowChipUiModel) {
        binding?.root?.setOnClickListener {
            listener.onClickChipItem(chip)
        }
    }

    interface ChipListener {
        fun onClickChipItem(chipUiModel: TokoNowChipUiModel)
    }
}