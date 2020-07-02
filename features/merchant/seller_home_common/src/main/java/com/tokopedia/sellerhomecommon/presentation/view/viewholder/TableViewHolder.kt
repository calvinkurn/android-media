package com.tokopedia.sellerhomecommon.presentation.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.presentation.model.TableWidgetUiModel
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
        with(itemView) {
            tvTableWidgetTitle.text = element.title
            shcTableView.showTable(element.data?.dataSet.orEmpty())

            setupTooltip(element)
        }
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