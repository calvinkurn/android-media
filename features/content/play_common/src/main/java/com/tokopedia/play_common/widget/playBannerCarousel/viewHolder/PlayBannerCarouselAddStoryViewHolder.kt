package com.tokopedia.play_common.widget.playBannerCarousel.viewHolder

import android.view.View
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.loadImageCircle
import com.tokopedia.play_common.widget.playBannerCarousel.model.PlayBannerCarouselAddStoryDataModel
import kotlinx.android.synthetic.main.item_play_banner_create_story.view.*

class PlayBannerCarouselAddStoryViewHolder (view: View): BasePlayBannerCarouselViewHolder<PlayBannerCarouselAddStoryDataModel>(view){
    override fun bind(dataModel: PlayBannerCarouselAddStoryDataModel) {
        itemView.background_image_profile?.loadImage(dataModel.backgroundUrl)
        itemView.image_profile?.loadImageCircle(dataModel.profileUrl)
        itemView.channel_title?.text = dataModel.content
    }
}