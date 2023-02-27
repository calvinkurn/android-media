package com.tokopedia.mvc.domain.entity.enums

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class VoucherTargetBuyer(val id: Int): Parcelable  {
    ALL_BUYER(0),
    NEW_FOLLOWER(1),
    NEW_BUYER(2),
    MEMBER(3)
}
