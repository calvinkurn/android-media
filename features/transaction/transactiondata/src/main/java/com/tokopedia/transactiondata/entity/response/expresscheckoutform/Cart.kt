package com.tokopedia.transactiondata.entity.response.expresscheckoutform

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.transactiondata.entity.response.expresscheckoutform.variant.ProductVariant
import com.tokopedia.transactiondata.entity.response.shippingaddressform.GroupShop

/**
 * Created by Irfan Khoirul on 30/11/18.
 */

data class Cart(

        @SerializedName("errors")
        @Expose
        val errors: ArrayList<String>,

        @SerializedName("product_variants")
        @Expose
        val productVariants: ArrayList<ProductVariant>,

        @SerializedName("group_shop")
        @Expose
        val groupShops: ArrayList<GroupShop>

)