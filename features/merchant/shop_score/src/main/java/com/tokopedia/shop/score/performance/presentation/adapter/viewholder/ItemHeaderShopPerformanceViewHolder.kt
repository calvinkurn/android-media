package com.tokopedia.shop.score.performance.presentation.adapter.viewholder

import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.material.shape.CornerFamily
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.gm.common.constant.NEW_SELLER_DAYS
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.common.ShopScoreConstant
import com.tokopedia.shop.score.common.ShopScoreUtils
import com.tokopedia.shop.score.performance.presentation.adapter.ShopPerformanceListener
import com.tokopedia.shop.score.performance.presentation.model.HeaderShopPerformanceUiModel
import com.tokopedia.unifycomponents.ticker.TickerCallback
import kotlinx.android.synthetic.main.item_header_shop_performance.view.*

class ItemHeaderShopPerformanceViewHolder(view: View,
                                          private val shopPerformanceListener: ShopPerformanceListener
) : AbstractViewHolder<HeaderShopPerformanceUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_header_shop_performance
    }

    override fun bind(element: HeaderShopPerformanceUiModel?) {
        setBackgroundRadiusHeader()
        setupProgressBarScore(element)
        setupShopScoreLevelHeader(element)
        setupClickListenerHeader(element)
        setupDescHeaderShopPerformance(element)
        setupTicker(element)
    }

    private fun setupShopScoreLevelHeader(element: HeaderShopPerformanceUiModel?) {
        with(itemView) {
            tvPerformanceLevel?.text = getString(R.string.shop_performance_level_header, element?.shopLevel)

            tvShopScoreValue?.text = if (element?.shopScore != null) element.shopScore else "-"

            ivLevelBarShopScore?.background = ContextCompat.getDrawable(context,
                    ShopScoreUtils.getLevelBarWhite(element?.shopLevel.toIntOrZero()))
        }
    }

    private fun setupProgressBarScore(element: HeaderShopPerformanceUiModel?) {
        with(itemView) {
            val shopScore = element?.shopScore.toIntOrZero()
            if (element?.shopAge.orZero() < ShopScoreConstant.SHOP_AGE_SIXTY) {
                progressBarNewSeller?.show()
                progressBarScorePerformance?.hide()
            } else {
                progressBarNewSeller?.hide()
                progressBarScorePerformance?.show()
                progressBarScorePerformance?.setValue(shopScore)
                setupProgressBarScoreColor(shopScore)
            }
        }
    }

    private fun setupProgressBarScoreColor(shopScore: Int) {
        with(itemView) {
            when (shopScore) {
                in ShopScoreConstant.SHOP_SCORE_ZERO..ShopScoreConstant.SHOP_SCORE_FIFTY_NINE -> {
                    progressBarScorePerformance?.progressBarColor = intArrayOf(
                            ContextCompat.getColor(context, R.color.shop_score_progressbar_dms_red),
                            ContextCompat.getColor(context, R.color.shop_score_progressbar_dms_red)
                    )
                }
                in ShopScoreConstant.SHOP_SCORE_SIXTY..ShopScoreConstant.SHOP_SCORE_SIXTY_NINE -> {
                    progressBarScorePerformance?.progressBarColor = intArrayOf(
                            ContextCompat.getColor(context, R.color.shop_score_progressbar_dms_yellow),
                            ContextCompat.getColor(context, R.color.shop_score_progressbar_dms_yellow)
                    )
                }
                in ShopScoreConstant.SHOP_SCORE_SEVENTY..ShopScoreConstant.SHOP_SCORE_SEVENTY_NINE -> {
                    progressBarScorePerformance?.progressBarColor = intArrayOf(
                            ContextCompat.getColor(context, R.color.shop_score_progressbar_dms_green_light),
                            ContextCompat.getColor(context, R.color.shop_score_progressbar_dms_green_light)
                    )
                }

                in ShopScoreConstant.SHOP_SCORE_EIGHTY..ShopScoreConstant.SHOP_SCORE_ONE_HUNDRED -> {
                    progressBarScorePerformance?.progressBarColor = intArrayOf(
                            ContextCompat.getColor(context, R.color.shop_score_progressbar_dms_green_dark),
                            ContextCompat.getColor(context, R.color.shop_score_progressbar_dms_green_dark)
                    )
                }
                else -> { }
            }
        }
    }

    private fun setupClickListenerHeader(element: HeaderShopPerformanceUiModel?) {
        with(itemView) {

            if (element?.shopAge.orZero() < ShopScoreConstant.SHOP_AGE_SIXTY) {
                ic_shop_score_performance?.hide()
            } else {
                ic_shop_score_performance?.show()
            }

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
            val isNewSeller = element?.shopAge.orZero() < NEW_SELLER_DAYS
            tickerShopHasPenalty?.showWithCondition(element?.scorePenalty.orZero() < 0 && !isNewSeller)
            tickerShopHasPenalty?.apply {
                if (element?.scorePenalty != null) {
                    setHtmlDescription(getString(R.string.ticker_deduction_point_penalty, element.scorePenalty?.toString()))
                }
                if (!isNewSeller) {
                    shopPerformanceListener.onTickerImpressionToPenaltyPage()
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