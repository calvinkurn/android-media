package com.tokopedia.inbox.universalinbox.view.uimodel

data class UniversalInboxMenuUiModel (
    val title: String,
    val icon: Int,
    val counter: Int = 0,
    val additionalInfo: Any? = null
) {

    fun getShopInfo(): UniversalInboxShopInfoUiModel? {
        return if (additionalInfo is UniversalInboxShopInfoUiModel) {
            additionalInfo
        } else {
            null
        }
    }
}
