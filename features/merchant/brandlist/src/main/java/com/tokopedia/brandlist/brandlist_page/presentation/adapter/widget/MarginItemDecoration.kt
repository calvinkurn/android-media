package com.tokopedia.brandlist.brandlist_page.presentation.adapter.widget

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.brandlist.brandlist_page.presentation.adapter.viewholder.AllBrandViewHolder
import com.tokopedia.brandlist.brandlist_search.presentation.adapter.viewholder.BrandlistSearchRecommendationViewHolder
import com.tokopedia.brandlist.brandlist_search.presentation.adapter.viewholder.BrandlistSearchResultViewHolder
import com.tokopedia.brandlist.brandlist_search.presentation.adapter.viewholder.BrandlistSearchShimmeringViewHolder

class MarginItemDecoration(private val marginSize: Int) : RecyclerView.ItemDecoration() {

    companion object {
        const val FIRST_COLUMN_INDEX = 0
        const val SECOND_COLUMN_INDEX = 1
        const val THIRD_COLUMN_INDEX = 2
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {

        val position = parent.getChildAdapterPosition(view)
        val adapter = parent.adapter

        adapter?.let {

            val viewType = it.getItemViewType(position)

            // All Brand
            if (viewType == AllBrandViewHolder.LAYOUT) {
                val layoutParams = view.layoutParams as GridLayoutManager.LayoutParams

                when (layoutParams.spanIndex) {
                    FIRST_COLUMN_INDEX -> {
                        with(outRect) {
                            left = marginSize
                        }
                    }
                    SECOND_COLUMN_INDEX -> {
                        with(outRect) {
                            left = marginSize / 2
                            right = marginSize / 2
                        }
                    }
                    THIRD_COLUMN_INDEX -> {
                        with(outRect) {
                            right = marginSize
                        }
                    }
                }
            }
        }
    }
}