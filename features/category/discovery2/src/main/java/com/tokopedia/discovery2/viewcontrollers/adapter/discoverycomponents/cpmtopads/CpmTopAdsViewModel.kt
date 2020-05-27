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
import kotlinx.coroutines.withContext

class CpmTopAdsViewModel(val application: Application, private val components: ComponentsItem, val position: Int) : DiscoveryBaseViewModel(), CoroutineScope {

    private val cpmTopAdsList = MutableLiveData<Result<ArrayList<ComponentsItem>>>()
    private val promotedText = MutableLiveData<Result<String>>()
    private val brandName = MutableLiveData<Result<String>>()
    private val imageUrl = MutableLiveData<Result<String>>()

    @Inject
    lateinit var cpmTopAdsUseCase: CpmTopAdsUseCase



    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()

    init {
        initDaggerInject()
    }

    fun fetchCpmTopAdsData() {
        launchCatchError(block = {
            withContext(Dispatchers.IO){
                val data = components.data?.get(0)?.paramsMobile?.let { cpmTopAdsUseCase.getCpmTopAdsData(it) }
                if (data!=null){
                    cpmTopAdsList.postValue(Success(data.componentList))
                    promotedText.postValue(Success(data.promotedText))
                    brandName.postValue(Success(data.brandName))
                    imageUrl.postValue(Success(data.imageUrl))
                }
            }


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

    fun getCpmTopAdsList(): LiveData<Result<ArrayList<ComponentsItem>>> = cpmTopAdsList
    fun getPromotedText(): LiveData<Result<String>> =  promotedText
    fun getBrandName(): LiveData<Result<String>> =  brandName
    fun getImageUrl(): LiveData<Result<String>> =  imageUrl


}


