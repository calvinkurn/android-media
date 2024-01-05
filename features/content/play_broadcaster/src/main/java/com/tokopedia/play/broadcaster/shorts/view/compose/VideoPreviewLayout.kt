package com.tokopedia.play.broadcaster.shorts.view.compose

import android.net.Uri
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ui.PlayerView
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.compose.NestIcon
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.nest.principles.utils.noRippleClickable
import com.tokopedia.play.broadcaster.shorts.factory.PlayShortsMediaSourceFactory
import com.tokopedia.play_common.player.PlayVideoWrapper
import com.tokopedia.play_common.state.PlayVideoState

/**
 * Created By : Jonathan Darwin on January 03, 2024
 */
@Composable
fun VideoPreviewLayout(
    videoUri: String,
    videoWrapper: PlayVideoWrapper,
    onClose: () -> Unit,
) {
    var videoState: VideoState by remember {
        mutableStateOf(VideoState.Unknown)
    }

    ConstraintLayout {
        val (
            icClose,
            videoPreview,
            icPlay
        ) = createRefs()

        LaunchedEffect(Unit) {
            videoWrapper.addListener(object : PlayVideoWrapper.Listener {
                override fun onPlayerStateChanged(state: PlayVideoState) {
                    super.onPlayerStateChanged(state)
                    when (state) {
                        is PlayVideoState.Ended -> {
                            videoWrapper.videoPlayer.seekTo(1)
                            videoWrapper.pause(true)
                            videoState = VideoState.Pause
                        }
                        is PlayVideoState.Pause -> {
                            videoState = VideoState.Pause
                        }
                        else -> {}
                    }
                }
            })
        }

        DisposableEffect(
            AndroidView(
                factory = {
                    PlayerView(it).apply {
                        videoWrapper.playUri(Uri.parse(videoUri), autoPlay = false)

                        player = videoWrapper.videoPlayer
                        layoutParams = FrameLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                        useController = false

                        videoSurfaceView?.setOnClickListener {
                            if (videoState == VideoState.Play) {
                                videoState = VideoState.Pause
                                videoWrapper.pause(false)
                            }
                        }
                    }
                },
                modifier = Modifier.constrainAs(videoPreview) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
            )
        ) {
            onDispose {
                videoWrapper.stop()
                videoWrapper.release()
            }
        }

        if (videoState == VideoState.Pause) {
            NestIcon(
                iconId = IconUnify.PLAY,
                colorLightEnable = Color.White,
                colorLightDisable = Color.White,
                colorNightEnable = Color.White,
                colorNightDisable = Color.White,
                modifier = Modifier
                    .constrainAs(icPlay) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }
                    .clip(CircleShape)
                    .background(Color(0f, 0f, 0f, 0.4f))
                    .noRippleClickable {
                        videoWrapper.resume()
                        videoState = VideoState.Play
                    }
                    .padding(16.dp)
            )
        }

        NestIcon(
            iconId = IconUnify.CLOSE,
            colorLightEnable = Color.White,
            colorLightDisable = Color.White,
            colorNightEnable = Color.White,
            colorNightDisable = Color.White,
            modifier = Modifier
                .size(24.dp)
                .constrainAs(icClose) {
                    top.linkTo(parent.top, 16.dp)
                    start.linkTo(parent.start, 16.dp)
                }
                .noRippleClickable { onClose() }
        )
    }
}

private enum class VideoState {
    Unknown, Pause, Play
}
