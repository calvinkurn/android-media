/*
 * Copyright 2014 Google Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tokopedia.broadcaster.revamp.util.view

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import kotlin.math.abs

/**
 * Layout that adjusts to maintain a specific aspect ratio.
 */
class AspectFrameLayout : FrameLayout {

    companion object {
        const val DEFAULT_RATIO_WINDOW_SIZE = 0.5625
    }

    private var mTargetAspect = DEFAULT_RATIO_WINDOW_SIZE // initially use default window size

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    /**
     * Sets the desired aspect ratio.  The value is `width / height`.
     */
    @Suppress("MagicNumber")
    fun setAspectRatio(aspectRatio: Double) {
        if (aspectRatio < 0) {
            mTargetAspect = DEFAULT_RATIO_WINDOW_SIZE
        }
        if (mTargetAspect != aspectRatio) {
            mTargetAspect = aspectRatio
            requestLayout()
        }
    }

    @Suppress("MagicNumber")
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // Target aspect ratio will be < 0 if it hasn't been set yet.  In that case,
        // we just use whatever we've been handed.
        var mWidthMeasureSpec = widthMeasureSpec
        var mHeightMeasureSpec = heightMeasureSpec
        if (mTargetAspect > 0) {
            var initialWidth = MeasureSpec.getSize(widthMeasureSpec)
            var initialHeight = MeasureSpec.getSize(heightMeasureSpec)

            // factor the padding out
            val horizontalPadding = paddingLeft + paddingRight
            val vertPadding = paddingTop + paddingBottom
            initialWidth -= horizontalPadding
            initialHeight -= vertPadding
            val viewAspectRatio = initialWidth.toDouble() / initialHeight
            val aspectDiff = mTargetAspect / viewAspectRatio - 1
            if (abs(aspectDiff) < 0.01) {
                // We're very close already.  We don't want to risk switching from e.g. non-scaled
                // 1280x720 to scaled 1280x719 because of some floating-point round-off error,
                // so if we're really close just leave it alone.
            } else {
                if (aspectDiff > 0) {
                    // limited by narrow width; restrict height
                    initialHeight = (initialWidth / mTargetAspect).toInt()
                } else {
                    // limited by short height; restrict width
                    initialWidth = (initialHeight * mTargetAspect).toInt()
                }
                initialWidth += horizontalPadding
                initialHeight += vertPadding
                mWidthMeasureSpec = MeasureSpec.makeMeasureSpec(initialWidth, MeasureSpec.EXACTLY)
                mHeightMeasureSpec = MeasureSpec.makeMeasureSpec(initialHeight, MeasureSpec.EXACTLY)
            }
        }

        super.onMeasure(mWidthMeasureSpec, mHeightMeasureSpec)
    }
}
