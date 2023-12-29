package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.topadsheadline

import android.app.Application
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.repository.topads.TopAdsHeadlineRepository
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.topads.sdk.utils.*
import com.tokopedia.user.session.UserSession
import javax.inject.Inject

class TopAdsHeadlineViewModel(val application: Application, val components: ComponentsItem, val position: Int) : DiscoveryBaseViewModel() {

    @JvmField
    @Inject
    var topAdsHeadlineRepository: TopAdsHeadlineRepository? = null

    fun getHeadlineAdsParam(): String? {
        return topAdsHeadlineRepository?.getHeadlineAdsParams(components.pageEndPoint, components.data?.get(0)?.paramsMobile ?: "")
    }

    fun isUserLoggedIn(): Boolean {
        return UserSession(application).isLoggedIn
    }
}
