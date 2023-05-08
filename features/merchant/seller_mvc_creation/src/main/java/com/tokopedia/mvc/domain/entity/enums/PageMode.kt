package com.tokopedia.mvc.domain.entity.enums

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class PageMode : Parcelable {
    CREATE,
    EDIT,
    DUPLICATE
}
