package com.tokopedia.discovery2.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
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

class DiscoveryViewModel(application: Application) : BaseViewModel(application), CoroutineScope {

    private val discoveryResponse = MutableLiveData<Result<DiscoveryResponse>>()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()

    fun getDiscoveryData(endPoint: String) {
        progBarVisibility.value = true
        launchCatchError(
                block = {
                    val data = DiscoveryDataUseCase().getDiscoveryData(repository, endPoint)
                    Log.d("langsukdata", data.title)
                    data?.let {
                        discoveryResponse.value = Success(it)
                    }
                },
                onError = {
                    Log.d("langsukdataerr", "jdguycwveu")
                    discoveryResponse.value = Fail(it)
                }
        )

    }

    fun getDiscoveryResponse():MutableLiveData<Result<DiscoveryResponse>> = discoveryResponse
}