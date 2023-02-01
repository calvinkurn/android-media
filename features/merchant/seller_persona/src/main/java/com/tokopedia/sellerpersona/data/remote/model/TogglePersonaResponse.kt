package com.tokopedia.sellerpersona.data.remote.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.extensions.view.EMPTY

/**
 * Created by @ilhamsuaib on 01/02/23.
 */

data class TogglePersonaResponse(
    @SerializedName("toggleUserPersona")
    val toggleUserPersona: TogglePersonaModel = TogglePersonaModel()
)

data class TogglePersonaModel(
    @SerializedName("error")
    val isError: Boolean = false,
    @SerializedName("errorMsg")
    val errorMsg: String = String.EMPTY
)