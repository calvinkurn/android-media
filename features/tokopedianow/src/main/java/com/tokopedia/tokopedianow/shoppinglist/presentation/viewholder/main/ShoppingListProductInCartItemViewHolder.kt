package com.tokopedia.tokopedianow.shoppinglist.presentation.viewholder.main

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowShoppingListProductInCartItemBinding
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.main.ShoppingListProductInCartItemUiModel

class ShoppingListProductInCartItemViewHolder(
    private val binding: ItemTokopedianowShoppingListProductInCartItemBinding
): RecyclerView.ViewHolder(binding.root) {
    fun bind(uiModel: ShoppingListProductInCartItemUiModel) {
        binding.root.loadImage(uiModel.imageUrl)
    }
}
