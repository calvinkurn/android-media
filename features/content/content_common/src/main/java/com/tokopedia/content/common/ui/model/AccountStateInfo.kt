package com.tokopedia.content.common.ui.model

/**
 * Created by fachrizalmrsln on 31/08/22
 */
data class AccountStateInfo(
    val type: AccountStateInfoType = AccountStateInfoType.Unknown,
    val selectedAccount: ContentAccountUiModel = ContentAccountUiModel.Empty,
    val tnc: List<TermsAndConditionUiModel> = emptyList(),
)

enum class AccountStateInfoType {
    Live, NoUsername, NotAcceptTNC, Banned, Unknown
}