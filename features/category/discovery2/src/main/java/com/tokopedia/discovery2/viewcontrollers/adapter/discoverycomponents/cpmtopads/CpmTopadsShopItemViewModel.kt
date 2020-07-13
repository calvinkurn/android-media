package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.cpmtopads

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.di.DaggerDiscoveryComponent
import com.tokopedia.discovery2.usecase.topAdsUseCase.DiscoveryTopAdsTrackingUseCase
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import javax.inject.Inject

class CpmTopadsShopItemViewModel(val application: Application, val components: ComponentsItem, val position: Int) : DiscoveryBaseViewModel() {

    private val componentData: MutableLiveData<ComponentsItem> = MutableLiveData()

    @Inject
    lateinit var discoveryTopAdsTrackingUseCase: DiscoveryTopAdsTrackingUseCase


    init {
        initDaggerInject()
        componentData.value = components
    }

    fun getComponent(): LiveData<ComponentsItem> {
        return componentData
    }

    fun sendTopAdsClickTracking(url: String?) {
        if (url != null) {
            discoveryTopAdsTrackingUseCase.sendTopAdsTracking(this::class.qualifiedName, url)
        }
    }

    fun sendTopAdsViewTracking(url: String?) {
        if (url != null && !components.topAdsTrackingStatus) {
            discoveryTopAdsTrackingUseCase.sendTopAdsTracking(this::class.qualifiedName, url)
            components.topAdsTrackingStatus = true
        }
    }

    override fun initDaggerInject() {
        DaggerDiscoveryComponent.builder()
                .baseAppComponent((application.applicationContext as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
    }
}