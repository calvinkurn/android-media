package com.tokopedia.entertainment.pdp.listener

import com.tokopedia.entertainment.pdp.data.PackageItem

interface OnBindItemTicketListener {

    fun quantityEditorValueButtonClicked(idPackages:String,idPackagesItem: String, packageItem : PackageItem,
                                         totalPrice: Int, qty: String, isError: Boolean,
                                         product_name: String, product_id: String,
                                         price: String,selectedDate: String, packageName: String)
    fun getSelectedDate():String

    fun resetPackage()

    fun clickRecommendation(list: List<String>)
}