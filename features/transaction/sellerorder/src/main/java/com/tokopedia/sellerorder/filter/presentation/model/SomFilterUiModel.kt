package com.tokopedia.sellerorder.filter.presentation.model

import android.os.Parcelable
import com.tokopedia.sellerorder.filter.presentation.adapter.TypeFactorySomFilterAdapter
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SomFilterUiModel(
        var nameFilter: String = "",
        var somFilterData: List<SomFilterChipsUiModel> = listOf(),
        val canSelectMany: Boolean = false,
        val isDividerVisible: Boolean = false
) : BaseSomFilter, Parcelable {
    override fun type(typeFactory: TypeFactorySomFilterAdapter): Int {
        return typeFactory.type(this)
    }
}