package com.tokopedia.product.edit.view.adapter.viewholder

import android.support.annotation.LayoutRes
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.product.edit.R
import com.tokopedia.product.edit.view.viewmodel.VideoRecommendationViewModel

class VideoRecommendationViewHolder(itemView: View) : AbstractViewHolder<VideoRecommendationViewModel>(itemView) {

    private var imageThumbnail: ImageView = itemView.findViewById(R.id.image_thumbnail)
    private var textTitle: TextView = itemView.findViewById(R.id.text_title)
    private var textChannel: TextView = itemView.findViewById(R.id.text_channel)
    private var checkboxChosen: CheckBox = itemView.findViewById(R.id.checkbox_chosen)

    override fun bind(videoRecommendationViewModel: VideoRecommendationViewModel) {
        textTitle.text = videoRecommendationViewModel.snippetTitle
        textChannel.text = videoRecommendationViewModel.snippetChannel
        ImageHandler.loadImageThumbs(imageThumbnail.context, imageThumbnail, videoRecommendationViewModel.thumbnailUrl)
        checkboxChosen.isChecked = videoRecommendationViewModel.chosen
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_product_add_video_recommendation
    }
}