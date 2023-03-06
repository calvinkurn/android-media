package com.tokopedia.discovery2.viewcontrollers.fragment

import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
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

class DiscoveryExtensibleFragment : DiscoveryFragment() {
    private var tempViewModel: DiscoveryViewModel? = null

    @JvmField
    @Inject
    var pageLoadTimePerformanceInterfaceInjected: PageLoadTimePerformanceInterface? = null

    @Inject
    lateinit var trackingQueueInjected: TrackingQueue

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

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

    override fun provideCategorySource(): Boolean {
        return false
    }

    //Todo::
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
        activity?.let { activity ->
            discoveryComponent = DaggerDiscoveryComponent.builder()
                .discoveryModule(DiscoveryModule(DiscoveryRepoProvider()))
                .baseAppComponent((activity.applicationContext as BaseMainApplication).baseAppComponent)
                .build().also {
                    it.inject(this)
                }
            tempViewModel = ViewModelProvider(
                activity,
                viewModelFactory
            )[DiscoveryViewModel::class.java].apply {
                activity.lifecycle.addObserver(BaseLifeCycleObserver(this))
            }

        }
    }

}
