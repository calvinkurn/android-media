package com.tokopedia.smartbills.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.smartbills.presentation.adapter.SmartBillsAdapterFactory

data class RechargeListSmartBills(
        @SerializedName("UserID")
        @Expose
        val userID: String = "",
        @SerializedName("Total")
        @Expose
        val total: Int = 0,
        @SerializedName("TotalText")
        @Expose
        val totalText: String = "",
        @SerializedName("Month")
        @Expose
        val month: String = "",
        @SerializedName("MonthText")
        @Expose
        val monthText: String = "",
        @SerializedName("DateRangeText")
        @Expose
        val dateRangeText: String = "",
        @SerializedName("IsOngoing")
        @Expose
        val isOngoing: Boolean = true,
        @SerializedName("Summaries")
        @Expose
        val summaries: List<RechargeStatementBills.Summaries> = listOf(),
        @SerializedName("Sections")
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
    @SerializedName("Title")
    @Expose
    val title: String = "",
    @SerializedName("Type")
    @Expose
    val type: Int = 0,
    @SerializedName("Text")
    @Expose
    val text: String = "",
    @SerializedName("Bills")
    @Expose
    val bills: List<RechargeBills> = listOf(),
    val positionAccordion: Int = 0
): RechargeBillsModel() {
    override fun type(typeFactory: SmartBillsAdapterFactory): Int {
        return typeFactory.type(this)
    }
}
