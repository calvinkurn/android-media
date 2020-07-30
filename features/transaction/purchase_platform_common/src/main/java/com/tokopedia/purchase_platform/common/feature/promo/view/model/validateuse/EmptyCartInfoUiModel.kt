package com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by fwidjaja on 13/03/20.
 */
@Parcelize
data class EmptyCartInfoUiModel (
        var imgUrl: String = "",
        var message: String = "",
        var detail: String = ""
): Parcelable