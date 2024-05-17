@file:Suppress("ParamFieldAnnotation")

package com.tokopedia.buy_more_get_more.minicart.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by @ilhamsuaib on 05/12/23.
 */

@Parcelize
class MiniCartParam(
    val shopIds: List<Long> = emptyList(),
    val offerIds: List<Long> = emptyList(),
    val warehouseIds: List<Long> = emptyList(),
    val offerJsonData: String = "{}",
    val cartId: String = ""
) : Parcelable
