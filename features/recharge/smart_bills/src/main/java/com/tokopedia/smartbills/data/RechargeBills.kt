package com.tokopedia.smartbills.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.common.topupbills.data.RechargeField
import com.tokopedia.smartbills.presentation.adapter.SmartBillsAdapterFactory

data class RechargeBills(
        @SerializedName("index")
        @Expose
        val index: Int = -1,
        @SerializedName("productID")
        @Expose
        val productID: Int = 0,
        @SerializedName("productName")
        @Expose
        val productName: String = "",
        @SerializedName("categoryID")
        @Expose
        val categoryID: Int = 0,
        @SerializedName("categoryName")
        @Expose
        val categoryName: String = "",
        @SerializedName("operatorID")
        @Expose
        val operatorID: Int = 0,
        @SerializedName("operatorName")
        @Expose
        val operatorName: String = "",
        @SerializedName("clientNumber")
        @Expose
        val clientNumber: String = "",
        @SerializedName("amount")
        @Expose
        val amount: Float = 0f,
        @SerializedName("amountText")
        @Expose
        val amountText: String = "",
        @SerializedName("iconURL")
        @Expose
        val iconURL: String = "",
        @SerializedName("date")
        @Expose
        val date: String = "",
        @SerializedName("dateText")
        @Expose
        val dateText: String = "",
        @SerializedName("disabled")
        @Expose
        val disabled: Boolean = false,
        @SerializedName("disabledText")
        @Expose
        val disabledText: String = "",
        @SerializedName("checkoutFields")
        @Expose
        val checkoutFields: List<RechargeField> = listOf(),
        @SerializedName("billName")
        @Expose
        val billName: String = "",
        var selected: Boolean = false,
        var errorMessage: String = ""
): Visitable<SmartBillsAdapterFactory> {
        override fun type(typeFactory: SmartBillsAdapterFactory): Int {
                return typeFactory.type(this)
        }
}