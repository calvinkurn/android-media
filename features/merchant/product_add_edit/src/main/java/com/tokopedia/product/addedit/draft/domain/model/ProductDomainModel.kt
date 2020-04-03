package com.tokopedia.product.addedit.draft.domain.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class ProductDomainModel(

): Parcelable {
    companion object{
        const val TYPE = 382
    }
}