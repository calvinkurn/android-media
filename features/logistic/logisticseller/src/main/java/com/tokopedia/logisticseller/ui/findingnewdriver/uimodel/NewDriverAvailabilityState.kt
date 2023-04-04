package com.tokopedia.logisticseller.ui.findingnewdriver.uimodel

import com.tokopedia.logisticseller.data.model.FindingNewDriverModel

sealed class NewDriverAvailabilityState {
    data class Success(val data: FindingNewDriverModel) : NewDriverAvailabilityState()
    data class Fail(val errorMessage: String?) : NewDriverAvailabilityState()
    object Loading : NewDriverAvailabilityState()
}
