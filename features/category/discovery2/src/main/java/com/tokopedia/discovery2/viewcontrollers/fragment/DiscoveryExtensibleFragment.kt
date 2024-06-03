package com.tokopedia.discovery2.viewcontrollers.fragment

import android.os.Bundle
import androidx.annotation.Keep
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.analytics.btm.BtmApi
import com.tokopedia.analytics.btm.Tokopedia
import com.tokopedia.analytics.byteio.AppLogInterface
import com.tokopedia.analytics.byteio.IAdsLog
import com.tokopedia.analytics.byteio.PageName
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface
import com.tokopedia.basemvvm.viewmodel.BaseLifeCycleObserver
import com.tokopedia.discovery2.analytics.BaseDiscoveryAnalytics
import com.tokopedia.discovery2.analytics.DiscoveryAnalytics
import com.tokopedia.discovery2.datamapper.discoComponentQuery
import com.tokopedia.discovery2.di.DaggerDiscoveryComponent
import com.tokopedia.discovery2.di.DiscoveryModule
import com.tokopedia.discovery2.di.DiscoveryRepoProvider
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity.Companion.SOURCE
import com.tokopedia.discovery2.viewmodel.DiscoveryViewModel
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.user.session.UserSession
import javax.inject.Inject

@Keep
class DiscoveryExtensibleFragment : DiscoveryFragment(), AppLogInterface, IAdsLog {
    private var tempViewModel: DiscoveryViewModel? = null

    @JvmField
    @Inject
    var pageLoadTimePerformanceInterfaceInjected: PageLoadTimePerformanceInterface? = null

    @Inject
    lateinit var trackingQueueInjected: TrackingQueue

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

//    init {
//        BtmApi.registerBtmPageOnCreate(this, Tokopedia.OfficialStore)
//    }

    override fun provideAnalytics(): BaseDiscoveryAnalytics {
        return DiscoveryAnalytics(
            discoveryViewModel.pageType,
            discoveryViewModel.pagePath,
            discoveryViewModel.pageIdentifier,
            discoveryViewModel.campaignCode,
            discoComponentQuery?.get(SOURCE) ?: "",
            UserSession(activity),
            trackingQueue = trackingQueue
        )
    }

    override fun provideTrackingQueue(): TrackingQueue {
        return trackingQueueInjected
    }

    // Todo::
    fun injectionComponent() {
//        After you inject this fragment
//        How should we handle pageLoadTimeInterface.
        pageLoadTimePerformanceInterface = pageLoadTimePerformanceInterfaceInjected
    }

    override fun injectDiscoveryViewModel() {
        discoveryViewModel = tempViewModel ?: run {
            initInjector()
            tempViewModel!!
        }
    }

    override fun initInjector() {
        context?.let { context ->
            discoveryComponent = DaggerDiscoveryComponent.builder()
                .discoveryModule(DiscoveryModule(DiscoveryRepoProvider()))
                .baseAppComponent((context.applicationContext as BaseMainApplication).baseAppComponent)
                .build().also {
                    it.inject(this)
                }
            tempViewModel = ViewModelProvider(
                this,
                viewModelFactory
            )[DiscoveryViewModel::class.java].apply {
                this@DiscoveryExtensibleFragment.lifecycle.addObserver(BaseLifeCycleObserver(this))
            }
        }
    }

    override fun getPageName(): String {
        return PageName.OFFICIAL_STORE
    }

    override fun getAdsPageName(): String {
        return PageName.OFFICIAL_STORE
    }

    override fun isEnterFromWhitelisted(): Boolean {
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BtmApi.registerBtmPageOnCreate(this, Tokopedia.OfficialStore)
    }
}
