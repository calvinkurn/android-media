package com.tokopedia.review.feature.inbox.buyerreview.view.widgets

import android.view.View

class ShareItem constructor(
    private val icon: String,
    var name: String,
    var onClickListener: View.OnClickListener
) {
    fun getIcon(): String {
        return icon
    }
}