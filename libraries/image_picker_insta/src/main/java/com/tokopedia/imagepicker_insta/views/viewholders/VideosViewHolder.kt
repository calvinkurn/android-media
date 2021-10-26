package com.tokopedia.imagepicker_insta.views.viewholders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.imagepicker_insta.R
import com.tokopedia.imagepicker_insta.models.ImageAdapterData
import com.tokopedia.imagepicker_insta.models.VideoData
import com.tokopedia.unifyprinciples.Typography

class VideosViewHolder(videoView: View) : PhotosViewHolder(videoView) {

    companion object {
        fun getInstance(parent: ViewGroup, contentHeight: Int): VideosViewHolder {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.imagepicker_insta_item_view_videos, parent, false)
            v.layoutParams.apply {
                height = contentHeight
            }
            return VideosViewHolder(v)
        }
    }

    val tvDuration = videoView.findViewById<Typography>(R.id.tv_duration)

    override fun setData(imageAdapterData: ImageAdapterData, contentHeight: Int) {
        super.setData(imageAdapterData, contentHeight)
        tvDuration.text = (imageAdapterData.asset as? VideoData)?.durationText
    }
}