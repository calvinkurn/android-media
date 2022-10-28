package com.tokopedia.tokomember_seller_dashboard.view.adapter.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokomember_seller_dashboard.view.adapter.factory.TokomemberIntroFactory

data class TokomemberIntroItem(
    var tokoVisitable : ArrayList<Visitable<*>> = ArrayList()
)

open class TokomemberIntroModel {
    var isVisited: Boolean = false
}

data class TokomemberIntroTextItem(
    @SerializedName("text")
    val text: String?,
    @SerializedName("imgUrl")
    val imgUrl: String?,
    var isAnimationFinished: Boolean = false

    ) : Visitable<TokomemberIntroFactory>, TokomemberIntroModel() {

    override fun type(typeFactory: TokomemberIntroFactory): Int {
        return typeFactory.type(this)
    }
}

data class TokomemberIntroButtonItem(
    @SerializedName("text")
    val text: String?,
    @SerializedName("url")
    val url: String?,

    ) : Visitable<TokomemberIntroFactory>, TokomemberIntroModel() {

    override fun type(typeFactory: TokomemberIntroFactory): Int {
        return typeFactory.type(this)
    }
}

data class TokomemberIntroBenefitImageItem(
    @SerializedName("imgUrl")
    val imgUrl: String?,
    var isAnimationFinished: Boolean = false

) : Visitable<TokomemberIntroFactory>, TokomemberIntroModel() {

    override fun type(typeFactory: TokomemberIntroFactory): Int {
        return typeFactory.type(this)
    }
}
