package com.tokopedia.shop.flashsale.domain.entity.enums

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class PageMode : Parcelable {
    CREATE,
    UPDATE,
    DRAFT
}
