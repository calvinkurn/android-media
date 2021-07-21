package com.tokopedia.moneyin.viewmodel.liveState

sealed class MoneyInCheckoutState
data class ScheduleTimeError(val errMsg: String) : MoneyInCheckoutState()
data class CourierPriceError(val errMsg: String) : MoneyInCheckoutState()
data class MutationCheckoutError(val errMsg: String) : MoneyInCheckoutState()
