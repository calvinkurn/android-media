package com.tokopedia.play.widget.ui.carousel

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieCompositionFactory
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.play.widget.R
import com.tokopedia.play.widget.databinding.ItemPlayWidgetVideoContentBinding
import com.tokopedia.play.widget.ui.carousel.product.PlayWidgetCarouselProductAdapter
import com.tokopedia.play.widget.ui.carousel.product.PlayWidgetCarouselProductItemDecoration
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetProduct
import com.tokopedia.play.widget.ui.model.ext.isMuted
import com.tokopedia.play.widget.ui.type.PlayWidgetChannelType
import com.tokopedia.play.widget.ui.widget.carousel.PlayWidgetCarouselDiffCallback

/**
 * Created by kenny.hadisaputra on 04/05/23
 */
class PlayWidgetVideoContentViewHolder(
    private val binding: ItemPlayWidgetVideoContentBinding,
    private val listener: Listener,
) : RecyclerView.ViewHolder(binding.root) {

    private val adapter = PlayWidgetCarouselProductAdapter(
        object : PlayWidgetCarouselProductAdapter.ViewHolder.Listener {
            override fun onClicked(
                viewHolder: PlayWidgetCarouselProductAdapter.ViewHolder,
                product: PlayWidgetProduct
            ) {
                listener.onProductClicked(this@PlayWidgetVideoContentViewHolder, product)
            }
        }
    )

    init {
        setMuted(true)

        binding.rvProducts.adapter = adapter
        binding.rvProducts.addItemDecoration(
            PlayWidgetCarouselProductItemDecoration(binding.rvProducts.context)
        )
    }

    fun bind(data: PlayWidgetChannelUiModel) {
        binding.tvLiveBadge.showWithCondition(data.channelType == PlayWidgetChannelType.Live)
        binding.viewPlayWidgetTotalViews.tvTotalViews.text = data.totalView.totalViewFmt
        binding.viewPlayWidgetCaption.root.text = data.title
        binding.viewPlayWidgetPartnerInfo.tvName.text = data.partner.name

        adapter.submitList(data.products)

        setMuted(data.isMuted)
        setMutedListener(data)
    }

    fun bind(data: PlayWidgetChannelUiModel, payloads: Set<String>) {
        payloads.forEach {
            when (it) {
                PlayWidgetCarouselDiffCallback.PAYLOAD_MUTE_CHANGE -> {
                    setMuted(shouldMuted = data.isMuted, animate = true)
                    setMutedListener(data)
                }
            }
        }
    }

    private fun setMuted(shouldMuted: Boolean, animate: Boolean = false) {
        val lottieComposition = LottieCompositionFactory.fromRawRes(
            binding.root.context,
            if (shouldMuted) R.raw.play_widget_lottie_mute_on_off
            else R.raw.play_widget_lottie_mute_off_on
        )

        lottieComposition.addListener { composition ->
            binding.viewPlayWidgetActionButton.root.setComposition(composition)

            if (animate) binding.viewPlayWidgetActionButton.root.playAnimation()
            else binding.viewPlayWidgetActionButton.root.progress = 1f
        }
    }

    private fun setMutedListener(data: PlayWidgetChannelUiModel) {
        binding.viewPlayWidgetActionButton.root.setOnClickListener {
            listener.onMuteButtonClicked(
                this,
                data,
                !data.isMuted,
            )
        }
    }

    companion object {

        fun create(parent: ViewGroup, listener: Listener): PlayWidgetVideoContentViewHolder {
            return PlayWidgetVideoContentViewHolder(
                ItemPlayWidgetVideoContentBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false,
                ),
                listener,
            )
        }
    }

    interface Listener {
        fun onMuteButtonClicked(
            viewHolder: PlayWidgetVideoContentViewHolder,
            data: PlayWidgetChannelUiModel,
            shouldMute: Boolean,
        )

        fun onProductClicked(
            viewHolder: PlayWidgetVideoContentViewHolder,
            product: PlayWidgetProduct,
        )
    }
}
