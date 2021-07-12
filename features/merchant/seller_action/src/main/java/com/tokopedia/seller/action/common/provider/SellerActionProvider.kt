package com.tokopedia.seller.action.common.provider

interface SellerActionProvider {

    fun getDefaultDate(): String
    fun getFormattedDate(date: String): String

}