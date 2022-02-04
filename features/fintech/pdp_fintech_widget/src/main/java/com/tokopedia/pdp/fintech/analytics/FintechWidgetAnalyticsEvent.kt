package com.tokopedia.pdp.fintech.analytics

open class BaseAnalyticsData()
{
    var productId:String = ""
    var timeStamp: Long = System.currentTimeMillis()
    var partnerName:String = ""

}

open class StatusBuyer:BaseAnalyticsData(){
    val userStatus: String = ""
}