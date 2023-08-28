package com.tokopedia.discovery2.viewmodel.livestate

import com.tokopedia.kotlin.extensions.view.EMPTY

sealed class DiscoveryLiveState
data class RouteToApplink(val applink: String) : DiscoveryLiveState()
data class GoToAgeRestriction(val departmentId : String?, val origin : Int) : DiscoveryLiveState()

data class NavToolbarConfig(val isExtendedLayout: Boolean = false, val color: String = String.EMPTY)
