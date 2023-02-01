package com.tokopedia.sellerpersona.data.remote.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.extensions.view.EMPTY

/**
 * Created by @ilhamsuaib on 29/01/23.
 */

data class FetchPersonaListResponse(
    @SerializedName("fetchPersonaListData")
    val data: PersonaListDataModel = PersonaListDataModel()
)

data class PersonaListDataModel(
    @SerializedName("error") val error: Boolean = true,
    @SerializedName("errorMsg") val errorMsg: String = String.EMPTY,
    @SerializedName("data") val personaList: List<PersonaListModel> = listOf()
)

data class PersonaListModel(
    @SerializedName("name") val name: String = String.EMPTY,
    @SerializedName("header") val header: PersonaItemHeaderModel = PersonaItemHeaderModel(),
    @SerializedName("body") val body: PersonaItemBodyModel = PersonaItemBodyModel()
)

data class PersonaItemHeaderModel(
    @SerializedName("title") val title: String = String.EMPTY,
    @SerializedName("subtitle") val subtitle: String = String.EMPTY,
    @SerializedName("image") val image: String = String.EMPTY,
    @SerializedName("backgroundImage") val backgroundImage: String = String.EMPTY
)

data class PersonaItemBodyModel(
    @SerializedName("title") val title: String = String.EMPTY,
    @SerializedName("itemList") val itemList: List<String> = emptyList()
)