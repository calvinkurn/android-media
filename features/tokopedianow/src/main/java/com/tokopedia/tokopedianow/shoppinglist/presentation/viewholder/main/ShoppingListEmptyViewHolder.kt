package com.tokopedia.tokopedianow.shoppinglist.presentation.viewholder.main

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.imageassets.TokopediaImageUrl.IMG_SHOPPING_LIST_EMPTY
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowShoppingListEmptyBinding
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.main.ShoppingListEmptyUiModel
import com.tokopedia.utils.view.binding.viewBinding

class ShoppingListEmptyViewHolder(
    itemView: View
): AbstractViewHolder<ShoppingListEmptyUiModel>(itemView) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_shopping_list_empty
    }

    private var binding: ItemTokopedianowShoppingListEmptyBinding? by viewBinding()

    override fun bind(element: ShoppingListEmptyUiModel) {
        binding?.iuEmpty?.loadImage(IMG_SHOPPING_LIST_EMPTY)
    }
}
