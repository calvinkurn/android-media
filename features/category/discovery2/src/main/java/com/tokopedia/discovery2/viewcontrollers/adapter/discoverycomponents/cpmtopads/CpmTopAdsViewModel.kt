package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.cpmtopads

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.di.DaggerDiscoveryComponent
import com.tokopedia.discovery2.usecase.CpmTopAdsUseCase
import com.tokopedia.discovery2.usecase.topAdsUseCase.TopAdsTrackingUseCase
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.topads.sdk.domain.model.CpmModel
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class CpmTopAdsViewModel(val application: Application, private val components: ComponentsItem, val position: Int) : DiscoveryBaseViewModel(), CoroutineScope {

    @Inject
    lateinit var cpmTopAdsUseCase: CpmTopAdsUseCase

    @Inject
    lateinit var discoveryTopAdsTrackingUseCase: TopAdsTrackingUseCase

    private val cpmData = MutableLiveData<Result<CpmModel>>()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()


    override fun onAttachToViewHolder() {
        super.onAttachToViewHolder()
        fetchCpmTopAdsData()
    }

    private fun fetchCpmTopAdsData() {
        launchCatchError(block = {
            withContext(Dispatchers.IO) {
                val data = components.data?.get(0)?.paramsMobile?.let { cpmTopAdsUseCase.getCpmTopAdsData(components.id, components.pageEndPoint) }
                if (data != null) {
                    cpmData.postValue(Success(components.cpmData as CpmModel))
                }
            }
        }, onError = {
            it.printStackTrace()
        })

    }


    fun sendTopAdsTrackingClick(url: String?, productId: String, productName: String, imageUrl: String) {
        if (url != null) {
            discoveryTopAdsTrackingUseCase.hitClick(this::class.qualifiedName, url, productId, productName, imageUrl)
        }
    }

    fun sendTopAdsTrackingImpressions(url: String?, productId: String, productName: String, imageUrl: String) {
        if (url != null) {
            discoveryTopAdsTrackingUseCase.hitImpressions(this::class.qualifiedName, url, productId, productName, imageUrl)
        }
    }

    fun getCpmData(): LiveData<Result<CpmModel>> = cpmData

    fun getComponentData(): ComponentsItem {
        return components
    }

    fun getComponentPosition(): Int {
        return components.position
    }

    fun isUserLoggedIn(): Boolean {
        return UserSession(application).isLoggedIn
    }
}


