package com.tokopedia.inbox.universalinbox.view.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.inbox.universalinbox.view.adapter.typefactory.UniversalInboxTypeFactory

data class UniversalInboxMenuUiModel(
    val type: MenuItemType,
    val title: String,
    val icon: Int,
    var counter: Int = 0,
    val applink: String,
    val label: UniversalInboxMenuLabel,
    val additionalInfo: Any? = null
) : Visitable<UniversalInboxTypeFactory> {

    override fun type(typeFactory: UniversalInboxTypeFactory): Int {
        return typeFactory.type(this)
    }

    fun getShopInfo(): UniversalInboxShopInfoUiModel? {
        return if (additionalInfo is UniversalInboxShopInfoUiModel) {
            additionalInfo
        } else {
            null
        }
    }

    companion object {
        fun areItemsTheSame(
            oldItem: UniversalInboxMenuUiModel,
            newItem: UniversalInboxMenuUiModel
        ): Boolean {
            return oldItem.title == newItem.title
        }

        fun areContentsTheSame(
            oldItem: UniversalInboxMenuUiModel,
            newItem: UniversalInboxMenuUiModel
        ): Boolean {
            return (
                oldItem.type == newItem.type &&
                    oldItem.applink == newItem.applink &&
                    oldItem.counter == newItem.counter &&
                    oldItem.icon == newItem.counter &&
                    oldItem.label == newItem.label &&
                    oldItem.additionalInfo == newItem.additionalInfo
                )
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
