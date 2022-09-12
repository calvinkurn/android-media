package com.tokopedia.tkpd.flashsale.common.extension

import androidx.core.widget.NestedScrollView
import com.tokopedia.campaign.delegates.HasPaginatedList
import com.tokopedia.kotlin.extensions.view.ZERO

fun NestedScrollView.enablePaging(loadNextPage: () -> Unit) {
    this.apply {
        viewTreeObserver.addOnScrollChangedListener {
            val scrollState: Int =
                this.getChildAt(this.childCount.dec()).bottom - (this.height + this.scrollY)
            if (scrollState == Int.ZERO) {
                loadNextPage()
            }
        }
    }
}