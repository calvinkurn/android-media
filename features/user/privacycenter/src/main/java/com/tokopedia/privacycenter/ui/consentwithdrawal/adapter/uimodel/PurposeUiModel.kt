package com.tokopedia.privacycenter.ui.consentwithdrawal.adapter.uimodel

import com.tokopedia.privacycenter.data.ConsentPurposeItemDataModel

data class PurposeUiModel(
    val isMandatoryPurpose: Boolean,
    val data: ConsentPurposeItemDataModel
) : ConsentWithdrawalUiModel
