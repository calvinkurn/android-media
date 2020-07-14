package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.cpmtopads

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.di.DaggerDiscoveryComponent
import com.tokopedia.discovery2.usecase.CpmTopAdsUseCase
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.topads.sdk.domain.model.CpmModel
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class CpmTopAdsViewModel(val application: Application, private val components: ComponentsItem, val position: Int) : DiscoveryBaseViewModel(), CoroutineScope {

    private val cpmTopAdsList = MutableLiveData<Result<ArrayList<ComponentsItem>>>()
    private val promotedText = MutableLiveData<Result<String>>()
    private val brandName = MutableLiveData<Result<String>>()
    private val imageUrl = MutableLiveData<Result<String>>()

    private val cpmData = MutableLiveData<Result<CpmModel>>()

    @Inject
    lateinit var cpmTopAdsUseCase: CpmTopAdsUseCase


    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()

    init {
        initDaggerInject()
    }

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

//                if (data != null) {
//                    cpmTopAdsList.postValue(Success(components.cpmData?.componentList as ArrayList<ComponentsItem>))
//                    promotedText.postValue(Success(components.cpmData?.promotedText as String))
//                    brandName.postValue(Success(components.cpmData?.brandName as String))
//                    imageUrl.postValue(Success(components.cpmData?.imageUrl as String))
//                }
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
    fun getPromotedText(): LiveData<Result<String>> = promotedText
    fun getBrandName(): LiveData<Result<String>> = brandName
    fun getImageUrl(): LiveData<Result<String>> = imageUrl

    fun getCpmData(): LiveData<Result<CpmModel>> = cpmData


}


