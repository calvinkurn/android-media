package com.tokopedia.play.widget.ui.widget.carousel

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.airbnb.lottie.LottieCompositionFactory
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.play.widget.R
import com.tokopedia.play.widget.databinding.ViewPlayWidgetCardCarouselChannelBinding
import com.tokopedia.play.widget.player.PlayVideoPlayer
import com.tokopedia.play.widget.player.PlayVideoPlayerReceiver
import com.tokopedia.play.widget.ui.carousel.product.PlayWidgetCarouselProductAdapter
import com.tokopedia.play.widget.ui.carousel.product.PlayWidgetCarouselProductItemDecoration
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetProduct
import com.tokopedia.play.widget.ui.model.ext.isMuted
import com.tokopedia.play.widget.ui.type.PlayWidgetChannelType

/**
 * Created by kenny.hadisaputra on 17/05/23
 */
class PlayWidgetCardCarouselChannelView : FrameLayout, PlayVideoPlayerReceiver {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    private val binding = ViewPlayWidgetCardCarouselChannelBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    private lateinit var mModel: PlayWidgetChannelUiModel

    private var mListener: Listener? = null

    private val adapter = PlayWidgetCarouselProductAdapter(
        object : PlayWidgetCarouselProductAdapter.ViewHolder.Listener {
            override fun onClicked(
                viewHolder: PlayWidgetCarouselProductAdapter.ViewHolder,
                product: PlayWidgetProduct
            ) {
                mListener?.onProductClicked(this@PlayWidgetCardCarouselChannelView, product)
            }
        }
    )

    private var mPlayer: PlayVideoPlayer? = null

    private val playerListener = object : PlayVideoPlayer.VideoPlayerListener {
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            binding.playerView.showWithCondition(isPlaying)
        }
    }

    init {
        showMuteButton(false, animate = false)
        setMuted(true)

        binding.rvProducts.adapter = adapter
        binding.rvProducts.addItemDecoration(
            PlayWidgetCarouselProductItemDecoration(binding.rvProducts.context)
        )
    }

    override fun setPlayer(player: PlayVideoPlayer?) {
        mPlayer?.listener = null
        mPlayer = player
        binding.playerView.player = player?.getPlayer()
        if (player == null) {
            binding.playerView.gone()
        } else {
            if (::mModel.isInitialized) {
                player.videoUrl = mModel.video.videoUrl
                player.shouldCache = !mModel.video.isLive
                player.start()
            }
            player.listener = playerListener
        }
    }

    override fun getPlayer(): PlayVideoPlayer? {
        return mPlayer
    }

    override fun isPlayable(): Boolean {
        return mModel.channelType == PlayWidgetChannelType.Live ||
            mModel.channelType == PlayWidgetChannelType.Vod
    }

    fun setModel(model: PlayWidgetChannelUiModel) {
        this.mModel = model

        binding.tvLiveBadge.showWithCondition(model.channelType == PlayWidgetChannelType.Live)
        binding.viewPlayWidgetTotalViews.tvTotalViews.text = model.totalView.totalViewFmt
        binding.viewPlayWidgetCaption.root.text = model.title
        binding.viewPlayWidgetPartnerInfo.tvName.text = model.partner.name
        binding.imgCover.setImageUrl(model.video.coverUrl)

        adapter.submitList(model.products)

        setMuted(model.isMuted)
    }

    fun setListener(listener: Listener?) {
        mListener = listener
    }

    fun resetProductPosition() {
        binding.rvProducts.scrollToPosition(0)
    }

    fun showMuteButton(shouldShow: Boolean, animate: Boolean = true) {
        if (animate) {
            TransitionManager.beginDelayedTransition(
                binding.root,
                AutoTransition()
                    .addTarget(binding.viewPlayWidgetActionButton.root)
            )
        }

        binding.viewPlayWidgetActionButton.root.showWithCondition(shouldShow)
    }

    fun setMuted(shouldMuted: Boolean, animate: Boolean = false) {
        val lottieComposition = LottieCompositionFactory.fromRawRes(
            binding.root.context,
            if (shouldMuted) R.raw.play_widget_lottie_sound_on_off
            else R.raw.play_widget_lottie_sound_off_on
        )

        lottieComposition.addListener { composition ->
            binding.viewPlayWidgetActionButton.root.setComposition(composition)

            if (animate) binding.viewPlayWidgetActionButton.root.playAnimation()
            else binding.viewPlayWidgetActionButton.root.progress = 1f
        }

        binding.viewPlayWidgetActionButton.root.setOnClickListener {
            mListener?.onMuteButtonClicked(
                this,
                mModel,
                !shouldMuted,
            )
        }
    }

    interface Listener {
        fun onMuteButtonClicked(
            view: PlayWidgetCardCarouselChannelView,
            item: PlayWidgetChannelUiModel,
            shouldMute: Boolean,
        )

        fun onProductClicked(
            view: PlayWidgetCardCarouselChannelView,
            product: PlayWidgetProduct,
        )
    }
}
