package com.tokopedia.shop.score.performance.presentation.adapter.viewholder.tablet

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.databinding.ItemDetailPerformanceBinding
import com.tokopedia.shop.score.performance.presentation.adapter.ShopPerformanceListener
import com.tokopedia.shop.score.performance.presentation.model.tablet.ItemDetailPerformanceTabletUiModel
import com.tokopedia.shop.score.performance.presentation.widget.DetailPerformanceWidget
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.utils.view.binding.viewBinding

class ItemDetailPerformanceTabletViewHolder(
    view: View,
    private val shopPerformanceListener: ShopPerformanceListener
) : AbstractViewHolder<ItemDetailPerformanceTabletUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_detail_performance
    }

    private val binding: ItemDetailPerformanceBinding? by viewBinding()

    override fun bind(element: ItemDetailPerformanceTabletUiModel?) {
        binding?.run {
            detailPerformanceWidgetTablet?.setData(element, shopPerformanceListener) {
                setCardItemDetailPerformanceBackground(element)
            }
        }
    }

    private fun ItemDetailPerformanceBinding.setCardItemDetailPerformanceBackground(
        element: ItemDetailPerformanceTabletUiModel?
    ) {
        detailPerformanceWidgetTablet?.binding?.run {
            if (element?.isDividerHide == true) {
                cardItemDetailShopPerformance.setPadding(
                    DetailPerformanceWidget.ZERO_PADDING.toPx(),
                    DetailPerformanceWidget.ZERO_PADDING.toPx(),
                    DetailPerformanceWidget.ZERO_PADDING.toPx(),
                    DetailPerformanceWidget.SIXTEEN_PADDING.toPx()
                )
                root.context?.let {
                    detailPerformanceWidgetTablet.binding?.cardItemDetailShopPerformance?.setBackgroundResource(
                        R.drawable.corner_rounded_performance_list_tablet
                    )
                }
            }
        }
    }
}
