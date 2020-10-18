package com.tokopedia.sellerhomecommon.presentation.view.viewholder

import android.graphics.Color
import android.graphics.drawable.ScaleDrawable
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.presentation.model.SectionWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.TooltipUiModel
import com.tokopedia.sellerhomecommon.utils.DateTimeUtil
import com.tokopedia.sellerhomecommon.utils.clearUnifyDrawableEnd
import com.tokopedia.sellerhomecommon.utils.setUnifyDrawableEnd
import kotlinx.android.synthetic.main.shc_section_widget.view.*

/**
 * Created By @ilhamsuaib on 20/05/20
 */

class SectionViewHolder(
        itemView: View?,
        private val listener: Listener
) : AbstractViewHolder<SectionWidgetUiModel>(itemView) {

    companion object {
        val RES_LAYOUT: Int = R.layout.shc_section_widget
    }

    override fun bind(element: SectionWidgetUiModel) {
        with(itemView) {
            tvSectionTitle.text = element.title
            tvSectionSubTitle.visibility = if (element.subtitle.isNotBlank()) View.VISIBLE else View.GONE
            tvSectionSubTitle.text = element.subtitle.parseDateTemplate().toString().parseAsHtml()

            element.tooltip?.let { tooltip ->
                val shouldShowTooltip = tooltip.shouldShow && (tooltip.content.isNotBlank() || tooltip.list.isNotEmpty())
                if (shouldShowTooltip) {
                    tvSectionTitle.setUnifyDrawableEnd(IconUnify.INFORMATION)
                    tvSectionTitle.setOnClickListener {
                        showSectionTooltip(element, tooltip)
                    }
                } else {
                    tvSectionTitle.clearUnifyDrawableEnd()
                }
            }
        }
    }

    private fun showSectionTooltip(model: SectionWidgetUiModel, tooltip: TooltipUiModel) {
        listener.sendSectionTooltipClickEvent(model)
        listener.onTooltipClicked(tooltip)
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

    interface Listener : BaseViewHolderListener {

        fun sendSectionTooltipClickEvent(model: SectionWidgetUiModel) {}
    }
}