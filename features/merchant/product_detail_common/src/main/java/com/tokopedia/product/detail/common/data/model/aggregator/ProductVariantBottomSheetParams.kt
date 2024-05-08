package com.tokopedia.product.detail.common.data.model.aggregator

import android.graphics.Point
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.product.detail.common.ProductDetailCommonConstant.DEFAULT_PRICE_MINIMUM_SHIPPING
import kotlinx.parcelize.Parcelize

/**
 * Created by Yehezkiel on 11/05/21
 */
data class ProductVariantBottomSheetParams(
    // general info
    var productId: String = "",
    var pageSource: String = "",
    var trackerCdListName: String = "",
    var isTokoNow: Boolean = false,
    var whId: String = "",
    var shopId: String = "",
    var dismissAfterTransaction: Boolean = false,
    var saveAfterClose: Boolean = true,
    var extParams: String = "",
    var showQtyEditor: Boolean = false,
    var changeVariantOnCart: ChangeVariant = ChangeVariant(),
    var dismissWhenTransactionError: Boolean = false,

    /**
     * PDP only
     */
    var pdpSession: String = "",
    // only be used in AtcVariantViewModel, dont use this except from AtcVariantViewModel
    var variantAggregator: ProductVariantAggregatorUiData = ProductVariantAggregatorUiData(),
    var miniCartData: Map<String, MiniCartItem.MiniCartItemProduct>? = null,
    var alreadyFollowShop: Boolean = false,
    var cartPosition: Point? = null,

    // Basic info pdp
    var minimumShippingPrice: Double = DEFAULT_PRICE_MINIMUM_SHIPPING,
    var trackerAttribution: String = "",
    var trackerListNamePdp: String = "",
    var isShopOwner: Boolean = false,
    var cacheId: String = ""
) {
    fun showQtyEditorOrTokoNow(): Boolean = isTokoNow || showQtyEditor

    @Parcelize
    data class ChangeVariant(
        @SerializedName("cartId")
        @Expose
        val cartId: String = "",
        @SerializedName("currentQuantity")
        @Expose
        val currentQuantity: Int = Int.ZERO
    ) : Parcelable
}
