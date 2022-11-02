package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.view.View
import android.view.ViewPropertyAnimator
import androidx.core.view.animation.PathInterpolatorCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.databinding.ItemBuyerOrderDetailProductListToggleBinding
import com.tokopedia.buyerorderdetail.presentation.model.ProductListUiModel
import com.tokopedia.buyerorderdetail.presentation.model.StringRes

class ProductListToggleViewHolder(
    itemView: View,
    private val listener: Listener
) : AbstractViewHolder<ProductListUiModel.ProductListToggleUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_buyer_order_detail_product_list_toggle
        private const val ICON_ROTATION_COLLAPSED = 0f
        private const val ICON_ROTATION_EXPANDED = -180f
        private const val TRANSITION_DURATION = 300L
        private const val CUBIC_BEZIER_X1 = 0.63f
        private const val CUBIC_BEZIER_X2 = 0.29f
        private const val CUBIC_BEZIER_Y1 = 0.01f
        private const val CUBIC_BEZIER_Y2 = 1f
    }

    private val binding = ItemBuyerOrderDetailProductListToggleBinding.bind(itemView)
    private var animation: ViewPropertyAnimator? = null

    private fun bindToggleText(text: StringRes) = with(binding) {
        tvBuyerOrderDetailProductListToggle.text = text.getStringValueWithDefaultParam(root.context)
    }

    private fun bindToggleIcon(collapsed: Boolean, animate: Boolean) = with(binding) {
        animation?.cancel()
        rotateToggleIcon(getToggleIconRotation(collapsed), animate)
    }

    private fun bindListener(collapsed: Boolean) {
        binding.root.setOnClickListener {
            if (collapsed) {
                listener.onExpandProductList()
            } else {
                listener.onCollapseProductList()
            }
        }
    }

    private fun getToggleIconRotation(collapsed: Boolean): Float {
        return if (collapsed) {
            ICON_ROTATION_COLLAPSED
        } else {
            ICON_ROTATION_EXPANDED
        }
    }

    private fun rotateToggleIcon(targetRotation: Float, animate: Boolean) = with(binding) {
        if (animate) {
            animation = icBuyerOrderDetailProductListToggle
                .animate().apply {
                    rotation(targetRotation)
                        .setDuration(TRANSITION_DURATION)
                        .setInterpolator(
                            PathInterpolatorCompat.create(
                                CUBIC_BEZIER_X1,
                                CUBIC_BEZIER_Y1,
                                CUBIC_BEZIER_X2,
                                CUBIC_BEZIER_Y2
                            )
                        )
                        .start()
                }
        } else {
            icBuyerOrderDetailProductListToggle.rotation = targetRotation
        }
    }

    override fun bind(element: ProductListUiModel.ProductListToggleUiModel?) {
        element?.run {
            bindToggleText(text)
            bindToggleIcon(collapsed = collapsed, animate = false)
            bindListener(collapsed)
        }
    }

    override fun bind(
        element: ProductListUiModel.ProductListToggleUiModel?,
        payloads: MutableList<Any>
    ) {
        payloads.firstOrNull()?.let {
            if (it is Pair<*, *>) {
                val (oldItem, newItem) = it
                if (oldItem is ProductListUiModel.ProductListToggleUiModel && newItem is ProductListUiModel.ProductListToggleUiModel) {
                    if (oldItem.text != newItem.text) {
                        bindToggleText(newItem.text)
                    }
                    if (oldItem.collapsed != newItem.collapsed) {
                        bindToggleIcon(newItem.collapsed, true)
                        bindListener(newItem.collapsed)
                    }
                    return
                }
            }
        }
    }

    interface Listener {
        fun onCollapseProductList()
        fun onExpandProductList()
    }
}
