package com.tokopedia.play_common.widget.playBannerCarousel.viewHolder

import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.play_common.R
import com.tokopedia.play_common.widget.playBannerCarousel.event.PlayBannerCarouselViewEventListener
import com.tokopedia.play_common.widget.playBannerCarousel.extension.loadImage
import com.tokopedia.play_common.widget.playBannerCarousel.extension.showOrHideView
import com.tokopedia.play_common.widget.playBannerCarousel.model.PlayBannerCarouselItemDataModel
import com.tokopedia.play_common.widget.playBannerCarousel.model.PlayBannerWidgetType
import kotlinx.android.synthetic.main.item_play_banner_carousel.view.*
import kotlinx.android.synthetic.main.layout_viewer_badge.view.*


class PlayBannerCarouselItemViewHolder (private val parent: View): BasePlayBannerCarouselViewHolder<PlayBannerCarouselItemDataModel>(parent){

    val containerPlayer: FrameLayout = parent.container_player
    override fun bind(dataModel: PlayBannerCarouselItemDataModel, listener: PlayBannerCarouselViewEventListener?) {
        parent.tag = this
        parent.thumbnail?.loadImage(dataModel.coverUrl)
        parent.channel_title?.text = dataModel.channelTitle
        parent.channel_name?.text = dataModel.channelCreator
        parent.viewer?.text = dataModel.countView
        parent.promo_badge?.showOrHideView(dataModel.isPromo && dataModel.widgetType != PlayBannerWidgetType.UPCOMING)
        parent.live_badge?.showOrHideView(dataModel.isLive && dataModel.widgetType != PlayBannerWidgetType.UPCOMING)
        parent.viewer_badge?.showOrHideView(dataModel.isShowTotalView && dataModel.widgetType != PlayBannerWidgetType.UPCOMING)
        parent.reminder?.showOrHideView(dataModel.widgetType == PlayBannerWidgetType.UPCOMING)
        parent.reminder.setImageDrawable(ContextCompat.getDrawable(itemView.context, if(dataModel.remindMe) R.drawable.ic_play_reminder else R.drawable.ic_play_reminder_non_active))
        parent.container_content?.setOnClickListener { listener?.onItemOverlayClick(dataModel, adapterPosition) }
        parent.reminder?.setOnClickListener {
            dataModel.remindMe = !dataModel.remindMe
            parent.reminder.setImageDrawable(ContextCompat.getDrawable(itemView.context, if(dataModel.remindMe) R.drawable.ic_play_reminder else R.drawable.ic_play_reminder_non_active))
            listener?.onReminderClick(dataModel, adapterPosition)
        }
        parent.channel_up_coming_date?.showOrHideView(dataModel.widgetType == PlayBannerWidgetType.UPCOMING)
        parent.channel_up_coming_date?.text = dataModel.startTime
        itemView.container_player?.setOnClickListener { listener?.onItemClick(dataModel, adapterPosition) }
        itemView.addOnImpressionListener(dataModel){ listener?.onItemImpress(dataModel, adapterPosition) }
    }
}