package com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by fwidjaja on 11/03/20.
 */
@Parcelize
data class LastApplyEmptyCartInfoUiModel(
        var imgUrl: String = "",
        var message: String = "",
        var detail: String = ""
) : Parcelable