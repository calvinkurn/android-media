package com.tokopedia.sellerhomecommon.presentation.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.charts.config.PieChartConfig
import com.tokopedia.charts.model.PieChartEntry
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.media.loader.loadImage
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.common.const.SellerHomeUrl
import com.tokopedia.sellerhomecommon.databinding.*
import com.tokopedia.sellerhomecommon.presentation.model.PieChartWidgetUiModel
import com.tokopedia.sellerhomecommon.utils.clearUnifyDrawableEnd
import com.tokopedia.sellerhomecommon.utils.setUnifyDrawableEnd
import com.tokopedia.sellerhomecommon.utils.toggleWidgetHeight
import com.tokopedia.unifycomponents.NotificationUnify

/**
 * Created By @ilhamsuaib on 06/07/20
 */

class PieChartViewHolder(
    itemView: View,
    private val listener: Listener
) : AbstractViewHolder<PieChartWidgetUiModel>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.shc_pie_chart_widget
    }

    private val binding by lazy { ShcPieChartWidgetBinding.bind(itemView) }
    private val errorStateBinding by lazy { binding.shcPieChartErrorState }
    private val loadingStateBinding by lazy { binding.shcPieChartLoadingState }
    private val emptyStateBinding by lazy {
        ShcPartialPieChartWidgetEmptyBinding.bind(binding.root)
    }

    override fun bind(element: PieChartWidgetUiModel) {
        with(binding) {
            if (!listener.getIsShouldRemoveWidget()) {
                root.toggleWidgetHeight(true)
            }
            tvPieChartTitle.text = element.title
            setTagNotification(element.tag)
            setupTooltip(element)

            observeState(element)
        }
    }

    private fun observeState(element: PieChartWidgetUiModel) {
        val data = element.data

        when {
            data == null -> setOnLoading()
            data.error.isNotBlank() -> {
                setOnError()
                listener.setOnErrorWidget(adapterPosition, element, data.error)
            }
            else -> setOnSuccess(element)
        }
    }

    private fun setOnLoading() {
        with(binding) {
            loadingStateBinding.shimmerWidgetCommon.visible()
            errorStateBinding.commonWidgetErrorState.gone()
            pieChartShc.gone()
            tvPieChartValue.gone()
            tvPieChartSubValue.gone()
            emptyStateBinding.groupShcPieChartEmpty.gone()
        }
    }

    private fun setTagNotification(tag: String) {
        val isTagVisible = tag.isNotBlank()
        with(binding) {
            notifTagPieChart.showWithCondition(isTagVisible)
            if (isTagVisible) {
                notifTagPieChart.setNotification(
                    tag,
                    NotificationUnify.TEXT_TYPE,
                    NotificationUnify.COLOR_TEXT_TYPE
                )
            }
        }
    }

    private fun setOnSuccess(element: PieChartWidgetUiModel) = with(binding) {
        loadingStateBinding.shimmerWidgetCommon.gone()
        errorStateBinding.commonWidgetErrorState.gone()
        pieChartShc.visible()
        tvPieChartValue.visible()
        tvPieChartSubValue.visible()
        emptyStateBinding.groupShcPieChartEmpty.gone()

        if (element.isEmpty()) {
            if (element.isShowEmpty) {
                if (element.shouldShowEmptyStateIfEmpty()) {
                    showEmptyState(element)
                } else {
                    setupPieChart(element)
                }
                setupLastUpdatedInfo(element)
            } else {
                if (listener.getIsShouldRemoveWidget()) {
                    listener.removeWidget(adapterPosition, element)
                } else {
                    listener.onRemoveWidget(adapterPosition)
                    root.toggleWidgetHeight(false)
                }
            }
        } else {
            setupPieChart(element)
            setupLastUpdatedInfo(element)
        }

        root.addOnImpressionListener(element.impressHolder) {
            listener.sendPieChartImpressionEvent(element)
        }
    }

    private fun setupLastUpdatedInfo(element: PieChartWidgetUiModel) {
        binding.luvShcPieChart.run {
            element.data?.lastUpdated?.let { lastUpdated ->
                setLastUpdated(lastUpdated.lastUpdatedInMillis)
                setRefreshButtonVisibility(lastUpdated.shouldShow)
                setRefreshButtonClickListener {
                    listener.onReloadWidget(element)
                }
            }
        }
    }

    private fun setOnError() {
        with(binding) {
            errorStateBinding.commonWidgetErrorState.visible()
            pieChartShc.gone()
            loadingStateBinding.shimmerWidgetCommon.gone()
            tvPieChartValue.gone()
            tvPieChartSubValue.gone()
            emptyStateBinding.groupShcPieChartEmpty.gone()

            errorStateBinding.imgWidgetOnError.loadImage(
                com.tokopedia.globalerror.R.drawable.unify_globalerrors_connection
            )
        }
    }

    private fun setupTooltip(element: PieChartWidgetUiModel) = with(binding) {
        val tooltip = element.tooltip
        val shouldShowTooltip = (tooltip?.shouldShow == true) && (tooltip.content.isNotBlank()
                || tooltip.list.isNotEmpty())
        if (shouldShowTooltip) {
            tvPieChartTitle.setUnifyDrawableEnd(IconUnify.INFORMATION)
            tvPieChartTitle.setOnClickListener {
                listener.onTooltipClicked(tooltip ?: return@setOnClickListener)
            }
        } else {
            tvPieChartTitle.clearUnifyDrawableEnd()
        }
    }

    private fun getPieChartData(element: PieChartWidgetUiModel): List<PieChartEntry> {
        return element.data?.data?.item?.map {
            PieChartEntry(
                value = it.value,
                valueFmt = it.valueFmt,
                hexColor = it.color,
                legend = it.legend
            )
        }.orEmpty()
    }

    private fun setupPieChart(element: PieChartWidgetUiModel) {
        with(binding) {
            val data = element.data?.data
            tvPieChartValue.text = data?.summary?.valueFmt.orEmpty()
            tvPieChartSubValue.text = data?.summary?.diffPercentageFmt.orEmpty().parseAsHtml()

            pieChartShc.init(PieChartConfig.getDefaultConfig())
            pieChartShc.setData(getPieChartData(element))
            pieChartShc.invalidateChart()

            setupSeeMoreCta(element)
        }
    }

    private fun setupSeeMoreCta(element: PieChartWidgetUiModel) {
        with(binding) {
            val isCtaVisible = element.appLink.isNotBlank() && element.ctaText.isNotBlank()
            val ctaVisibility = if (isCtaVisible) View.VISIBLE else View.GONE
            btnShcPieChartSeeMore.visibility = ctaVisibility
            btnShcPieChartSeeMore.text = element.ctaText

            if (isCtaVisible) {
                val iconColor = root.context.getResColor(
                    com.tokopedia.unifyprinciples.R.color.Unify_G400
                )
                val iconWidth = root.context.resources.getDimension(
                    com.tokopedia.unifyprinciples.R.dimen.layout_lvl3
                )
                val iconHeight = root.context.resources.getDimension(
                    com.tokopedia.unifyprinciples.R.dimen.layout_lvl3
                )
                btnShcPieChartSeeMore.setUnifyDrawableEnd(
                    IconUnify.CHEVRON_RIGHT,
                    iconColor,
                    iconWidth,
                    iconHeight
                )
                btnShcPieChartSeeMore.setOnClickListener {
                    onSeeMoreClicked(element)
                }
            }
        }
    }

    private fun onSeeMoreClicked(element: PieChartWidgetUiModel) {
        listener.sendPieChartSeeMoreClickEvent(element)
        openAppLink(element.appLink)
    }

    private fun openAppLink(appLink: String) {
        RouteManager.route(itemView.context, appLink)
    }

    private fun showEmptyState(element: PieChartWidgetUiModel) {
        with(binding) {
            pieChartShc.gone()
            loadingStateBinding.shimmerWidgetCommon.gone()
            tvPieChartValue.gone()
            tvPieChartSubValue.gone()
        }
        emptyStateBinding.groupShcPieChartEmpty.show()
        emptyStateBinding.tvShcPieChartEmptyTitle.run {
            text = element.emptyState.title.takeIf { it.isNotBlank() }
                ?: getString(R.string.shc_empty_state_title)
            visible()
        }
        emptyStateBinding.tvShcPieChartEmptyDesc.run {
            text = element.emptyState.description
            showWithCondition(element.emptyState.description.isNotBlank())
        }
        emptyStateBinding.btnShcPieChartEmpty.run {
            text = element.emptyState.ctaText
            showWithCondition(element.emptyState.ctaText.isNotBlank())
            setOnClickListener {
                if (RouteManager.route(itemView.context, element.emptyState.appLink)) {
                    listener.sendPieChartEmptyStateCtaClickEvent(element)
                }
            }
        }
        val imageUrl = element.emptyState.imageUrl
            .takeIf { it.isNotBlank() } ?: SellerHomeUrl.IMG_EMPTY_STATE
        emptyStateBinding.imgShcPieChartEmpty.loadImage(imageUrl)
    }

    interface Listener : BaseViewHolderListener {

        fun sendPieChartImpressionEvent(model: PieChartWidgetUiModel) {}
        fun sendPieChartEmptyStateCtaClickEvent(element: PieChartWidgetUiModel) {}
        fun sendPieChartSeeMoreClickEvent(model: PieChartWidgetUiModel) {}
    }
}