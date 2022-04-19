package com.tokopedia.shop.score.performance.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.databinding.ItemDetailPerformanceBinding
import com.tokopedia.shop.score.performance.presentation.adapter.ItemShopPerformanceListener
import com.tokopedia.shop.score.performance.presentation.model.ItemDetailPerformanceUiModel
import com.tokopedia.shop.score.performance.presentation.widget.DetailPerformanceWidget
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.utils.view.binding.viewBinding

class ItemDetailPerformanceViewHolder(
    view: View,
    private val itemShopPerformanceListener: ItemShopPerformanceListener
) : AbstractViewHolder<ItemDetailPerformanceUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_detail_performance
    }

    private val binding: ItemDetailPerformanceBinding? by viewBinding()

    override fun bind(element: ItemDetailPerformanceUiModel?) {
        binding?.run {
            detailPerformanceWidget.setData(element, itemShopPerformanceListener) {
                setCardItemDetailPerformanceBackground(element)
            }
        }
    }

    private fun ItemDetailPerformanceBinding.setCardItemDetailPerformanceBackground(
        element: ItemDetailPerformanceUiModel?
    ) {
        detailPerformanceWidget.binding?.run {
            if (element?.isDividerHide == true) {
                cardItemDetailShopPerformance.setPadding(
                    DetailPerformanceWidget.SIXTEEN_PADDING.toPx(),
                    DetailPerformanceWidget.ZERO_PADDING.toPx(),
                    DetailPerformanceWidget.SIXTEEN_PADDING.toPx(),
                    DetailPerformanceWidget.SIXTEEN_PADDING.toPx()
                )
                root.context?.let {
                    detailPerformanceWidget.binding?.cardItemDetailShopPerformance?.setBackgroundResource(
                        R.drawable.corner_rounded_performance_list
                    )
                }
            } else {
                cardItemDetailShopPerformance.setPadding(
                    DetailPerformanceWidget.SIXTEEN_PADDING.toPx(),
                    DetailPerformanceWidget.ZERO_PADDING.toPx(),
                    DetailPerformanceWidget.SIXTEEN_PADDING.toPx(),
                    DetailPerformanceWidget.ZERO_PADDING.toPx()
                )
            }
        }
    }
}