package com.tokopedia.promousage.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PromoItemCta(
    val type: String = "",
    val text: String = "",
    val appLink: String = ""
) : Parcelable {
    companion object {
        const val TYPE_REGISTER_GOPAY_LATER_CICIL = "register_gpl_cicil"
    }
}
