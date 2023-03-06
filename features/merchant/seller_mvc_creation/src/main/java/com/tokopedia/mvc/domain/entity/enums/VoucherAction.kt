package com.tokopedia.mvc.domain.entity.enums

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class VoucherAction : Parcelable {
    CREATE,
    UPDATE
}
