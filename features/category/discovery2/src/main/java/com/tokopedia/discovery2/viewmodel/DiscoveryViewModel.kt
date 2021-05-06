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
import com.tokopedia.discovery.common.model.ProductCardOptionsModel
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.analytics.DISCOVERY_DEFAULT_PAGE_TYPE
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.data.PageInfo
import com.tokopedia.discovery2.datamapper.DiscoveryPageData
import com.tokopedia.discovery2.datamapper.discoComponentQuery
import com.tokopedia.discovery2.usecase.CustomTopChatUseCase
import com.tokopedia.discovery2.usecase.DiscoveryDataUseCase
import com.tokopedia.discovery2.usecase.quickcouponusecase.QuickCouponUseCase
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity.Companion.ACTIVE_TAB
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity.Companion.CATEGORY_ID
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity.Companion.COMPONENT_ID
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity.Companion.EMBED_CATEGORY
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity.Companion.PIN_PRODUCT
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity.Companion.PRODUCT_ID
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity.Companion.SOURCE
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity.Companion.TARGET_COMP_ID
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.masterproductcarditem.WishListManager
import com.tokopedia.discovery2.viewmodel.livestate.DiscoveryLiveState
import com.tokopedia.discovery2.viewmodel.livestate.GoToAgeRestriction
import com.tokopedia.discovery2.viewmodel.livestate.RouteToApplink
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
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
private const val IS_ADULT = 1

class DiscoveryViewModel @Inject constructor(private val discoveryDataUseCase: DiscoveryDataUseCase,
                                             private val userSession: UserSessionInterface,
                                             private val trackingQueue: TrackingQueue,
                                             private val pageLoadTimePerformanceInterface: PageLoadTimePerformanceInterface?) : BaseViewModel(), CoroutineScope {

    private val discoveryPageInfo = MutableLiveData<Result<PageInfo>>()
    private val discoveryFabLiveData = MutableLiveData<Result<ComponentsItem>>()
    private val discoveryResponseList = MutableLiveData<Result<List<ComponentsItem>>>()
    private val discoveryLiveStateData = MutableLiveData<DiscoveryLiveState>()
    private val wishlistUpdateLiveData = MutableLiveData<ProductCardOptionsModel>()
    private val discoveryBottomNavLiveData = MutableLiveData<Result<ComponentsItem>>()
    var pageIdentifier: String = ""
    var pageType: String = ""
    var pagePath: String = ""
    var campaignCode: String = ""
    var chooseAddressVisibilityLiveData = MutableLiveData<Boolean>()
    private var bottomTabNavDataComponent : ComponentsItem?  = null

    @Inject
    lateinit var customTopChatUseCase: CustomTopChatUseCase

    @Inject
    lateinit var quickCouponUseCase: QuickCouponUseCase

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()


    fun getDiscoveryData(queryParameterMap: MutableMap<String, String?>, userAddressData: LocalCacheModel?) {
        launchCatchError(
                block = {
                    pageLoadTimePerformanceInterface?.stopPreparePagePerformanceMonitoring()
                    pageLoadTimePerformanceInterface?.startNetworkRequestPerformanceMonitoring()
                    val data = discoveryDataUseCase.getDiscoveryPageDataUseCase(pageIdentifier, queryParameterMap, userAddressData)
                    pageLoadTimePerformanceInterface?.stopNetworkRequestPerformanceMonitoring()
                    pageLoadTimePerformanceInterface?.startRenderPerformanceMonitoring()
                    data.let {
                        setDiscoveryLiveState(it.pageInfo)
                        withContext(Dispatchers.Default) {
                            discoveryResponseList.postValue(Success(it.components))
                            findCustomTopChatComponentsIfAny(it.components)
                            findBottomTabNavDataComponentsIfAny(it.components)
                        }
                        setPageInfo(it)
                    }
                },
                onError = {
                    discoveryPageInfo.value = Fail(it)
                }
        )
    }

    private fun setDiscoveryLiveState(pageInfo: PageInfo) {
        if(!pageInfo.redirectionUrl.isNullOrEmpty() && discoveryLiveStateData.value != RouteToApplink(pageInfo.redirectionUrl ?: "")){
            discoveryLiveStateData.value = RouteToApplink(pageInfo.redirectionUrl ?: "")
        } else if(pageInfo.redirectionUrl.isNullOrEmpty() && pageInfo.isAdult == IS_ADULT && discoveryLiveStateData.value != GoToAgeRestriction(pageInfo.identifier, pageInfo.origin)){
            discoveryLiveStateData.value = GoToAgeRestriction(pageInfo.identifier, pageInfo.origin)
        }
    }

    private fun setPageInfo(discoPageData: DiscoveryPageData?) {
        discoPageData?.pageInfo?.let { pageInfoData ->
            pageType = if(pageInfoData.type.isNullOrEmpty()) DISCOVERY_DEFAULT_PAGE_TYPE else pageInfoData.type
            pagePath = pageInfoData.path ?: ""
            chooseAddressVisibilityLiveData.value = pageInfoData.showChooseAddress
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
    fun getDiscoveryLiveStateData(): LiveData<DiscoveryLiveState> = discoveryLiveStateData
    fun getDiscoveryBottomNavLiveData(): LiveData<Result<ComponentsItem>> = discoveryBottomNavLiveData

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
                SOURCE to intentUri.getQueryParameter(SOURCE),
                COMPONENT_ID to intentUri.getQueryParameter(COMPONENT_ID),
                ACTIVE_TAB to intentUri.getQueryParameter(ACTIVE_TAB),
                TARGET_COMP_ID to intentUri.getQueryParameter(TARGET_COMP_ID),
                PRODUCT_ID to intentUri.getQueryParameter(PRODUCT_ID),
                PIN_PRODUCT to intentUri.getQueryParameter(PIN_PRODUCT),
                CATEGORY_ID to intentUri.getQueryParameter(CATEGORY_ID),
                EMBED_CATEGORY to intentUri.getQueryParameter(EMBED_CATEGORY)
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

    fun getQueryParameterMapFromBundle(bundle: Bundle?): MutableMap<String, String?> {
        return mutableMapOf(
                SOURCE to bundle?.getString(SOURCE, ""),
                COMPONENT_ID to bundle?.getString(COMPONENT_ID, ""),
                ACTIVE_TAB to bundle?.getString(ACTIVE_TAB, ""),
                TARGET_COMP_ID to bundle?.getString(TARGET_COMP_ID, ""),
                PRODUCT_ID to bundle?.getString(PRODUCT_ID, ""),
                PIN_PRODUCT to bundle?.getString(PIN_PRODUCT, ""),
                CATEGORY_ID to getCategoryId(bundle),
                EMBED_CATEGORY to bundle?.getString(EMBED_CATEGORY, "")
        )
    }

    private fun getCategoryId(bundle: Bundle?): String? {
        discoComponentQuery?.let {
            return if (it[CATEGORY_ID].isNullOrEmpty()) {
                bundle?.getString(CATEGORY_ID, "") ?: ""
            } else {
                it[CATEGORY_ID]
            }
        }
        return bundle?.getString(CATEGORY_ID, "") ?: ""
    }

    private fun findBottomTabNavDataComponentsIfAny(components: List<ComponentsItem>?) {
        bottomTabNavDataComponent = components?.find {
            it.name == ComponentNames.BottomNavigation.componentName && it.renderByDefault
        }
        if (bottomTabNavDataComponent != null) {
            discoveryBottomNavLiveData.postValue(Success(bottomTabNavDataComponent!!))
        } else {
            discoveryBottomNavLiveData.postValue(Fail(Throwable()))
        }
    }

    fun getTabItem(position: Int): DataItem? {
        bottomTabNavDataComponent?.let {
            it.data?.let { tabData ->
                if (tabData.size > position) {
                    return tabData[position]
                }
            }
        }
        return null
    }

    fun updateWishlist(productCardOptionsModel: ProductCardOptionsModel){
        WishListManager.onWishListUpdated(productCardOptionsModel,this.pageIdentifier)
    }

    fun checkAddressVisibility() = chooseAddressVisibilityLiveData
    fun getAddressVisibilityValue() = chooseAddressVisibilityLiveData.value ?: false
}