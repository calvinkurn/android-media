package com.tokopedia.broadcast.message.view.listener

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.broadcast.message.data.model.TopChatBlastSeller
import com.tokopedia.broadcast.message.common.data.model.TopChatBlastSellerMetaData

interface BroadcastMessageListView: CustomerView {
    fun onSuccessGetMetaData(metaData: TopChatBlastSellerMetaData?)
    fun onErrorGetMetaData(throwable: Throwable)
    fun onSuccessGetBlastMessage(response: TopChatBlastSeller.BlastSellerList?)
    fun onErrorGetBlastMessage(throwable: Throwable?)
}