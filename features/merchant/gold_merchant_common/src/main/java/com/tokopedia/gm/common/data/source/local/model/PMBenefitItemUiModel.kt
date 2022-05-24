package com.tokopedia.gm.common.data.source.local.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by @ilhamsuaib on 22/04/22.
 */

@Parcelize
data class PMBenefitItemUiModel(
    val iconUrl: String,
    val benefitDescription: String
): Parcelable