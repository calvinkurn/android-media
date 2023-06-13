package com.tokopedia.sellerpersona.data.remote.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.extensions.view.EMPTY

/**
 * Created by @ilhamsuaib on 06/02/23.
 */

data class SetPersonaResponse(
    @SerializedName("setUserPersona")
    val setUserPersonaData: SetUserPersonaDataModel = SetUserPersonaDataModel()
)

data class SetUserPersonaDataModel(
    @SerializedName("persona") val persona: String = String.EMPTY,
    @SerializedName("error") val isError: Boolean = true,
    @SerializedName("errorMsg") val errorMsg: String = String.EMPTY
)