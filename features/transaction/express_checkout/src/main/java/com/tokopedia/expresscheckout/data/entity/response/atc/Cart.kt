package com.tokopedia.expresscheckout.data.entity.response.atc

import com.google.gson.annotations.SerializedName
import com.tokopedia.transactiondata.entity.response.shippingaddressform.GroupShop

/**
 * Created by Irfan Khoirul on 30/11/18.
 */

data class Cart(

        @SerializedName("errors")
        val errors: ArrayList<String>,

        @SerializedName("group_shop")
        val groupShops: ArrayList<GroupShop>

)