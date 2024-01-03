package com.tokopedia.play.broadcaster.shorts.view.compose

import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ui.PlayerView
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.compose.NestIcon
import com.tokopedia.play.broadcaster.shorts.factory.PlayShortsMediaSourceFactory

/**
 * Created By : Jonathan Darwin on January 03, 2024
 */
@Composable
fun VideoPreviewLayout(
    videoUri: String,
    exoPlayer: ExoPlayer,
    mediaSourceFactory: PlayShortsMediaSourceFactory,
    onClose: () -> Unit,
) {
    ConstraintLayout {
        val (
            icClose,
            videoPreview
        ) = createRefs()

        LaunchedEffect(Unit) {
            val mediaSource = mediaSourceFactory.create(videoUri)
            exoPlayer.prepare(mediaSource)
            exoPlayer.playWhenReady = true
        }

        DisposableEffect(
            AndroidView(
                modifier = Modifier.constrainAs(videoPreview) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                },
                factory = {
                    PlayerView(it).apply {
                        player = exoPlayer
                        layoutParams = FrameLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                        useController = false
                    }
                }
            )
        ) {
            onDispose {
                exoPlayer.playWhenReady = false
                exoPlayer.release()
            }
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
                .clickable { onClose() }
        )
    }
}
