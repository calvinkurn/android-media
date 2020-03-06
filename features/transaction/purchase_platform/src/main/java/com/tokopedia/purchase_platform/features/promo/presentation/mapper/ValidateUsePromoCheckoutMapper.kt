package com.tokopedia.purchase_platform.features.promo.presentation.mapper

import com.tokopedia.purchase_platform.features.promo.presentation.uimodel.validate_use.AdditionalInfoUiModel
import com.tokopedia.purchase_platform.features.promo.data.response.validate_use.AdditionalInfo
import com.tokopedia.purchase_platform.features.promo.presentation.uimodel.validate_use.ErrorDetailUiModel
import com.tokopedia.purchase_platform.features.promo.presentation.uimodel.validate_use.MessageInfoUiModel

/**
 * Created by fwidjaja on 2020-03-05.
 */
class ValidateUsePromoCheckoutMapper {

    companion object {
        fun mapToAdditionalInfoUiModel(additionalInfo: AdditionalInfo) : AdditionalInfoUiModel {
            return AdditionalInfoUiModel(
                    messageInfoUiModel = MessageInfoUiModel(
                            message = additionalInfo.messageInfo?.message,
                            detail = additionalInfo.messageInfo?.detail
                    ) ,
                    errorDetailUiModel = ErrorDetailUiModel(
                            message = additionalInfo.errorDetail?.message
                    )
            )
        }
    }
}