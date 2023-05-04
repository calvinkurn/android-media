package com.tokopedia.gifting.domain.model

import com.google.gson.annotations.SerializedName

class Metadata {
    @SerializedName("Pictures")
    var pictures: List<Picture> = emptyList()

    @SerializedName("NotesTemplate")
    var notesTemplate  : String = ""

    @SerializedName("Description")
    var description    : String = ""

    @SerializedName("CustomInfoJSON")
    var customInfoJSON : String = ""
}
