package com.tokopedia.sellerhome.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.common.utils.parseAsHtml
import com.tokopedia.sellerhome.common.utils.parseDateTemplate
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
                    btnSectionInfo.visibility = View.VISIBLE
                    btnSectionInfo.setOnClickListener {
                        listener.onTooltipClicked(tooltip)
                    }
                    tvSectionTitle.setOnClickListener {
                        listener.onTooltipClicked(tooltip)
                    }
                } else
                    btnSectionInfo.visibility = View.GONE
            }
        }
    }

    interface Listener : BaseViewHolderListener
}