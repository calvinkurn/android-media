package com.tokopedia.sellerpersona.data.remote.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.extensions.view.EMPTY

/**
 * Created by @ilhamsuaib on 30/01/23.
 */

data class GetPersonaStatusResponse(
    @SerializedName("GetSellerDashboardPageLayout")
    val data: PersonaStatusModel = PersonaStatusModel()
)

data class PersonaStatusModel(
    @SerializedName("persona")
    val persona: String = String.EMPTY,
    @SerializedName("persona")
    val personaStatus: String = String.EMPTY,
)