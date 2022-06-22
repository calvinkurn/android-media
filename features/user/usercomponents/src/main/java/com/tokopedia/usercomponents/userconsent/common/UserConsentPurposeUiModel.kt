package com.tokopedia.usercomponents.userconsent.common

import com.tokopedia.usercomponents.userconsent.common.UserConsentCollectionDataModel.CollectionPointDataModel.PurposeDataModel

data class UserConsentPurposeUiModel(
    var purposes: PurposeDataModel = PurposeDataModel()
)