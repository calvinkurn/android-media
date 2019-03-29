package com.tokopedia.topads.dashboard

class TopAdsDashboardTracking(private val router: TopAdsDashboardRouter){
    fun eventTopAdsProductClickKeywordDashboard() {
        router.eventTopAdsProductClickKeywordDashboard()
    }

    fun eventTopAdsProductClickProductDashboard() {
        router.eventTopAdsProductClickProductDashboard()
    }

    fun eventTopAdsProductClickGroupDashboard(){
        router.eventTopAdsProductClickGroupDashboard()
    }

    fun eventTopAdsProductAddBalance(){
        router.eventTopAdsProductAddBalance()
    }

    fun eventTopAdsShopChooseDateCustom(){
        router.eventTopAdsShopChooseDateCustom()
    }

    fun eventTopAdsShopDatePeriod(label: String){
        router.eventTopAdsShopDatePeriod(label)
    }

    fun eventTopAdsProductStatisticBar(label: String){
        router.eventTopAdsProductStatisticBar(label)
    }

    fun eventTopAdsShopStatisticBar(label: String){
        router.eventTopAdsShopStatisticBar(label)
    }

    fun eventOpenTopadsPushNotification(label: String){
        router.eventOpenTopadsPushNotification(label)
    }
}