package com.tokopedia.centralizedpromo.view.model

sealed class CentralizedPromoEvent {
    data class FilterUpdate(val selectedTabFilterData: Pair<String, String>) :
        CentralizedPromoEvent()

    data class UpdateRbacBottomSheet(val key: String) :
        CentralizedPromoEvent()

    data class UpdateToasterState(val showToaster: Boolean) :
        CentralizedPromoEvent()

    object SwipeRefresh : CentralizedPromoEvent()
}