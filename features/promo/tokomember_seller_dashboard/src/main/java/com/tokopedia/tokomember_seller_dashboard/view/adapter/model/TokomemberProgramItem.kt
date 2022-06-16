package com.tokopedia.tokomember_seller_dashboard.view.adapter.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokomember_seller_dashboard.view.adapter.factory.TmCardBgFactory
import com.tokopedia.tokomember_seller_dashboard.view.adapter.factory.TmCardColorFactory

data class TokomemberCardItem(
    var tokoVisitable: ArrayList<Visitable<*>>
)

open class TokomemberProgramModel {
    var isVisited: Boolean = false
}

data class TokomemberCardColorItem(
    var tokoVisitableCardColor: ArrayList<Visitable<*>> = ArrayList()
)

data class TokomemberCardBgItem(
    var tokoVisitableCardBg: ArrayList<Visitable<*>> = ArrayList()
)

open class TokomemberCardBgModel {
    var isVisited: Boolean = false
}

data class TokomemberCardColor(
    @Expose
    @SerializedName("colorCode")
    val colorCode: String? = null,
    @Expose
    @SerializedName("id")
    val id: String? = null,
    var isSelected:Boolean = false,
    var tokoCardPatternList: ArrayList<String>
) : Visitable<TmCardColorFactory>, TokomemberProgramModel() {

    override fun type(typeFactory: TmCardColorFactory): Int {
        return typeFactory.type(this)
    }
}

data class TokomemberCardBg(
    @Expose
    @SerializedName("imageUrl")
    val imageUrl: String? = null
) : Visitable<TmCardBgFactory>, TokomemberProgramModel() {

    override fun type(typeFactory: TmCardBgFactory): Int {
        return typeFactory.type(this)
    }
}