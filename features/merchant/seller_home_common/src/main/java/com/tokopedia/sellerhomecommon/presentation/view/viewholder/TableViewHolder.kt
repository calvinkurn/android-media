package com.tokopedia.sellerhomecommon.presentation.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.presentation.model.TableDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.TableWidgetUiModel
import kotlinx.android.synthetic.main.shc_partial_common_widget_state_error.view.*
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
                listener.setOnErrorWidget(adapterPosition, element)
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
                if (dataSet[0].rows.isEmpty()) {
                    setOnTableEmpty(element)
                } else {
                    tvShcTableOnEmpty.gone()
                    shcTableView.visible()
                    shcTableView.showTable(element.data?.dataSet.orEmpty())
                    shcTableView.addOnSlideImpressionListener { position, isEmpty ->
                        listener.sendTableImpressionEvent(element, position, isEmpty)
                    }
                }
            } else {
                setOnTableEmpty(element)
            }
        }

        setupCta(element)
    }

    private fun setOnTableEmpty(element: TableWidgetUiModel) = with(itemView) {
        tvShcTableOnEmpty.visible()
        shcTableView.gone()
        listener.sendTableImpressionEvent(element, 0, true)
    }

    private fun showLoadingState() = with(itemView) {
        shimmerTableWidgetWidget.visible()
        shcTableView.gone()
        tvShcTableOnEmpty.gone()
    }

    private fun showErrorState() = with(itemView) {
        shimmerTableWidgetWidget.gone()
        commonWidgetErrorState.visible()
        tvTableWidgetTitle.visible()
        shcTableView.gone()
        tvShcTableOnEmpty.gone()

        ImageHandler.loadImageWithId(imgWidgetOnError, R.drawable.unify_globalerrors_connection)
    }

    private fun setupCta(element: TableWidgetUiModel) {
        val isCtaVisible = element.appLink.isNotBlank() && element.ctaText.isNotBlank()
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

    private fun openAppLink(appLink: String, dataKey: String) {
        RouteManager.route(itemView.context, appLink)
    }

    private fun setupTooltip(element: TableWidgetUiModel) = with(itemView) {
        val tooltip = element.tooltip
        val shouldShowTooltip = (tooltip?.shouldShow == true) && (tooltip.content.isNotBlank() || tooltip.list.isNotEmpty())
        if (shouldShowTooltip) {
            btnTableInformation.visible()
            tvTableWidgetTitle.setOnClickListener {
                listener.onTooltipClicked(tooltip ?: return@setOnClickListener)
            }
            btnTableInformation.setOnClickListener {
                listener.onTooltipClicked(tooltip ?: return@setOnClickListener)
            }
        } else {
            btnTableInformation.gone()
        }
    }

    interface Listener : BaseViewHolderListener {

        fun sendTableImpressionEvent(model: TableWidgetUiModel, slideNumber: Int, isSlideEmpty: Boolean) {}
    }
}