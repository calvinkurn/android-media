package com.tokopedia.product.manage.feature.campaignstock.ui.dataview

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ReservedEventInfoModel(
        val eventType: String,
        val eventName: String,
        val eventDesc: String,
        val stock: String,
        val actionWording: String,
        val actionUrl: String,
        val products: ArrayList<ReservedStockProductModel>): Parcelable