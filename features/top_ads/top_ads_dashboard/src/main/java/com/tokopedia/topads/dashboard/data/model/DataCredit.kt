package com.tokopedia.topads.dashboard.data.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.topads.dashboard.view.adapter.TopAdsCreditTypeFactory
import kotlinx.android.parcel.Parcelize

/**
 * Created by zulfikarrahman on 11/4/16.
 */
@Parcelize
data class DataCredit (
    @SerializedName("product_id")
    @Expose
    val productId: String = "",

    @SerializedName("product_type")
    @Expose
    val productType: String = "",

    @SerializedName("product_price")
    @Expose
    val productPrice: String = "",

    @SerializedName("product_bonus")
    @Expose
    val productBonus: String = "",

    @SerializedName("product_url")
    @Expose
    val productUrl: String = "",

    @SerializedName("default")
    @Expose
    val selected: Int = 0) : Parcelable, Visitable<TopAdsCreditTypeFactory>{

    override fun type(typeFactory: TopAdsCreditTypeFactory): Int {
        return typeFactory.type(this)
    }
}