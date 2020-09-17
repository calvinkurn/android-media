package com.tokopedia.discovery2.viewmodel

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.PageInfo
import com.tokopedia.discovery2.datamapper.DiscoveryPageData
import com.tokopedia.discovery2.repository.discoveryPage.DiscoveryUIConfigGQLRepository
import com.tokopedia.discovery2.usecase.CustomTopChatUseCase
import com.tokopedia.discovery2.usecase.DiscoveryDataUseCase
import com.tokopedia.discovery2.usecase.quickcouponusecase.QuickCouponUseCase
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity.Companion.PINNED_ACTIVE_TAB
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity.Companion.PINNED_COMPONENT_ID
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity.Companion.PINNED_COMP_ID
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity.Companion.PRODUCT_ID
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity.Companion.SOURCE_QUERY
import com.tokopedia.discovery2.viewcontrollers.activity.REACT_NATIVE
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.trackingoptimizer.TrackingQueue
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


private const val PINNED_COMPONENT_FAIL_STATUS = -1

class DiscoveryViewModel @Inject constructor(private val discoveryDataUseCase: DiscoveryDataUseCase,
                                             private val discoveryUIConfigRepo: DiscoveryUIConfigGQLRepository,
                                             private val userSession: UserSessionInterface,
                                             private val trackingQueue: TrackingQueue,
                                             private val pageLoadTimePerformanceInterface: PageLoadTimePerformanceInterface?) : BaseViewModel(), CoroutineScope {

    private val discoveryPageInfo = MutableLiveData<Result<PageInfo>>()
    private val discoveryFabLiveData = MutableLiveData<Result<ComponentsItem>>()
    private val discoveryResponseList = MutableLiveData<Result<List<ComponentsItem>>>()
    private val discoveryUIConfig = MutableLiveData<Result<String>>()
    var pageIdentifier: String = ""
    var pageType: String = ""
    var pagePath: String = ""
    var campaignCode: String = ""

    @Inject
    lateinit var customTopChatUseCase: CustomTopChatUseCase

    @Inject
    lateinit var quickCouponUseCase: QuickCouponUseCase

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()


    fun getDiscoveryData(queryParameterMap: Map<String, String?>) {
        launchCatchError(
                block = {
                    pageLoadTimePerformanceInterface?.stopPreparePagePerformanceMonitoring()
                    pageLoadTimePerformanceInterface?.startNetworkRequestPerformanceMonitoring()
                    val data = discoveryDataUseCase.getDiscoveryPageDataUseCase(pageIdentifier, queryParameterMap)
                    pageLoadTimePerformanceInterface?.stopNetworkRequestPerformanceMonitoring()
                    pageLoadTimePerformanceInterface?.startRenderPerformanceMonitoring()
                    data.let {
                        withContext(Dispatchers.Default) {
                            discoveryResponseList.postValue(Success(it.components))

                        }
                        setPageInfo(it)
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
                    discoveryUIConfig.postValue(Success(REACT_NATIVE))
                }
        )
    }

    private fun setUIConfig(config: String?) {
        discoveryUIConfig.postValue(Success(config ?: REACT_NATIVE))
    }

    private fun setPageInfo(discoPageData: DiscoveryPageData?) {
        discoPageData?.pageInfo?.let { pageInfoData ->
            pageType = pageInfoData.type ?: ""
            pagePath = pageInfoData.path ?: ""
            pageInfoData.additionalInfo = discoPageData.additionalInfo
            campaignCode = pageInfoData.campaignCode ?: ""
            discoveryPageInfo.value = Success(pageInfoData)
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

    fun getMapOfQueryParameter(intentUri: Uri): Map<String, String?> {
        return mapOf(
                SOURCE_QUERY to intentUri.getQueryParameter(SOURCE_QUERY),
                PINNED_COMPONENT_ID to intentUri.getQueryParameter(PINNED_COMPONENT_ID),
                PINNED_ACTIVE_TAB to intentUri.getQueryParameter(PINNED_ACTIVE_TAB),
                PINNED_COMP_ID to intentUri.getQueryParameter(PINNED_COMP_ID),
                PRODUCT_ID to intentUri.getQueryParameter(PRODUCT_ID)
        )
    }

    fun scrollToPinnedComponent(listComponent: List<ComponentsItem>, pinnedComponentId: String?): Int {
        listComponent.forEachIndexed { index, componentsItem ->
            if (componentsItem.id == pinnedComponentId) {
                return index
            }
        }
        return PINNED_COMPONENT_FAIL_STATUS
    }

    fun getQueryParameterMapFromBundle(bundle: Bundle?): Map<String, String?> {
        return mapOf(
                PINNED_ACTIVE_TAB to bundle?.getString(PINNED_ACTIVE_TAB, ""),
                PINNED_COMP_ID to bundle?.getString(PINNED_COMP_ID, ""),
                PRODUCT_ID to bundle?.getString(PRODUCT_ID, "")
        )
    }
}