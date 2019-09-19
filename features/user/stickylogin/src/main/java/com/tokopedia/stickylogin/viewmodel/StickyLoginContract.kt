package com.tokopedia.stickylogin.viewmodel

import com.tokopedia.stickylogin.internal.StickyLoginConstant

interface StickyLoginContract {
    fun getContent(query: String, page: StickyLoginConstant.Page, onError: ((Throwable) -> Unit)?)
}