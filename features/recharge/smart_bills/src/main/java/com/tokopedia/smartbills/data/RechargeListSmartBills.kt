package com.tokopedia.smartbills.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.smartbills.presentation.adapter.SmartBillsAdapterFactory

data class RechargeListSmartBills(
        @SerializedName("userID")
        @Expose
        val userID: String = "",
        @SerializedName("total")
        @Expose
        val total: Int = 0,
        @SerializedName("totalText")
        @Expose
        val totalText: String = "",
        @SerializedName("month")
        @Expose
        val month: String = "",
        @SerializedName("monthText")
        @Expose
        val monthText: String = "",
        @SerializedName("dateRangeText")
        @Expose
        val dateRangeText: String = "",
        @SerializedName("isOngoing")
        @Expose
        val isOngoing: Boolean = true,
        @SerializedName("summaries")
        @Expose
        val summaries: List<RechargeStatementBills.Summaries> = listOf(),
        @SerializedName("sections")
        @Expose
        val sections: List<Section> = listOf(),

        ){
    data class Response(
            @SerializedName("rechargeSBMList")
            @Expose
            val response: RechargeListSmartBills? = null
    )
}

data class Section(
    @SerializedName("title")
    @Expose
    val title: String = "",
    @SerializedName("type")
    @Expose
    val type: Int = 0,
    @SerializedName("text")
    @Expose
    val text: String = "",
    @SerializedName("bills")
    @Expose
    val bills: List<RechargeBills> = listOf(),
    val positionAccordion: Int = 0
): RechargeBillsModel() {
    override fun type(typeFactory: SmartBillsAdapterFactory): Int {
        return typeFactory.type(this)
    }
}
