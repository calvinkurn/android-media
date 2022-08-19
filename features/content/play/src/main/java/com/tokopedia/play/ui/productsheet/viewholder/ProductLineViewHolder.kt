package com.tokopedia.play.ui.productsheet.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.play.databinding.ItemProductLineBinding
import com.tokopedia.play.view.custom.ProductBottomSheetCardView
import com.tokopedia.play.view.uimodel.PlayProductUiModel

/**
 * Created by jegul on 03/03/20
 */
class ProductLineViewHolder(
    private val binding: ItemProductLineBinding,
    listener: ProductBottomSheetCardView.Listener,
) : BaseViewHolder(binding.root) {

    init {
        binding.root.setListener(listener)
    }

    fun bind(item: PlayProductUiModel.Product) {
        binding.root.setItem(item)
    }

    companion object {

        fun create(
            parent: ViewGroup,
            listener: ProductBottomSheetCardView.Listener,
        ) = ProductLineViewHolder(
            ItemProductLineBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            ),
            listener,
        )
    }

    interface Listener {
        fun onClicked(viewHolder: ProductLineViewHolder, product: PlayProductUiModel.Product)
        fun onBuyProduct(product: PlayProductUiModel.Product)
        fun onAtcProduct(product: PlayProductUiModel.Product)
    }
}
