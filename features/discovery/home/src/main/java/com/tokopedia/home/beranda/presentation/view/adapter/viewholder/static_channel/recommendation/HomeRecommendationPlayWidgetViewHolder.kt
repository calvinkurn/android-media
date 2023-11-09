package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.recommendation

import android.view.View
import androidx.annotation.LayoutRes
import com.google.android.exoplayer2.ExoPlaybackException
import com.tokopedia.home.R
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationPlayWidgetUiModel
import com.tokopedia.home.beranda.presentation.view.helper.HomeRecommendationVideoWidgetManager
import com.tokopedia.home.databinding.ItemHomeRecommendationPlayWidgetBinding
import com.tokopedia.play.widget.ui.PlayVideoWidgetView
import com.tokopedia.recommendation_widget_common.widget.entitycard.viewholder.BaseRecommendationForYouViewHolder

class HomeRecommendationPlayWidgetViewHolder(
    view: View,
    homeRecommendationPlayWidgetManager: HomeRecommendationVideoWidgetManager
) : BaseRecommendationForYouViewHolder<HomeRecommendationPlayWidgetUiModel>(
    view,
    HomeRecommendationPlayWidgetUiModel::class.java
) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_home_recommendation_play_widget
    }

    private val binding = ItemHomeRecommendationPlayWidgetBinding.bind(itemView)

    init {
        binding.homeRecomPlayWidgetVideo.setListener(object : PlayVideoWidgetView.Listener {
            override fun onVideoFinishedPlaying(view: PlayVideoWidgetView, position: Int) {
//                homeRecommendationPlayWidgetManager.bind()
            }

            override fun onVideoError(view: PlayVideoWidgetView, error: ExoPlaybackException) {
                // no op
            }
        })
        homeRecommendationPlayWidgetManager.bind(binding.homeRecomPlayWidgetVideo)
    }

    override fun bind(element: HomeRecommendationPlayWidgetUiModel) {
        // todo adding PlayWidgetVideoManager
        bindHomeRecomPlayWidgetVideo(element)
    }

    override fun bindPayload(newItem: HomeRecommendationPlayWidgetUiModel?) {
        newItem?.let {
            bindHomeRecomPlayWidgetVideo(it)
        }
    }

    private fun bindHomeRecomPlayWidgetVideo(element: HomeRecommendationPlayWidgetUiModel) {
        binding.homeRecomPlayWidgetVideo.bind(element.playVideoWidgetUiModel)
    }
}
