package com.tokopedia.tokopedianow.shoppinglist.presentation.viewholder.main

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.applink.RouteManager
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowShoppingListProductInCartItemBinding
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.main.ShoppingListProductCartItemUiModel

class ShoppingListProductCartItemViewHolder(
    private val binding: ItemTokopedianowShoppingListProductInCartItemBinding
): RecyclerView.ViewHolder(binding.root) {
    fun bind(data: ShoppingListProductCartItemUiModel) {
        binding.root.loadImage(data.imageUrl)
        binding.root.setOnClickListener {
            if (data.appLink.isNotBlank()) {
                RouteManager.route(binding.root.context, data.appLink)
            }
        }
    }
}
