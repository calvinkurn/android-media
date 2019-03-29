package com.tokopedia.broadcast.message.view.listener

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.broadcast.message.data.model.BlastMessageResponse

interface BroadcastMessagePreviewView: CustomerView {
    fun showLoading()
    fun hideLoading()
    fun onErrorSubmitBlastMessage(t: Throwable?)
    fun onSuccessSubmitBlastMessage(result: BlastMessageResponse)
}