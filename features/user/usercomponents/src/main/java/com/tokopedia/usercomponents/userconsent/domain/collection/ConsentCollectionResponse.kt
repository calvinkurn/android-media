package com.tokopedia.usercomponents.userconsent.domain.collection

import com.google.gson.annotations.SerializedName
import com.tokopedia.usercomponents.userconsent.common.UserConsentCollectionDataModel

data class ConsentCollectionResponse(
    @SerializedName("GetCollectionPoint", alternate = ["GetCollectionPointWithConsent"])
    var data: UserConsentCollectionDataModel = UserConsentCollectionDataModel()
)
