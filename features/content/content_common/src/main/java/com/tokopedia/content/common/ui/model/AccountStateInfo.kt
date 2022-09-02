package com.tokopedia.content.common.ui.model

/**
 * Created by fachrizalmrsln on 31/08/22
 */
data class AccountStateInfo(
    val type: AccountStateInfoType,
    val selectedAccount: ContentAccountUiModel,
) {
    companion object {
        val Empty: AccountStateInfo
            get() = AccountStateInfo(
                type = AccountStateInfoType.Unknown,
                selectedAccount = ContentAccountUiModel.Empty,
            )
    }
}

sealed class AccountStateInfoType {
    object Live : AccountStateInfoType()
    object NoUsername : AccountStateInfoType()
    object NotAcceptTNC : AccountStateInfoType()
    object Banned : AccountStateInfoType()
    object Unknown : AccountStateInfoType()
}