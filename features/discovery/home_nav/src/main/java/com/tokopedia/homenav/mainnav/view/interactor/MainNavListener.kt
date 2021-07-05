package com.tokopedia.homenav.mainnav.view.interactor

import com.tokopedia.homenav.base.diffutil.HomeNavListener
import com.tokopedia.trackingoptimizer.TrackingQueue

interface MainNavListener : HomeNavListener{

    fun onProfileLoginClicked()

    fun onProfileRegisterClicked()

    fun onProfileSectionClicked()

    fun onErrorProfileRefreshClicked(position: Int)

    fun onErrorShopInfoRefreshClicked(position: Int)

    fun onErrorBuListClicked(position: Int)

    fun onErrorTransactionListClicked(position: Int)

    fun getTrackingQueueObj(): TrackingQueue?

    fun putEEToTrackingQueue(data: HashMap<String, Any>)
}