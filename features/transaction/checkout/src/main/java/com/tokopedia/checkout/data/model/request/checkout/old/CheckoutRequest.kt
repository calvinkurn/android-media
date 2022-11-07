package com.tokopedia.checkout.data.model.request.checkout.old

import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.tokopedia.checkout.data.model.request.checkout.cross_sell.CrossSellRequest
import com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics
import kotlinx.parcelize.Parcelize
import kotlin.collections.ArrayList

@Parcelize
data class CheckoutRequest(
        @SerializedName("promos")
        var promos: List<PromoRequest>? = null,
        @SerializedName("promo_code")
        var promoCode: String? = null,
        @SerializedName("is_donation")
        var isDonation: Int = 0,
        @SerializedName("egold_data")
        var egoldData: EgoldData? = null,
        @SerializedName("data")
        var data: List<DataCheckoutRequest>? = null,
        @SerializedName("tokopedia_corner_data")
        var cornerData: TokopediaCornerData? = null,
        @SerializedName("has_promo_stacking")
        var hasPromoStacking: Boolean = false,
        @SerializedName("promo_codes")
        var promoCodes: ArrayList<String>? = null,
        @SerializedName("leasing_id")
        @SuppressLint("Invalid Data Type")
        var leasingId: Long = 0,
        @SerializedName("feature_type")
        var featureType: Int = 0,
        @SerializedName("cross_sell")
        var crossSell: CrossSellRequest? = null,
        @SerializedName("prescription_ids")
        var prescriptionIds: ArrayList<String>? = null,
) : Parcelable {

    val protectionAnalyticsData: ArrayList<String>
        get() {
            val pppLabelList = arrayListOf<String>()
            data?.forEach { data ->
                data.shopProducts?.forEach { shopProduct ->
                    shopProduct.productData?.forEach { productData ->
                        if (productData.isProtectionAvailable) {
                            if (productData.isPurchaseProtection) {
                                pppLabelList.add("${productData.protectionTitle} - " +
                                        "${ConstantTransactionAnalytics.EventLabel.SUCCESS_TICKED_PPP} - " +
                                        "${productData.productCategoryId} - ${productData.protectionPricePerProduct} - ${productData.cartId}")
                            } else
                                pppLabelList.add("${productData.protectionTitle} - " +
                                        "${ConstantTransactionAnalytics.EventLabel.SUCCESS_UNTICKED_PPP} - " +
                                        "${productData.productCategoryId} - ${productData.protectionPricePerProduct} - ${productData.cartId}")
                        }
                    }
                }
            }
            return pppLabelList
        }

}