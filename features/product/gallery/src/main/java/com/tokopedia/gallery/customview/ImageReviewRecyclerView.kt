package com.tokopedia.gallery.customview

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet

import com.tokopedia.abstraction.base.view.recyclerview.VerticalRecyclerView
import com.tokopedia.abstraction.base.view.widget.DividerItemDecoration
import com.tokopedia.gallery.R
import com.tokopedia.gallery.adapter.viewholder.GalleryItemViewHolder

class ImageReviewRecyclerView : RecyclerView {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {
        init()
    }

    private fun init() {
        val gridLayoutManager = GridLayoutManager(context, SPAN_COUNT)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (adapter.getItemViewType(position) == GalleryItemViewHolder.LAYOUT) {
                    1
                } else {
                    SPAN_COUNT
                }
            }
        }
        this.layoutManager = gridLayoutManager
        this.addItemDecoration(GalleryItemDecoration(resources.getDimensionPixelSize(R.dimen.gallery_item_spacing)))
    }

    companion object {
        private val SPAN_COUNT = 3
    }
}
