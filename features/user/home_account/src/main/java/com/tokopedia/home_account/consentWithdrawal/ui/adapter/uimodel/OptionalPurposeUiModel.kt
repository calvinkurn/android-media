package com.tokopedia.home_account.consentWithdrawal.ui.adapter.uimodel

import com.tokopedia.home_account.consentWithdrawal.data.ConsentPurposeItemDataModel

data class OptionalPurposeUiModel(
    var data: ConsentPurposeItemDataModel = ConsentPurposeItemDataModel()
) : ConsentWithdrawalUiModel
