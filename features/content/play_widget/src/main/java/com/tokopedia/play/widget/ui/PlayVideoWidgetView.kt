package com.tokopedia.play.widget.ui

import android.animation.Animator
import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import com.airbnb.lottie.LottieDrawable
import com.google.android.exoplayer2.Player
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.media.loader.loadImage
import com.tokopedia.play.widget.R
import com.tokopedia.play.widget.databinding.ViewPlayVideoWidgetBinding
import com.tokopedia.play.widget.player.VideoPlayer
import com.tokopedia.play.widget.ui.model.PlayVideoWidgetUiModel
import com.tokopedia.play.widget.util.PlayWidgetLottieLoadHelper
import com.tokopedia.unifycomponents.CardUnify2

/**
 * Created by kenny.hadisaputra on 19/10/23
 */
class PlayVideoWidgetView : CardUnify2 {

    constructor(context: Context) : super(context, null)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initAttrs(attrs)
    }

    private val binding = ViewPlayVideoWidgetBinding.inflate(
        LayoutInflater.from(context),
        this
    )

    private var mSizeMode = SizeMode.FollowWidth

    private val lottieLoadHelper = PlayWidgetLottieLoadHelper(context)

    private val playerListener = object : Player.EventListener {
        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            binding.playerView.showWithCondition(
                playWhenReady && playbackState == Player.STATE_READY
            )
        }
    }
    private val player by lazyThreadSafetyNone {
        VideoPlayer(context).apply {
            addPlayerListener(playerListener)
        }
    }

    init {
        animateOnPress = ANIMATE_BOUNCE

        addOnAttachStateChangeListener(object : OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {

            }

            override fun onViewDetachedFromWindow(v: View) {
                pauseVideo()
            }
        })

        listOf(
            R.string.lottie_sound_on_off,
            R.string.lottie_sound_off_on,
            R.string.lottie_sound_on_loop
        ).forEach { res ->
            lottieLoadHelper.preload(context.getString(res))
        }

    }

    private fun initAttrs(attrs: AttributeSet) {
        val attributeArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.PlayVideoWidgetView,
        )

        mSizeMode = SizeMode.of(
            attributeArray.getInt(R.styleable.PlayVideoWidgetView_sizeMode, SizeMode.FollowWidth.value)
        )

        attributeArray.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        val (widthSpec, heightSpec) = when (mSizeMode) {
            SizeMode.FollowWidth -> {
                MeasureSpec.makeMeasureSpec(
                    widthSize,
                    MeasureSpec.EXACTLY,
                ) to MeasureSpec.makeMeasureSpec(
                    (widthSize / 9f * 16f).toInt(),
                    MeasureSpec.EXACTLY,
                )
            }
            SizeMode.FollowHeight -> {
                MeasureSpec.makeMeasureSpec(
                    (heightSize / 16f * 9f).toInt(),
                    MeasureSpec.EXACTLY,
                ) to MeasureSpec.makeMeasureSpec(
                    heightSize,
                    MeasureSpec.EXACTLY,
                )
            }
            SizeMode.Custom -> {
                widthMeasureSpec to heightMeasureSpec
            }
        }

        super.onMeasure(widthSpec, heightSpec)
    }

    fun bind(model: PlayVideoWidgetUiModel) {
        binding.totalWatchView.setTotalWatch(model.totalView)
        binding.imgCover.loadImage(model.coverUrl)
        binding.tvTitle.text = model.title
        binding.imgAvatar.loadImage(model.avatarUrl)
        binding.imgBadge.showWithCondition(model.badgeUrl.isNotBlank())
        binding.imgBadge.loadImage(model.badgeUrl)
        binding.tvPartnerName.text = model.partnerName
        binding.tvLiveBadge.root.showWithCondition(model.isLive)

        bindMute(model.isMuted)
        bindPlayer(model.videoUrl)
    }

    fun pauseVideo() {
        Log.d("PlayVideoWidget", "Pause")
        player.pause()
    }

    fun resumeVideo() {
        Log.d("PlayVideoWidget", "Resume")
        player.start()
    }

    fun releaseVideo() {
        Log.d("PlayVideoWidget", "Release")
        player.release()
    }

    private fun bindPlayer(videoUrl: String) {
        binding.playerView.player = player.player
        player.stop()
        player.loadUri(Uri.parse(videoUrl))
    }

    private fun bindMute(isMuted: Boolean) {
        if (isMuted) setMuted()
        else setNotMuted()
    }

    private fun setMuted() {
        val lottieUrl = context.getString(R.string.lottie_sound_on_off)
        if (lottieLoadHelper.hasLoaded(lottieUrl)) {
            binding.viewPlayWidgetActionButton.lottieAction.cancelAnimation()
            binding.viewPlayWidgetActionButton.lottieAction.hide()
            binding.viewPlayWidgetActionButton.iconActionFallback.setImage(IconUnify.VOLUME_MUTE)
            binding.viewPlayWidgetActionButton.iconActionFallback.show()
        } else {
            binding.viewPlayWidgetActionButton.iconActionFallback.hide()
            binding.viewPlayWidgetActionButton.lottieAction.removeAllAnimatorListeners()
            binding.viewPlayWidgetActionButton.lottieAction.cancelAnimation()
            binding.viewPlayWidgetActionButton.lottieAction.setAnimationFromUrl(lottieUrl)
            binding.viewPlayWidgetActionButton.lottieAction.repeatCount = 0
            binding.viewPlayWidgetActionButton.lottieAction.show()
            binding.viewPlayWidgetActionButton.lottieAction.playAnimation()
        }
    }

    private fun setNotMuted() {
        val lottieUrl = context.getString(R.string.lottie_sound_off_on)
        val loopingLottieUrl = context.getString(R.string.lottie_sound_on_loop)

        if (!lottieLoadHelper.hasLoaded(lottieUrl) || !lottieLoadHelper.hasLoaded(loopingLottieUrl)) {
            binding.viewPlayWidgetActionButton.lottieAction.cancelAnimation()
            binding.viewPlayWidgetActionButton.lottieAction.hide()
            binding.viewPlayWidgetActionButton.iconActionFallback.setImage(IconUnify.VOLUME_UP)
            binding.viewPlayWidgetActionButton.iconActionFallback.show()
        } else {
            binding.viewPlayWidgetActionButton.iconActionFallback.hide()
            binding.viewPlayWidgetActionButton.lottieAction.removeAllAnimatorListeners()
            binding.viewPlayWidgetActionButton.lottieAction.cancelAnimation()
            binding.viewPlayWidgetActionButton.lottieAction.addAnimatorListener(
                object : Animator.AnimatorListener {
                    override fun onAnimationStart(animator: Animator) {
                    }

                    override fun onAnimationEnd(animator: Animator) {
                        setSoundLoopLottie()
                    }

                    override fun onAnimationCancel(animator: Animator) {
                    }

                    override fun onAnimationRepeat(animator: Animator) {
                    }
                }
            )
            binding.viewPlayWidgetActionButton.lottieAction.setAnimationFromUrl(lottieUrl)
            binding.viewPlayWidgetActionButton.lottieAction.repeatCount = 0
            binding.viewPlayWidgetActionButton.lottieAction.show()
            binding.viewPlayWidgetActionButton.lottieAction.playAnimation()
        }
    }

    private fun setSoundLoopLottie() {
        val lottieUrl = context.getString(R.string.lottie_sound_on_loop)

        binding.viewPlayWidgetActionButton.iconActionFallback.hide()
        binding.viewPlayWidgetActionButton.lottieAction.removeAllAnimatorListeners()
        binding.viewPlayWidgetActionButton.lottieAction.cancelAnimation()
        binding.viewPlayWidgetActionButton.lottieAction.setAnimationFromUrl(lottieUrl)
        binding.viewPlayWidgetActionButton.lottieAction.repeatCount = LottieDrawable.INFINITE
        binding.viewPlayWidgetActionButton.lottieAction.show()
        binding.viewPlayWidgetActionButton.lottieAction.playAnimation()
    }

    enum class SizeMode(internal val value: Int) {
        FollowWidth(0),
        FollowHeight(1),
        Custom(2);

        companion object {

            private val values = values()
            fun of(value: Int): SizeMode {
                values.forEach {
                    if (value == it.value) return it
                }
                return FollowWidth
            }
        }
    }
}
