package com.tokopedia.topupbills.telco.data

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.topupbills.telco.prepaid.adapter.TelcoProductAdapterFactory
import kotlinx.android.parcel.Parcelize

/**
 * Created by nabillasabbaha on 10/05/19.
 */
@Parcelize
data class TelcoProduct(
        @SerializedName("id")
        @Expose
        val id: String = "",
        @SerializedName("attributes")
        @Expose
        val attributes: TelcoAttributesProduct = TelcoAttributesProduct(),
        var isTitle: Boolean = false,
        var titleSection: String = "",
        var productPosition: Int = 0)
    : Parcelable, Visitable<TelcoProductAdapterFactory> {

    override fun type(typeFactoryProduct: TelcoProductAdapterFactory) = typeFactoryProduct.type(this)

    fun isSpecialProductPromo(): Boolean = if (attributes.productLabels.isNotEmpty())
        attributes.productLabels[0].equals(SPECIAL_PROMO_LABEL, true)
    else false

    companion object {
        const val SPECIAL_PROMO_LABEL: String = "Traktiran Pengguna Baru"
    }
}
