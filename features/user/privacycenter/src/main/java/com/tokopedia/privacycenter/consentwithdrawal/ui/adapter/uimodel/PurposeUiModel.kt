package com.tokopedia.privacycenter.consentwithdrawal.ui.adapter.uimodel

import com.tokopedia.privacycenter.consentwithdrawal.data.ConsentPurposeItemDataModel

data class PurposeUiModel(
    val isMandatoryPurpose: Boolean,
    val data: ConsentPurposeItemDataModel
) : ConsentWithdrawalUiModel
