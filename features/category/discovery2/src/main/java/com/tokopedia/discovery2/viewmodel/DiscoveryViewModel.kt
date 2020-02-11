package com.tokopedia.discovery2.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DiscoveryResponse
import com.tokopedia.discovery2.usecase.DiscoveryDataUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.tradein_common.viewmodel.BaseViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class DiscoveryViewModel @Inject constructor(private val discoveryDataUseCase: DiscoveryDataUseCase,
                                             private val userSession: UserSessionInterface) : BaseViewModel(), CoroutineScope {

    private val discoveryPageTitle = MutableLiveData<Result<String>>()
    private val discoveryResponseList = MutableLiveData<Result<ArrayList<ComponentsItem>>>()
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
                            withContext(Dispatchers.Default) {
                                checkLoginAndUpdateList(data.components)
                            }
                            discoveryPageTitle.postValue(Success(it.title ?: ""))
                        }
                    }
                },
                onError = {
                    discoveryPageTitle.value = Fail(it)
                }
        )

    }

    private fun checkLoginAndUpdateList(components: List<ComponentsItem>?) {
        if (!userSession.isLoggedIn) {
            val list = components?.filter { it.name != "topads" }
            discoveryResponseList.postValue(Success(list as ArrayList<ComponentsItem>))

        } else {
            discoveryResponseList.postValue(Success(components as ArrayList<ComponentsItem>))
        }

    }

    fun getDiscoveryPageTitle(): LiveData<Result<String>> = discoveryPageTitle
    fun getDiscoveryResponseList(): LiveData<Result<ArrayList<ComponentsItem>>> = discoveryResponseList


}