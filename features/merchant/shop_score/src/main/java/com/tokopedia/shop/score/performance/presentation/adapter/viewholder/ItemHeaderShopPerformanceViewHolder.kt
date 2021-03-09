package com.tokopedia.shop.score.performance.presentation.adapter.viewholder

import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.common.ShopScoreUtils
import com.tokopedia.shop.score.performance.presentation.adapter.ShopPerformanceListener
import com.tokopedia.shop.score.performance.presentation.model.HeaderShopPerformanceUiModel
import com.tokopedia.unifycomponents.ticker.TickerCallback
import kotlinx.android.synthetic.main.item_header_shop_performance.view.*

class ItemHeaderShopPerformanceViewHolder(view: View,
                                          private val shopPerformanceListener: ShopPerformanceListener): AbstractViewHolder<HeaderShopPerformanceUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_header_shop_performance
    }

    override fun bind(element: HeaderShopPerformanceUiModel?) {
        with(itemView) {
            containerCornerShopPerformance?.background = ContextCompat.getDrawable(context, R.drawable.bg_performance_headline)
            containerHeaderShopPerformance?.background = ContextCompat.getDrawable(context, R.drawable.corner_rounded_performance_headline)
            containerHeaderShopPerformance?.clipToOutline = true
            tvPerformanceLevel?.text = getString(R.string.shop_performance_level_header, element?.shopLevel.orZero().toString())

            tvShopScoreValue?.text = if (element?.shopScore != null) element.shopScore.toString() else "-"

            progressBarScorePerformance?.setValue(element?.shopScore.orZero())

            ivLevelBarShopScore?.background = ContextCompat.getDrawable(context,
                    ShopScoreUtils.getLevelBarWhite(element?.shopLevel.orZero()))

            ic_performance_level_information?.setOnClickListener {
                shopPerformanceListener.onTooltipLevelClicked(element?.shopLevel.orZero())
            }
            ic_shop_score_performance?.setOnClickListener {
                shopPerformanceListener.onTooltipScoreClicked()
            }

            tvHeaderShopService?.text = getString(element?.titleHeaderShopService.orZero()).orEmpty()
            tvDescShopService?.text = getString(element?.descHeaderShopService.orZero()).orEmpty()

            tickerShopHasPenalty?.showWithCondition(element?.scorePenalty != null)
            tickerShopHasPenalty?.apply {
                if (element?.scorePenalty != null) {
                    setHtmlDescription(getString(R.string.ticker_deduction_point_penalty, element.scorePenalty?.toString()))
                }
                setDescriptionClickEvent(object : TickerCallback {
                    override fun onDescriptionViewClick(linkUrl: CharSequence) {
                        shopPerformanceListener.onTickerClickedToPenaltyPage()
                    }

                    override fun onDismiss() {}
                })
            }
        }
    }

}