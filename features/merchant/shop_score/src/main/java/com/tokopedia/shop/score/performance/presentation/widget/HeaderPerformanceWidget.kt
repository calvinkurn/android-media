package com.tokopedia.shop.score.performance.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.tokopedia.gm.common.constant.NEW_SELLER_DAYS
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isLessThanZero
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.common.ShopScoreConstant
import com.tokopedia.shop.score.common.ShopScoreUtils
import com.tokopedia.shop.score.databinding.HeaderPerformanceWidgetBinding
import com.tokopedia.shop.score.performance.presentation.adapter.ShopPerformanceListener
import com.tokopedia.shop.score.performance.presentation.model.HeaderShopPerformanceUiModel
import com.tokopedia.unifycomponents.ticker.TickerCallback
import java.lang.NumberFormatException

class HeaderPerformanceWidget : ConstraintLayout {

    val binding: HeaderPerformanceWidgetBinding?
        get() = _binding

    private var _binding: HeaderPerformanceWidgetBinding? = null
    private var shopPerformanceListener: ShopPerformanceListener? = null

    constructor (context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        _binding = HeaderPerformanceWidgetBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun setData(
        element: HeaderShopPerformanceUiModel?,
        shopPerformanceListener: ShopPerformanceListener
    ) {
        this.shopPerformanceListener = shopPerformanceListener
        setupProgressBarScore(element)
        setupShopScoreLevelHeader(element)
        setupClickListenerHeader(element)
        setupDescHeaderShopPerformance(element)
        setupTicker(element)
    }

    private fun setupShopScoreLevelHeader(element: HeaderShopPerformanceUiModel?) {
        binding?.run {
            tvPerformanceLevel.text =
                root.context.getString(R.string.shop_performance_level_header, element?.shopLevel)

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
                shopPerformanceListener?.onTooltipLevelClicked(element?.shopLevel.toLongOrZero())
            }
            icShopScorePerformance.setOnClickListener {
                shopPerformanceListener?.onTooltipScoreClicked()
            }
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
                        context.getString(
                            R.string.ticker_deduction_point_penalty,
                            element.scorePenalty?.toString()
                        )
                    )
                }
                if (!isNewSeller) {
                    shopPerformanceListener?.onTickerImpressionToPenaltyPage()
                }
                setDescriptionClickEvent(object : TickerCallback {
                    override fun onDescriptionViewClick(linkUrl: CharSequence) {
                        shopPerformanceListener?.onTickerClickedToPenaltyPage()
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
                tvHeaderShopServiceNewSeller.text =
                    element.titleHeaderShopService?.let { context.getString(it) }.orEmpty()
                tvDescShopServiceNewSeller.text =
                    element.descHeaderShopService?.let { context.getString(it) }.orEmpty()
            } else {
                tvHeaderShopService.show()
                tvDescShopService.show()
                cardDescNewSeller.hide()
                tvHeaderShopService.text =
                    element?.titleHeaderShopService?.let { context.getString(it) }.orEmpty()
                tvDescShopService.text =
                    element?.descHeaderShopService?.let { context.getString(it) }.orEmpty()
            }
        }
    }
}