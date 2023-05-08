package com.tokopedia.purchase_platform.common.feature.gifting.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PopUpData(
    var button: ButtonData = ButtonData(),
    var description: String = "",
    var title: String = ""
) : Parcelable {

    fun isNeedToShowPopUp(): Boolean {
        return title.isNotBlank() && description.isNotBlank()
    }
}
