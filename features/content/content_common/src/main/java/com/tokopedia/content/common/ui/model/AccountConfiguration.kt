package com.tokopedia.content.common.ui.model

/**
 * Created by fachrizalmrsln on 31/08/22
 */
data class AccountConfiguration(
    val type: AccountConfigurationType,
    val selectedAccount: ContentAccountUiModel,
) {
    companion object {
        val Empty: AccountConfiguration
            get() = AccountConfiguration(
                type = AccountConfigurationType.Unknown,
                selectedAccount = ContentAccountUiModel.Empty,
            )
    }
}

sealed class AccountConfigurationType {
    object Live : AccountConfigurationType()
    object NoUsername : AccountConfigurationType()
    object NotAcceptTNC : AccountConfigurationType()
    object Banned : AccountConfigurationType()
    object Unknown : AccountConfigurationType()
}