package com.tokopedia.sellerhomecommon.presentation.view.viewholder

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.asUpperCase
import com.tokopedia.kotlin.extensions.view.getResColor
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.databinding.ShcSectionWidgetBinding
import com.tokopedia.sellerhomecommon.presentation.model.SectionWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.TooltipUiModel
import com.tokopedia.sellerhomecommon.utils.DateTimeUtil
import com.tokopedia.sellerhomecommon.utils.clearUnifyDrawableEnd
import com.tokopedia.sellerhomecommon.utils.setUnifyDrawableEnd
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created By @ilhamsuaib on 20/05/20
 */

class SectionViewHolder(
    itemView: View,
    private val listener: Listener
) : AbstractViewHolder<SectionWidgetUiModel>(itemView) {

    companion object {
        val RES_LAYOUT: Int = R.layout.shc_section_widget
    }

    private val binding by lazy { ShcSectionWidgetBinding.bind(itemView) }

    override fun bind(element: SectionWidgetUiModel) {
        with(binding) {
            root.toggleSectionWidgetHeight(element.shouldShow)
            tvSectionTitle.text = element.title
            tvSectionSubTitle.showWithCondition(element.subtitle.isNotBlank())
            tvSectionSubTitle.text = element.subtitle.parseDateTemplate().toString().parseAsHtml()

            element.tooltip?.let { tooltip ->
                val shouldShowTooltip =
                    tooltip.shouldShow && (tooltip.content.isNotBlank() || tooltip.list.isNotEmpty())
                if (shouldShowTooltip) {
                    tvSectionTitle.setUnifyDrawableEnd(IconUnify.INFORMATION)
                    tvSectionTitle.setOnClickListener {
                        showSectionTooltip(element, tooltip)
                    }
                } else {
                    tvSectionTitle.clearUnifyDrawableEnd()
                }
            }
            setTextColor(element)
        }
    }

    private fun setTextColor(element: SectionWidgetUiModel) {
        with(binding) {
            val titleTextColor = root.context.getResColor(element.titleTextColorId)
            val subTitleTextColor = root.context.getResColor(element.subTitleTextColorId)
            tvSectionTitle.setTextColor(titleTextColor)
            tvSectionSubTitle.setTextColor(subTitleTextColor)
        }
    }

    private fun showSectionTooltip(model: SectionWidgetUiModel, tooltip: TooltipUiModel) {
        listener.sendSectionTooltipClickEvent(model)
        listener.onTooltipClicked(tooltip)
    }

    private fun String.parseDateTemplate(): CharSequence {

        val regex = mapOf(
            "{DATE_YESTERDAY_PAST_7D}" to {
                DateTimeUtil.getFormattedDate(7, "dd MMM yy").asUpperCase()
            },
            "{DATE_YESTERDAY}" to { DateTimeUtil.getFormattedDate(1, "dd MMM yy").asUpperCase() },
            "{NOW_DD_MMMM_YYYY_hh:mm_WIB}" to {
                DateTimeUtil.format(
                    System.currentTimeMillis().minus(TimeUnit.HOURS.toMillis(1)),
                    "dd MMMM yyyy (HH:00 z)",
                    TimeZone.getTimeZone("Asia/Jakarta")
                )
            }
        )

        val pattern = "\\{([^}]*?)\\}".toRegex()
        return pattern.replace(this) {
            regex[it.value]?.invoke() ?: it.value
        }
    }

    private fun View.toggleSectionWidgetHeight(isShown: Boolean) {
        layoutParams.height =
            if (isShown) {
                ConstraintLayout.LayoutParams.WRAP_CONTENT
            } else {
                0
            }
        requestLayout()
    }

    interface Listener : BaseViewHolderListener {

        fun sendSectionTooltipClickEvent(model: SectionWidgetUiModel) {}
    }
}