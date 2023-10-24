package com.tokopedia.tokopedianow.category.presentation.viewholder

import android.content.Context
import android.view.View
import androidx.annotation.LayoutRes
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.tokopedia.tokopedianow.R
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.productcard.compact.productcard.presentation.customview.ProductCardCompactView
import com.tokopedia.productcard.compact.productcard.presentation.listener.ProductCardCompactViewListener
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryShowcaseItemUiModel
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowProductGridCardBinding
import com.tokopedia.utils.view.binding.viewBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class CategoryShowcaseItemViewHolder(
    itemView: View,
    private var listener: CategoryShowcaseItemListener? = null,
    private val lifecycleOwner: LifecycleOwner? = null
) : AbstractViewHolder<CategoryShowcaseItemUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_product_grid_card
    }

    private var binding: ItemTokopedianowProductGridCardBinding? by viewBinding()

    private var job: Job? = null

    override fun bind(element: CategoryShowcaseItemUiModel) {
        job = lifecycleOwner?.lifecycleScope?.launch(Dispatchers.Main.immediate) {
            binding?.productCard?.apply {
                bind(
                    model = element.productCardModel,
                    listener = createProductListener(element)
                )

                setOnClickListener {
                    listener?.onProductCardClicked(
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
    }

    override fun bind(element: CategoryShowcaseItemUiModel?, payloads: MutableList<Any>) {
        if (payloads.firstOrNull() == true && element != null) {
            binding?.productCard?.bind(
                model = element.productCardModel,
                listener = createProductListener(element)
            )
        }
    }

    override fun onViewRecycled() {
        job?.cancel()
        super.onViewRecycled()
    }

    private fun createProductListener(
        element: CategoryShowcaseItemUiModel
    ) = ProductCardCompactViewListener(
        onQuantityChangedListener = { quantity ->
            listener?.onProductCardQuantityChanged(
                position = layoutPosition,
                product = element,
                quantity = quantity
            )
        },
        clickAddVariantListener = {
            listener?.onProductCardAddVariantClicked(
                context = itemView.context,
                position = layoutPosition,
                product = element
            )
        },
        blockAddToCartListener = {
            listener?.onProductCardAddToCartBlocked()
        }
    )

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
        fun onProductCardAddToCartBlocked()
    }
}
