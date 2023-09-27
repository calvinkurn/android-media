package com.tokopedia.epharmacy.network.params

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

class CartGeneralAddToCartInstantParams(
    @SerializedName("business_data")
    var businessData: CartGeneralAddToCartInstantRequestBusinessData,
    @SerializedName("source")
    var source: String
) : GqlParam {
    data class CartGeneralAddToCartInstantRequestBusinessData(
        @SerializedName("business_id")
        var businessId: String,
        @SerializedName("product_list")
        var productList: List<CartGeneralAddToCartInstantRequestProductData>
    ) {
        data class CartGeneralAddToCartInstantRequestProductData(
            @SerializedName("product_id")
            var productId: String,
            @SerializedName("quantity")
            var quantity: Int,
            @SerializedName("metadata")
            var metaData: CartGeneralCustomStruct,
            @SerializedName("shop_id")
            var shopId: String,
            @SerializedName("note")
            var note: String
        ) {
            data class CartGeneralCustomStruct(
                @SerializedName("epharmacy_group_id")
                var epharmacyGroupId: String,
                @SerializedName("enabler_id")
                var enablerId: Long,
                @SerializedName("toko_consultation_id")
                var tConsultationId: Long
            )
        }
    }
}
