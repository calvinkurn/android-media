package com.tokopedia.gamification.giftbox.presentation.helpers

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.gamification.giftbox.presentation.adapter.RewardSummaryAdapter

class RewardItemDecoration(val smallSpace: Int, val mediumSpace: Int, val largeSpace: Int) : RecyclerView.ItemDecoration() {

    var indexTillBigPrize = -1

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.layoutManager?.getPosition(view)
        if (position != null) {
            if (parent.adapter is RewardSummaryAdapter) {
                val item = (parent.adapter as RewardSummaryAdapter).dataList[position]
                if (item.couponDetail != null) {
                    if (indexTillBigPrize != -1) {
                        if (position < indexTillBigPrize) {
                            outRect.bottom = largeSpace
                        } else {
                            outRect.bottom = mediumSpace
                        }
                    }
                } else {
                    outRect.bottom = smallSpace
                }
            }

        }
    }
}