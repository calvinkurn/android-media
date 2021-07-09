package com.tokopedia.topupbills.telco.data

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.topupbills.telco.prepaid.adapter.TelcoProductAdapterFactory
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TelcoCatalogDataCollection(
        @SerializedName("name")
        @Expose
        val name: String = "",
        @SerializedName("products")
        @Expose
        val products: List<TelcoProduct> = listOf())
    : Parcelable, Visitable<TelcoProductAdapterFactory> {
    override fun type(typeFactoryProduct: TelcoProductAdapterFactory) = typeFactoryProduct.type(this)

    fun isMccm(): Boolean = name == "Flash Sale"
}
