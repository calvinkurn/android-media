package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.recommendation

import android.view.View
import androidx.annotation.LayoutRes
import com.google.android.exoplayer2.ExoPlaybackException
import com.tokopedia.home.R
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationPlayWidgetUiModel
import com.tokopedia.home.beranda.presentation.view.helper.HomeRecommendationVideoWidgetManager
import com.tokopedia.home.databinding.ItemHomeRecommendationPlayWidgetBinding
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.play.widget.ui.PlayVideoWidgetView
import com.tokopedia.recommendation_widget_common.widget.entitycard.viewholder.BaseRecommendationForYouViewHolder

class HomeRecommendationPlayWidgetViewHolder(
    view: View,
    homeRecommendationPlayWidgetManager: HomeRecommendationVideoWidgetManager,
    private val listener: Listener
) : BaseRecommendationForYouViewHolder<HomeRecommendationPlayWidgetUiModel>(
    view,
    HomeRecommendationPlayWidgetUiModel::class.java
) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_home_recommendation_play_widget
    }

    private val binding = ItemHomeRecommendationPlayWidgetBinding.bind(itemView)
    private var item: HomeRecommendationPlayWidgetUiModel? = null

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
        this.item = element
        bindHomeRecomPlayWidgetVideo(element)
        setOnPlayVideoImpressionListener()
        setHomePlayWidgetVideoClick()
    }

    override fun bindPayload(newItem: HomeRecommendationPlayWidgetUiModel?) {
        newItem?.let {
            bindHomeRecomPlayWidgetVideo(it)
        }
    }

    private fun bindHomeRecomPlayWidgetVideo(element: HomeRecommendationPlayWidgetUiModel) {
        binding.homeRecomPlayWidgetVideo.bind(element.playVideoWidgetUiModel)
    }

    private fun setHomePlayWidgetVideoClick() {
        item?.let { element ->
            binding.homeRecomPlayWidgetVideo.setOnClickListener {
                listener.onPlayVideoWidgetClick(element, bindingAdapterPosition)
            }

            itemView.setOnClickListener {
                listener.onPlayVideoWidgetClick(element, bindingAdapterPosition)
            }
        }
    }

    private fun setOnPlayVideoImpressionListener() {
        item?.let { element ->
            binding.homeRecomPlayWidgetVideo.addOnImpressionListener(
                element,
                object : ViewHintListener {
                    override fun onViewHint() {
                        listener.onPlayVideoWidgetImpress(element, bindingAdapterPosition)
                    }
                }
            )
        }
    }

    interface Listener {
        fun onPlayVideoWidgetClick(element: HomeRecommendationPlayWidgetUiModel, position: Int)
        fun onPlayVideoWidgetImpress(element: HomeRecommendationPlayWidgetUiModel, position: Int)
    }
}
