package com.tokopedia.discovery2.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.PageInfo
import com.tokopedia.discovery2.repository.discoveryPage.DiscoveryUIConfigGQLRepository
import com.tokopedia.discovery2.usecase.CustomTopChatUseCase
import com.tokopedia.discovery2.usecase.DiscoveryDataUseCase
import com.tokopedia.discovery2.usecase.quickcouponusecase.QuickCouponUseCase
import com.tokopedia.discovery2.viewcontrollers.activity.REACT_NATIVE
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.*
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class DiscoveryViewModel @Inject constructor(private val discoveryDataUseCase: DiscoveryDataUseCase,
                                             private val discoveryUIConfigRepo: DiscoveryUIConfigGQLRepository,
                                             private val userSession: UserSessionInterface,
                                             private val trackingQueue: TrackingQueue) : BaseViewModel(), CoroutineScope {

    private val discoveryPageInfo = MutableLiveData<Result<PageInfo>>()
    private val discoveryFabLiveData = MutableLiveData<Result<ComponentsItem>>()
    private val discoveryResponseList = MutableLiveData<Result<List<ComponentsItem>>>()
    private val discoveryUIConfig = MutableLiveData<Result<String>>()
    private val phoneVerificationLiveData = MutableLiveData<Boolean>()
    var pageIdentifier: String = ""
    var pageType: String = ""
    var pagePath: String = ""

    @Inject
    lateinit var customTopChatUseCase: CustomTopChatUseCase
    @Inject
    lateinit var quickCouponUseCase: QuickCouponUseCase

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()


    fun getDiscoveryData() {
        launchCatchError(
                block = {
                    val data = discoveryDataUseCase.getDiscoveryPageDataUseCase(pageIdentifier)
                    data.let {
                        withContext(Dispatchers.Default) {
                            discoveryResponseList.postValue(Success(it.components))

                        }
                        setPageInfo(it.pageInfo)
                    }
                },
                onError = {
                    discoveryPageInfo.value = Fail(it)
                }
        )

    }

    fun getDiscoveryUIConfig() {
        launchCatchError(
                block = {
                    val data = discoveryUIConfigRepo.getDiscoveryUIConfigData()
                    setUIConfig(data.discoveryPageUIConfig?.data?.config)
                },
                onError = {
                    discoveryUIConfig.postValue(Fail(it))
                }
        )
    }

    private fun setUIConfig(config: String?) {
        discoveryUIConfig.postValue(Success(config ?: REACT_NATIVE))
    }

    private fun setPageInfo(pageInfo: PageInfo?) {
        if (pageInfo != null) {
            pageType = pageInfo.type ?: ""
            pagePath = pageInfo.path ?: ""
            discoveryPageInfo.value = Success(pageInfo)
        }
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


    fun getBitmapFromURL(src: String?): Bitmap? = runBlocking {
        getBitmap(src).await()
    }

    fun getBitmap(src: String?) = async(Dispatchers.IO) {
        val url = URL(src)
        val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
        connection.doInput = true
        connection.connect()
        val input: InputStream = connection.inputStream
        BitmapFactory.decodeStream(input)
    }

    fun getDiscoveryPageInfo(): LiveData<Result<PageInfo>> = discoveryPageInfo
    fun getDiscoveryResponseList(): LiveData<Result<List<ComponentsItem>>> = discoveryResponseList
    fun getDiscoveryFabLiveData(): LiveData<Result<ComponentsItem>> = discoveryFabLiveData
    fun getDiscoveryUIConfigLiveData(): LiveData<Result<String>> = discoveryUIConfig

    private fun fetchTopChatMessageId(context: Context, appLinks: String, shopId: Int) {
        val queryMap: MutableMap<String, Any> = mutableMapOf("fabShopId" to shopId, "source" to "discovery")
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
                    it.printStackTrace()
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

    fun phoneVerificationLiveStatus() = phoneVerificationLiveData

    fun checkMobileVerificationStatus(){
        launchCatchError(
                block = {
                    quickCouponUseCase.getMobileVerificationStatus().verificationStatus?.let {
                        phoneVerificationLiveData.value = it.phoneVerified
                    }
                },
                onError = {
                    it.printStackTrace()
                }
        )
    }

    override fun doOnPause() {
        super.doOnPause()
        trackingQueue.sendAll()
    }

    override fun doOnDestroy() {
        super.doOnDestroy()
        clearPageData()
    }

    fun clearPageData() {
        discoveryDataUseCase.clearPage(pageIdentifier)
    }
}