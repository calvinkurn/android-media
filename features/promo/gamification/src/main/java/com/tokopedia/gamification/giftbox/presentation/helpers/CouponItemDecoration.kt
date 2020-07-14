package com.tokopedia.gamification.giftbox.presentation.helpers

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class CouponItemDecoration(val isTablet: Boolean = false,
                           val listItemWidthInTablet: Int,
                           val screenWidth: Int,
                           var topSpace:Int,
                           var rightSpace:Int
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {

        val adapter = parent.adapter
        if (adapter != null) {

            if (isTablet) {
                if (adapter.itemCount == 1) {
                    if (parent.layoutManager?.getPosition(view) == 0) {
                        outRect.left = screenWidth/2 - listItemWidthInTablet/2
                    }
                } else if (adapter.itemCount > 1) {
                    if (parent.layoutManager?.getPosition(view) == 0) {
                        outRect.left = screenWidth/2 - listItemWidthInTablet/2
                    }
                    outRect.right = rightSpace
                }
            } else {

                if (adapter.itemCount == 1) {
                    if (parent.layoutManager?.getPosition(view) == 0) {
                        outRect.left = topSpace
                        outRect.right = topSpace
                    }
                } else if (adapter.itemCount > 1) {
                    if (parent.layoutManager?.getPosition(view) == 0) {
                        outRect.left = topSpace
                    }
                    outRect.right = rightSpace
                }
            }

        }

    }
}