package com.tokopedia.expresscheckout.domain.entity.variant

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Irfan Khoirul on 28/11/18.
 */

data class ProductVariant(

        @SerializedName("parent_id")
        @Expose
        val code: Int,

        @SerializedName("default_child")
        @Expose
        val message: Int,

        @SerializedName("variant")
        @Expose
        val type: Variant,

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