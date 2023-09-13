package com.tokopedia.inbox.universalinbox.view.uimodel

data class UniversalInboxMenuUiModel (
    val type: MenuItemType,
    val title: String,
    val icon: Int,
    var counter: Int = 0,
    val applink: String,
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

enum class MenuItemType {
    CHAT_BUYER, CHAT_SELLER, DISCUSSION, REVIEW
}
