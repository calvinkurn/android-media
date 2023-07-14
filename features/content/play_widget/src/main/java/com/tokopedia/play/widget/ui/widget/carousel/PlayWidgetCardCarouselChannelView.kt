package com.tokopedia.play.widget.ui.widget.carousel

import android.animation.Animator
import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.TouchDelegate
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.view.doOnLayout
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.airbnb.lottie.LottieCompositionFactory
import com.airbnb.lottie.LottieDrawable
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.loader.loadImage
import com.tokopedia.play.widget.R
import com.tokopedia.play.widget.databinding.ViewPlayWidgetCardCarouselChannelBinding
import com.tokopedia.play.widget.player.PlayVideoPlayer
import com.tokopedia.play.widget.player.PlayVideoPlayerReceiver
import com.tokopedia.play.widget.ui.carousel.product.PlayWidgetCarouselProductAdapter
import com.tokopedia.play.widget.ui.carousel.product.PlayWidgetCarouselProductItemDecoration
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetProduct
import com.tokopedia.play.widget.ui.model.ext.isMuted
import com.tokopedia.play.widget.ui.model.ext.isWithProductNoCaptionVariant
import com.tokopedia.play.widget.ui.type.PlayWidgetChannelType

/**
 * Created by kenny.hadisaputra on 17/05/23
 */
@Suppress("LateinitUsage")
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
            override fun onImpressed(
                viewHolder: PlayWidgetCarouselProductAdapter.ViewHolder,
                product: PlayWidgetProduct
            ) {
                if (!::mModel.isInitialized) return

                mListener?.onProductImpressed(
                    this@PlayWidgetCardCarouselChannelView,
                    mModel,
                    product,
                    viewHolder.absoluteAdapterPosition
                )
            }

            override fun onClicked(
                viewHolder: PlayWidgetCarouselProductAdapter.ViewHolder,
                product: PlayWidgetProduct
            ) {
                if (!::mModel.isInitialized) return

                mListener?.onProductClicked(
                    this@PlayWidgetCardCarouselChannelView,
                    mModel,
                    product,
                    viewHolder.absoluteAdapterPosition
                )
            }
        }
    )

    private var mPlayer: PlayVideoPlayer? = null

    private val playerListener = object : PlayVideoPlayer.VideoPlayerListener {
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            TransitionManager.beginDelayedTransition(
                binding.root,
                AutoTransition()
                    .addTarget(binding.playerView)
            )
            binding.playerView.showWithCondition(isPlaying)
        }
    }

    private val offset12 = resources.getDimensionPixelOffset(R.dimen.play_widget_dp_12)

    private val downloadedLottieSet = mutableSetOf<Int>()

    init {
        preloadLottie()
        showMuteButton(false, animate = false)
        setMuted(true)

        binding.rvProducts.adapter = adapter
        binding.rvProducts.addItemDecoration(
            PlayWidgetCarouselProductItemDecoration(binding.rvProducts.context)
        )

        binding.viewPlayWidgetOverlay.root.setOnClickListener {
            mListener?.onOverlayClicked(this, mModel)
        }

        binding.viewPlayWidgetActionButton.root.doOnLayout {
            val parent = it.parent
            if (parent !is ViewGroup) return@doOnLayout

            val rect = Rect()
            it.getHitRect(rect)
            rect.inset(-offset12, -offset12)
            parent.touchDelegate = TouchDelegate(rect, it)
        }
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
                player.isLive = mModel.video.isLive
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

    fun setModel(model: PlayWidgetChannelUiModel, invalidate: Boolean = true) {
        this.mModel = model
        if (invalidate) invalidateUi(model)
    }

    private fun invalidateUi(model: PlayWidgetChannelUiModel) {
        binding.tvLiveBadge.showWithCondition(model.video.isLive && model.channelType == PlayWidgetChannelType.Live)
        binding.viewPlayWidgetTotalViews.tvTotalViews.text = model.totalView.totalViewFmt

        binding.imgCover.scaleType = ImageView.ScaleType.CENTER
        binding.imgCover.loadImage(model.video.coverUrl) {
            listener(
                onSuccess = { _, _ -> binding.imgCover.scaleType = ImageView.ScaleType.CENTER_CROP }
            )
        }

        binding.viewPlayWidgetPartnerInfo.tvName.text = model.partner.name
        binding.viewPlayWidgetPartnerInfo.imgAvatar.loadImage(model.partner.avatarUrl)
        if (model.partner.badgeUrl.isBlank()) {
            binding.viewPlayWidgetPartnerInfo.imgBadge.hide()
        } else {
            binding.viewPlayWidgetPartnerInfo.imgBadge.setImageUrl(model.partner.badgeUrl)
            binding.viewPlayWidgetPartnerInfo.imgBadge.show()
        }
        binding.viewPlayWidgetPartnerInfo.root.setOnClickListener {
            mListener?.onPartnerClicked(this, model)
        }

        binding.viewPlayWidgetCaption.root.text = model.title
        binding.viewPlayWidgetCaption.root.showWithCondition(!model.isWithProductNoCaptionVariant)

        binding.scrollableHostProducts.showWithCondition(
            model.isWithProductNoCaptionVariant && model.products.isNotEmpty()
        )
        adapter.setNewItems(model.products)

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

    fun setShowOverlay(isOverlayShown: Boolean) {
        binding.viewPlayWidgetOverlay.root.showWithCondition(isOverlayShown)
    }

    fun showMuteButton(shouldShow: Boolean, animate: Boolean = true) {
        if (animate) {
            TransitionManager.beginDelayedTransition(
                binding.root,
                AutoTransition()
                    .addTarget(binding.viewPlayWidgetActionButton.root)
                    .setDuration(DURATION_ACTION_TRANSITION)
            )
        }

        binding.viewPlayWidgetActionButton.root.showWithCondition(shouldShow)

        if (::mModel.isInitialized && !mModel.isMuted) {
            showSoundLoopLottie()
        }
    }

    fun setMuted(shouldMuted: Boolean, animate: Boolean = false) {
        if (shouldMuted) {
            setMuteLottie(animate)
        } else {
            setUnMuteLottie(animate)
        }

        mPlayer?.mute(shouldMuted)

        binding.viewPlayWidgetActionButton.root.setOnClickListener {
            mListener?.onMuteButtonClicked(
                this,
                mModel,
                !shouldMuted
            )
        }
    }

    private fun setMuteLottie(animate: Boolean) {
        val lottieRes = R.string.lottie_sound_on_off

        if (!downloadedLottieSet.contains(lottieRes) || !animate) {
            binding.viewPlayWidgetActionButton.lottieAction.cancelAnimation()
            binding.viewPlayWidgetActionButton.lottieAction.hide()
            binding.viewPlayWidgetActionButton.iconActionFallback.setImage(IconUnify.VOLUME_MUTE)
            binding.viewPlayWidgetActionButton.iconActionFallback.show()
        } else {
            LottieCompositionFactory.fromUrl(context, context.getString(lottieRes))
                .addListener { composition ->
                    binding.viewPlayWidgetActionButton.iconActionFallback.hide()
                    binding.viewPlayWidgetActionButton.lottieAction.show()
                    binding.viewPlayWidgetActionButton.lottieAction.removeAllAnimatorListeners()
                    binding.viewPlayWidgetActionButton.lottieAction.cancelAnimation()
                    binding.viewPlayWidgetActionButton.lottieAction.setComposition(composition)
                    binding.viewPlayWidgetActionButton.lottieAction.repeatCount = 0

                    binding.viewPlayWidgetActionButton.lottieAction.playAnimation()
                }
        }
    }

    private fun setUnMuteLottie(animate: Boolean) {
        val lottieRes = R.string.lottie_sound_off_on
        val loopingLottieRes = R.string.lottie_sound_on_loop

        if (!downloadedLottieSet.contains(lottieRes) || !downloadedLottieSet.contains(loopingLottieRes) || !animate) {
            binding.viewPlayWidgetActionButton.lottieAction.cancelAnimation()
            binding.viewPlayWidgetActionButton.lottieAction.hide()
            binding.viewPlayWidgetActionButton.iconActionFallback.setImage(IconUnify.VOLUME_UP)
            binding.viewPlayWidgetActionButton.iconActionFallback.show()
        } else {
            LottieCompositionFactory.fromUrl(context, context.getString(lottieRes))
                .addListener { composition ->
                    binding.viewPlayWidgetActionButton.iconActionFallback.hide()
                    binding.viewPlayWidgetActionButton.lottieAction.show()
                    binding.viewPlayWidgetActionButton.lottieAction.removeAllAnimatorListeners()
                    binding.viewPlayWidgetActionButton.lottieAction.cancelAnimation()
                    binding.viewPlayWidgetActionButton.lottieAction.addAnimatorListener(
                        object : Animator.AnimatorListener {
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
    }

    private fun showSoundLoopLottie() {
        val lottieUrl = context.getString(R.string.lottie_sound_on_loop)

        LottieCompositionFactory.fromUrl(context, lottieUrl)
            .addListener { composition ->
                binding.viewPlayWidgetActionButton.iconActionFallback.hide()
                binding.viewPlayWidgetActionButton.lottieAction.show()
                binding.viewPlayWidgetActionButton.lottieAction.removeAllAnimatorListeners()
                binding.viewPlayWidgetActionButton.lottieAction.cancelAnimation()
                binding.viewPlayWidgetActionButton.lottieAction.setComposition(composition)
                binding.viewPlayWidgetActionButton.lottieAction.repeatCount = LottieDrawable.INFINITE

                binding.viewPlayWidgetActionButton.lottieAction.playAnimation()
            }
    }

    private fun preloadLottie() {
        listOf(
            R.string.lottie_sound_on_off,
            R.string.lottie_sound_off_on,
            R.string.lottie_sound_on_loop
        ).forEach { res ->
            LottieCompositionFactory.fromUrl(context, context.getString(res))
                .addListener {
                    downloadedLottieSet.add(res)
                }
        }
    }

    companion object {
        private const val DURATION_ACTION_TRANSITION = 200L
    }

    interface Listener {
        fun onMuteButtonClicked(
            view: PlayWidgetCardCarouselChannelView,
            item: PlayWidgetChannelUiModel,
            shouldMute: Boolean
        )

        fun onProductImpressed(
            view: PlayWidgetCardCarouselChannelView,
            item: PlayWidgetChannelUiModel,
            product: PlayWidgetProduct,
            position: Int
        )

        fun onProductClicked(
            view: PlayWidgetCardCarouselChannelView,
            item: PlayWidgetChannelUiModel,
            product: PlayWidgetProduct,
            position: Int
        )

        fun onPartnerClicked(
            view: PlayWidgetCardCarouselChannelView,
            item: PlayWidgetChannelUiModel
        )

        fun onOverlayClicked(
            view: PlayWidgetCardCarouselChannelView,
            item: PlayWidgetChannelUiModel
        )
    }
}
