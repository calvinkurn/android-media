package com.tokopedia.shop.home.view.adapter

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.shop.home.WidgetName.DISPLAY_DOUBLE_COLUMN
import com.tokopedia.shop.home.WidgetName.DISPLAY_SINGLE_COLUMN
import com.tokopedia.shop.home.WidgetName.DISPLAY_TRIPLE_COLUMN
import com.tokopedia.shop.home.WidgetName.SLIDER_SQUARE_BANNER
import com.tokopedia.unifycomponents.dpToPx
import com.tokopedia.unifycomponents.toPx

/**
 * Created by rizqiaryansa on 2020-02-24.
 */

class PaddingItemDecorationShopPage(private val typeWidget: String) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)

        when (typeWidget) {
            DISPLAY_SINGLE_COLUMN, DISPLAY_DOUBLE_COLUMN, DISPLAY_TRIPLE_COLUMN -> {
                val totalItemCount = parent.adapter?.itemCount.orZero()
                if (totalItemCount == 2) {
                    if (position == 0) {
                        outRect.right = 4f.dpToPx().toInt()
                    } else if (position == totalItemCount - 1) {
                        outRect.left = 4f.dpToPx().toInt()
                    }
                } else if (totalItemCount == 3) {
                    when (position) {
                        Int.ZERO -> {
                            outRect.right = 6f.dpToPx().toInt()
                        }
                        totalItemCount - 1 -> {
                            outRect.left = 6f.dpToPx().toInt()
                        }
                        else -> {
                            outRect.left = 3f.dpToPx().toInt()
                            outRect.right = 3f.dpToPx().toInt()
                        }
                    }
                }
            }
            SLIDER_SQUARE_BANNER -> {
                if (position > 0) {
                    parent.adapter.also {
                        outRect.left = 8.toPx()
                    }
                }
            }
        }
    }
}
