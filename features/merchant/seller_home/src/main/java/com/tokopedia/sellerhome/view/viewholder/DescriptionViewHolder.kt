package com.tokopedia.sellerhome.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.view.model.DescriptionWidgetUiModel
import kotlinx.android.synthetic.main.partial_sah_description_widget.view.*
import kotlinx.android.synthetic.main.partial_sah_description_widget.view.tv_description_title
import kotlinx.android.synthetic.main.partial_sah_error_description_widget.view.*
import kotlinx.android.synthetic.main.partial_sah_shimmering_description_widget.view.*

class DescriptionViewHolder(view: View?) : AbstractViewHolder<DescriptionWidgetUiModel>(view) {

    companion object {
        val RES_LAYOUT = R.layout.sah_description_widget
    }

    override fun bind(element: DescriptionWidgetUiModel) {
        with(itemView) {
            shimmer_description_layout.visibility = View.GONE
            error_description_layout.visibility = View.GONE
            tv_description_title.text = element.title
            tv_description_desc.text = element.subtitle
            if(element.ctaText.isNotEmpty()) {
                tv_description_url.text = element.ctaText
            } else {
                tv_description_url.visibility = View.GONE
            }
            tv_description_url.setOnClickListener {
                //GO TO LINK

            }
            ideal_description_layout.visibility = View.VISIBLE
        }
    }
}