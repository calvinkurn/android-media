package com.tokopedia.product.detail.postatc.base

import androidx.recyclerview.widget.StaggeredGridLayoutManager

class PostAtcLayoutManager : StaggeredGridLayoutManager(SPAN_COUNT, ORIENTATION) {
    companion object {
        private const val SPAN_COUNT = 1
        private const val ORIENTATION = VERTICAL
    }

    // Solve Fatal Exception: java.lang.IndexOutOfBoundsException: Inconsistency detected
    // https://stackoverflow.com/a/33985508/8868638
    override fun supportsPredictiveItemAnimations(): Boolean {
        return false
    }

}
