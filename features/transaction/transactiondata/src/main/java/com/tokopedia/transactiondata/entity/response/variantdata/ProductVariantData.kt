package com.tokopedia.transactiondata.entity.response.variantdata

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Irfan Khoirul on 28/11/18.
 */

data class ProductVariantData(

        @SerializedName("parent_id")
        @Expose
        val parentId: Int,

        @SerializedName("default_child")
        @Expose
        val defaultChild: Int,

        @SerializedName("variant")
        @Expose
        val variants: ArrayList<Variant>,

        @SerializedName("children")
        @Expose
        val children: ArrayList<Child>,

        @SerializedName("is_enabled")
        @Expose
        val isEnabled: Boolean,

        @SerializedName("stock")
        @Expose
        val stock: Int

)