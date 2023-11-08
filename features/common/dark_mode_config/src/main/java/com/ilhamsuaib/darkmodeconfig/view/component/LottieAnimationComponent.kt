package com.ilhamsuaib.darkmodeconfig.view.component

import android.animation.ValueAnimator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.airbnb.lottie.LottieAnimationView

/**
 * Created by @ilhamsuaib on 07/11/23.
 */

@Composable
internal fun LottieAnimationComponent(
    source: String,
    repeatCount: Int = ValueAnimator.INFINITE,
    modifier: Modifier
) {
    AndroidView(
        modifier = modifier,
        factory = {
            LottieAnimationView(it).apply {
                setFailureListener {
                    // we need to set our own failure listener to avoid crash
                    // especially for xiaomi android 8
                }
            }
        },
        update = {
            it.repeatCount = repeatCount
            it.setAnimationFromUrl(source)
            it.playAnimation()
        }
    )
}