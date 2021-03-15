package com.tokopedia.shop.score.performance.presentation.adapter.viewholder

import android.graphics.Color
import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
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
            setBackgroundColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N0))
            if (adapterPosition + 1 == element?.lastPosition) {
                cardItemDetailShopPerformance?.background = ContextCompat.getDrawable(context, R.drawable.corner_rounded_performance_list)
                separatorItemDetail?.hide()
            }
            tvTitlePerformanceProgress.text = element?.titleDetailPerformance.orEmpty()
            tvPerformanceValue.text = element?.valueDetailPerformance ?: "-"
            if (element?.colorValueDetailPerformance?.isNotBlank() == true) {
                tvPerformanceValue.setTextColor(Color.parseColor(element.colorValueDetailPerformance))
            }
            tvPerformanceTarget.text = getString(R.string.item_detail_performance_target, element?.targetDetailPerformance.orEmpty())
            setOnClickListener {
                itemShopPerformanceListener.onItemClickedToDetailBottomSheet(element?.titleDetailPerformance.orEmpty())
            }
            ic_item_performance_right?.setOnClickListener {
                itemShopPerformanceListener.onItemClickedToDetailBottomSheet(element?.titleDetailPerformance.orEmpty())
            }
        }
    }

}