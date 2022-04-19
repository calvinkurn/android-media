package com.tokopedia.topupbills.telco.data

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseListCheckableTypeFactory
import kotlinx.android.parcel.Parcelize

data class TelcoFilterTagComponent(
        @SerializedName("name")
        @Expose
        val name: String = "",
        @SerializedName("text")
        @Expose
        val text: String = "",
        @SerializedName("param_name")
        @Expose
        val paramName: String = "",
        @SerializedName("data_collections")
        @Expose
        val filterTagDataCollections: List<FilterTagDataCollection> = mutableListOf()
)

@Parcelize
data class FilterTagDataCollection(
        @SerializedName("key")
        @Expose
        val key: String = "",
        @SerializedName("value")
        @Expose
        val value: String = ""
): Parcelable, Visitable<BaseListCheckableTypeFactory<FilterTagDataCollection>> {

        override fun type(typeFactory: BaseListCheckableTypeFactory<FilterTagDataCollection>): Int {
                return typeFactory.type(this)
        }
}