package com.tokopedia.checkout.domain.model.checkout

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Prompt(
        var eligible: Boolean = false,
        var title: String = "",
        var description: String = "",
        var button: PromptButton = PromptButton()
) : Parcelable

@Parcelize
data class PromptButton(
        @SerializedName("text")
        var text: String = "",
        @SerializedName("link")
        var link: String = ""
) : Parcelable