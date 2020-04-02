package com.tokopedia.productcard.test.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

internal fun withDrawable(@DrawableRes id: Int): Matcher<View?> {
    return DrawableMatcher(id)
}

private class DrawableMatcher (
        @DrawableRes private val id: Int
) : TypeSafeMatcher<View>() {

    private var context: Context? = null

    private var isNotImageView = false
    private var imageViewDoesNotHaveDrawable = false

    override fun describeTo(description: Description?) {
        when {
            isNotImageView -> description?.appendText("is not an image view")
            imageViewDoesNotHaveDrawable -> description?.appendText("does not have drawable")
            else -> description?.appendText("drawable is not ${getResourceName()}")
        }
    }

    private fun getResourceName(): String {
        return context?.resources?.getResourceEntryName(id) ?: ""
    }

    override fun matchesSafely(item: View?): Boolean {
        if (item !is ImageView) {
            isNotImageView = true
            return false
        }

        context = item.context

        val expectedDrawable = item.context.getDrawable(id) ?: return false
        val bitmap = getBitmap(item.drawable)
        val otherBitmap = getBitmap(expectedDrawable)

        return bitmap.sameAs(otherBitmap)
    }

    private fun getBitmap(drawable: Drawable): Bitmap {
        val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }
}