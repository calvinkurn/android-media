package com.tokopedia.discovery2.viewmodel.livestate

sealed class DiscoveryLiveState
data class RouteToApplink(val applink: String) : DiscoveryLiveState()
data class GoToAgeRestriction(val departmentId : String?, val origin : Int) : DiscoveryLiveState()
