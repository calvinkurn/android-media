package com.tokopedia.shop.common.graphql.data.shopetalase

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.shop.common.constant.ShopEtalaseTypeDef.ETALASE_DEFAULT
import kotlinx.android.parcel.Parcelize

@Parcelize
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
                            val type: Int = ETALASE_DEFAULT,
                            @SerializedName("highlighted")
                            @Expose
                            val highlighted: Boolean = false,
                            @SerializedName("alias")
                            @Expose
                            val alias: String = "",
                            @SerializedName("useAce")
                            @Expose
                            val useAce: Boolean = true,
                            @SerializedName("badge")
                            @Expose
                            val badge: String = "") : Parcelable {
}
