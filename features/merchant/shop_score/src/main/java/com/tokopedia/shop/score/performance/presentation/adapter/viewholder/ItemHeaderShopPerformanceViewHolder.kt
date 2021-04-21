package com.tokopedia.shop.score.performance.presentation.adapter.viewholder

import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.material.shape.CornerFamily
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.common.ShopScoreUtils
import com.tokopedia.shop.score.performance.presentation.adapter.ItemHeaderShopPerformanceListener
import com.tokopedia.shop.score.performance.presentation.adapter.ShopPerformanceListener
import com.tokopedia.shop.score.performance.presentation.model.HeaderShopPerformanceUiModel
import com.tokopedia.unifycomponents.ticker.TickerCallback
import kotlinx.android.synthetic.main.item_header_shop_performance.view.*

class ItemHeaderShopPerformanceViewHolder(view: View,
                                          private val shopPerformanceListener: ShopPerformanceListener,
                                          private val itemHeaderShopPerformanceListener: ItemHeaderShopPerformanceListener
) : AbstractViewHolder<HeaderShopPerformanceUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_header_shop_performance
    }

    private val impressHolderTicker = ImpressHolder()

    override fun bind(element: HeaderShopPerformanceUiModel?) {
        with(itemView) {
            itemHeaderShopPerformanceListener.onViewHeaderListener(containerCornerShopPerformance)
        }

        setBackgroundRadiusHeader()
        setupShopScoreLevelHeader(element)
        setupClickListenerHeader(element)
        setupDescHeaderShopPerformance(element)
        setupTicker(element)
    }

    private fun setupShopScoreLevelHeader(element: HeaderShopPerformanceUiModel?) {
        with(itemView) {
            tvPerformanceLevel?.text = getString(R.string.shop_performance_level_header, element?.shopLevel)

            tvShopScoreValue?.text = if (element?.shopScore != null) element.shopScore else "-"

            progressBarScorePerformance?.setValue(element?.shopScore.toIntOrZero())

            ivLevelBarShopScore?.background = ContextCompat.getDrawable(context,
                    ShopScoreUtils.getLevelBarWhite(element?.shopLevel.toIntOrZero()))
        }
    }

    private fun setupClickListenerHeader(element: HeaderShopPerformanceUiModel?) {
        with(itemView) {
            ic_performance_level_information?.setOnClickListener {
                shopPerformanceListener.onTooltipLevelClicked(element?.shopLevel.toIntOrZero())
            }
            ic_shop_score_performance?.setOnClickListener {
                shopPerformanceListener.onTooltipScoreClicked()
            }
        }
    }

    private fun setBackgroundRadiusHeader() {
        with(itemView) {
            val roundedRadius = 16F
            containerHeaderShopPerformance.shapeAppearanceModel = containerHeaderShopPerformance.shapeAppearanceModel
                    .toBuilder()
                    .setTopRightCorner(CornerFamily.ROUNDED, roundedRadius)
                    .setTopLeftCorner(CornerFamily.ROUNDED, roundedRadius)
                    .build()
        }
    }

    private fun setupTicker(element: HeaderShopPerformanceUiModel?) {
        with(itemView) {
            tickerShopHasPenalty?.showWithCondition(element?.scorePenalty.orZero() < 0)
            tickerShopHasPenalty?.apply {
                addOnImpressionListener(impressHolderTicker) {

                }
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

    private fun setupDescHeaderShopPerformance(element: HeaderShopPerformanceUiModel?) {
        with(itemView) {
            if (element?.showCardNewSeller == true) {
                tvHeaderShopService?.hide()
                tvDescShopService?.hide()
                cardDescNewSeller?.show()
                tvHeaderShopServiceNewSeller?.text = element.titleHeaderShopService ?: "-"
                tvDescShopServiceNewSeller?.text = element.descHeaderShopService ?: "-"
            } else {
                tvHeaderShopService?.show()
                tvDescShopService?.show()
                cardDescNewSeller?.hide()
                tvHeaderShopService?.text = element?.titleHeaderShopService ?: "-"
                tvDescShopService?.text = element?.descHeaderShopService ?: "-"
            }
        }
    }
}