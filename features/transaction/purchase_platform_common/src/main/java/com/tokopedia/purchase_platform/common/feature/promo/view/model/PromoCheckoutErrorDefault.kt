package com.tokopedia.purchase_platform.common.feature.promo.view.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by fwidjaja on 07/03/20.
 */
@Parcelize
data class PromoCheckoutErrorDefault(
    var title: String = "",
    var desc: String = ""
) : Parcelable
