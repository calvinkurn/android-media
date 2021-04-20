package com.tokopedia.shop.score.performance.presentation.adapter.viewholder

import android.graphics.Color
import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.performance.presentation.adapter.ItemShopPerformanceListener
import com.tokopedia.shop.score.performance.presentation.model.ItemDetailPerformanceUiModel
import com.tokopedia.unifycomponents.toPx
import kotlinx.android.synthetic.main.item_detail_shop_performance.view.*

class ItemDetailPerformanceViewHolder(view: View,
                                      private val itemShopPerformanceListener: ItemShopPerformanceListener)
    : AbstractViewHolder<ItemDetailPerformanceUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_detail_shop_performance
        const val POSITION_ITEM_DETAIL_PERFORMANCE_COACH_MARK = 2
    }

    private val impressHolder = ImpressHolder()
    override fun bind(element: ItemDetailPerformanceUiModel?) {
        with(itemView) {
            if (adapterPosition == POSITION_ITEM_DETAIL_PERFORMANCE_COACH_MARK) {
                addOnImpressionListener(impressHolder) {
                    itemShopPerformanceListener.onViewItemDetailPerformanceListener(this)
                }
            }

            setupItemDetailPerformance(element)

            setOnClickListener {
                itemShopPerformanceListener.onItemClickedToDetailBottomSheet(
                        element?.titleDetailPerformance.orEmpty(),
                        element?.identifierDetailPerformance.orEmpty()
                )
            }
            ic_item_performance_right?.setOnClickListener {
                itemShopPerformanceListener.onItemClickedToDetailBottomSheet(
                        element?.titleDetailPerformance.orEmpty(),
                        element?.identifierDetailPerformance.orEmpty()
                )
            }
        }
    }

    private fun setupItemDetailPerformance(element: ItemDetailPerformanceUiModel?) {
        with(itemView) {
            setBackgroundColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N0))
            separatorItemDetail?.showWithCondition(element?.isDividerHide == false)

            if (element?.isDividerHide == true) {
                cardItemDetailShopPerformance?.background = ContextCompat.getDrawable(context, R.drawable.corner_rounded_performance_list)
                cardItemDetailShopPerformance?.setPadding(16.toPx(), 0.toPx(), 16.toPx(), 8.toPx())
            }
            tvTitlePerformanceProgress?.text = element?.titleDetailPerformance.orEmpty()
            tvPerformanceValue?.text = StringBuilder("${element?.valueDetailPerformance} ${element?.targetDetailPerformance}")
            if (element?.colorValueDetailPerformance?.isNotBlank() == true) {
                tvPerformanceValue.setTextColor(Color.parseColor(element.colorValueDetailPerformance))
            }
            tvPerformanceTarget?.text = getString(R.string.item_detail_performance_target, element?.targetDetailPerformance.orEmpty())
        }
    }
}