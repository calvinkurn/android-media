package com.tokopedia.sellerhome.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.view.model.DescriptionWidgetUiModel
import kotlinx.android.synthetic.main.sah_partial_description_error.view.*
import kotlinx.android.synthetic.main.sah_partial_description_widget.view.*
import kotlinx.android.synthetic.main.sah_partial_shimmering_description.view.*

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
            setupDetails(element)
            ideal_description_layout.visibility = View.VISIBLE
        }
    }

    private fun setupDetails(element: DescriptionWidgetUiModel) {
        with(itemView) {
            if (element.ctaText.isNotEmpty() && element.appLink.isNotEmpty()) {
                tv_description_url.text = element.ctaText
                tv_description_url.visibility = View.VISIBLE
                iv_description_arrow.visibility = View.VISIBLE
                tv_description_url.setOnClickListener {
                    goToDetails(element)
                }
                iv_description_arrow.setOnClickListener {
                    goToDetails(element)
                }
            } else {
                tv_description_url.visibility = View.GONE
                iv_description_arrow.visibility = View.GONE
            }
        }
    }

    private fun goToDetails(element: DescriptionWidgetUiModel) {
        RouteManager.route(itemView.context, element.appLink)
    }
}