package com.tokopedia.digital.home.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.digital.home.presentation.adapter.RechargeHomepageAdapterTypeFactory

data class RechargeTickerHomepageModel(
        @SerializedName("rechargeTicker")
        @Expose
        val rechargeTickers: List<RechargeTicker> = listOf(),
) : RechargeHomepageSectionModel{
    companion object{
        const val TICKER_VISITABLE_ID = "0"
    }

    override fun visitableId(): String {
        return TICKER_VISITABLE_ID
    }

    override fun equalsWith(b: Any?): Boolean {
        return false
    }

    override fun type(typeFactory: RechargeHomepageAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}

data class RechargeTicker(
        @SerializedName("ID")
        @Expose
        val id: Int = 0,
        @SerializedName("Name")
        @Expose
        val name: String = "",
        @SerializedName("Content")
        @Expose
        val content: String = "",
        @SerializedName("Type")
        @Expose
        val type: String = "",
        @SerializedName("Environment")
        @Expose
        val environment: String = "",
        @SerializedName("ActionLink")
        @Expose
        val actionLink: String = "",
        @SerializedName("ActionText")
        @Expose
        val actionText: String = "",
        @SerializedName("StartDate")
        @Expose
        val startDate: String = "",
        @SerializedName("ExpireDate")
        @Expose
        val expiredDate: String = "",
        @SerializedName("Status")
        @Expose
        val status: Int = 0
)