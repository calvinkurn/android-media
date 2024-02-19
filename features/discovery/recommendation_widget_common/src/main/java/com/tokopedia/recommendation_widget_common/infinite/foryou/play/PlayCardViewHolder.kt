package com.tokopedia.recommendation_widget_common.infinite.foryou.play

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.analytics.byteio.AppLogAnalytics
import com.tokopedia.analytics.byteio.EventName
import com.tokopedia.analytics.byteio.TrackProduct
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.play.widget.ui.PlayVideoWidgetView
import com.tokopedia.recommendation_widget_common.R
import com.tokopedia.recommendation_widget_common.databinding.WidgetPlayRecomBinding
import com.tokopedia.recommendation_widget_common.infinite.foryou.BaseRecommendationViewHolder
import com.tokopedia.utils.view.binding.viewBinding

class PlayCardViewHolder constructor(
    view: View,
    manager: PlayVideoWidgetManager,
    private val listener: Listener,
    private val playListener: PlayVideoWidgetView.Listener
) : BaseRecommendationViewHolder<PlayCardModel>(
    view,
    PlayCardModel::class.java
) {

    private val binding: WidgetPlayRecomBinding? by viewBinding()

    init {
        manager.bind(binding?.homeRecomPlayWidgetVideo)

        binding?.homeRecomPlayWidgetVideo?.setListener(object : PlayVideoWidgetView.Listener by playListener {
            override fun onVideoFinishedPlaying(view: PlayVideoWidgetView) {
                binding?.homeRecomPlayWidgetVideo?.resetPlaybackPosition()
            }
        })
    }

    override fun bind(element: PlayCardModel) {
        bindHomeRecomPlayWidgetVideo(element)
        setOnPlayVideoImpressionListener(element)
        setHomePlayWidgetVideoClick(element)
    }

    private fun bindHomeRecomPlayWidgetVideo(element: PlayCardModel) {
        binding?.homeRecomPlayWidgetVideo?.bind(element.playVideoWidgetUiModel)
    }

    private fun setHomePlayWidgetVideoClick(element: PlayCardModel) {
        binding?.homeRecomPlayWidgetVideo?.setOnClickListener {
            listener.onPlayCardClicked(element, bindingAdapterPosition)
        }

        itemView.setOnClickListener {
            listener.onPlayCardClicked(element, bindingAdapterPosition)
        }
    }

    private fun setOnPlayVideoImpressionListener(element: PlayCardModel) {
        binding?.homeRecomPlayWidgetVideo?.addOnImpressionListener(
            element,
            object : ViewHintListener {
                override fun onViewHint() {
                    listener.onPlayCardImpressed(element, bindingAdapterPosition)
                    AppLogAnalytics.sendProductImpression(TrackProduct("1", false, 1, ""))
                }
            }
        )
    }

    interface Listener {
        fun onPlayCardClicked(element: PlayCardModel, position: Int)
        fun onPlayCardImpressed(element: PlayCardModel, position: Int)
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.widget_play_recom
    }
}
