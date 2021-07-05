package com.tokopedia.topchat.assertion

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

class DrawableMatcher(private val resourceId: Int) : TypeSafeMatcher<View?>() {
    override fun describeTo(description: Description?) {
        description?.appendText("with drawable from resource id: ")
        description?.appendValue(resourceId)
    }

    override fun matchesSafely(item: View?): Boolean {
        if(item !is ImageView) return false
        if (resourceId < 0) {
            return false
        }

        val resourceBitmap = getBitmap(item.background)
        val expectedDrawable: Drawable = item.context.getDrawable(resourceId) ?: return false
        val expectedBitmap = getBitmap(expectedDrawable)

        return expectedBitmap.sameAs(resourceBitmap)
    }

    private fun getBitmap(drawable: Drawable): Bitmap {
        var width = drawable.intrinsicWidth
        var height = drawable.intrinsicHeight
        if( width <= 0) width = DEFAULT_SIZE
        if(height <= 0) height = DEFAULT_SIZE

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

    companion object {
        private const val DEFAULT_SIZE = 300
        /**
         * How to use:
         * DrawableMatcher.compareDrawable(R.id.image_view, R.drawable.image)
         */
        fun compareDrawable(viewId: Int, resourceId: Int) : ViewInteraction? {
            return onView(withId(viewId)).check(matches(DrawableMatcher(resourceId)))
        }
    }
}