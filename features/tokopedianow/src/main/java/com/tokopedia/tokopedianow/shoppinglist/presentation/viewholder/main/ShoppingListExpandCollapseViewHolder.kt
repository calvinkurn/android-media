package com.tokopedia.tokopedianow.shoppinglist.presentation.viewholder.main

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.getDimens
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.util.TypographyUtil.setRightImageDrawable
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowShoppingListExpandCollapseBinding
import com.tokopedia.tokopedianow.shoppinglist.util.ShoppingListProductLayoutType
import com.tokopedia.tokopedianow.shoppinglist.util.ShoppingListProductState
import com.tokopedia.tokopedianow.shoppinglist.util.ShoppingListProductState.EXPAND
import com.tokopedia.tokopedianow.shoppinglist.util.ShoppingListProductState.COLLAPSE
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.main.ShoppingListExpandCollapseUiModel
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.unifycomponents.R as unifycomponentsR
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class ShoppingListExpandCollapseViewHolder(
    itemView: View,
    private val listener: ShoppingListExpandCollapseListener? = null
): AbstractViewHolder<ShoppingListExpandCollapseUiModel>(itemView) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_shopping_list_expand_collapse
    }

    private val binding: ItemTokopedianowShoppingListExpandCollapseBinding? by viewBinding()
    override fun bind(data: ShoppingListExpandCollapseUiModel) {
        binding?.apply {
            val color = ContextCompat.getColor(root.context, unifyprinciplesR.color.Unify_GN500)
            when(data.productState) {
                COLLAPSE -> {
                    tpTitle.text = root.context.getString(R.string.tokopedianow_shopping_list_expand_text, data.remainingTotalProduct)
                    tpTitle.setRightImageDrawable(
                        drawable = ContextCompat.getDrawable(root.context, unifycomponentsR.drawable.iconunify_chevron_down),
                        width = root.getDimens(R.dimen.tokopedianow_shopping_list_chevron_icon_size),
                        height = root.getDimens(R.dimen.tokopedianow_shopping_list_chevron_icon_size),
                        color = color
                    )
                    root.setOnClickListener {
                        listener?.onClickWidget(
                            productState = EXPAND,
                            productLayoutType = data.productLayoutType
                        )
                    }
                }
                EXPAND -> {
                    tpTitle.text = root.context.getString(R.string.tokopedianow_shopping_list_collapse_text)
                    tpTitle.setRightImageDrawable(
                        drawable = ContextCompat.getDrawable(root.context, unifycomponentsR.drawable.iconunify_chevron_up),
                        width = root.getDimens(R.dimen.tokopedianow_shopping_list_chevron_icon_size),
                        height = root.getDimens(R.dimen.tokopedianow_shopping_list_chevron_icon_size),
                        color = color
                    )
                    root.setOnClickListener {
                        listener?.onClickWidget(
                            productState = COLLAPSE,
                            productLayoutType = data.productLayoutType
                        )
                    }
                }
            }
        }
    }

    interface ShoppingListExpandCollapseListener {
        fun onClickWidget(
            productState: ShoppingListProductState,
            productLayoutType: ShoppingListProductLayoutType
        )
    }
}
