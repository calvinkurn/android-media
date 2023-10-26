package com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by fwidjaja on 16/03/20.
 */
@Parcelize
data class LastApplyUsageSummariesUiModel(
    var description: String = "",
    var type: String = "",
    var amountStr: String = "",
    var amount: Int = -1,
    var currencyDetailsStr: String = ""
) : Parcelable  {

    companion object {
        private const val TYPE_TOTAL_PRODUCT_PRICE = "total_product_price"
    }

    fun isDiscount(): Boolean = type == TYPE_TOTAL_PRODUCT_PRICE
}
