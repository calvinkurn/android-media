package com.tokopedia.tokopedianow.shoppinglist.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.getDimens
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowShoppingListProductInCartBinding
import com.tokopedia.tokopedianow.shoppinglist.presentation.adapter.main.ShoppingListProductInCartAdapter
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.ShoppingListProductInCartUiModel
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.unifycomponents.R as unifycomponentsR
import com.tokopedia.list.R as listR
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class ShoppingListProductInCartViewHolder (
    itemView: View
): AbstractViewHolder<ShoppingListProductInCartUiModel>(itemView) {
    companion object {
        private const val DEFAULT_BOUND = 0

        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_shopping_list_product_in_cart
    }

    private var binding: ItemTokopedianowShoppingListProductInCartBinding? by viewBinding()

    init {
        binding?.apply {
            val drawable = ContextCompat.getDrawable(root.context, unifycomponentsR.drawable.iconunify_chevron_down)
            if (drawable != null) {
                val width = root.getDimens(listR.dimen.unify_space_16)
                val height = root.getDimens(listR.dimen.unify_space_16)
                drawable.setBounds(
                    DEFAULT_BOUND,
                    DEFAULT_BOUND,
                    width,
                    height
                )
                drawable.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(ContextCompat.getColor(root.context, unifyprinciplesR.color.Unify_GN500), BlendModeCompat.SRC_ATOP)
                tpSeeDetail.setTextColor(ContextCompat.getColor(root.context, unifyprinciplesR.color.Unify_GN500))
                tpSeeDetail.setCompoundDrawables(null, null, drawable, null)
            }
        }
    }

    override fun bind(element: ShoppingListProductInCartUiModel) {
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
