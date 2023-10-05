package com.tokopedia.buy_more_get_more.olp.data.mapper

import com.tokopedia.buy_more_get_more.olp.data.response.SharingDataByOfferIDResponse
import com.tokopedia.buy_more_get_more.olp.domain.entity.SharingDataByOfferIdUiModel
import javax.inject.Inject

class SharingDataByOfferIDMapper @Inject constructor() {

    fun map(response: SharingDataByOfferIDResponse): SharingDataByOfferIdUiModel {
        return SharingDataByOfferIdUiModel(
            responseHeader = response.sharingDataByOfferId.responseHeader.toResponseHeaderModel(),
            offerData = response.sharingDataByOfferId.offerData.toOfferDataModel()
        )
    }

    private fun SharingDataByOfferIDResponse.ResponseHeader.toResponseHeaderModel(): SharingDataByOfferIdUiModel.ResponseHeader {
        return SharingDataByOfferIdUiModel.ResponseHeader(
            success = success,
            errorCode = errorCode,
            processTime = processTime
        )
    }

    private fun SharingDataByOfferIDResponse.OfferData.toOfferDataModel(): SharingDataByOfferIdUiModel.OfferData {
        return SharingDataByOfferIdUiModel.OfferData(
            imageUrl = imageUrl,
            title = title,
            description = description,
            deeplink = deeplink,
            tag = tag,
            pageType = pageType,
            campaignName = campaignName
        )
    }
}
