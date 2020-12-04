package com.tokopedia.product.manage.common.feature.broadcastchat.domain

import com.tokopedia.product.manage.common.feature.broadcastchat.data.model.ChatBlastSellerMetaDataResponse
import com.tokopedia.product.manage.common.feature.broadcastchat.presentation.model.ChatBlastSellerEntryPointUiModel
import javax.inject.Inject

class ChatBlastSellerMetaDataMapper @Inject constructor() {

    fun mapToChatBlastSellerMetaData(data: ChatBlastSellerMetaDataResponse.ChatBlastSellerMetadata): ChatBlastSellerEntryPointUiModel {
        return ChatBlastSellerEntryPointUiModel(data.url.orEmpty())
    }
}