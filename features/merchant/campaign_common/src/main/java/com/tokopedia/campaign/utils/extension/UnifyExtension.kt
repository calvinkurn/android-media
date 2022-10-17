package com.tokopedia.campaign.utils.extension

import com.tokopedia.campaign.R
import com.tokopedia.unifycomponents.UnifyButton

fun UnifyButton?.startLoading(onLoadingText : String = "") {
    this?.run {
        val text = onLoadingText.ifEmpty { context.getString(R.string.campaign_common_please_wait) }
        isLoading = true
        loadingText = text
        isClickable = false
    }
}

fun UnifyButton?.stopLoading() {
    this?.run {
        isLoading = false
        isClickable = true
    }
}