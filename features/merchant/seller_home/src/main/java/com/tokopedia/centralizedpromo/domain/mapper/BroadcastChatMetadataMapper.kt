package com.tokopedia.centralizedpromo.domain.mapper

import com.tokopedia.centralizedpromo.domain.model.ChatBlastSellerMetadataResponse
import com.tokopedia.centralizedpromo.view.model.ChatBlastSellerMetadataUiModel
import com.tokopedia.kotlin.extensions.view.toZeroIfNull
import javax.inject.Inject

class BroadcastChatMetadataMapper @Inject constructor() {
    fun mapDomainDataModelToUiDataModel(response: ChatBlastSellerMetadataResponse?): ChatBlastSellerMetadataUiModel {
        return response?.chatBlastSellerMetadata?.let {
            ChatBlastSellerMetadataUiModel(it.promo.toZeroIfNull(), it.promoType.toZeroIfNull())
        } ?: ChatBlastSellerMetadataUiModel()
    }
}