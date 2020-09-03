package com.tokopedia.play_common.widget.playBannerCarousel.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Matrix
import android.graphics.RectF
import android.os.Looper
import android.util.AttributeSet
import android.view.*
import android.widget.FrameLayout
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.text.Cue
import com.google.android.exoplayer2.text.TextOutput
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.PlayerControlView
import com.google.android.exoplayer2.ui.spherical.SingleTapListener
import com.google.android.exoplayer2.util.Assertions
import com.google.android.exoplayer2.video.VideoListener
import com.tokopedia.play_common.R

class VideoPlayerView @JvmOverloads constructor(context: Context, attrs: AttributeSet? =  /* attrs= */null, defStyleAttr: Int =  /* defStyleAttr= */0) : FrameLayout(context, attrs, defStyleAttr) {

    private val componentListener: ComponentListener
    private var contentFrame: AspectRatioFrameLayout

    var videoSurfaceView: TextureView

    private var player: Player? = null

    private var keepContentOnPlayerReset = false

    private var textureViewRotation = 0

    init {
        componentListener = ComponentListener()
        var playerLayoutId = R.layout.layout_video_player_view
        var resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
        if (attrs != null) {
            val a = context.theme.obtainStyledAttributes(attrs, R.styleable.PlayerView, 0, 0)
            try {
                playerLayoutId = a.getResourceId(R.styleable.PlayerView_player_layout_id, playerLayoutId)
                resizeMode = a.getInt(R.styleable.PlayerView_resize_mode, resizeMode)
                keepContentOnPlayerReset = a.getBoolean(
                        R.styleable.PlayerView_keep_content_on_player_reset, keepContentOnPlayerReset)
            } finally {
                a.recycle()
            }
        }
        LayoutInflater.from(context).inflate(playerLayoutId, this)
        descendantFocusability = ViewGroup.FOCUS_AFTER_DESCENDANTS

        // Content frame.
        contentFrame = findViewById(R.id.exo_content_frame)
        setResizeModeRaw(contentFrame, resizeMode)


        // Create a surface view and insert it into the content frame, if there is one.
        val params = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        videoSurfaceView = TextureView(context)
        videoSurfaceView.layoutParams = params
        contentFrame.addView(videoSurfaceView, 0)
    }

    /** Returns the player currently set on this view, or null if no player is set.  */
    fun getPlayer(): Player? {
        return player
    }

    fun setPlayer(player: Player?) {
        Assertions.checkState(Looper.myLooper() == Looper.getMainLooper())
        Assertions.checkArgument(
                player == null || player.applicationLooper == Looper.getMainLooper())
        if (this.player === player) {
            return
        }
        val oldPlayer = this.player
        if (oldPlayer != null) {
            oldPlayer.removeListener(componentListener)
            val oldVideoComponent = oldPlayer.videoComponent
            if (oldVideoComponent != null) {
                oldVideoComponent.removeVideoListener(componentListener)
                oldVideoComponent.clearVideoTextureView(videoSurfaceView)
            }
            val oldTextComponent = oldPlayer.textComponent
            oldTextComponent?.removeTextOutput(componentListener)
        }
        this.player = player

        updateForCurrentTrackSelections( /* isNewPlayer= */true)
        if (player != null) {
            val newVideoComponent = player.videoComponent
            if (newVideoComponent != null) {
                newVideoComponent.setVideoTextureView(videoSurfaceView as TextureView?)
                newVideoComponent.addVideoListener(componentListener)
            }
            val newTextComponent = player.textComponent
            newTextComponent?.addTextOutput(componentListener)
            player.addListener(componentListener)

        }
    }

    override fun setVisibility(visibility: Int) {
        super.setVisibility(visibility)
        if (videoSurfaceView is SurfaceView) {
            // Work around https://github.com/google/ExoPlayer/issues/3160.
            videoSurfaceView.visibility = visibility
        }
    }

    var resizeMode: Int
        get() {
            Assertions.checkStateNotNull(contentFrame)
            return contentFrame.resizeMode
        }
        set(resizeMode) {
            Assertions.checkStateNotNull(contentFrame)
            contentFrame.resizeMode = resizeMode
        }


    fun onContentAspectRatioChanged(
            contentAspectRatio: Float,
            contentFrame: AspectRatioFrameLayout?) {
        contentFrame?.setAspectRatio(contentAspectRatio)
    }

    fun updateForCurrentTrackSelections(isNewPlayer: Boolean) {
        val player = player
        if (player == null || player.currentTrackGroups.isEmpty) {
            return
        }
        val selections = player.currentTrackSelections
        for (i in 0 until selections.length) {
            if (player.getRendererType(i) == C.TRACK_TYPE_VIDEO && selections[i] != null) {
                return
            }
        }
    }

    inner class ComponentListener : Player.EventListener, TextOutput, VideoListener, OnLayoutChangeListener, SingleTapListener, PlayerControlView.VisibilityListener {
        // TextOutput implementation
        override fun onCues(cues: List<Cue>) {

        }

        // VideoListener implementation
        @SuppressLint("SyntheticAccessor")
        override fun onVideoSizeChanged(
                width: Int, height: Int, unappliedRotationDegrees: Int, pixelWidthHeightRatio: Float) {
            var videoAspectRatio: Float = if (height == 0 || width == 0) 1f else width * pixelWidthHeightRatio / height
            // Try to apply rotation transformation when our surface is a TextureView.
            if (unappliedRotationDegrees == 90 || unappliedRotationDegrees == 270) {
                // We will apply a rotation 90/270 degree to the output texture of the TextureView.
                // In this case, the output video's width and height will be swapped.
                videoAspectRatio = 1 / videoAspectRatio
            }
            if (textureViewRotation != 0) {
                videoSurfaceView.removeOnLayoutChangeListener(this)
            }
            textureViewRotation = unappliedRotationDegrees
            if (textureViewRotation != 0) {
                // The texture view's dimensions might be changed after layout step.
                // So add an OnLayoutChangeListener to apply rotation after layout step.
                videoSurfaceView.addOnLayoutChangeListener(this)
            }
            applyTextureViewRotation(videoSurfaceView, textureViewRotation)
            onContentAspectRatioChanged(videoAspectRatio, contentFrame)
        }

        override fun onRenderedFirstFrame() {
        }

        override fun onTracksChanged(tracks: TrackGroupArray, selections: TrackSelectionArray) {
            updateForCurrentTrackSelections( /* isNewPlayer= */false)
        }

        // Player.EventListener implementation
        override fun onPlayerStateChanged(playWhenReady: Boolean, @Player.State playbackState: Int) {

        }

        override fun onPositionDiscontinuity(@Player.DiscontinuityReason reason: Int) {

        }

        // OnLayoutChangeListener implementation
        @SuppressLint("SyntheticAccessor")
        override fun onLayoutChange(
                view: View,
                left: Int,
                top: Int,
                right: Int,
                bottom: Int,
                oldLeft: Int,
                oldTop: Int,
                oldRight: Int,
                oldBottom: Int) {
            applyTextureViewRotation(view as TextureView, textureViewRotation)
        }

        // SingleTapListener implementation
        override fun onSingleTapUp(e: MotionEvent): Boolean {
            return false
        }

        // PlayerControlView.VisibilityListener implementation
        override fun onVisibilityChange(visibility: Int) {

        }
    }

    companion object {
        fun setResizeModeRaw(aspectRatioFrame: AspectRatioFrameLayout, resizeMode: Int) {
            aspectRatioFrame.resizeMode = resizeMode
        }

        /** Applies a texture rotation to a [TextureView].  */
        fun applyTextureViewRotation(textureView: TextureView, textureViewRotation: Int) {
            val transformMatrix = Matrix()
            val textureViewWidth = textureView.width.toFloat()
            val textureViewHeight = textureView.height.toFloat()
            if (textureViewWidth != 0f && textureViewHeight != 0f && textureViewRotation != 0) {
                val pivotX = textureViewWidth / 2
                val pivotY = textureViewHeight / 2
                transformMatrix.postRotate(textureViewRotation.toFloat(), pivotX, pivotY)

                // After rotation, scale the rotated texture to fit the TextureView size.
                val originalTextureRect = RectF(0f, 0f, textureViewWidth, textureViewHeight)
                val rotatedTextureRect = RectF()
                transformMatrix.mapRect(rotatedTextureRect, originalTextureRect)
                transformMatrix.postScale(
                        textureViewWidth / rotatedTextureRect.width(),
                        textureViewHeight / rotatedTextureRect.height(),
                        pivotX,
                        pivotY)
            }
            textureView.setTransform(transformMatrix)
        }
    }
}