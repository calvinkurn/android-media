package com.tokopedia.play.widget.ui.widget.carousel

import android.animation.Animator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.airbnb.lottie.LottieCompositionFactory
import com.airbnb.lottie.LottieDrawable
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.play.widget.R
import com.tokopedia.play.widget.databinding.ViewPlayWidgetCardCarouselChannelBinding
import com.tokopedia.play.widget.player.PlayVideoPlayer
import com.tokopedia.play.widget.player.PlayVideoPlayerReceiver
import com.tokopedia.play.widget.ui.carousel.product.PlayWidgetCarouselProductAdapter
import com.tokopedia.play.widget.ui.carousel.product.PlayWidgetCarouselProductItemDecoration
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetPartnerUiModel
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
                player.repeat(!mModel.video.isLive)
                player.mute(mModel.isMuted)
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

    fun reset() {
        binding.imgCover.setImageDrawable(null)
    }

    fun setModel(model: PlayWidgetChannelUiModel, invalidate: Boolean = true) {
        this.mModel = model
        if (invalidate) invalidateUi(model)
    }

    private fun invalidateUi(model: PlayWidgetChannelUiModel) {
        binding.tvLiveBadge.showWithCondition(model.channelType == PlayWidgetChannelType.Live)
        binding.viewPlayWidgetTotalViews.tvTotalViews.text = model.totalView.totalViewFmt
        binding.viewPlayWidgetCaption.root.text = model.title
        binding.imgCover.setImageUrl(model.video.coverUrl)

        binding.viewPlayWidgetPartnerInfo.tvName.text = model.partner.name
        binding.viewPlayWidgetPartnerInfo.imgAvatar.setImageUrl(model.partner.avatarUrl)
        if (model.partner.badgeUrl.isBlank()) {
            binding.viewPlayWidgetPartnerInfo.imgBadge.hide()
        } else {
            binding.viewPlayWidgetPartnerInfo.imgBadge.setImageUrl(model.partner.badgeUrl)
            binding.viewPlayWidgetPartnerInfo.imgBadge.show()
        }
        binding.viewPlayWidgetPartnerInfo.root.setOnClickListener {
            mListener?.onPartnerClicked(this, model.partner)
        }


        adapter.submitList(model.products)

        setMuted(model.isMuted)
    }

    fun setListener(listener: Listener?) {
        mListener = listener
    }

    fun resetProductPosition() {
        binding.rvProducts.scrollToPosition(0)
    }

    fun updateTotalView(totalView: String) {
        binding.viewPlayWidgetTotalViews.tvTotalViews.text = totalView
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

        if (::mModel.isInitialized && !mModel.isMuted) {
            showSoundLoopLottie()
        }
    }

    fun setMuted(shouldMuted: Boolean, animate: Boolean = false) {
        if (shouldMuted) setMuteLottie(animate)
        else setUnMuteLottie(animate)

        mPlayer?.mute(shouldMuted)

        binding.viewPlayWidgetActionButton.root.setOnClickListener {
            mListener?.onMuteButtonClicked(
                this,
                mModel,
                !shouldMuted,
            )
        }
    }

    private fun setMuteLottie(animate: Boolean) {
        val lottieUrl = context.getString(R.string.lottie_sound_on_off)

        val fallbackToImage = {
            binding.viewPlayWidgetActionButton.lottieAction.hide()
            binding.viewPlayWidgetActionButton.iconActionFallback.setImage(IconUnify.VOLUME_MUTE)
            binding.viewPlayWidgetActionButton.iconActionFallback.show()
        }

        if (!animate) {
            fallbackToImage()
            return
        }

        LottieCompositionFactory.fromUrl(context, lottieUrl)
            .addFailureListener {
                fallbackToImage()
            }
            .addListener { composition ->
                binding.viewPlayWidgetActionButton.iconActionFallback.hide()
                binding.viewPlayWidgetActionButton.lottieAction.show()
                binding.viewPlayWidgetActionButton.lottieAction.removeAllAnimatorListeners()
                binding.viewPlayWidgetActionButton.lottieAction.setComposition(composition)
                binding.viewPlayWidgetActionButton.lottieAction.repeatCount = 0

                binding.viewPlayWidgetActionButton.lottieAction.playAnimation()
            }
    }

    private fun setUnMuteLottie(animate: Boolean) {
        val lottieUrl = context.getString(R.string.lottie_sound_off_on)

        val fallbackToImage = {
            binding.viewPlayWidgetActionButton.lottieAction.hide()
            binding.viewPlayWidgetActionButton.iconActionFallback.setImage(IconUnify.VOLUME_UP)
            binding.viewPlayWidgetActionButton.iconActionFallback.show()
        }

        if (!animate) {
            fallbackToImage()
            return
        }

        LottieCompositionFactory.fromUrl(context, lottieUrl)
            .addFailureListener {
                fallbackToImage()
            }
            .addListener { composition ->
                binding.viewPlayWidgetActionButton.iconActionFallback.hide()
                binding.viewPlayWidgetActionButton.lottieAction.show()
                binding.viewPlayWidgetActionButton.lottieAction.removeAllAnimatorListeners()
                binding.viewPlayWidgetActionButton.lottieAction.addAnimatorListener(
                    object: Animator.AnimatorListener {
                        override fun onAnimationStart(animator: Animator) {

                        }

                        override fun onAnimationEnd(animator: Animator) {
                            showSoundLoopLottie()
                        }

                        override fun onAnimationCancel(animator: Animator) {

                        }

                        override fun onAnimationRepeat(animator: Animator) {

                        }
                    }
                )
                binding.viewPlayWidgetActionButton.lottieAction.setComposition(composition)
                binding.viewPlayWidgetActionButton.lottieAction.repeatCount = 0

                binding.viewPlayWidgetActionButton.lottieAction.playAnimation()
            }
    }

    private fun showSoundLoopLottie() {
        val lottieUrl = context.getString(R.string.lottie_sound_on_loop)

        LottieCompositionFactory.fromUrl(context, lottieUrl)
            .addFailureListener {
                binding.viewPlayWidgetActionButton.lottieAction.hide()
                binding.viewPlayWidgetActionButton.iconActionFallback.setImage(IconUnify.VOLUME_UP)
                binding.viewPlayWidgetActionButton.iconActionFallback.show()
            }
            .addListener { composition ->
                binding.viewPlayWidgetActionButton.iconActionFallback.hide()
                binding.viewPlayWidgetActionButton.lottieAction.show()
                binding.viewPlayWidgetActionButton.lottieAction.removeAllAnimatorListeners()
                binding.viewPlayWidgetActionButton.lottieAction.setComposition(composition)
                binding.viewPlayWidgetActionButton.lottieAction.repeatCount = LottieDrawable.INFINITE

                binding.viewPlayWidgetActionButton.lottieAction.playAnimation()
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

        fun onPartnerClicked(
            view: PlayWidgetCardCarouselChannelView,
            partner: PlayWidgetPartnerUiModel,
        )
    }
}
