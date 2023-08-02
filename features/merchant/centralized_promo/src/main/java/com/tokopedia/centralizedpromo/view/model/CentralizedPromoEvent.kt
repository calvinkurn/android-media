package com.tokopedia.centralizedpromo.view.model

sealed class CentralizedPromoEvent {
    data class FilterUpdate(val selectedTabFilterData: Pair<String, String>) :
        CentralizedPromoEvent()

    data class UpdateRbacBottomSheet(val key: String) :
        CentralizedPromoEvent()

    object SwipeRefresh : CentralizedPromoEvent()
}