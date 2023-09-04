package com.tokopedia.filter.common.data

import android.os.Parcelable
import com.tokopedia.filter.common.helper.copyParcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SortModel(
    override val options: List<Sort>,
    override val title: String,
): OptionHolder, Parcelable {

    override val isColorFilter: Boolean
        get() = false

    override val isLocationFilter: Boolean
        get() = false

    override val search: Search
        get() = Search()

    override fun copy(): OptionHolder? = copyParcelable()
}

