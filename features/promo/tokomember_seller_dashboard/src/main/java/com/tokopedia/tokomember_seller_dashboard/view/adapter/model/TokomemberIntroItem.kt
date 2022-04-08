package com.tokopedia.tokomember_seller_dashboard.view.adapter.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokomember_seller_dashboard.view.adapter.factory.TokomemberIntroFactory

data class TokomemberIntroItem(
    var tokoVisitable : ArrayList<Visitable<*>>
)

open class TokomemberIntroModel {
    var isVisited: Boolean = false
}

data class TokomemberIntroHeaderItem(
    @SerializedName("title")
    val title: String?,
    @SerializedName("desc")
    val description: String?,

    ) : Visitable<TokomemberIntroFactory>, TokomemberIntroModel() {

    override fun type(typeFactory: TokomemberIntroFactory): Int {
        return typeFactory.type(this)
    }
}

data class TokomemberIntroTextItem(
    @SerializedName("text")
    val text: String?,
    @SerializedName("imgUrl")
    val imgUrl: String?,

    ) : Visitable<TokomemberIntroFactory>, TokomemberIntroModel() {

    override fun type(typeFactory: TokomemberIntroFactory): Int {
        return typeFactory.type(this)
    }
}

data class TokomemberIntroVideoItem(
    @SerializedName("url")
    val url: String?,
    @SerializedName("type")
    val type: String?,

    ) : Visitable<TokomemberIntroFactory>, TokomemberIntroModel() {

    override fun type(typeFactory: TokomemberIntroFactory): Int {
        return typeFactory.type(this)
    }
}