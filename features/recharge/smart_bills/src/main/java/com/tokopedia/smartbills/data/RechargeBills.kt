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
        @SerializedName("Flag")
        @Expose
        val flag: Boolean = false,
        @SerializedName("Index")
        @Expose
        val index: Int = -1,
        @SerializedName("ProductID")
        @Expose
        val productID: Int = 0,
        @SerializedName("ProductName")
        @Expose
        val productName: String = "",
        @SerializedName("CategoryID")
        @Expose
        val categoryID: Int = 0,
        @SerializedName("CategoryName")
        @Expose
        val categoryName: String = "",
        @SerializedName("OperatorID")
        @Expose
        val operatorID: Int = 0,
        @SerializedName("OperatorName")
        @Expose
        val operatorName: String = "",
        @SerializedName("ClientNumber")
        @Expose
        val clientNumber: String = "",
        @SerializedName("Amount")
        @Expose
        val amount: Float = 0f,
        @SerializedName("AmountText")
        @Expose
        val amountText: String = "",
        @SerializedName("IconURL")
        @Expose
        val iconURL: String = "",
        @SerializedName("Date")
        @Expose
        val date: String = "",
        @SerializedName("DateText")
        @Expose
        val dateText: String = "",
        @SerializedName("Disabled")
        @Expose
        val disabled: Boolean = false,
        @SerializedName("DisabledText")
        @Expose
        val disabledText: String = "",
        @SerializedName("CheckoutFields")
        @Expose
        val checkoutFields: List<RechargeField> = listOf(),
        @SerializedName("BillName")
        @Expose
        val billName: String = "",
        @SerializedName("IsChecked")
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
        @SerializedName("Section")
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