package com.tokopedia.smartbills.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RechargeCatalogMenuAddBills(
        @SerializedName("rechargeCatalogMenu")
        @Expose
        val response: List<SmartBillsCatalogMenu> = emptyList()
)