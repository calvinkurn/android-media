package com.tokopedia.sellerhomecommon.presentation.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.presentation.model.TableDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.TableWidgetUiModel
import com.tokopedia.sellerhomecommon.utils.clearUnifyDrawableEnd
import com.tokopedia.sellerhomecommon.utils.setUnifyDrawableEnd
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.shc_partial_common_widget_state_error.view.*
import kotlinx.android.synthetic.main.shc_partial_post_list_widget.view.*
import kotlinx.android.synthetic.main.shc_partial_widget_table_loading.view.*
import kotlinx.android.synthetic.main.shc_widget_table.view.*

/**
 * Created By @ilhamsuaib on 10/06/20
 */

class TableViewHolder(
        itemView: View?,
        private val listener: Listener
) : AbstractViewHolder<TableWidgetUiModel>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.shc_widget_table
    }

    private val tableFilter: Typography? = itemView?.findViewById(R.id.filterShcTable)

    override fun bind(element: TableWidgetUiModel) {
        itemView.tvTableWidgetTitle.text = element.title
        itemView.tvTableWidgetTitle.visible()
        itemView.commonWidgetErrorState.gone()

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
        with(itemView) {
            commonWidgetErrorState.gone()
            shimmerTableWidgetWidget.gone()

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
                    shcTableView?.addOnHtmlClickListener { url, isEmpty ->
                        listener.sendTableHyperlinkClickEvent(element.dataKey, url, isEmpty)
                    }
                }
            } else {
                if (element.isShowEmpty) {
                    setOnTableEmpty(element)
                } else {
                    listener.removeWidget(adapterPosition, element)
                }
            }
        }

        setupCta(element)
    }

    private fun setOnTableEmpty(element: TableWidgetUiModel) = with(itemView) {
        if (element.emptyState.imageUrl.isNotBlank() && element.emptyState.title.isNotBlank() &&
                element.emptyState.description.isNotBlank() && element.emptyState.ctaText.isNotBlank() &&
                element.emptyState.appLink.isNotBlank()) {
            imgShcTableEmpty.run {
                visible()
                ImageHandler.loadImageWithoutPlaceholderAndError(this, element.emptyState.imageUrl.takeIf { it.isNotBlank() })
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

    private fun showLoadingState() = with(itemView) {
        shimmerTableWidgetWidget.visible()
        shcTableView.gone()
        tvShcTableOnEmpty.gone()
        imgShcTableEmpty.gone()
        tvShcTableEmptyTitle.gone()
        tvShcTableEmptyDescription.gone()
        btnShcTableEmpty.gone()
    }

    private fun showErrorState() = with(itemView) {
        shimmerTableWidgetWidget.gone()
        commonWidgetErrorState.visible()
        tvTableWidgetTitle.visible()
        shcTableView.gone()
        tvShcTableOnEmpty.gone()
        imgShcTableEmpty.gone()
        tvShcTableEmptyTitle.gone()
        tvShcTableEmptyDescription.gone()
        btnShcTableEmpty.gone()

        ImageHandler.loadImageWithId(imgWidgetOnError, com.tokopedia.globalerror.R.drawable.unify_globalerrors_connection)
    }

    private fun setupCta(element: TableWidgetUiModel) {
        val isCtaVisible = element.appLink.isNotBlank() && element.ctaText.isNotBlank() && !element.data?.dataSet.isNullOrEmpty()
        val ctaVisibility = if (isCtaVisible) View.VISIBLE else View.GONE
        with(itemView) {
            btnTableCta.visibility = ctaVisibility
            icTableCta.visibility = ctaVisibility

            if (isCtaVisible) {
                btnTableCta.text = element.ctaText
                btnTableCta.setOnClickListener {
                    openAppLink(element.appLink, element.dataKey)
                }
                icTableCta.setOnClickListener {
                    openAppLink(element.appLink, element.dataKey)
                }
            }
        }
    }

    private fun setupTableFilter(element: TableWidgetUiModel) {
        val isFilterAvailable = element.tableFilters.isNotEmpty()
        if (isFilterAvailable) {
            val selectedFilter = element.tableFilters.find { it.isSelected }
            tableFilter?.run {
                visible()
                setupTableFilterImpressionListener(element)
                text = selectedFilter?.name.orEmpty()
                setUnifyDrawableEnd(IconUnify.CHEVRON_DOWN)
                setOnClickListener {
                    listener.showTableFilter(element, adapterPosition)
                    listener.sendTableFilterClick(element)
                }
            }
        } else {
            tableFilter?.gone()
        }
    }

    private fun setupTableFilterImpressionListener(element: TableWidgetUiModel) {
        // We are using the widget impress holder as it is not used for any impression tracking purposes,
        // while in fact, we are tracking the impression of filter options
        itemView.addOnImpressionListener(element.impressHolder) {
            listener.sendTableFilterImpression(element)
        }
    }

    private fun openAppLink(appLink: String, dataKey: String) {
        RouteManager.route(itemView.context, appLink)
    }

    private fun setupTooltip(element: TableWidgetUiModel) = with(itemView) {
        val tooltip = element.tooltip
        val shouldShowTooltip = (tooltip?.shouldShow == true) && (tooltip.content.isNotBlank() || tooltip.list.isNotEmpty())
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

        fun sendTableImpressionEvent(model: TableWidgetUiModel, slideNumber: Int, maxSlidePosition: Int, isSlideEmpty: Boolean) {}
        fun sendTableHyperlinkClickEvent(dataKey: String, url: String, isEmpty: Boolean)
        fun sendTableEmptyStateCtaClickEvent(element: TableWidgetUiModel) {}
        fun showTableFilter(element: TableWidgetUiModel, adapterPosition: Int) {}
        fun sendTableFilterClick(element: TableWidgetUiModel) {}
        fun sendTableFilterImpression(element: TableWidgetUiModel) {}

    }
}