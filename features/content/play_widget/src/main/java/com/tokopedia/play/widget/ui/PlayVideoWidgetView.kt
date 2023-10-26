package com.tokopedia.play.widget.ui

import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.BehindLiveWindowException
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.media.loader.loadImage
import com.tokopedia.play.widget.R
import com.tokopedia.play.widget.databinding.ViewPlayVideoWidgetBinding
import com.tokopedia.play.widget.player.VideoPlayer
import com.tokopedia.play.widget.ui.model.PlayVideoWidgetUiModel
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

    private var mModel = PlayVideoWidgetUiModel.Empty

    private val playerListener = object : Player.EventListener {
        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            binding.playerView.showWithCondition(
                playWhenReady && playbackState == Player.STATE_READY
            )
        }

        override fun onPlayerError(error: ExoPlaybackException) {
            if (error.type != ExoPlaybackException.TYPE_SOURCE) return
            if (error.sourceException !is BehindLiveWindowException) return

            bindPlayer(mModel, resetState = false)
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
    }

    private fun initAttrs(attrs: AttributeSet) {
        val attributeArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.PlayVideoWidgetView
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
                    MeasureSpec.EXACTLY
                ) to MeasureSpec.makeMeasureSpec(
                    (widthSize / 9f * 16f).toInt(),
                    MeasureSpec.EXACTLY
                )
            }
            SizeMode.FollowHeight -> {
                MeasureSpec.makeMeasureSpec(
                    (heightSize / 16f * 9f).toInt(),
                    MeasureSpec.EXACTLY
                ) to MeasureSpec.makeMeasureSpec(
                    heightSize,
                    MeasureSpec.EXACTLY
                )
            }
            SizeMode.Custom -> {
                widthMeasureSpec to heightMeasureSpec
            }
        }

        super.onMeasure(widthSpec, heightSpec)
    }

    fun bind(model: PlayVideoWidgetUiModel) {
        mModel = model

        binding.totalWatchView.setTotalWatch(model.totalView)
        binding.imgCover.loadImage(model.coverUrl)
        binding.tvTitle.text = model.title
        binding.imgAvatar.loadImage(model.avatarUrl)
        binding.imgBadge.showWithCondition(model.badgeUrl.isNotBlank())
        binding.imgBadge.loadImage(model.badgeUrl)
        binding.tvPartnerName.text = model.partnerName
        binding.tvLiveBadge.root.showWithCondition(model.isLive)

        bindPlayer(model)
    }

    fun stopVideo() {
        player.stop()
    }

    fun pauseVideo() {
        player.pause()
    }

    fun startVideo() {
        player.start(mModel.createVideoPlayerConfig(true))
    }

    fun releaseVideo() {
        player.release()
    }

    private fun bindPlayer(
        model: PlayVideoWidgetUiModel,
        resetState: Boolean = true
    ) {
        binding.playerView.player = player.player
        player.repeat(true)
        player.mute(true)
        player.stop()
        player.loadUri(
            Uri.parse(model.videoUrl),
            config = model.createVideoPlayerConfig(resetState)
        )
    }

    private fun PlayVideoWidgetUiModel.createVideoPlayerConfig(
        resetState: Boolean
    ): VideoPlayer.Config {
        return VideoPlayer.Config(
            isLive = isLive,
            resetState = resetState,
            duration = duration
        )
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
