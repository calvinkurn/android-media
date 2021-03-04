package com.tokopedia.shop.score.performance.presentation.adapter.viewholder

import android.graphics.Color
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.performance.presentation.adapter.ItemShopPerformanceListener
import com.tokopedia.shop.score.performance.presentation.model.ItemDetailPerformanceUiModel
import kotlinx.android.synthetic.main.item_detail_shop_performance.view.*

class ItemDetailPerformanceViewHolder(view: View,
                                      private val itemShopPerformanceListener: ItemShopPerformanceListener)
    : AbstractViewHolder<ItemDetailPerformanceUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_detail_shop_performance
    }

    override fun bind(element: ItemDetailPerformanceUiModel?) {
        with(itemView) {
            tvTitlePerformanceProgress.text = element?.titleDetailPerformance.orEmpty()
            tvPerformanceValue.text = element?.valueDetailPerformance.orEmpty()
            if (element?.colorValueDetailPerformance?.isNotBlank() == true) {
                tvPerformanceValue.setTextColor(Color.parseColor(element.colorValueDetailPerformance))
            }
            tvPerformanceTarget.text = element?.targetDetailPerformance.orEmpty()
            setOnClickListener {
                itemShopPerformanceListener.onItemClickedToDetailBottomSheet(element?.titleDetailPerformance.orEmpty())
            }
            ic_item_performance_right?.setOnClickListener {
                itemShopPerformanceListener.onItemClickedToDetailBottomSheet(element?.titleDetailPerformance.orEmpty())
            }
        }
    }

}