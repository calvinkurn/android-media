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
        var text: String = "",
        var link: String = ""
) : Parcelable