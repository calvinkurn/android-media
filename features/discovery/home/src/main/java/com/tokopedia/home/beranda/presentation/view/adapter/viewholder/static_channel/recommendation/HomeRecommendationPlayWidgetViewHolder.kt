package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.recommendation

import android.view.View
import androidx.annotation.LayoutRes
import com.google.android.exoplayer2.ExoPlaybackException
import com.tokopedia.home.R
import com.tokopedia.home.beranda.domain.ForYouDataMapper.toModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationPlayWidgetUiModel
import com.tokopedia.home.databinding.ItemHomeRecommendationPlayWidgetBinding
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.play.widget.ui.PlayVideoWidgetView
import com.tokopedia.recommendation_widget_common.infinite.foryou.BaseRecommendationViewHolder
import com.tokopedia.recommendation_widget_common.infinite.foryou.GlobalRecomListener
import com.tokopedia.recommendation_widget_common.infinite.foryou.play.PlayVideoWidgetManager

class HomeRecommendationPlayWidgetViewHolder(
    view: View,
    homeRecommendationPlayWidgetManager: PlayVideoWidgetManager,
    private val listener: GlobalRecomListener
) : BaseRecommendationViewHolder<HomeRecommendationPlayWidgetUiModel>(
    view,
    HomeRecommendationPlayWidgetUiModel::class.java
) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_home_recommendation_play_widget
    }

    private val binding = ItemHomeRecommendationPlayWidgetBinding.bind(itemView)

    init {
        homeRecommendationPlayWidgetManager.bind(binding.homeRecomPlayWidgetVideo)

        binding.homeRecomPlayWidgetVideo.setListener(object : PlayVideoWidgetView.Listener {
            override fun onVideoFinishedPlaying(view: PlayVideoWidgetView) {
                binding.homeRecomPlayWidgetVideo.resetPlaybackPosition()
            }

            override fun onVideoError(view: PlayVideoWidgetView, error: ExoPlaybackException) {
                // no op
            }
        })
    }

    override fun bind(element: HomeRecommendationPlayWidgetUiModel) {
        bindHomeRecomPlayWidgetVideo(element)
        setOnPlayVideoImpressionListener(element)
        setHomePlayWidgetVideoClick(element)
    }

    private fun bindHomeRecomPlayWidgetVideo(element: HomeRecommendationPlayWidgetUiModel) {
        binding.homeRecomPlayWidgetVideo.bind(element.playVideoWidgetUiModel)
    }

    private fun setHomePlayWidgetVideoClick(element: HomeRecommendationPlayWidgetUiModel) {
        binding.homeRecomPlayWidgetVideo.setOnClickListener {
            listener.onPlayCardClicked(element.toModel(), bindingAdapterPosition)
        }

        itemView.setOnClickListener {
            listener.onPlayCardClicked(element.toModel(), bindingAdapterPosition)
        }
    }

    private fun setOnPlayVideoImpressionListener(element: HomeRecommendationPlayWidgetUiModel) {
        binding.homeRecomPlayWidgetVideo.addOnImpressionListener(
            element,
            object : ViewHintListener {
                override fun onViewHint() {
                    listener.onPlayCardImpressed(element.toModel(), bindingAdapterPosition)
                }
            }
        )
    }
}
