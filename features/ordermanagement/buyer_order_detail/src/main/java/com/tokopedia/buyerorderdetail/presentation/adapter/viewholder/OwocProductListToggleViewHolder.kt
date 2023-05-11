package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.view.View
import android.view.ViewPropertyAnimator
import androidx.core.view.animation.PathInterpolatorCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.databinding.ItemOwocProductListToggleBinding
import com.tokopedia.buyerorderdetail.presentation.model.BaseOwocVisitableUiModel
import com.tokopedia.buyerorderdetail.presentation.model.OwocProductListUiModel
import com.tokopedia.buyerorderdetail.presentation.model.StringRes

class OwocProductListToggleViewHolder(
    itemView: View,
    private val listener: Listener
) : AbstractViewHolder<OwocProductListUiModel.ProductListToggleUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_owoc_product_list_toggle
        private const val ICON_ROTATION_COLLAPSED = 0f
        private const val ICON_ROTATION_EXPANDED = -180f
        private const val TRANSITION_DURATION = 300L
        private const val CUBIC_BEZIER_X1 = 0.63f
        private const val CUBIC_BEZIER_X2 = 0.29f
        private const val CUBIC_BEZIER_Y1 = 0.01f
        private const val CUBIC_BEZIER_Y2 = 1f
    }

    private val binding = ItemOwocProductListToggleBinding.bind(itemView)
    private var animation: ViewPropertyAnimator? = null

    private fun bindToggleText(text: StringRes) = with(binding) {
        tvOwocProductListToggle.text = text.getStringValueWithDefaultParam(root.context)
    }

    private fun bindToggleIcon(collapsed: Boolean, animate: Boolean) = with(binding) {
        animation?.cancel()
        rotateToggleIcon(getToggleIconRotation(collapsed), animate)
    }

    private fun bindListener(expandedProducts: List<BaseOwocVisitableUiModel>, isExpanded: Boolean) {
        binding.root.setOnClickListener {
            if (isExpanded) {
                listener.onExpandProductList(expandedProducts = expandedProducts, isExpanded = isExpanded)
            } else {
                listener.onCollapseProductList(expandedProducts = expandedProducts, isExpanded = isExpanded)
            }
        }
    }

    private fun getToggleIconRotation(expanded: Boolean): Float {
        return if (expanded) {
            ICON_ROTATION_EXPANDED
        } else {
            ICON_ROTATION_COLLAPSED
        }
    }

    private fun rotateToggleIcon(targetRotation: Float, animate: Boolean) = with(binding) {
        if (animate) {
            animation = icOwocProductListToggle
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
            icOwocProductListToggle.rotation = targetRotation
        }
    }

    override fun bind(element: OwocProductListUiModel.ProductListToggleUiModel?) {
        if (element == null) return
        bindToggleText(element.text)
        bindToggleIcon(collapsed = element.isExpanded, animate = false)
        bindListener(element.expandProductList, element.isExpanded)
    }

    override fun bind(
        element: OwocProductListUiModel.ProductListToggleUiModel?,
        payloads: MutableList<Any>
    ) {
        payloads.firstOrNull()?.let {
            if (it is Pair<*, *>) {
                val (oldItem, newItem) = it
                if (oldItem is OwocProductListUiModel.ProductListToggleUiModel && newItem is OwocProductListUiModel.ProductListToggleUiModel) {
                    if (oldItem.text != newItem.text) {
                        bindToggleText(newItem.text)
                    }
                    if (oldItem.isExpanded != newItem.isExpanded) {
                        bindToggleIcon(newItem.isExpanded, true)
                        bindListener(newItem.expandProductList, newItem.isExpanded)
                    }
                    return
                }
            }
        }
    }

    interface Listener {
        fun onCollapseProductList(expandedProducts: List<BaseOwocVisitableUiModel>, isExpanded: Boolean)
        fun onExpandProductList(expandedProducts: List<BaseOwocVisitableUiModel>, isExpanded: Boolean)
    }
}

