package com.tokopedia.sellerhomecommon.presentation.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.getResColor
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.databinding.ShcWidgetTableBinding
import com.tokopedia.sellerhomecommon.presentation.model.TableDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.TableWidgetUiModel
import com.tokopedia.sellerhomecommon.utils.clearUnifyDrawableEnd
import com.tokopedia.sellerhomecommon.utils.setUnifyDrawableEnd
import com.tokopedia.sellerhomecommon.utils.toggleWidgetHeight
import com.tokopedia.unifycomponents.NotificationUnify

/**
 * Created By @ilhamsuaib on 10/06/20
 */

class TableViewHolder(
    itemView: View,
    private val listener: Listener
) : AbstractViewHolder<TableWidgetUiModel>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.shc_widget_table
    }

    private val binding by lazy { ShcWidgetTableBinding.bind(itemView) }
    private val errorStateBinding by lazy { binding.shcTableErrorStateView }
    private val loadingStateBinding by lazy { binding.shcTableLoadingStateView }

    override fun bind(element: TableWidgetUiModel) {
        if (!listener.getIsShouldRemoveWidget()) {
            itemView.toggleWidgetHeight(true)
        }
        binding.tvTableWidgetTitle.text = element.title
        binding.tvTableWidgetTitle.visible()
        errorStateBinding.commonWidgetErrorState.gone()

        setTagNotification(element.tag)
        setupTooltip(element)

        val data: TableDataUiModel? = element.data
        when {
            data == null -> showLoadingState()
            data.error.isNotBlank() -> {
                showErrorState()
                listener.setOnErrorWidget(adapterPosition, element, data.error)
            }
            else -> setOnSuccess(element)
        }
    }

    private fun setOnSuccess(element: TableWidgetUiModel) {
        with(binding) {
            errorStateBinding.commonWidgetErrorState.gone()
            loadingStateBinding.shimmerTableWidgetWidget.gone()

            val dataSet = element.data?.dataSet.orEmpty()
            if (dataSet.isNotEmpty()) {
                setupTableFilter(element)
                if (dataSet[0].rows.isEmpty()) {
                    setOnTableEmpty(element)
                } else {
                    tvShcTableOnEmpty.gone()
                    imgShcTableEmpty.gone()
                    tvShcTableEmptyTitle.gone()
                    tvShcTableEmptyDescription.gone()
                    btnShcTableEmpty.gone()
                    shcTableView.visible()
                    shcTableView.showTable(element.data?.dataSet.orEmpty())
                    shcTableView.addOnSlideImpressionListener { position, maxPosition, isEmpty ->
                        listener.sendTableImpressionEvent(element, position, maxPosition, isEmpty)
                    }
                    shcTableView.setOnSwipeListener { position, maxPosition, isEmpty ->
                        listener.sendTableOnSwipeEvent(element, position, maxPosition, isEmpty)
                    }
                    shcTableView.addOnHtmlClickListener { url, isEmpty ->
                        listener.sendTableHyperlinkClickEvent(element.dataKey, url, isEmpty)
                    }
                }
                setupLastUpdatedInfo(element)
            } else {
                if (element.isShowEmpty) {
                    setOnTableEmpty(element)
                    setupLastUpdatedInfo(element)
                } else {
                    if (listener.getIsShouldRemoveWidget()) {
                        itemView.toggleWidgetHeight(false)
                        listener.removeWidget(adapterPosition, element)
                    } else {
                        listener.onRemoveWidget(adapterPosition)
                        itemView.toggleWidgetHeight(false)
                    }
                }
            }
        }

        setupCta(element)
    }

    private fun setupLastUpdatedInfo(element: TableWidgetUiModel) {
        binding.luvShcTable.run {
            element.data?.lastUpdated?.let { lastUpdated ->
                setLastUpdated(lastUpdated.lastUpdatedInMillis)
                setRefreshButtonVisibility(lastUpdated.shouldShow)
                setRefreshButtonClickListener {
                    listener.onReloadWidget(element)
                }
            }
        }
    }

    private fun setOnTableEmpty(element: TableWidgetUiModel) = with(binding) {
        if (element.emptyState.imageUrl.isNotBlank() && element.emptyState.title.isNotBlank() &&
            element.emptyState.description.isNotBlank() && element.emptyState.ctaText.isNotBlank() &&
            element.emptyState.appLink.isNotBlank()
        ) {
            imgShcTableEmpty.run {
                visible()
                val imageUrl = element.emptyState.imageUrl.takeIf { it.isNotBlank() }
                loadImage(imageUrl)
            }
            tvShcTableEmptyTitle.run {
                text = element.emptyState.title
                visible()
            }
            tvShcTableEmptyDescription.run {
                text = element.emptyState.description
                visible()
            }
            btnShcTableEmpty.run {
                text = element.emptyState.ctaText
                visible()
                setOnClickListener {
                    if (RouteManager.route(context, element.emptyState.appLink)) {
                        listener.sendTableEmptyStateCtaClickEvent(element)
                    }
                }
            }
            tvShcTableOnEmpty.gone()
        } else {
            tvShcTableOnEmpty.visible()
        }
        shcTableView.gone()
        listener.sendTableImpressionEvent(element, 0, 0, true)
    }

    private fun showLoadingState() = with(binding) {
        loadingStateBinding.shimmerTableWidgetWidget.visible()
        shcTableView.gone()
        tvShcTableOnEmpty.gone()
        imgShcTableEmpty.gone()
        tvShcTableEmptyTitle.gone()
        tvShcTableEmptyDescription.gone()
        btnShcTableEmpty.gone()
    }

    private fun showErrorState() = with(binding) {
        loadingStateBinding.shimmerTableWidgetWidget.gone()
        errorStateBinding.commonWidgetErrorState.visible()
        tvTableWidgetTitle.visible()
        shcTableView.gone()
        tvShcTableOnEmpty.gone()
        imgShcTableEmpty.gone()
        tvShcTableEmptyTitle.gone()
        tvShcTableEmptyDescription.gone()
        btnShcTableEmpty.gone()

        errorStateBinding.imgWidgetOnError.loadImage(
            com.tokopedia.globalerror.R.drawable.unify_globalerrors_connection
        )
    }

    private fun setupCta(element: TableWidgetUiModel) {
        val isCtaVisible = element.appLink.isNotBlank() && element.ctaText.isNotBlank()
                && !element.data?.dataSet.isNullOrEmpty()
        val ctaVisibility = if (isCtaVisible) View.VISIBLE else View.GONE
        with(binding) {
            btnTableCta.visibility = ctaVisibility

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
                btnTableCta.setUnifyDrawableEnd(
                    IconUnify.CHEVRON_RIGHT,
                    iconColor,
                    iconWidth,
                    iconHeight
                )
                btnTableCta.text = element.ctaText
                btnTableCta.setOnClickListener {
                    onSeeMoreClicked(element)
                }
            }
        }
    }

    private fun onSeeMoreClicked(element: TableWidgetUiModel) {
        val isEmpty = element.data?.dataSet?.isEmpty().orFalse()
        listener.sendTableSeeMoreClickEvent(element, isEmpty)
        openAppLink(element)
    }

    private fun setupTableFilter(element: TableWidgetUiModel) {
        val isFilterAvailable = element.tableFilters.isNotEmpty()
        if (isFilterAvailable) {
            val selectedFilter = element.tableFilters.find { it.isSelected }
            binding.filterShcTable.run {
                visible()
                setupTableFilterImpressionListener(element)
                text = selectedFilter?.name.orEmpty()
                setUnifyDrawableEnd(IconUnify.CHEVRON_DOWN)
                setOnClickListener {
                    listener.showTableFilter(element, adapterPosition)
                }
            }
        } else {
            binding.filterShcTable.gone()
        }
    }

    private fun setupTableFilterImpressionListener(element: TableWidgetUiModel) {
        // We are using the widget impress holder as it is not used for any impression tracking purposes,
        // while in fact, we are tracking the impression of filter options
        itemView.addOnImpressionListener(element.impressHolder) {
            listener.sendTableFilterImpression(element)
        }
    }

    private fun openAppLink(element: TableWidgetUiModel) {
        RouteManager.route(itemView.context, element.appLink)
    }

    private fun setTagNotification(tag: String) {
        val isTagVisible = tag.isNotBlank()
        with(binding) {
            notifTagTable.showWithCondition(isTagVisible)
            if (isTagVisible) {
                notifTagTable.setNotification(
                    tag,
                    NotificationUnify.TEXT_TYPE,
                    NotificationUnify.COLOR_TEXT_TYPE
                )
            }
        }
    }

    private fun setupTooltip(element: TableWidgetUiModel) = with(binding) {
        val tooltip = element.tooltip
        val shouldShowTooltip = (tooltip?.shouldShow == true)
                && (tooltip.content.isNotBlank() || tooltip.list.isNotEmpty())
        if (shouldShowTooltip) {
            tvTableWidgetTitle.setUnifyDrawableEnd(IconUnify.INFORMATION)
            tvTableWidgetTitle.setOnClickListener {
                listener.onTooltipClicked(tooltip ?: return@setOnClickListener)
            }
        } else {
            tvTableWidgetTitle.clearUnifyDrawableEnd()
        }
    }

    interface Listener : BaseViewHolderListener {

        fun sendTableImpressionEvent(
            model: TableWidgetUiModel,
            slidePosition: Int,
            maxSlidePosition: Int,
            isSlideEmpty: Boolean
        ) {
        }

        fun sendTableOnSwipeEvent(
            element: TableWidgetUiModel,
            slidePosition: Int,
            maxSlidePosition: Int,
            isSlideEmpty: Boolean
        ) {
        }

        fun sendTableHyperlinkClickEvent(dataKey: String, url: String, isEmpty: Boolean)
        fun sendTableEmptyStateCtaClickEvent(element: TableWidgetUiModel) {}
        fun showTableFilter(element: TableWidgetUiModel, adapterPosition: Int) {}
        fun sendTableFilterImpression(element: TableWidgetUiModel) {}
        fun sendTableSeeMoreClickEvent(element: TableWidgetUiModel, isEmpty: Boolean) {}
    }
}