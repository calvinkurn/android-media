package com.tokopedia.cart.domain.model.updatecart

import android.os.Parcelable
import com.tokopedia.cart.domain.model.cartlist.ButtonData
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PromptPageData(
        var image: String = "",
        var title: String = "",
        var descriptions: String = "",
        var buttons: List<ButtonData> = emptyList()
) : Parcelable