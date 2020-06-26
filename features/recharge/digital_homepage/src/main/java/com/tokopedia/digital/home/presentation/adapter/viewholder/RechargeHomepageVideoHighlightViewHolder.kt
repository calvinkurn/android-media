package com.tokopedia.digital.home.presentation.adapter.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.model.RechargeHomepageSections
import com.tokopedia.digital.home.model.RechargeHomepageVideoHighlightModel
import com.tokopedia.digital.home.presentation.Util.DigitalHomepageTrackingActionConstant.VIDEO_HIGHLIGHT_CLICK
import com.tokopedia.digital.home.presentation.Util.DigitalHomepageTrackingActionConstant.VIDEO_HIGHLIGHT_IMPRESSION
import com.tokopedia.digital.home.presentation.listener.OnItemBindListener
import com.tokopedia.kotlin.extensions.view.*

/**
 * @author by resakemal on 15/06/20.
 */

class RechargeHomepageVideoHighlightViewHolder(
        val view: View,
        val listener: OnItemBindListener
): AbstractViewHolder<RechargeHomepageVideoHighlightModel>(view) {

    internal val container = view.findViewById<ConstraintLayout>(R.id.bannerPlay)
    private val thumbnailView = view.findViewById<ImageView>(R.id.thumbnail_image_play)
    private val viewer = view.findViewById<TextView>(R.id.viewer)
    private val live = view.findViewById<View>(R.id.live)
    private val titlePlay = view.findViewById<TextView>(R.id.title_play)
    private val broadcasterName = view.findViewById<TextView>(R.id.title_description)
    private val title = view.findViewById<TextView>(R.id.title)
    private val subTitle = view.findViewById<TextView>(R.id.subtitle)

    companion object {
        @LayoutRes val LAYOUT = R.layout.view_recharge_home_video_highlight
    }

    override fun bind(element: RechargeHomepageVideoHighlightModel) {
        if (element.section.items.isNotEmpty()) {
            initView(element.section)
            container.show()
        } else {
            container.hide()
        }
    }

    private fun initView(section: RechargeHomepageSections.Section){
        title.text = section.title
        subTitle.text = section.subtitle

        val item = section.items[0]
        thumbnailView.show()
        thumbnailView.loadImage(item.mediaUrl)

        broadcasterName.text = item.label2
        titlePlay.text = item.label1
        if (item.label3.isNotEmpty()) {
            viewer.text = item.label3
            viewer.show()
        }

        live.show()

        container.setOnClickListener {
            listener.onRechargeSectionItemClicked(item, adapterPosition, VIDEO_HIGHLIGHT_CLICK)
        }

        container.addOnImpressionListener(section){
            listener.onRechargeSectionItemImpression(section.items, VIDEO_HIGHLIGHT_IMPRESSION)
        }
    }
}
