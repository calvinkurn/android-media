package com.tokopedia.sellerorder.common.presenter.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by fwidjaja on 2019-09-13.
 */

@Parcelize
data class SomSubFilter(
        val id: Int = 0,
        val name: String = "",
        val key: String = "",
        val typeView: String = "",
        val typeFilter: String = "",
        val listValue: List<Int> = arrayListOf(),
        var isChecked: Boolean = false
): Parcelable