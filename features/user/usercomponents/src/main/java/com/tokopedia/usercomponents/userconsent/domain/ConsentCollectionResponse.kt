package com.tokopedia.usercomponents.userconsent.domain

import com.google.gson.annotations.SerializedName
import com.tokopedia.usercomponents.userconsent.common.ConsentCollectionDataModel

data class ConsentCollectionResponse(
    @SerializedName("GetCollectionPoint")
    var data: ConsentCollectionDataModel = ConsentCollectionDataModel()
)