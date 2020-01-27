package com.tokopedia.sellerhome.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.view.model.DescriptionState
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
        with(element) {
            showOnError(element.state)
            showShimmer(element.state)
            itemView.tv_description_title.text = title
            itemView.tv_description_desc.text = description
            itemView.tv_description_url.setOnClickListener {
                //GO TO LINK

            }
        }
    }

    private fun showOnError(errorState: DescriptionState){
        if(errorState != DescriptionState.ERROR) return
        with(itemView) {
            tv_description_desc.visibility = View.GONE
            tv_description_url.visibility = View.GONE
            iv_description_arrow.visibility = View.GONE
            shimmer_description_layout.visibility = View.GONE
            iv_description_error.visibility = View.VISIBLE
        }
    }

    private fun showShimmer(loadingState: DescriptionState){
        if(loadingState != DescriptionState.LOADING) return
        with(itemView) {
            shimmer_description_layout.visibility = View.VISIBLE
        }
    }



}