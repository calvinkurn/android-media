package com.tokopedia.sellerhome.view.model

import android.os.Parcelable
import com.tokopedia.kotlin.extensions.view.EMPTY
import kotlinx.parcelize.Parcelize

/**
 * Created by @ilhamsuaib on 01/02/23.
 */

@Parcelize
data class SellerHomeDataUiModel(
    val toasterMessage: String = String.EMPTY,
    val toasterCta: String = String.EMPTY
) : Parcelable