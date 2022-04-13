package com.tokopedia.tokomember_seller_dashboard.view.adapter.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokomember_seller_dashboard.view.adapter.factory.TokomemberCardBgFactory
import com.tokopedia.tokomember_seller_dashboard.view.adapter.factory.TokomemberCardColorFactory

data class TokomemberCardItem(
    var tokoVisitable: ArrayList<Visitable<*>>
)

open class TokomemberProgramModel {
    var isVisited: Boolean = false
}

data class TokomemberCardColorItem(
    var tokoVisitableCardColor: ArrayList<Visitable<*>>
)

data class TokomemberCardBgItem(
    var tokoVisitableCardBg: ArrayList<Visitable<*>>
)

open class TokomemberCardBgModel {
    var isVisited: Boolean = false
}

data class TokomemberCardColor(
    @Expose
    @SerializedName("colorCode")
    val colorCode: String? = null
) : Visitable<TokomemberCardColorFactory>, TokomemberProgramModel() {

    override fun type(typeFactory: TokomemberCardColorFactory): Int {
        return typeFactory.type(this)
    }
}

data class TokomemberCardBg(
    @Expose
    @SerializedName("imageUrl")
    val imageUrl: String? = null
) : Visitable<TokomemberCardBgFactory>, TokomemberProgramModel() {

    override fun type(typeFactory: TokomemberCardBgFactory): Int {
        return typeFactory.type(this)
    }
}