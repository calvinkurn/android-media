package com.tokopedia.thankyou_native.presentation.adapter.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.thankyou_native.presentation.adapter.factory.GyroRecommendationFactory


data class GyroRecommendation(
        var title : String,
        var description: String,
        var gyroVisitable : ArrayList<Visitable<*>>
)


open class GyroModel {
    var isVisited : Boolean = false
}

data class GyroRecommendationListItem(
        var id : Long,
        @SerializedName("url")
        val url : String?,
        @SerializedName("url_android")
        val urlApp : String?,
        @SerializedName("image")
        val image : String?,
        @SerializedName("title")
        val title : String?,
        @SerializedName("desc")
        val description : String?
) : Visitable<GyroRecommendationFactory>, GyroModel() {

    override fun type(typeFactory: GyroRecommendationFactory): Int {
        return typeFactory.type(this)
    }
}