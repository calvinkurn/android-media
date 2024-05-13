package com.tokopedia.autocompletecomponent.unify.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.topads.sdk.domain.model.CpmModel

data class UniverseSuggestionUnifyModel(
    @SerializedName("data")
    @Expose
    val `data`: List<SuggestionUnify> = listOf(),

    @SerializedName("flags")
    @Expose
    val flags: List<SuggestionUnifyFlag> = listOf(),

    @SerializedName("header")
    @Expose
    val header: SuggestionUnifyHeader = SuggestionUnifyHeader(),

    @SerializedName("headlineAds")
    @Expose
    var cpmModel: CpmModel = CpmModel()
) {

    fun requestId(): String = getFlags(REQUEST_ID)

    fun wordsSource(): String = getFlags(WORDS_SOURCE)

    fun getFlags(name: String) = flags.find { it.enable && it.name == name }?.value ?: ""

    companion object {
        private const val REQUEST_ID = "request_id"
        private const val WORDS_SOURCE = "words_source"
    }
}
