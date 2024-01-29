package com.tokopedia.recommendation_widget_common.widget.foryou.play

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.play.widget.ui.PlayVideoWidgetView
import com.tokopedia.play.widget.ui.PlaybackException
import com.tokopedia.recommendation_widget_common.R
import com.tokopedia.recommendation_widget_common.databinding.WidgetPlayRecomBinding
import com.tokopedia.recommendation_widget_common.widget.foryou.BaseForYouViewHolder
import com.tokopedia.utils.view.binding.viewBinding

class PlayWidgetViewHolder constructor(
    view: View,
    private val listener: Listener
) : BaseForYouViewHolder<PlayWidgetModel>(
    view,
    PlayWidgetModel::class.java
) {

    private val binding: WidgetPlayRecomBinding? by viewBinding()

    init {
        binding?.homeRecomPlayWidgetVideo?.setListener(object : PlayVideoWidgetView.Listener {
            override fun onVideoFinishedPlaying(view: PlayVideoWidgetView) {
                binding?.homeRecomPlayWidgetVideo?.resetPlaybackPosition()
            }

            override fun onVideoError(view: PlayVideoWidgetView, error: PlaybackException) {
                // no op
            }
        })
    }

    override fun bind(element: PlayWidgetModel) {
        bindHomeRecomPlayWidgetVideo(element)
        setOnPlayVideoImpressionListener(element)
        setHomePlayWidgetVideoClick(element)
    }

    private fun bindHomeRecomPlayWidgetVideo(element: PlayWidgetModel) {
        binding?.homeRecomPlayWidgetVideo?.bind(element.playVideoWidgetUiModel)
    }

    private fun setHomePlayWidgetVideoClick(element: PlayWidgetModel) {
        binding?.homeRecomPlayWidgetVideo?.setOnClickListener {
            listener.onPlayVideoWidgetClick(element, bindingAdapterPosition)
        }

        itemView.setOnClickListener {
            listener.onPlayVideoWidgetClick(element, bindingAdapterPosition)
        }
    }

    private fun setOnPlayVideoImpressionListener(element: PlayWidgetModel) {
        binding?.homeRecomPlayWidgetVideo?.addOnImpressionListener(
            element,
            object : ViewHintListener {
                override fun onViewHint() {
                    listener.onPlayVideoWidgetImpress(element, bindingAdapterPosition)
                }
            }
        )
    }

    interface Listener {
        fun onPlayVideoWidgetClick(element: PlayWidgetModel, position: Int)
        fun onPlayVideoWidgetImpress(element: PlayWidgetModel, position: Int)
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.widget_play_recom
    }
}
