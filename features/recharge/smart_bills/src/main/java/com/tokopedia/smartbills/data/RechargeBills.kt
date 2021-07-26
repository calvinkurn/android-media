package com.tokopedia.smartbills.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.common.topupbills.data.RechargeField
import com.tokopedia.smartbills.presentation.adapter.SmartBillsAdapterFactory

data class RechargeBills(
        @SerializedName("UUID")
        @Expose
        val uuid: String = "",
        @SerializedName("flag")
        @Expose
        val flag: Boolean = false,
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
        @SerializedName("isChecked")
        @Expose
        var isChecked: Boolean = false,
        var errorMessage: String = "",
        @SerializedName("DueDate")
        @Expose
        var dueDate: String = "",
        @SerializedName("DueMessage")
        @Expose
        var dueMessage: DueUrgency = DueUrgency(),
        @SerializedName("DueDateLabel")
        @Expose
        var dueDateLabel: DueUrgency = DueUrgency(),
        @SerializedName("sections")
        @Expose
        var section: Section = Section(),

        ): RechargeBillsModel() {
        override fun type(typeFactory: SmartBillsAdapterFactory): Int {
                return typeFactory.type(this)
        }

        enum class Source(val label: String) {
                DEFAULT("default"),
                FAVORITE_WIDGET("favorite"),
                REMINDER_WIDGET("reminder");

                companion object {
                        @JvmStatic
                        fun getSourceByString(label: String): Source {
                                for (type in values()) {
                                        if (type.label == label) return type
                                }
                                return DEFAULT
                        }
                }
        }
}