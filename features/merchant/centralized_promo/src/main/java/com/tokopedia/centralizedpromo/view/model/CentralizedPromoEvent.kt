package com.tokopedia.centralizedpromo.view.model

sealed class CentralizedPromoEvent {
    data class FilterUpdate(val selectedTabFilterData: Pair<String, String>) :
        CentralizedPromoEvent()

    data class UpdateRbacBottomSheet(val key: String) :
        CentralizedPromoEvent()

    data class CoachMarkShown(val key: String) :
        CentralizedPromoEvent()

    object LoadPromoCreation : CentralizedPromoEvent()

    object LoadOnGoingPromo : CentralizedPromoEvent()

    object SwipeRefresh : CentralizedPromoEvent()
}