package com.tokopedia.checkout.view.uimodel

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class EgoldAttributeModel(
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
    var egoldTieringModelArrayList: ArrayList<EgoldTieringModel> = ArrayList(),
    var isEnabled: Boolean = true,
    var hyperlinkText: String? = null,
    var hyperlinkUrl: String? = null,
    var isShowHyperlink: Boolean = false
) : Parcelable
