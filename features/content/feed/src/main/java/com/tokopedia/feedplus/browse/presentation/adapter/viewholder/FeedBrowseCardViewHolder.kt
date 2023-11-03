package com.tokopedia.feedplus.browse.presentation.adapter.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.feedplus.browse.presentation.adapter.FeedBrowsePayloads
import com.tokopedia.feedplus.databinding.ItemFeedBrowseCardBinding
import com.tokopedia.feedplus.databinding.ItemFeedBrowseChannelErrorBinding
import com.tokopedia.feedplus.databinding.ItemFeedBrowseChannelLoadingBinding
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.widget.PlayWidgetCardView
import com.tokopedia.unifyprinciples.UnifyMotion

/**
 * Created by meyta.taliti on 11/08/23.
 */
internal class FeedBrowseChannelViewHolder2 private constructor() {

    internal class Channel private constructor(
        binding: ItemFeedBrowseCardBinding,
        private val listener: Listener
    ) : RecyclerView.ViewHolder(binding.root) {

        private val playWidget = binding.root

        private val playWidgetListener = object : PlayWidgetCardView.Listener {
            override fun onCardClicked(view: PlayWidgetCardView, item: PlayWidgetChannelUiModel) {
                listener.onCardClicked(item, absoluteAdapterPosition)
            }
        }

        init {
            playWidget.setListener(playWidgetListener)
        }

        fun bind(item: PlayWidgetChannelUiModel) {
            playWidget.setData(item)
            playWidget.addOnImpressionListener(item.impressHolder) {
                listener.onCardImpressed(item, absoluteAdapterPosition)
            }
        }

        fun bindPayloads(item: PlayWidgetChannelUiModel, payloads: FeedBrowsePayloads) {
            playWidget.setData(item)
        }

        companion object {
            fun create(
                parent: ViewGroup,
                listener: Listener,
            ): Channel {
                return Channel(
                    ItemFeedBrowseCardBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ),
                    listener
                )
            }
        }

        interface Listener {
            fun onCardImpressed(
                item: PlayWidgetChannelUiModel,
                position: Int
            )
            fun onCardClicked(
                item: PlayWidgetChannelUiModel,
                position: Int
            )
        }
    }

    internal class Loading private constructor(
        binding: ItemFeedBrowseChannelLoadingBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun create(parent: ViewGroup): Loading {
                return Loading(
                    ItemFeedBrowseChannelLoadingBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
        }
    }

    internal class Error private constructor(
        private val binding: ItemFeedBrowseChannelErrorBinding,
        listener: Listener,
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
                override fun onViewAttachedToWindow(v: View) {

                }

                override fun onViewDetachedFromWindow(v: View) {
                    stopAnimating()
                }
            })

            binding.root.setOnClickListener {
                startAnimating()
                listener.onRetry(this)
            }
        }

        private val rotateAnimation by lazy {
            RotateAnimation(
                0f, 359f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
            ).apply {
                duration = 1000
                interpolator = UnifyMotion.LINEAR
                repeatCount = Animation.INFINITE
            }
        }

        private fun startAnimating() {
            binding.btnRetry.startAnimation(rotateAnimation)
        }

        fun stopAnimating() {
            binding.btnRetry.clearAnimation()
        }

        companion object {
            fun create(parent: ViewGroup, listener: Listener): Error {
                return Error(
                    ItemFeedBrowseChannelErrorBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ),
                    listener,
                )
            }
        }

        interface Listener {
            fun onRetry(viewHolder: Error)
        }
    }
}
