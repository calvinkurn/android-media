package com.tokopedia.discovery2.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.usecase.CustomTopChatUseCase
import com.tokopedia.discovery2.usecase.DiscoveryDataUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
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
    private val discoveryFabLiveData = MutableLiveData<Result<ComponentsItem>>()
    private val discoveryResponseList = MutableLiveData<Result<ArrayList<ComponentsItem>>>()
    var pageIdentifier: String = ""

    @Inject
    lateinit var customTopChatUseCase: CustomTopChatUseCase

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()


    fun getDiscoveryData() {
        launchCatchError(
                block = {
                    withContext(Dispatchers.IO) {
                        val data = discoveryDataUseCase.getDiscoveryData(pageIdentifier)
                        data.let {
                            withContext(Dispatchers.Default) {
                                checkLoginAndUpdateList(data.components)
                            }
                            withContext(Dispatchers.Default) {
                                findCustomTopChatComponentsIfAny(data.components)
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

    private fun findCustomTopChatComponentsIfAny(components: List<ComponentsItem>?) {
        val customTopChatComponent = components?.find {
            it.name == ComponentNames.CustomTopchat.componentName
        }
        if (customTopChatComponent != null) {
            discoveryFabLiveData.postValue(Success(customTopChatComponent))
        } else {
            discoveryFabLiveData.postValue(Fail(Throwable()))
        }

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
    fun getDiscoveryFabLiveData(): LiveData<Result<ComponentsItem>> = discoveryFabLiveData

    private fun fetchTopChatMessageId(context: Context, appLinks: String, shopId: Int) {
        val queryMap:MutableMap<String, Any> = mutableMapOf("fabShopId" to shopId, "source" to "discovery")
        launchCatchError(
                block = {
                    val customTopChatResponse = customTopChatUseCase.getCustomTopChatMessageId(queryMap)
                    customTopChatResponse?.let {
                        it.chatExistingChat?.let { chatExistingChat ->
                            if (chatExistingChat.messageId != 0) {
                                RouteManager.route(context, appLinks.plus(chatExistingChat.messageId.toString()))
                            }
                        }
                    }
                },
                onError = {

                }
        )
    }

    fun openCustomTopChat(context: Context, appLinks: String, shopId: Int) {
        if (!userSession.isLoggedIn) {
            RouteManager.route(context, ApplinkConst.LOGIN)
        } else {
            fetchTopChatMessageId(context, appLinks, shopId)
        }
    }
}