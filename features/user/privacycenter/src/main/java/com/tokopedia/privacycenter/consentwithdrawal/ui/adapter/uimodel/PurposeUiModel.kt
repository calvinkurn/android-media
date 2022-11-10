package com.tokopedia.privacycenter.consentwithdrawal.ui.adapter.uimodel

import com.tokopedia.privacycenter.consentwithdrawal.data.ConsentPurposeItemDataModel

data class PurposeUiModel(
    var isMandatoryPurpose: Boolean,
    var data: ConsentPurposeItemDataModel
) : ConsentWithdrawalUiModel
