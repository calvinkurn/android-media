package com.tokopedia.power_merchant.subscribe.view.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created By @ilhamsuaib on 17/06/21
 */

@Parcelize
data class PMProStatusInfoUiModel(
        val autoExtendDateFmt: String,
        val pmActiveShopScoreThreshold: Int,
        val pmProActiveShopScoreThreshold: Int,
        val itemSoldThreshold: Long,
        val netItemValueThreshold: Long
): Parcelable