package com.tokopedia.home_component.util

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home_component.viewholders.shorten.internal.ShortenVisitable
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.setLayoutHeight
import java.util.*

object ShortenUtils {

    // Max shorten container widget (side-to-side: 2 widgets)
    const val TWO_SQUARE_CONTAINER_LIMIT = 2

    // Max product item per container widget
    const val TWO_SQUARE_MAX_PRODUCT_LIMIT = 2

    // this is an intentional to make it global variables,
    // we can reduce a redundant calculation and set the view height
    // by using this [maxHeight] variable as validation.
    private var maxHeight = 0

    /**
     * Calculate at least there are 4 items in all its sub widget.
     *
     * This state maintains the UX of shorten widget,
     * hence we have to consist to set it with a proper widget.
     */
    fun List<ShortenVisitable>.isValidShortenWidget(): Boolean {
        var gridCount = 0

        for (element in this) gridCount += element.itemCount()

        return gridCount == (TWO_SQUARE_CONTAINER_LIMIT * TWO_SQUARE_MAX_PRODUCT_LIMIT)
    }

    /**
     * Because there's a chance height of each shorten widget are not equals,
     * thus we have to calculate which tallest widget and use the size for all sub widget.
     */
    fun calculateTallestItemAndSetForAllItems(recyclerView: RecyclerView?) {
        if (recyclerView?.isComputingLayout == true || maxHeight.isMoreThanZero()) return

        val childCount = recyclerView?.childCount ?: return
        val childViewRef = mutableListOf<View>()

        for (i in 0 until childCount) {
            val view = recyclerView.getChildAt(i) ?: return

            val currentSizeHeight = view.measuredHeight
            maxHeight = maxOf(maxHeight, currentSizeHeight)

            childViewRef.add(view)
        }

        childViewRef.forEach {
            it.setLayoutHeight(maxHeight)
            it.requestLayout()
        }
    }

    fun createFormatTargetDate(expiredTime: Date, serverTimeOffset: Long): Calendar {
        return Calendar.getInstance().apply {
            val currentDate = Date()
            val currentMillisecond: Long = currentDate.time + serverTimeOffset
            val timeDiff = expiredTime.time - currentMillisecond
            add(Calendar.SECOND, (timeDiff / 1000 % 60).toInt())
            add(Calendar.MINUTE, (timeDiff / (60 * 1000) % 60).toInt())
            add(Calendar.HOUR, (timeDiff / (60 * 60 * 1000)).toInt())
        }
    }
}
