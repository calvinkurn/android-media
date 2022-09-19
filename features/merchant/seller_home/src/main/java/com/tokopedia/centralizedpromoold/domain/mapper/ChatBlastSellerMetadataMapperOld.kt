package com.tokopedia.centralizedpromoold.domain.mapper

import com.tokopedia.centralizedpromoold.domain.model.ChatBlastSellerMetadataResponse
import com.tokopedia.centralizedpromoold.view.model.ChatBlastSellerMetadataUiModel
import com.tokopedia.kotlin.extensions.view.toZeroIfNull
import javax.inject.Inject

class ChatBlastSellerMetadataMapperOld @Inject constructor() {
    fun mapDomainDataModelToUiDataModel(response: ChatBlastSellerMetadataResponse?): ChatBlastSellerMetadataUiModel {
        return response?.chatBlastSellerMetadata?.let {
            ChatBlastSellerMetadataUiModel(it.promo.toZeroIfNull(), it.promoType.toZeroIfNull(), it.url.orEmpty())
        } ?: ChatBlastSellerMetadataUiModel()
    }
}