package com.tokopedia.checkout.view.uimodel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class EgoldAttributeModel(
        var isEligible: Boolean = false,
        var isTiering: Boolean = false,
        var minEgoldRange: Int = 0,
        var maxEgoldRange: Int = 0,
        var titleText: String? = null,
        var subText: String? = null,
        var tickerText: String? = null,
        var tooltipText: String? = null,
        var isChecked: Boolean = false,
        var buyEgoldValue: Long = 0,
        var egoldTieringModelArrayList: List<EgoldTieringModel> = emptyList()

) : Parcelable