package com.tokopedia.play_common.widget.playBannerCarousel.viewHolder

import android.view.View
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.play_common.widget.playBannerCarousel.event.PlayBannerCarouselViewEventListener
import com.tokopedia.play_common.widget.playBannerCarousel.model.PlayBannerCarouselOverlayImageDataModel

class PlayBannerCarouselOverlayImageViewHolder (parent: View): BasePlayBannerCarouselViewHolder<PlayBannerCarouselOverlayImageDataModel>(parent){
    override fun bind(dataModel: PlayBannerCarouselOverlayImageDataModel, listener: PlayBannerCarouselViewEventListener?) {
        itemView.setOnClickListener {
            listener?.onOverlayImageBannerClick(dataModel)
        }
        itemView.addOnImpressionListener(dataModel){
            listener?.onOverlayImageBannerImpress(dataModel)
        }
    }
}