package com.tokopedia.tokofood.feature.ordertracking.domain.mapper

import com.tokopedia.tokofood.feature.ordertracking.domain.model.DriverPhoneNumberResponse
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.DriverPhoneNumberUiModel
import javax.inject.Inject

open class DriverPhoneNumberMapper @Inject constructor() {

    fun mapToDriverPhoneNumberUiModel(
        tokofoodDriverPhoneNumber: DriverPhoneNumberResponse.TokofoodDriverPhoneNumber
    ): DriverPhoneNumberUiModel {
        return DriverPhoneNumberUiModel(
            tokofoodDriverPhoneNumber.isCallable,
            tokofoodDriverPhoneNumber.phoneNumber.orEmpty()
        )
    }
}
