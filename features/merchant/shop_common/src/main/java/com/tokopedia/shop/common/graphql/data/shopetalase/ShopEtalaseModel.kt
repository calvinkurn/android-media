package com.tokopedia.shop.common.graphql.data.shopetalase

import android.os.Parcel
import android.os.Parcelable

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import com.tokopedia.shop.common.constant.ShopEtalaseTypeDef.ETALASE_DEFAULT

/**
 * Created by hendry on 08/08/18.
 */

data class ShopEtalaseModel(@SerializedName("id")
                            @Expose
                            val id: String = "",
                            @SerializedName("name")
                            @Expose
                            var name: String = "",
                            @SerializedName("count")
                            @Expose
                            val count: Int = 0,
                            @SerializedName("type")
                            @Expose
                            val type: Int = ETALASE_DEFAULT) {
}
