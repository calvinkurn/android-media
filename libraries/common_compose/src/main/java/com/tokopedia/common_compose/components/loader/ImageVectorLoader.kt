package com.tokopedia.common_compose.components.loader

import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Created by yovi.putra on 31/03/23"
 * Project name: compose_skeleton
 **/

@OptIn(ExperimentalAnimationGraphicsApi::class)
@Composable
internal fun ImageVectorLoader(
    modifier: Modifier = Modifier,
    resourceId: Int,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
) {
    val image = AnimatedImageVector.animatedVectorResource(resourceId)
    var atEnd by remember { mutableStateOf(false) }
    // This state is necessary to control start/stop animation
    val isRunning = remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()
    // This function is called when the component is first launched
    // and lately when the button is pressed
    suspend fun runAnimation() {
        while (isRunning.value) {
            atEnd = !atEnd
            delay(image.totalDuration.toLong())
        }
    }

    val painter = rememberAnimatedVectorPainter(image, atEnd)
    val painterAnimated = rememberAnimatedVectorPainter(image, !atEnd)
    // Auto-play the animation when the component is displayed.
    DisposableEffect(key1 = image, effect = {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    isRunning.value = true
                    scope.launch {
                        runAnimation()
                    }
                }
                Lifecycle.Event.ON_PAUSE -> {
                    isRunning.value = false
                }
                else -> {
                    // no ops
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    })

    Image(
        painter = if (atEnd) painter else painterAnimated,
        contentDescription = null,
        modifier = modifier,
        contentScale = ContentScale.FillBounds
    )
}
