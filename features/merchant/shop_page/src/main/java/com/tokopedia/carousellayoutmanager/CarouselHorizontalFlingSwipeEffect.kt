package com.tokopedia.carousellayoutmanager

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.orZero

class CarouselHorizontalFlingSwipeEffect(
    private val recyclerView: RecyclerView,
    private val carouselLayoutManager: CarouselLayoutManager,
    private val itemCount: Int,
    private val currentSelectedItemPositionWhenUserTouchItem: () -> Int
): RecyclerView.OnFlingListener() {

    companion object{
        private const val VELOCITY_X_THRESHOLD = 1000
    }

    override fun onFling(velocityX: Int, velocityY: Int): Boolean {
        if (kotlin.math.abs(velocityX) > VELOCITY_X_THRESHOLD) {
            val currentSelectedItem = carouselLayoutManager.centerItemPosition.orZero()
            var cur = currentSelectedItem
            if (velocityX > Int.ZERO) {
                ++cur
            } else {
                --cur
            }
            if (cur >= itemCount) {
                cur = Int.ZERO
            } else if (cur < Int.ZERO) {
                cur = itemCount - 1
            }
            if (currentSelectedItemPositionWhenUserTouchItem.invoke() != currentSelectedItem) {
                recyclerView.smoothScrollToPosition(currentSelectedItem)
            } else {
                recyclerView.smoothScrollToPosition(cur)
            }
            return true
        }
        return false
    }

}
