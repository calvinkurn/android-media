package com.tokopedia.topads.keyword.view.listener

import com.tokopedia.abstraction.base.view.listener.CustomerView

interface TopAdsKeywordNewAddView: CustomerView {
    fun onSuccessSaveKeyword()
    fun onFailedSaveKeyword(e: Throwable)
}