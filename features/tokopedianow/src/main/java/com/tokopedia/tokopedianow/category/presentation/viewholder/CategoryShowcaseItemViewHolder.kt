package com.tokopedia.tokopedianow.category.presentation.viewholder

import android.content.Context
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.tokopedianow.R
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryShowcaseItemUiModel
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowProductGridCardBinding
import com.tokopedia.utils.view.binding.viewBinding

class CategoryShowcaseItemViewHolder(
    itemView: View,
    private var listener: CategoryShowcaseItemListener? = null
) : AbstractViewHolder<CategoryShowcaseItemUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_product_grid_card
    }

    private var binding: ItemTokopedianowProductGridCardBinding? by viewBinding()

    override fun bind(element: CategoryShowcaseItemUiModel) {
        binding?.productCard?.apply {
            setData(
                model = element.productCardModel
            )
            setOnClickListener {
                listener?.onProductCardClicked(
                    context = context,
                    position = layoutPosition,
                    product = element
                )
            }
            setOnClickQuantityEditorListener { quantity ->
                listener?.onProductCardQuantityChanged(
                    position = layoutPosition,
                    product = element,
                    quantity = quantity
                )
            }
            setOnClickQuantityEditorVariantListener {
                listener?.onProductCardAddVariantClicked(
                    context = context,
                    position = layoutPosition,
                    product = element
                )
            }
            addOnImpressionListener(element) {
                listener?.onProductCardImpressed(
                    position = layoutPosition,
                    product = element
                )
            }
        }
    }

    override fun bind(element: CategoryShowcaseItemUiModel?, payloads: MutableList<Any>) {
        if (payloads.firstOrNull() == true && element != null) {
            binding?.productCard?.setData(
                model = element.productCardModel
            )
        }
    }

    interface CategoryShowcaseItemListener {
        fun onProductCardAddVariantClicked(
            context: Context,
            position: Int,
            product: CategoryShowcaseItemUiModel
        )
        fun onProductCardQuantityChanged(
            position: Int,
            product: CategoryShowcaseItemUiModel,
            quantity: Int
        )
        fun onProductCardClicked(
            context: Context,
            position: Int,
            product: CategoryShowcaseItemUiModel
        )
        fun onProductCardImpressed(
            position: Int,
            product: CategoryShowcaseItemUiModel
        )
    }
}
