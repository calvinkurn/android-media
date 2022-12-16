package com.tokopedia.logisticseller.ui.findingnewdriver.uimodel

import com.tokopedia.logisticseller.data.model.FindingNewDriverModel

sealed class NewDriverAvailabilityState {
    data class Success(val data: FindingNewDriverModel) : NewDriverAvailabilityState()
    object Fail : NewDriverAvailabilityState()
    object Loading : NewDriverAvailabilityState()
}
