package com.tokopedia.common_compose.components.loader

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.VectorComposable
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Created by yovi.putra on 15/05/23"
 * Project name: android-tokopedia-core
 **/

@Composable
internal fun NestInfiniteLoader(
    modifier: Modifier = Modifier,
    name: String,
    totalDuration: Long,
    vectorWidth: Float,
    vectorHeight: Float,
    updateState: () -> Unit,
    tintColor: Color = Color.Unspecified,
    lifecycleOwner: LifecycleOwner,
    content: @Composable @VectorComposable  (viewportWidth: Float, viewportHeight: Float) -> Unit
) {
    var isRunning by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()
    val vectorPainter = rememberVectorPainter(
        defaultWidth = vectorWidth.dp,
        defaultHeight = vectorHeight.dp,
        viewportWidth = vectorWidth,
        viewportHeight = vectorHeight,
        autoMirror = false,
        tintColor = tintColor,
        content = content
    )

    suspend fun runLoopingAnimation() {
        while (isRunning) {
            updateState() // update state to recompose path animation, start from begin
            delay(totalDuration) // waiting until animation finish
        }
    }

    DisposableEffect(key1 = Unit, effect = {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> { // run animation when on resume lifecycle
                    isRunning = true
                    scope.launch {
                        runLoopingAnimation()
                    }
                }
                Lifecycle.Event.ON_PAUSE -> { // stop animation when on pause lifecycle
                    isRunning = false
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
        modifier = modifier,
        painter = vectorPainter,
        contentDescription = name,
        contentScale = ContentScale.FillBounds
    )
}
