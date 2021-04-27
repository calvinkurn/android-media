package com.tokopedia.shop.score.performance.presentation.adapter.viewholder

import android.graphics.Color
import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.common.ShopScoreConstant.AND_SYMBOL
import com.tokopedia.shop.score.common.ShopScoreConstant.AND_TEXT
import com.tokopedia.shop.score.performance.presentation.adapter.ItemShopPerformanceListener
import com.tokopedia.shop.score.performance.presentation.model.ItemDetailPerformanceUiModel
import com.tokopedia.unifycomponents.toPx
import kotlinx.android.synthetic.main.item_detail_shop_performance.view.*

class ItemDetailPerformanceViewHolder(view: View,
                                      private val itemShopPerformanceListener: ItemShopPerformanceListener)
    : AbstractViewHolder<ItemDetailPerformanceUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_detail_shop_performance
        const val PERCENT = "%"
    }

    override fun bind(element: ItemDetailPerformanceUiModel?) {
        with(itemView) {
            setupItemDetailPerformance(element)

            val titleBottomSheet = if (element?.titleDetailPerformance?.startsWith(getString(R.string.desc_calculation_open_seller_app)) == true) {
                getString(R.string.desc_calculation_open_seller_app)
            } else {
                if (element?.titleDetailPerformance?.contains(AND_TEXT) == true) {
                    element.titleDetailPerformance.replace(AND_TEXT, AND_SYMBOL)
                } else {
                    element?.titleDetailPerformance.orEmpty()
                }
            }
            setOnClickListener {
                itemShopPerformanceListener.onItemClickedToDetailBottomSheet(
                        titleBottomSheet,
                        element?.identifierDetailPerformance.orEmpty()
                )
            }
            ic_item_performance_right?.setOnClickListener {
                itemShopPerformanceListener.onItemClickedToDetailBottomSheet(
                        titleBottomSheet,
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
                cardItemDetailShopPerformance?.setPadding(16.toPx(), 0.toPx(), 16.toPx(), 16.toPx())
            } else {
                cardItemDetailShopPerformance?.setPadding(16.toPx(), 0.toPx(), 16.toPx(), 0.toPx())
            }
            tvTitlePerformanceProgress?.text = element?.titleDetailPerformance.orEmpty()
            tvPerformanceValue?.text =
                    if (element?.parameterValueDetailPerformance == PERCENT)
                        StringBuilder("${element.valueDetailPerformance}${element.parameterValueDetailPerformance}")
                    else
                        StringBuilder("${element?.valueDetailPerformance} ${element?.parameterValueDetailPerformance}")
            if (element?.colorValueDetailPerformance?.isNotBlank() == true) {
                tvPerformanceValue.setTextColor(Color.parseColor(element.colorValueDetailPerformance))
            }
            tvPerformanceTarget?.text = getString(R.string.item_detail_performance_target, element?.targetDetailPerformance.orEmpty())
        }
    }
}