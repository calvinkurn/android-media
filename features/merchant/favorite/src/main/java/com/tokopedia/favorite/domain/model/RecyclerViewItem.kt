package com.tokopedia.favorite.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
open class RecyclerViewItem : Serializable, Parcelable {

    override fun describeContents(): Int {
        return 0
    }

}
