package com.tokopedia.sellerhomecommon.presentation.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
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
            shcTableView.visible()
            commonWidgetErrorState.gone()
            shimmerTableWidgetWidget.gone()

            shcTableView.showTable(element.data?.dataSet.orEmpty())
        }

        setupCta(element)
    }

    private fun showLoadingState() = with(itemView) {
        shimmerTableWidgetWidget.visible()
        shcTableView.gone()
    }

    private fun showErrorState() = with(itemView) {
        shimmerTableWidgetWidget.gone()
        commonWidgetErrorState.visible()
        tvTableWidgetTitle.visible()
        shcTableView.gone()

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
        if (!tooltip?.content.isNullOrBlank() || !tooltip?.list.isNullOrEmpty()) {
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

    }
}