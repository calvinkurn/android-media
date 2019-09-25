package com.tokopedia.product.manage.item.video.view.adapter.viewholder

import androidx.annotation.LayoutRes
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.product.manage.item.R
import com.tokopedia.product.manage.item.utils.YoutubeUtil
import com.tokopedia.product.manage.item.video.view.listener.VideoRecommendationListener
import com.tokopedia.product.manage.item.video.view.model.VideoRecommendationViewModel
import kotlinx.android.synthetic.main.item_product_add_video_recommendation.view.*

class VideoRecommendationViewHolder(itemView: View,
                                    private var videoRecommendationListener: VideoRecommendationListener) : AbstractViewHolder<VideoRecommendationViewModel>(itemView) {

    init {
        setViews()
    }

    private fun setViews(){
        itemView.checkboxChosen.setOnClickListener({
            videoRecommendationListener.onCheckboxClicked(adapterPosition, itemView.checkboxChosen.isChecked)
        })
    }

    override fun bind(videoRecommendationViewModel: VideoRecommendationViewModel) {
        itemView.textTitle.text = videoRecommendationViewModel.snippetTitle
        itemView.textChannel.text = videoRecommendationViewModel.snippetChannel
        itemView.textDuration.text = YoutubeUtil.convertYoutubeTimeFormattoHHMMSS(videoRecommendationViewModel.duration)
        ImageHandler.loadImageRounded2(itemView.imageThumbnail.context, itemView.imageThumbnail, videoRecommendationViewModel.thumbnailUrl)
        itemView.checkboxChosen.isChecked = videoRecommendationViewModel.chosen
        itemView.setOnClickListener({
            itemView.checkboxChosen.isChecked = !itemView.checkboxChosen.isChecked
            videoRecommendationListener.onCheckboxClicked(adapterPosition, itemView.checkboxChosen.isChecked)
        })
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_product_add_video_recommendation
    }
}