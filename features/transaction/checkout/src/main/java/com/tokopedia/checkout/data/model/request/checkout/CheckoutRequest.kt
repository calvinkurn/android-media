package com.tokopedia.checkout.data.model.request.checkout

import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.*

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
        @JvmField
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
        var leasingId: Int = 0
) : Parcelable {

    val isHavingPurchaseProtectionEnabled: Boolean
        get() {
            data?.forEach { data ->
                data.shopProducts?.forEach { shopProduct ->
                    shopProduct.productData?.forEach { productData ->
                        if (productData.isPurchaseProtection) {
                            return true
                        }
                    }
                }
            }
            return false
        }

}