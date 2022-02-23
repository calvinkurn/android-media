package com.tokopedia.gifting.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Metadata {
    @SerializedName("Pictures")
    @Expose
    var pictures: List<Picture>? = null

    @SerializedName("NotesTemplate")
    @Expose
    var notesTemplate: String? = null
}