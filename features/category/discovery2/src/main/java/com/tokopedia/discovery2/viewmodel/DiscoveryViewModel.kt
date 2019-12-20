package com.tokopedia.discovery2.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.GenerateUrl
import com.tokopedia.discovery2.data.DiscoveryResponse
import com.tokopedia.discovery2.usecase.DiscoveryDataUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.tradein_common.viewmodel.BaseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.withContext

class DiscoveryViewModel(application: Application) : BaseViewModel(application), CoroutineScope {

    private val discoveryResponse = MutableLiveData<Result<DiscoveryResponse>>()
    var pageIdentifier: String = ""

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()

    fun getDiscoveryData() {
        progBarVisibility.value = true
        launchCatchError(
                block = {
                    withContext(Dispatchers.IO){
                        val data = DiscoveryDataUseCase().getDiscoveryData(repository, GenerateUrl.getUrl(pageIdentifier))
                        data?.let {
                            discoveryResponse.postValue(Success(it))
                        }
                    }
                },
                onError = {
                    discoveryResponse.value = Fail(it)
                }
        )

    }

    fun getDiscoveryResponse():MutableLiveData<Result<DiscoveryResponse>> = discoveryResponse
}