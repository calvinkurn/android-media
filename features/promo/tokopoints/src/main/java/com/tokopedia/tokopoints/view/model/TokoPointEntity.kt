package com.tokopedia.tokopoints.view.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TokoPointEntity(
    @Expose
    @SerializedName("resultStatus")
    var resultStatus: ResultStatusEntity = ResultStatusEntity(),
    @Expose
    @SerializedName("status")
    var status: TokoPointStatusEntity = TokoPointStatusEntity(),
    @Expose
    @SerializedName("sheetHowToGet")
    var lobs: LobDetails = LobDetails(),
)
