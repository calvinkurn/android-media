package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.recommendation

import android.view.View
import androidx.annotation.LayoutRes
import com.google.android.exoplayer2.ExoPlaybackException
import com.tokopedia.home.R
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationPlayWidgetUiModel
import com.tokopedia.home.databinding.ItemHomeRecommendationPlayWidgetBinding
import com.tokopedia.play.widget.ui.PlayVideoWidgetView
import com.tokopedia.recommendation_widget_common.widget.entitycard.viewholder.BaseRecommendationForYouViewHolder

class HomeRecommendationPlayWidgetViewHolder(
    private val view: View
) : BaseRecommendationForYouViewHolder<HomeRecommendationPlayWidgetUiModel>(
    view,
    HomeRecommendationPlayWidgetUiModel::class.java
) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_home_recommendation_play_widget
    }

    private val binding = ItemHomeRecommendationPlayWidgetBinding.bind(itemView)

    override fun bind(element: HomeRecommendationPlayWidgetUiModel) {
        // todo adding PlayWidgetVideoManager
        binding.homeRecomPlayWidgetVideo.setListener(object : PlayVideoWidgetView.Listener {
            override fun onVideoFinishedPlaying(view: PlayVideoWidgetView) {
            }

            override fun onVideoError(view: PlayVideoWidgetView, error: ExoPlaybackException) {
            }
        })
        binding.homeRecomPlayWidgetVideo.bind(element.playVideoWidgetUiModel)
    }

    override fun bindPayload(newItem: HomeRecommendationPlayWidgetUiModel?) {
        newItem?.let {
        }
    }
}
