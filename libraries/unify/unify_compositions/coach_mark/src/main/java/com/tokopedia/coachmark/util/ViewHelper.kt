/*
 * Copyright 2019 Tokopedia
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Created by Hendy on 4/13/2017.
 * Edited by Meyta
 */
package com.tokopedia.coachmark.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.ShapeDrawable
import android.view.View
import android.view.ViewParent

object ViewHelper {

    fun getRelativePositionRec(myView: View, root: ViewParent, location: IntArray) {
        if (myView.parent === root) {
            location[0] += myView.left
            location[1] += myView.top
        } else {
            location[0] += myView.left
            location[1] += myView.top
            getRelativePositionRec(myView.parent as View, root, location)
        }
    }

    fun setBackgroundColor(v: View, color: Int) {
        val background = v.background
        if (background is ShapeDrawable) {
            background.paint.color = color
        } else if (background is GradientDrawable) {
            background.setColor(color)
        } else {
            v.setBackgroundColor(color)
        }
    }

    fun getCroppedBitmap(bitmap: Bitmap, centerLocation: IntArray, radius: Int): Bitmap {
        val output = Bitmap.createBitmap(2 * radius,
                2 * radius, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)

        val color = -0xbdbdbe
        val paint = Paint()

        val sourceRect = Rect(centerLocation[0] - radius,
                centerLocation[1] - radius,
                centerLocation[0] + radius,
                centerLocation[1] + radius)
        val destRect = Rect(0, 0, 2 * radius, 2 * radius)

        paint.isAntiAlias = true
        canvas.drawARGB(0, 0, 0, 0)
        paint.color = color
        canvas.drawCircle(radius.toFloat(), radius.toFloat(),
                radius.toFloat(), paint)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, sourceRect, destRect, paint)
        return output
    }

    fun getCroppedBitmap(bitmap: Bitmap, rectLocation: IntArray): Bitmap {
        val xStart = rectLocation[0]
        val yStart = rectLocation[1]
        val xEnd = rectLocation[2]
        val yEnd = rectLocation[3]
        val width = xEnd - xStart
        val height = yEnd - yStart

        val output = Bitmap.createBitmap(width,
                height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)

        val color = -0xbdbdbe
        val paint = Paint()

        val sourceRect = Rect(xStart,
                yStart,
                xEnd,
                yEnd)
        val destRect = Rect(0, 0, width, height)

        paint.isAntiAlias = true
        canvas.drawARGB(0, 0, 0, 0)
        paint.color = color
        canvas.drawRect(destRect, paint)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, sourceRect, destRect, paint)
        return output
    }

    fun getStatusBarHeight(context: Context): Int {
        var height = 0
        val resId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resId > 0) {
            height = context.resources.getDimensionPixelSize(resId)
        }
        return height
    }
}
