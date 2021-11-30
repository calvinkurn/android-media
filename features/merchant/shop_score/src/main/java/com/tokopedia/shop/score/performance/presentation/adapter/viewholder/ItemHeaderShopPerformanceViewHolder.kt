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
import com.tokopedia.shop.score.databinding.ItemHeaderShopPerformanceBinding
import com.tokopedia.shop.score.performance.presentation.adapter.ShopPerformanceListener
import com.tokopedia.shop.score.performance.presentation.model.HeaderShopPerformanceUiModel
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.utils.view.binding.viewBinding
import java.lang.NumberFormatException

class ItemHeaderShopPerformanceViewHolder(
    view: View,
    private val shopPerformanceListener: ShopPerformanceListener
) : AbstractViewHolder<HeaderShopPerformanceUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_header_shop_performance
        const val ROUNDED_RADIUS = 16F
    }

    private val binding: ItemHeaderShopPerformanceBinding? by viewBinding()

    override fun bind(element: HeaderShopPerformanceUiModel?) {
        setBackgroundRadiusHeader()
        setupProgressBarScore(element)
        setupShopScoreLevelHeader(element)
        setupClickListenerHeader(element)
        setupDescHeaderShopPerformance(element)
        setupTicker(element)
    }

    private fun setupShopScoreLevelHeader(element: HeaderShopPerformanceUiModel?) {
        binding?.run {
            tvPerformanceLevel.text =
                getString(R.string.shop_performance_level_header, element?.shopLevel)

            tvShopScoreValue.text = if (element?.shopScore != null) element.shopScore else "-"

            ivLevelBarShopScore.background = ContextCompat.getDrawable(
                root.context,
                ShopScoreUtils.getLevelBarWhite(element?.shopLevel.toLongOrZero())
            )
        }
    }

    private fun setupProgressBarScore(element: HeaderShopPerformanceUiModel?) {
        binding?.run {
            val shopScore = shopScoreFormatted(element?.shopScore)
            if (shopScore.isLessThanZero()) {
                progressBarNewSeller.show()
                progressBarScorePerformance.hide()
            } else {
                progressBarNewSeller.hide()
                progressBarScorePerformance.show()
                progressBarScorePerformance.setValue(shopScore)
                setupProgressBarScoreColor(shopScore)
            }
        }
    }

    private fun shopScoreFormatted(shopScore: String?): Int {
        return try {
            shopScore?.toIntOrNull() ?: ShopScoreConstant.SHOP_SCORE_NULL.toInt()
        } catch (e: NumberFormatException) {
            ShopScoreConstant.SHOP_SCORE_NULL.toInt()
        }
    }

    private fun setupProgressBarScoreColor(shopScore: Int) {
        binding?.run {
            when (shopScore) {
                in ShopScoreConstant.SHOP_SCORE_ZERO..ShopScoreConstant.SHOP_SCORE_FIFTY_NINE -> {
                    progressBarScorePerformance.progressBarColor = intArrayOf(
                        ContextCompat.getColor(
                            root.context,
                            R.color.shop_score_progressbar_dms_red
                        ),
                        ContextCompat.getColor(root.context, R.color.shop_score_progressbar_dms_red)
                    )
                }
                in ShopScoreConstant.SHOP_SCORE_SIXTY..ShopScoreConstant.SHOP_SCORE_SIXTY_NINE -> {
                    progressBarScorePerformance.progressBarColor = intArrayOf(
                        ContextCompat.getColor(
                            root.context,
                            R.color.shop_score_progressbar_dms_yellow
                        ),
                        ContextCompat.getColor(
                            root.context,
                            R.color.shop_score_progressbar_dms_yellow
                        )
                    )
                }
                in ShopScoreConstant.SHOP_SCORE_SEVENTY..ShopScoreConstant.SHOP_SCORE_SEVENTY_NINE -> {
                    progressBarScorePerformance.progressBarColor = intArrayOf(
                        ContextCompat.getColor(
                            root.context,
                            R.color.shop_score_progressbar_dms_green_light
                        ),
                        ContextCompat.getColor(
                            root.context,
                            R.color.shop_score_progressbar_dms_green_light
                        )
                    )
                }

                in ShopScoreConstant.SHOP_SCORE_EIGHTY..ShopScoreConstant.SHOP_SCORE_ONE_HUNDRED -> {
                    progressBarScorePerformance.progressBarColor = intArrayOf(
                        ContextCompat.getColor(
                            root.context,
                            R.color.shop_score_progressbar_dms_green_dark
                        ),
                        ContextCompat.getColor(
                            root.context,
                            R.color.shop_score_progressbar_dms_green_dark
                        )
                    )
                }
                else -> {
                }
            }
        }
    }

    private fun setupClickListenerHeader(element: HeaderShopPerformanceUiModel?) {
        binding?.run {

            val shopScore = shopScoreFormatted(element?.shopScore)

            if (shopScore.isLessThanZero()) {
                icShopScorePerformance.hide()
            } else {
                icShopScorePerformance.show()
            }

            icPerformanceLevelInformation.setOnClickListener {
                shopPerformanceListener.onTooltipLevelClicked(element?.shopLevel.toLongOrZero())
            }
            icShopScorePerformance.setOnClickListener {
                shopPerformanceListener.onTooltipScoreClicked()
            }
        }
    }

    private fun setBackgroundRadiusHeader() {
        binding?.run {
            containerHeaderShopPerformance.shapeAppearanceModel =
                containerHeaderShopPerformance.shapeAppearanceModel
                    .toBuilder()
                    .setTopRightCorner(CornerFamily.ROUNDED, ROUNDED_RADIUS)
                    .setTopLeftCorner(CornerFamily.ROUNDED, ROUNDED_RADIUS)
                    .build()
        }
    }

    private fun setupTicker(element: HeaderShopPerformanceUiModel?) {
        binding?.run {
            val isNewSeller = element?.shopAge.orZero() < NEW_SELLER_DAYS
            tickerShopHasPenalty.showWithCondition(
                element?.scorePenalty.orZero() < 0
                        && !isNewSeller
            )
            tickerShopHasPenalty.run {
                if (element?.scorePenalty != null) {
                    setHtmlDescription(
                        getString(
                            R.string.ticker_deduction_point_penalty,
                            element.scorePenalty?.toString()
                        )
                    )
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
        binding?.run {
            if (element?.showCard == true) {
                tvHeaderShopService.hide()
                tvDescShopService.hide()
                cardDescNewSeller.show()
                tvHeaderShopServiceNewSeller.text = element.titleHeaderShopService ?: "-"
                tvDescShopServiceNewSeller.text = element.descHeaderShopService ?: "-"
            } else {
                tvHeaderShopService.show()
                tvDescShopService.show()
                cardDescNewSeller.hide()
                tvHeaderShopService.text = element?.titleHeaderShopService ?: "-"
                tvDescShopService.text = element?.descHeaderShopService ?: "-"
            }
        }
    }
}