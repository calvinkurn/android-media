package com.tokopedia.travelhomepage.destination.presentation.adapter.viewholder

import android.text.Html
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.travelhomepage.R
import com.tokopedia.travelhomepage.destination.listener.OnViewHolderBindListener
import com.tokopedia.travelhomepage.destination.model.TravelDestinationSummaryModel
import kotlinx.android.synthetic.main.layout_travel_destination_summary.view.*

/**
 * @author by jessica on 2020-01-02
 */

class TravelDestinationSummaryViewHolder(itemView: View, private val onViewHolderBindListener: OnViewHolderBindListener)
    : AbstractViewHolder<TravelDestinationSummaryModel>(itemView) {

    override fun bind(element: TravelDestinationSummaryModel) {
        if (element.isLoaded) {
            with(itemView) {
                shimmering.hide()
                content_destination_summary.show()
                layout_destination_summary.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

                var list = mutableListOf<String>()
                for (img in element.images) {
                    list.add(img.imageUrl)
                }

                destination_summary_title.text = Html.fromHtml(element.title)
                destination_summary_description.text = element.description
                destination_summary_read_more.setOnClickListener {
                    destination_summary_description.maxLines = Integer.MAX_VALUE
                    destination_summary_read_more.hide()
                }

                val anim1 = TranslateAnimation(0f, 0f, -8f, 0f)
                anim1.duration = 300
                anim1.repeatCount = -1
                anim1.repeatMode = Animation.REVERSE
                arrow_up.animation = anim1

                onViewHolderBindListener.onCitySummaryLoaded(list, peek_layout.height, element.title)
            }
        } else {
            itemView.shimmering.show()
            itemView.content_destination_summary.hide()
        }
    }

    fun disableAnimation() {
        itemView.arrow_up.animation.cancel()
    }

    companion object {
        val LAYOUT = R.layout.layout_travel_destination_summary
    }

}