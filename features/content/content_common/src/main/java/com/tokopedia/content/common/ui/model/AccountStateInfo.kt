package com.tokopedia.content.common.ui.model

/**
 * Created by fachrizalmrsln on 31/08/22
 */
data class AccountStateInfo(
    val type: AccountStateInfoType = AccountStateInfoType.Unknown,
    val selectedAccount: ContentAccountUiModel = ContentAccountUiModel.Empty,
    val tnc: List<TermsAndConditionUiModel> = emptyList(),
)

sealed class AccountStateInfoType {
    object Live : AccountStateInfoType()
    object NoUsername : AccountStateInfoType()
    object NotAcceptTNC : AccountStateInfoType()
    object Banned : AccountStateInfoType()
    object Unknown : AccountStateInfoType()
}