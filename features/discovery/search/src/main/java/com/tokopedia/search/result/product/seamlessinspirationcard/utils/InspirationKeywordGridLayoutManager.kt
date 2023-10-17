package com.tokopedia.search.result.product.seamlessinspirationcard.utils

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager

class InspirationKeywordGridLayoutManager(
    context: Context,
    sizeList: Int
) : GridLayoutManager(context, sizeList, VERTICAL, false) {
    private val spanSizeLookup = InspirationSeamlessSpanSizeLookup()

    init {
        setSpanSizeLookup(getSpanSizeLookup())
    }

    override fun setSpanSizeLookup(spanSizeLookup: SpanSizeLookup?) {
        super.setSpanSizeLookup(getSpanSizeLookup())
    }

    override fun getSpanSizeLookup(): SpanSizeLookup {
        return spanSizeLookup
    }

    private inner class InspirationSeamlessSpanSizeLookup : SpanSizeLookup() {
        override fun getSpanSize(position: Int): Int {
            return 1
        }
    }
}
