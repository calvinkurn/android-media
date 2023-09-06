package com.tokopedia.inbox.universalinbox.view.uimodel

data class UniversalInboxMenuUiModel(
    val type: MenuItemType,
    val title: String,
    val icon: Int,
    var counter: Int = 0,
    val applink: String,
    val label: UniversalInboxMenuLabel,
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

data class UniversalInboxMenuLabel(
    val color: String = "",
    val text: String = ""
)

enum class MenuItemType(val counterType: String) {
    CHAT_BUYER("unreadsUser"),
    CHAT_SELLER("unreadsSeller"),
    DISCUSSION("talk"),
    REVIEW("review"),
    OTHER("")
}
