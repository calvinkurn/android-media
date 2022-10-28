package com.tokopedia.power_merchant.subscribe.view.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created By @ilhamsuaib on 17/06/21
 */

@Parcelize
data class PMProStatusInfoUiModel(
        val nextMonthlyRefreshDate: String,
        val pmActiveShopScoreThreshold: Int,
        val pmProActiveShopScoreThreshold: Int,
        val itemSoldThreshold: Long,
        val netItemValueThreshold: Long
): Parcelable