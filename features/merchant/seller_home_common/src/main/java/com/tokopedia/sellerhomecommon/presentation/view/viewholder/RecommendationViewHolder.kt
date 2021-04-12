package com.tokopedia.sellerhomecommon.presentation.view.viewholder

import android.view.View
import android.view.ViewStub
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.presentation.model.RecommendationUiModel
import com.tokopedia.sellerhomecommon.presentation.model.RecommendationWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.view.adapter.WidgetRecommendationItemAdapter
import com.tokopedia.sellerhomecommon.presentation.view.customview.ShopScorePMWidget
import com.tokopedia.sellerhomecommon.utils.clearUnifyDrawableEnd
import com.tokopedia.sellerhomecommon.utils.setUnifyDrawableEnd
import kotlinx.android.synthetic.main.shc_partial_common_widget_state_error.view.*
import kotlinx.android.synthetic.main.shc_recommendation_widget_error.view.*
import kotlinx.android.synthetic.main.shc_recommendation_widget_loading.view.*
import kotlinx.android.synthetic.main.shc_recommendation_widget_success.view.*

/**
 * Created By @ilhamsuaib on 05/04/21
 */

class RecommendationViewHolder(
        itemView: View,
        private val listener: Listener
) : AbstractViewHolder<RecommendationWidgetUiModel>(itemView) {

    companion object {
        val RES_LAYOUT = R.layout.shc_recommendation_widget
    }

    private val onLoadingView: View by itemView.viewStubInflater(R.id.stubShcRecommendationLoading)
    private val onErrorView: View by itemView.viewStubInflater(R.id.stubShcRecommendationError)
    private val onSuccessView: View by itemView.viewStubInflater(R.id.stubShcRecommendationSuccess)

    override fun bind(element: RecommendationWidgetUiModel) {
        val data = element.data
        when {
            data == null -> showLoadingState()
            data.error.isNotBlank() -> showErrorState(element)
            else -> setOnSuccess(element)
        }
    }

    private fun showErrorState(element: RecommendationWidgetUiModel) = with(onErrorView) {
        onSuccessView.containerShcRecommendationSuccess.gone()
        onLoadingView.containerShcRecommendationLoading.gone()
        containerShcRecommendationError.visible()

        tvShcRecommendationErrorStateTitle.text = element.title
        ImageHandler.loadImageWithId(imgWidgetOnError, com.tokopedia.globalerror.R.drawable.unify_globalerrors_connection)

        setupTooltip(tvShcRecommendationErrorStateTitle, element)
    }

    private fun showLoadingState() = with(onLoadingView) {
        onSuccessView.containerShcRecommendationSuccess.gone()
        onErrorView.containerShcRecommendationError.gone()
        containerShcRecommendationLoading.visible()
    }

    private fun setOnSuccess(element: RecommendationWidgetUiModel) {
        with(onSuccessView) {
            onLoadingView.containerShcRecommendationLoading.gone()
            onErrorView.containerShcRecommendationError.gone()
            containerShcRecommendationSuccess.visible()

            tvShcRecommendationTitle.text = element.title
            val level = element.data?.progressLevel?.bar?.value.orZero()
            slvShcShopLevel.show(level)

            setupRecommendations(element.data?.recommendation)

            val progressBar = element.data?.progressBar
            val progressTitle = progressBar?.text.orEmpty()
            val currentProgressText = progressBar?.bar?.value.orZero().toString()
            val setMaxProgressText = progressBar?.bar?.maxValue.orZero().toString()
            val currentProgressValue = progressBar?.bar?.value.orZero()
            val setMaxProgressValue = progressBar?.bar?.maxValue.orZero()
            val progressState = getProgressState(currentProgressValue, setMaxProgressValue)
            setupProgressBar(progressTitle, currentProgressText, setMaxProgressText, currentProgressValue, setMaxProgressValue, progressState)

            setupCta(element)
            setupTooltip(tvShcRecommendationTitle, element)
        }
    }

    private fun setupCta(element: RecommendationWidgetUiModel) {
        with(onSuccessView) {
            val isCtaVisible = element.appLink.isNotBlank() && element.ctaText.isNotBlank()
            val ctaVisibility = if (isCtaVisible) View.VISIBLE else View.GONE
            tvShcRecommendationCta.visibility = ctaVisibility

            if (isCtaVisible) {
                tvShcRecommendationCta.text = element.ctaText
                tvShcRecommendationCta.setOnClickListener {
                    RouteManager.route(context, element.appLink)
                }
                val iconColor = context.getResColor(com.tokopedia.unifyprinciples.R.color.Unify_G400)
                tvShcRecommendationCta.setUnifyDrawableEnd(IconUnify.CHEVRON_RIGHT, iconColor)
            }
        }
    }

    private fun setupTooltip(titleTextView: TextView, element: RecommendationWidgetUiModel) = with(itemView) {
        val tooltip = element.tooltip
        val shouldShowTooltip = (tooltip?.shouldShow == true) && (tooltip.content.isNotBlank() || tooltip.list.isNotEmpty())
        if (shouldShowTooltip) {
            titleTextView.setUnifyDrawableEnd(IconUnify.INFORMATION)
            titleTextView.setOnClickListener {
                listener.onTooltipClicked(tooltip ?: return@setOnClickListener)
            }
        } else {
            titleTextView.clearUnifyDrawableEnd()
        }
    }

    private fun setupRecommendations(data: RecommendationUiModel?) = with(onSuccessView) {
        data?.let { data ->
            val dp24 = context.resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.layout_lvl3)
            tvShcRecommendationHeaderItem.setUnifyDrawableEnd(IconUnify.CHEVRON_UP, width = dp24, height = dp24)
            tvShcRecommendationHeaderItem.text = data.title

            tvShcRecommendationHeaderItem.setOnClickListener {
                if (rvShcRecommendationList.isVisible) {
                    rvShcRecommendationList.gone()
                    tvShcRecommendationHeaderItem.setUnifyDrawableEnd(IconUnify.CHEVRON_DOWN, width = dp24, height = dp24)
                    horLineShcShopScore2.gone()
                } else {
                    rvShcRecommendationList.visible()
                    tvShcRecommendationHeaderItem.setUnifyDrawableEnd(IconUnify.CHEVRON_UP, width = dp24, height = dp24)
                    horLineShcShopScore2.visible()
                }
            }

            val adapter = WidgetRecommendationItemAdapter(data.recommendations)
            rvShcRecommendationList.layoutManager = object : LinearLayoutManager(context) {
                override fun canScrollVertically(): Boolean = false
            }
            rvShcRecommendationList.adapter = adapter
        }
    }

    private fun getProgressState(value: Int, max: Int): ShopScorePMWidget.State {
        val textColor = com.tokopedia.unifyprinciples.R.color.Unify_N700_96
        val barColor = when (value * max / 100) {
            in 80..100 -> com.tokopedia.unifyprinciples.R.color.Unify_G400
            in 70..79 -> com.tokopedia.unifyprinciples.R.color.Unify_G300
            in 60..69 -> com.tokopedia.unifyprinciples.R.color.Unify_Y300
            else -> com.tokopedia.unifyprinciples.R.color.Unify_R500
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
    ) = with(onSuccessView.sspShcShopScoreProgress) {
        setProgressTitle(progressTitle)
        setCurrentProgressText(currentProgressText)
        setMaxProgressText(maxProgressText)
        setProgressValue(currentProgress)
        setMaxProgressValue(maxProgress)
        setProgressColor(state)
    }

    private fun View.viewStubInflater(viewStubId: Int): Lazy<View> {
        return lazy {
            val viewStub: ViewStub = findViewById(viewStubId)
            viewStub.inflate()
        }
    }

    interface Listener : BaseViewHolderListener
}