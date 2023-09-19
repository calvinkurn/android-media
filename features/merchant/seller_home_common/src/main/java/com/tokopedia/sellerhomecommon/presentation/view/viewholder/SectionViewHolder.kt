package com.tokopedia.sellerhomecommon.presentation.view.viewholder

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.getResColor
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.databinding.ShcSectionWidgetBinding
import com.tokopedia.sellerhomecommon.presentation.model.SectionWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.TooltipUiModel
import com.tokopedia.sellerhomecommon.utils.clearUnifyDrawableEnd
import com.tokopedia.sellerhomecommon.utils.setUnifyDrawableEnd
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

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
            if (element.title.isNotBlank()) {
                tvSectionTitle.text = element.title.parseAsHtml()
            }
            tvSectionTitle.isVisible = element.title.isNotBlank()

            if (element.subtitle.isNotBlank()) {
                tvSectionSubTitle.text = element.subtitle.parseAsHtml()
            }
            tvSectionSubTitle.isVisible = element.subtitle.isNotBlank()

            setupTooltip(element)
            setTextColor(element)
        }
    }

    private fun setupTooltip(element: SectionWidgetUiModel) {
        with(binding) {
            element.tooltip?.let { tooltip ->
                tvSectionTitle.clearUnifyDrawableEnd()
                tvSectionSubTitle.clearUnifyDrawableEnd()

                val shouldShowTooltip =
                    tooltip.shouldShow && (tooltip.content.isNotBlank() || tooltip.list.isNotEmpty())

                if (!shouldShowTooltip) return

                val tooltipAnchor = when {
                    element.title.isNotBlank() -> tvSectionTitle
                    element.subtitle.isNotBlank() -> tvSectionSubTitle
                    else -> return
                }

                tooltipAnchor.setUnifyDrawableEnd(
                    iconId = IconUnify.INFORMATION,
                    colorIcon = root.context.getResColor(unifyprinciplesR.color.Unify_NN900)
                )
                tooltipAnchor.setOnClickListener {
                    showSectionTooltip(element, tooltip)
                }
            }
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

    private fun View.toggleSectionWidgetHeight(isShown: Boolean) {
        layoutParams.height =
            if (isShown) {
                ConstraintLayout.LayoutParams.WRAP_CONTENT
            } else {
                Int.ONE
            }
        requestLayout()
    }

    interface Listener : BaseViewHolderListener {

        fun sendSectionTooltipClickEvent(model: SectionWidgetUiModel) {}
    }
}