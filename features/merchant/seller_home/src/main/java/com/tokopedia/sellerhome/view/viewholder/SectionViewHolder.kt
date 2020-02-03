package com.tokopedia.sellerhome.view.viewholder

import android.view.View
import android.widget.Toast
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.view.model.SectionWidgetUiModel
import kotlinx.android.synthetic.main.sah_section_widget.view.*

/**
 * Created By @ilhamsuaib on 2020-01-24
 */

class SectionViewHolder(itemView: View?) : AbstractViewHolder<SectionWidgetUiModel>(itemView) {

    companion object {
        val RES_LAYOUT: Int = R.layout.sah_section_widget
    }

    override fun bind(element: SectionWidgetUiModel) {
        with(itemView) {
            tvSectionTitle.text = element.title
            tvSectionSubTitle.visibility = if (element.subtitle.isNotBlank()) View.VISIBLE else View.GONE
            tvSectionSubTitle.text = element.subtitle
            btnSectionInfo.setOnClickListener {
                Toast.makeText(context, "Show tooltip", Toast.LENGTH_SHORT).show()
            }
        }
    }
}