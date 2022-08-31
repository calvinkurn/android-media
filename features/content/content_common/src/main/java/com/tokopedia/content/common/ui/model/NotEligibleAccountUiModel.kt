package com.tokopedia.content.common.ui.model

/**
 * Created by fachrizalmrsln on 31/08/22
 */
data class NotEligibleAccountUiModel(
    val type: NotEligibleType,
    val selectedAccount: ContentAccountUiModel,
) {
    companion object {
        val Empty: NotEligibleAccountUiModel
            get() = NotEligibleAccountUiModel(
                type = NotEligibleType.Unknown,
                selectedAccount = ContentAccountUiModel.Empty,
            )
    }
}

sealed class NotEligibleType {
    object NoUsername : NotEligibleType()
    object Banned : NotEligibleType()
    object Unknown : NotEligibleType()
}