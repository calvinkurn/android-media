package com.tokopedia.tokopedianow.shoppinglist.presentation.viewholder.main

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowShoppingListProductInCartItemBinding
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.main.ShoppingListCartProductItemUiModel

class ShoppingListCartProductItemViewHolder(
    private val binding: ItemTokopedianowShoppingListProductInCartItemBinding
): RecyclerView.ViewHolder(binding.root) {
    fun bind(data: ShoppingListCartProductItemUiModel) {
        binding.root.loadImage(data.imageUrl)
    }
}
