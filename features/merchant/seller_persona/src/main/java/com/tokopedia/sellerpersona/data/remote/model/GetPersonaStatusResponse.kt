package com.tokopedia.sellerpersona.data.remote.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.sellerpersona.view.model.PERSONA_STATUS_NOT_ROLLED_OUT

/**
 * Created by @ilhamsuaib on 30/01/23.
 */

data class GetPersonaStatusResponse(
    @SerializedName("GetSellerDashboardPageLayout")
    val data: PersonaStatusModel = PersonaStatusModel()
)

data class PersonaStatusModel(
    @SerializedName("persona") val persona: String = String.EMPTY,
    @SerializedName("personaStatus") val status: Int = PERSONA_STATUS_NOT_ROLLED_OUT
)