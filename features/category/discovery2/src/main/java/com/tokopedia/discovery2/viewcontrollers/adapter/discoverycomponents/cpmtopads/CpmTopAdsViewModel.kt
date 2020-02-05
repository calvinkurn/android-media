package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.cpmtopads

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.data.cpmtopads.Headline
import com.tokopedia.discovery2.data.cpmtopads.ProductItem
import com.tokopedia.discovery2.di.DaggerDiscoveryComponent
import com.tokopedia.discovery2.discoverymapper.DiscoveryDataMapper
import com.tokopedia.discovery2.usecase.CpmTopAdsUseCase
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success

class CpmTopAdsViewModel(val application: Application, private val components: ComponentsItem) : DiscoveryBaseViewModel(), CoroutineScope {

    //private val cpmTopAdsData = MutableLiveData<Result<Headline>>()
    @Inject
    lateinit var cpmTopAdsUseCase: CpmTopAdsUseCase
    val listData = MutableLiveData<Result<ArrayList<ComponentsItem>>>()
    val cpmTopAdsData = MutableLiveData<Result<DiscoveryDataMapper.CpmTopAdsData>>()

    fun getCpmTopAdsLiveData(): LiveData<Result<DiscoveryDataMapper.CpmTopAdsData>> {
        return cpmTopAdsData
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()

    init {
        initDaggerInject()
    }

    fun fetchCpmTopAdsData() {
        launchCatchError(block = {
            val data = components.data?.get(0)?.paramsMobile?.let { cpmTopAdsUseCase.getCpmTopAdsData(it) }
            data?.let { cpmTopAdsData.postValue(Success(it)) }
        }, onError = {
            it.printStackTrace()
        })

    }

    override fun initDaggerInject() {
        DaggerDiscoveryComponent.builder()
                .baseAppComponent((application.applicationContext as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
    }


}


