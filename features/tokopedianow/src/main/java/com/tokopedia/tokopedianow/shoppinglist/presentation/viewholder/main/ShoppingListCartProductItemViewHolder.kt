package com.tokopedia.tokopedianow.shoppinglist.presentation.viewholder.main

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokopedianow.common.util.ImageUtil
import com.tokopedia.tokopedianow.common.util.ImageUtil.applyBrightnessFilter
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowShoppingListProductInCartItemBinding
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.main.ShoppingListCartProductItemUiModel
import com.tokopedia.tokopedianow.shoppinglist.util.ShoppingListProductLayoutType

class ShoppingListCartProductItemViewHolder(
    private val binding: ItemTokopedianowShoppingListProductInCartItemBinding
): RecyclerView.ViewHolder(binding.root) {
    private fun getImageBrightness(type: ShoppingListProductLayoutType): Float = if (type == ShoppingListProductLayoutType.UNAVAILABLE_SHOPPING_LIST) ImageUtil.OOS_BRIGHTNESS else ImageUtil.NORMAL_BRIGHTNESS

    fun bind(data: ShoppingListCartProductItemUiModel) {
        binding.root.loadImage(data.imageUrl) {
            listener(
                onSuccess = { _,_ ->
                    binding.root.applyBrightnessFilter(getImageBrightness(data.productLayoutType))
                }
            )
        }
    }
}
