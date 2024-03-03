package com.tokopedia.tokopedianow.shoppinglist.presentation.viewholder.main

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.getDimens
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.util.TypographyUtil.setRightImageDrawable
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowShoppingListProductCartBinding
import com.tokopedia.tokopedianow.shoppinglist.presentation.adapter.main.ShoppingListProductInCartAdapter
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.main.ShoppingListCartProductUiModel
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.unifycomponents.R as unifycomponentsR
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class ShoppingListProductCartViewHolder (
    itemView: View
): AbstractViewHolder<ShoppingListCartProductUiModel>(itemView) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_shopping_list_product_cart
    }

    private var binding: ItemTokopedianowShoppingListProductCartBinding? by viewBinding()

    init {
        binding?.apply {
            tpSeeDetail.setRightImageDrawable(
                drawable = ContextCompat.getDrawable(root.context, unifycomponentsR.drawable.iconunify_chevron_down),
                width = root.getDimens(R.dimen.tokopedianow_shopping_list_chevron_icon_size),
                height = root.getDimens(R.dimen.tokopedianow_shopping_list_chevron_icon_size),
                color = ContextCompat.getColor(root.context, unifyprinciplesR.color.Unify_GN500)
            )
        }
    }

    override fun bind(element: ShoppingListCartProductUiModel) {
        binding?.apply {
            rvProductInCart.layoutManager = LinearLayoutManager(
                root.context,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            rvProductInCart.adapter = ShoppingListProductInCartAdapter(
                itemList = element.productList
            )
        }
    }
}
