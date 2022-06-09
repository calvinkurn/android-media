package com.tokopedia.shop.flashsale.common.extension

import com.tokopedia.seller_shop_flash_sale.R
import com.tokopedia.unifycomponents.UnifyButton

fun UnifyButton?.showLoading() {
    this?.run {
        isLoading = true
        loadingText = this.context.getString(R.string.sfs_please_wait)
    }
}

fun UnifyButton?.stopLoading() {
    this?.run {
        isLoading = false
    }
}