package com.tokopedia.sellerorder.list.domain.mapper

import com.tokopedia.sellerorder.list.domain.model.SomListHeaderIconsInfoResponse
import com.tokopedia.sellerorder.list.presentation.models.PlusIconInfo
import com.tokopedia.sellerorder.list.presentation.models.SomListHeaderIconsInfoUiModel
import com.tokopedia.sellerorder.list.presentation.models.WaitingPaymentCounter
import javax.inject.Inject

class HeaderIconsInfoMapper @Inject constructor() {
    fun mapResponseToUiModel(orderFilterSom: SomListHeaderIconsInfoResponse.Data.OrderFilterSom): SomListHeaderIconsInfoUiModel {
        return SomListHeaderIconsInfoUiModel(
            waitingPaymentCounter = WaitingPaymentCounter(
                orderFilterSom.waitingPaymentCounter.amount,
                orderFilterSom.waitingPaymentCounter.text
            ),
            plusIconInfo = if (orderFilterSom.sellerInfo?.plusLogo?.isNotBlank() == true && orderFilterSom.sellerInfo.eduUrl?.isNotBlank() == true) {
                PlusIconInfo(
                    logoUrl = orderFilterSom.sellerInfo.plusLogo,
                    eduUrl = orderFilterSom.sellerInfo.eduUrl
                )
            } else {
                null
            }
        )
    }
}