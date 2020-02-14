package com.tokopedia.salam.umrah.travel.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.salam.umrah.R
import com.tokopedia.salam.umrah.common.util.UmrahUriFormat
import com.tokopedia.salam.umrah.travel.data.UmrahGallery
import kotlinx.android.synthetic.main.item_umrah_gallery_one_video.view.*

class UmrahTravelAgentGalleryVideoViewHolder(view: View, val listener: OnYoutubeClick) : AbstractViewHolder<UmrahGallery>(view) {
    override fun bind(element: UmrahGallery) {
        with(itemView) {

            if (element.title.isNotEmpty()) tg_umrah_gallery_one_video_title.text = element.title
            else tg_umrah_gallery_one_video_title.gone()

            if (element.subTitle.isNotEmpty()) tg_umrah_gallery_one_video_sub_title.text = element.subTitle
            else tg_umrah_gallery_one_video_sub_title.gone()

            if (element.description.isNotEmpty()) tg_umrah_gallery_one_video_desc.text = element.description
            else tg_umrah_gallery_one_video_desc.gone()

            if (element.medias[0].thumbnail.isNotEmpty())
                iw_umrah_gallery_video_thumbnail.loadImage(element.medias[0].thumbnail)
            else iw_umrah_gallery_video_thumbnail.gone()

            container_umrah_youtube_player.setOnClickListener {
                if(!element.medias[0].source.isNullOrEmpty())
                listener.onPlayYoutube(element, UmrahUriFormat.getLastPathUrl(element.medias[0].source), adapterPosition,0)
            }
        }
    }

    companion object {
        val LAYOUT = R.layout.item_umrah_gallery_one_video
    }

    interface OnYoutubeClick {
        fun onPlayYoutube(gallery: UmrahGallery,url: String,positionAdapter:Int, positionVideo:Int)
    }
}