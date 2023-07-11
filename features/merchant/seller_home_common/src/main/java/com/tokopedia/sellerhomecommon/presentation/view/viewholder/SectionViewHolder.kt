package com.tokopedia.sellerhomecommon.presentation.view.viewholder

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.getResColor
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.databinding.ShcSectionWidgetBinding
import com.tokopedia.sellerhomecommon.presentation.model.SectionWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.TooltipUiModel
import com.tokopedia.sellerhomecommon.utils.clearUnifyDrawableEnd
import com.tokopedia.sellerhomecommon.utils.setUnifyDrawableEnd

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
            tvSectionSubTitle.text = element.subtitle.parseAsHtml()

            element.tooltip?.let { tooltip ->
                val shouldShowTooltip =
                    tooltip.shouldShow && (tooltip.content.isNotBlank() || tooltip.list.isNotEmpty())
                if (shouldShowTooltip) {
                    tvSectionTitle.setUnifyDrawableEnd(
                        iconId = IconUnify.INFORMATION,
                        colorIcon = root.context.getResColor(com.tokopedia.unifyprinciples.R.color.Unify_NN900)
                    )
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