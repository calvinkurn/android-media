package com.tokopedia.purchase_platform.features.express_checkout.data.entity.response.atc

import com.google.gson.annotations.SerializedName
import com.tokopedia.purchase_platform.features.checkout.data.model.response.shipment_address_form.GroupShop

/**
 * Created by Irfan Khoirul on 30/11/18.
 */

data class Cart(

        @SerializedName("errors")
        val errors: ArrayList<String>,

        @SerializedName("group_shop")
        val groupShops: ArrayList<GroupShop>

)