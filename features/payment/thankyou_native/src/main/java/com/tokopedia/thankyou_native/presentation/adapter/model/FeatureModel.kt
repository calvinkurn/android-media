package com.tokopedia.thankyou_native.presentation.adapter.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.thankyou_native.presentation.adapter.factory.FeatureListingFactory

data class FeatureListItem(
        var id : Int,
        @SerializedName("url")
        val url : String?,
        @SerializedName("url_android")
        val urlApp : String?,
        @SerializedName("image")
        val image : String,
        @SerializedName("title")
        val title : String,
        @SerializedName("desc")
        val description : String
) : Visitable<FeatureListingFactory>{

    override fun type(typeFactory: FeatureListingFactory): Int {
        return typeFactory.type(this)
    }
}