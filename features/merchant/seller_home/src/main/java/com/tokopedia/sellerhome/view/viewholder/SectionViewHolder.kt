package com.tokopedia.sellerhome.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.asUpperCase
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.common.utils.DateTimeUtil
import com.tokopedia.sellerhome.view.model.SectionWidgetUiModel
import kotlinx.android.synthetic.main.sah_section_widget.view.*

/**
 * Created By @ilhamsuaib on 2020-01-24
 */

class SectionViewHolder(
        itemView: View?,
        private val listener: Listener
) : AbstractViewHolder<SectionWidgetUiModel>(itemView) {

    companion object {
        val RES_LAYOUT: Int = R.layout.sah_section_widget
    }

    override fun bind(element: SectionWidgetUiModel) {
        with(itemView) {
            tvSectionTitle.text = element.title
            tvSectionSubTitle.visibility = if (element.subtitle.isNotBlank()) View.VISIBLE else View.GONE
            tvSectionSubTitle.text = element.subtitle.parseDateTemplate().toString().parseAsHtml()

            element.tooltip?.let { tooltip ->
                if (tooltip.content.isNotBlank() || tooltip.list.isNotEmpty()) {
                    btnSectionInfo.visible()
                    btnSectionInfo.setOnClickListener {
                        listener.onTooltipClicked(tooltip)
                    }
                    tvSectionTitle.setOnClickListener {
                        listener.onTooltipClicked(tooltip)
                    }
                } else
                    btnSectionInfo.gone()
            }
        }
    }

    private fun String.parseDateTemplate(): CharSequence {

        val regex = mapOf(
                "{DATE_YESTERDAY_PAST_7D}" to { DateTimeUtil.getFormattedDate(7, "dd MMM yy").asUpperCase() },
                "{DATE_YESTERDAY}" to { DateTimeUtil.getFormattedDate(1, "dd MMM yy").asUpperCase() }
        )

        val pattern = "\\{([^}]*?)\\}".toRegex()
        return pattern.replace(this) {
            regex[it.value]?.invoke() ?: it.value
        }
    }

    interface Listener : BaseViewHolderListener
}