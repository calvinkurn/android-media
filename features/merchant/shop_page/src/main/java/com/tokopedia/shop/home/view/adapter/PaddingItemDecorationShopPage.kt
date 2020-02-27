package com.tokopedia.shop.home.view.adapter

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.shop.home.WidgetMultipleImageColumn
import com.tokopedia.shop.home.WidgetSliderSquareBanner

/**
 * Created by rizqiaryansa on 2020-02-24.
 */

class PaddingItemDecorationShopPage(private val typeWidget: String): RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)

        when(typeWidget) {
            WidgetMultipleImageColumn -> {
                if(position > 0) {
                    parent.adapter.also {
                        outRect.left = 3
                    }
                }
            }
            WidgetSliderSquareBanner -> {
                if(position > 0) {
                    parent.adapter.also {
                        outRect.left = 8
                    }
                }
            }
        }

    }
}
