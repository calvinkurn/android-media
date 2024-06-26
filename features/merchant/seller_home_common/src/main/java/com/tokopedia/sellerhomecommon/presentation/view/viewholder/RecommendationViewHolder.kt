package com.tokopedia.sellerhomecommon.presentation.view.viewholder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.getResColor
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.databinding.ShcRecommendationWidgetBinding
import com.tokopedia.sellerhomecommon.databinding.ShcRecommendationWidgetErrorBinding
import com.tokopedia.sellerhomecommon.databinding.ShcRecommendationWidgetLoadingBinding
import com.tokopedia.sellerhomecommon.databinding.ShcRecommendationWidgetSuccessBinding
import com.tokopedia.sellerhomecommon.presentation.model.RecommendationItemUiModel
import com.tokopedia.sellerhomecommon.presentation.model.RecommendationTickerUiModel
import com.tokopedia.sellerhomecommon.presentation.model.RecommendationWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.view.adapter.WidgetRecommendationItemAdapter
import com.tokopedia.sellerhomecommon.presentation.view.customview.ShopScorePMWidget
import com.tokopedia.sellerhomecommon.utils.clearUnifyDrawableEnd
import com.tokopedia.sellerhomecommon.utils.setUnifyDrawableEnd
import com.tokopedia.unifycomponents.NotificationUnify
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

/**
 * Created By @ilhamsuaib on 05/04/21
 */

class RecommendationViewHolder(
    itemView: View,
    private val listener: Listener
) : AbstractViewHolder<RecommendationWidgetUiModel>(itemView) {

    companion object {
        val RES_LAYOUT = R.layout.shc_recommendation_widget
        private const val DIMEN_8_DP = 8
        private const val DIMEN_12_DP = 12
        private const val DIMEN_16_DP = 16
        private const val DIMEN_20_DP = 20
        private const val INT_60 = 60
        private const val INT_69 = 69
        private const val INT_70 = 70
        private const val INT_79 = 79
        private const val INT_80 = 80
        private const val INT_100 = 100
    }

    private val binding by lazy { ShcRecommendationWidgetBinding.bind(itemView) }
    private val loadingStateBinding by lazy {
        val view = binding.stubShcRecommendationLoading.inflate()
        ShcRecommendationWidgetLoadingBinding.bind(view)
    }
    private val errorStateBinding by lazy {
        val view = binding.stubShcRecommendationError.inflate()
        ShcRecommendationWidgetErrorBinding.bind(view)
    }

    private val successStateBinding by lazy {
        val view = binding.stubShcRecommendationSuccess.inflate()
        ShcRecommendationWidgetSuccessBinding.bind(view)
    }

    override fun bind(element: RecommendationWidgetUiModel) {
        val data = element.data
        when {
            data == null || element.showLoadingState -> showLoadingState()
            data.error.isNotBlank() -> showErrorState(element)
            else -> setOnSuccess(element)
        }
    }

    private fun showErrorState(element: RecommendationWidgetUiModel) = with(errorStateBinding) {
        successStateBinding.containerShcRecommendationSuccess.gone()
        loadingStateBinding.containerShcRecommendationLoading.gone()
        containerShcRecommendationError.visible()
        shcRecommendationCommonErrorView.setOnReloadClicked {
            listener.onReloadWidget(element)
        }

        tvShcRecommendationErrorStateTitle.text = element.title

        setupTooltip(tvShcRecommendationErrorStateTitle, element)
    }

    private fun showLoadingState() {
        successStateBinding.containerShcRecommendationSuccess.gone()
        errorStateBinding.containerShcRecommendationError.gone()
        loadingStateBinding.containerShcRecommendationLoading.visible()
    }

    private fun setOnSuccess(element: RecommendationWidgetUiModel) {
        with(successStateBinding) {
            loadingStateBinding.containerShcRecommendationLoading.gone()
            errorStateBinding.containerShcRecommendationError.gone()
            containerShcRecommendationSuccess.visible()

            tvShcRecommendationTitle.text = element.title
            val progressLevel = element.data?.progressLevel
            slvShcShopLevel.show(progressLevel?.text.orEmpty(), progressLevel?.bar?.value.orZero())

            setupTicker(element)
            setupRecommendations(element)

            val progressBar = element.data?.progressBar
            val progressTitle = progressBar?.text.orEmpty()
            val currentProgressText = progressBar?.bar?.valueToDisplay.orEmpty()
            val setMaxProgressText = progressBar?.bar?.maxValue.orZero().toString()
            val currentProgressValue = progressBar?.bar?.value.orZero()
            val setMaxProgressValue = progressBar?.bar?.maxValue.orZero()
            val progressState = getProgressState(currentProgressValue, setMaxProgressValue)
            setupProgressBar(
                progressTitle,
                currentProgressText,
                setMaxProgressText,
                currentProgressValue,
                setMaxProgressValue,
                progressState
            )

            setupCta(element)
            setTagNotification(element.tag)
            setupTooltip(tvShcRecommendationTitle, element)
            setupLastUpdatedInfo(element)

            horLineShcRecommendationBtm.isVisible = luvShcRecommendation.isVisible
                || tvShcRecommendationCta.isVisible

            itemView.addOnImpressionListener(element.impressHolder) {
                listener.sendRecommendationImpressionEvent(element)
            }
        }
    }

    private fun setupLastUpdatedInfo(element: RecommendationWidgetUiModel) {
        with(successStateBinding.luvShcRecommendation) {
            element.data?.lastUpdated?.let { lastUpdated ->
                isVisible = lastUpdated.isEnabled
                setLastUpdated(lastUpdated.lastUpdatedInMillis)
                setRefreshButtonVisibility(lastUpdated.needToUpdated)
                setRefreshButtonClickListener {
                    refreshWidget(element)
                }
            }
        }
    }

    private fun refreshWidget(element: RecommendationWidgetUiModel) {
        listener.onReloadWidget(element)
    }

    private fun setTagNotification(tag: String) {
        val isTagVisible = tag.isNotBlank()
        with(successStateBinding) {
            notifTagRecommendation.showWithCondition(isTagVisible)
            if (isTagVisible) {
                notifTagRecommendation.setNotification(
                    tag,
                    NotificationUnify.TEXT_TYPE,
                    NotificationUnify.COLOR_TEXT_TYPE
                )
            }
        }
    }

    private fun setupTicker(element: RecommendationWidgetUiModel) {
        with(successStateBinding) {
            val ticker = element.data?.ticker
            if (ticker?.text.isNullOrBlank()) {
                tickerShcRecommendation.gone()

                val marginLeft = root.context.resources.getDimensionPixelSize(
                    unifyprinciplesR.dimen.unify_space_12
                )
                val marginTop = root.context.resources.getDimensionPixelSize(
                    unifyprinciplesR.dimen.layout_lvl3
                )
                slvShcShopLevel.setMargin(marginLeft, marginTop, Int.ZERO, Int.ZERO)
                return
            }

            ticker?.let {
                tickerShcRecommendation.visible()
                tickerShcRecommendation.setHtmlDescription(ticker.text)
                tickerShcRecommendation.tickerType = when (ticker.type) {
                    RecommendationTickerUiModel.TYPE_ERROR -> Ticker.TYPE_ERROR
                    RecommendationTickerUiModel.TYPE_INFO -> Ticker.TYPE_ANNOUNCEMENT
                    RecommendationTickerUiModel.TYPE_WARNING -> Ticker.TYPE_WARNING
                    else -> Ticker.TYPE_ANNOUNCEMENT
                }
                tickerShcRecommendation.setDescriptionClickEvent(object : TickerCallback {
                    override fun onDescriptionViewClick(linkUrl: CharSequence) {
                        if (RouteManager.route(root.context, linkUrl.toString())) {
                            listener.sendRecommendationTickerCtaClickEvent(element)
                        }
                    }

                    override fun onDismiss() {

                    }
                })

                val marginLeft = root.context.resources.getDimensionPixelSize(
                    unifyprinciplesR.dimen.unify_space_12
                )
                val marginTop = root.context.resources.getDimensionPixelSize(
                    unifyprinciplesR.dimen.layout_lvl1
                )
                slvShcShopLevel.setMargin(marginLeft, marginTop, Int.ZERO, Int.ZERO)
            }
        }
    }

    private fun setupCta(element: RecommendationWidgetUiModel) {
        with(successStateBinding) {
            val isCtaVisible = element.appLink.isNotBlank() && element.ctaText.isNotBlank()
            val ctaVisibility = if (isCtaVisible) View.VISIBLE else View.GONE
            tvShcRecommendationCta.visibility = ctaVisibility

            if (isCtaVisible) {
                tvShcRecommendationCta.text = element.ctaText
                tvShcRecommendationCta.setOnClickListener {
                    openAppLink(element)
                }
                val iconColor = root.context.getResColor(
                    unifyprinciplesR.color.Unify_GN500
                )
                val iconWidth = root.context.resources.getDimension(
                    unifyprinciplesR.dimen.layout_lvl3
                )
                val iconHeight = root.context.resources.getDimension(
                    unifyprinciplesR.dimen.layout_lvl3
                )
                tvShcRecommendationCta.setUnifyDrawableEnd(
                    IconUnify.CHEVRON_RIGHT,
                    iconColor,
                    iconWidth,
                    iconHeight
                )
            }
        }
    }

    private fun openAppLink(element: RecommendationWidgetUiModel) {
        if (RouteManager.route(itemView.context, element.appLink)) {
            listener.sendRecommendationCtaClickEvent(element)
        }
    }

    private fun setupTooltip(titleTextView: TextView, element: RecommendationWidgetUiModel) =
        with(itemView) {
            val tooltip = element.tooltip
            val shouldShowTooltip =
                (tooltip?.shouldShow == true) && (tooltip.content.isNotBlank() || tooltip.list.isNotEmpty())
            if (shouldShowTooltip) {
                titleTextView.setUnifyDrawableEnd(IconUnify.INFORMATION)
                titleTextView.setOnClickListener {
                    listener.onTooltipClicked(tooltip ?: return@setOnClickListener)
                }
            } else {
                titleTextView.clearUnifyDrawableEnd()
            }
        }

    private fun setupRecommendations(element: RecommendationWidgetUiModel) =
        with(successStateBinding) {
            val recommendation = element.data?.recommendation
            recommendation?.let { data ->
                val dp24 = root.context.resources.getDimension(
                    unifyprinciplesR.dimen.layout_lvl3
                )
                tvShcRecommendationHeaderItem.setUnifyDrawableEnd(
                    IconUnify.CHEVRON_UP,
                    width = dp24,
                    height = dp24
                )
                tvShcRecommendationHeaderItem.text = data.title

                val margin8dp = root.context.dpToPx(DIMEN_8_DP).toInt()
                tvShcRecommendationHeaderItem.setOnClickListener {
                    if (rvShcRecommendationList.isVisible) {
                        rvShcRecommendationList.gone()
                        tvShcRecommendationHeaderItem.setUnifyDrawableEnd(
                            IconUnify.CHEVRON_DOWN,
                            width = dp24,
                            height = dp24
                        )

                        if (element.ctaText.isBlank()) {
                            horLineShcShopScore2.gone()
                        } else {
                            horLineShcShopScore2.visible()
                        }

                        val margin = root.context.dpToPx(DIMEN_16_DP).toInt()
                        val marginTopLastUpdated = root.context.dpToPx(DIMEN_20_DP).toInt()
                        tvShcRecommendationCta.setMargin(Int.ZERO, margin, margin8dp, margin)
                        luvShcRecommendation.setMargin(
                            luvShcRecommendation.left,
                            marginTopLastUpdated,
                            0,
                            margin
                        )
                    } else {
                        rvShcRecommendationList.visible()
                        tvShcRecommendationHeaderItem.setUnifyDrawableEnd(
                            IconUnify.CHEVRON_UP,
                            width = dp24,
                            height = dp24
                        )
                        horLineShcShopScore2.visible()

                        val marginTopLastUpdated = root.context.dpToPx(DIMEN_12_DP).toInt()
                        val marginBottom = root.context.dpToPx(DIMEN_16_DP).toInt()
                        tvShcRecommendationCta.setMargin(
                            Int.ZERO,
                            margin8dp,
                            margin8dp,
                            marginBottom
                        )
                        luvShcRecommendation.setMargin(
                            luvShcRecommendation.left,
                            marginTopLastUpdated,
                            Int.ZERO,
                            marginBottom
                        )
                    }
                }

                val adapter = WidgetRecommendationItemAdapter(data.recommendations) {
                    listener.sendRecommendationItemClickEvent(element, it)
                }
                rvShcRecommendationList.layoutManager = object : LinearLayoutManager(root.context) {
                    override fun canScrollVertically(): Boolean = false
                }
                rvShcRecommendationList.adapter = adapter
            }
        }

    private fun getProgressState(value: Int, max: Int): ShopScorePMWidget.State {
        val textColor = unifyprinciplesR.color.Unify_NN950_96
        val barColor = when (value * max / INT_100) {
            in INT_80..INT_100 -> unifyprinciplesR.color.Unify_GN500
            in INT_70..INT_79 -> unifyprinciplesR.color.Unify_GN300
            in INT_60..INT_69 -> unifyprinciplesR.color.Unify_YN300
            else -> unifyprinciplesR.color.Unify_RN500
        }
        return ShopScorePMWidget.State.Custom(textColor, barColor, barColor)
    }

    private fun setupProgressBar(
        progressTitle: String,
        currentProgressText: String,
        maxProgressText: String,
        currentProgress: Int,
        maxProgress: Int,
        state: ShopScorePMWidget.State
    ) = with(successStateBinding.sspShcShopScoreProgress) {
        setProgressTitle(progressTitle)
        setCurrentProgressText(currentProgressText)
        setMaxProgressText(maxProgressText)
        setProgressValue(currentProgress)
        setMaxProgressValue(maxProgress)
        setProgressColor(state)
    }

    interface Listener : BaseViewHolderListener {
        fun sendRecommendationImpressionEvent(element: RecommendationWidgetUiModel) {}

        fun sendRecommendationCtaClickEvent(element: RecommendationWidgetUiModel) {}

        fun sendRecommendationItemClickEvent(
            element: RecommendationWidgetUiModel,
            item: RecommendationItemUiModel
        ) {
        }

        fun sendRecommendationTickerCtaClickEvent(element: RecommendationWidgetUiModel) {}
    }
}
