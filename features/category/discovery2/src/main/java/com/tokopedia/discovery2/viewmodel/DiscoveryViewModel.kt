package com.tokopedia.discovery2.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.discovery2.data.DiscoveryResponse
import com.tokopedia.discovery2.usecase.DiscoveryDataUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.tradein_common.viewmodel.BaseViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class DiscoveryViewModel @Inject constructor(private val discoveryDataUseCase: DiscoveryDataUseCase) : BaseViewModel(), CoroutineScope {

    private val discoveryResponse = MutableLiveData<Result<DiscoveryResponse>>()
    var pageIdentifier: String = ""

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()


    fun getDiscoveryData() {
        progBarVisibility.value = true
        launchCatchError(
                block = {
                    withContext(Dispatchers.IO) {
                        val data = discoveryDataUseCase.getDiscoveryData(pageIdentifier)
                        data.let {
                            discoveryResponse.postValue(Success(it))
                        }
                    }
                },
                onError = {
                    discoveryResponse.value = Fail(it)
                }
        )

    }

    fun getDiscoveryResponse(): MutableLiveData<Result<DiscoveryResponse>> = discoveryResponse



}