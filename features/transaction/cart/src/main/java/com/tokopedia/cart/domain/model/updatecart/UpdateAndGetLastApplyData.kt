package com.tokopedia.cart.domain.model.updatecart

import android.os.Parcelable
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoUiModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class UpdateAndGetLastApplyData(
    var updateCartData: UpdateCartData? = null,
    var promoUiModel: PromoUiModel? = null
) : Parcelable
