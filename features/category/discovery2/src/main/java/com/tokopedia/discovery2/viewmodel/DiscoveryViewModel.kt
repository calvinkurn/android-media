package com.tokopedia.discovery2.viewmodel

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.cartcommon.data.request.updatecart.UpdateCartRequest
import com.tokopedia.cartcommon.data.response.updatecart.UpdateCartV2Data
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.discovery.common.model.ProductCardOptionsModel
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.analytics.DISCOVERY_DEFAULT_PAGE_TYPE
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.data.PageInfo
import com.tokopedia.discovery2.data.ScrollData
import com.tokopedia.discovery2.datamapper.DiscoveryPageData
import com.tokopedia.discovery2.datamapper.discoComponentQuery
import com.tokopedia.discovery2.usecase.CustomTopChatUseCase
import com.tokopedia.discovery2.usecase.discoveryPageUseCase.DiscoveryDataUseCase
import com.tokopedia.discovery2.usecase.discoveryPageUseCase.DiscoveryInjectCouponDataUseCase
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
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext


private const val PINNED_COMPONENT_FAIL_STATUS = -1
private const val IS_ADULT = 1
private const val SCROLL_DEPTH = 100

class DiscoveryViewModel @Inject constructor(private val discoveryDataUseCase: DiscoveryDataUseCase,
                                             private val getMiniCartUseCase: GetMiniCartListSimplifiedUseCase,
                                             private val addToCartUseCase: AddToCartUseCase,
                                             private val updateCartUseCase: UpdateCartUseCase,
                                             private val deleteCartUseCase: DeleteCartUseCase,
                                             private val userSession: UserSessionInterface,
                                             private val trackingQueue: TrackingQueue,
                                             private val pageLoadTimePerformanceInterface: PageLoadTimePerformanceInterface?) : BaseViewModel(), CoroutineScope {

    private val discoveryPageInfo = MutableLiveData<Result<PageInfo>>()
    private val discoveryFabLiveData = MutableLiveData<Result<ComponentsItem>>()
    private val discoveryResponseList = MutableLiveData<Result<List<ComponentsItem>>>()
    private val discoveryLiveStateData = MutableLiveData<DiscoveryLiveState>()
    private val discoveryBottomNavLiveData = MutableLiveData<Result<ComponentsItem>>()
    private val discoveryAnchorTabLiveData = MutableLiveData<Result<ComponentsItem>>()

    var miniCartSimplifiedData: MiniCartSimplifiedData? = null

    val miniCartAdd: LiveData<Result<AddToCartDataModel>>
        get() = _miniCartAdd
    private val _miniCartAdd = SingleLiveEvent<Result<AddToCartDataModel>>()

    val miniCart: LiveData<Result<MiniCartSimplifiedData>>
        get() = _miniCart
    private val _miniCart = MutableLiveData<Result<MiniCartSimplifiedData>>()

    val miniCartUpdate: LiveData<Result<UpdateCartV2Data>>
        get() = _miniCartUpdate
    private val _miniCartUpdate = SingleLiveEvent<Result<UpdateCartV2Data>>()

    val miniCartRemove: LiveData<Result<Pair<String,String>>>
        get() = _miniCartRemove
    private val _miniCartRemove = SingleLiveEvent<Result<Pair<String,String>>>()

    val miniCartOperationFailed:LiveData<Pair<Int,Int>>
        get() = _miniCartOperationFailed
    private val _miniCartOperationFailed = SingleLiveEvent<Pair<Int,Int>>()

    private val _scrollState  = MutableLiveData<ScrollData>()
    val scrollState: LiveData<ScrollData> = _scrollState

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

    @Inject
    lateinit var discoveryInjectCouponDataUseCase: DiscoveryInjectCouponDataUseCase

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()


    fun getMiniCartItem(productId: String): MiniCartItem? {
        val items = miniCartSimplifiedData?.miniCartItems.orEmpty()
        return items.firstOrNull { it.productId == productId }
    }

    fun addProductToCart(
        parentPosition:Int,
        position:Int,
        productId: String,
        quantity: Int,
        shopId: String
    ) {
        val miniCartItem = getMiniCartItem(productId)
        when {
            miniCartItem == null -> addItemToCart(parentPosition,position,productId, shopId, quantity)
            quantity.isZero() -> removeItemCart(parentPosition,position,miniCartItem)
            else -> updateItemCart(parentPosition,position,miniCartItem, quantity)
        }
    }


    private fun addItemToCart(
        parentPosition:Int,
        position:Int,
        productId: String,
        shopId: String,
        quantity: Int) {
        val addToCartRequestParams = AddToCartUseCase.getMinimumParams(
            productId = productId,
            shopId = shopId,
            quantity = quantity
        )
        addToCartUseCase.setParams(addToCartRequestParams)
        addToCartUseCase.execute({
            _miniCartAdd.postValue(Success(it))
        }, {
            _miniCartAdd.postValue(Fail(it))
            _miniCartOperationFailed.postValue(Pair(parentPosition,position))
        })
    }

    private fun updateItemCart(
        parentPosition:Int,
        position:Int,
        miniCartItem: MiniCartItem,
        quantity: Int) {
        miniCartItem.quantity = quantity
        val updateCartRequest = UpdateCartRequest(
            cartId = miniCartItem.cartId,
            quantity = miniCartItem.quantity,
            notes = miniCartItem.notes
        )
        updateCartUseCase.setParams(
            updateCartRequestList = listOf(updateCartRequest),
            source = UpdateCartUseCase.VALUE_SOURCE_UPDATE_QTY_NOTES,
        )
        updateCartUseCase.execute({
            _miniCartUpdate.value = Success(it)
        }, {
            _miniCartUpdate.postValue(Fail(it))
            _miniCartOperationFailed.postValue(Pair(parentPosition,position))
        })
    }

    fun getMiniCart(shopId: List<String>, warehouseId: String?) {
        if(!shopId.isNullOrEmpty() && warehouseId.toLongOrZero() != 0L && userSession.isLoggedIn) {
            launchCatchError(block = {
                getMiniCartUseCase.setParams(shopId)
                getMiniCartUseCase.execute({
                    miniCartSimplifiedData = it
                    _miniCart.postValue(Success(it))
                }, {
                    _miniCart.postValue(Fail(it))
                })
            }) {
                _miniCart.postValue(Fail(it))
            }
        }
    }

    private fun removeItemCart(parentPosition: Int, position: Int, miniCartItem: MiniCartItem) {
        deleteCartUseCase.setParams(
            cartIdList = listOf(miniCartItem.cartId)
        )
        deleteCartUseCase.execute({
            val productId = miniCartItem.productId
            val data = Pair(productId, it.data.message.joinToString(separator = ", "))
            _miniCartRemove.postValue(Success(data))
        }, {
            _miniCartRemove.postValue(Fail(it))
            _miniCartOperationFailed.postValue(Pair(parentPosition, position))
        })
    }

    fun getDiscoveryData(queryParameterMap: MutableMap<String, String?>, userAddressData: LocalCacheModel?) {
        pageLoadTimePerformanceInterface?.stopPreparePagePerformanceMonitoring()
        launchCatchError(
                block = {
                    pageLoadTimePerformanceInterface?.startNetworkRequestPerformanceMonitoring()
                    val data = discoveryDataUseCase.getDiscoveryPageDataUseCase(pageIdentifier, queryParameterMap, userAddressData)
                    pageLoadTimePerformanceInterface?.stopNetworkRequestPerformanceMonitoring()
                    pageLoadTimePerformanceInterface?.startRenderPerformanceMonitoring()
                    data.let {
                        setDiscoveryLiveState(it.pageInfo)
                        setPageInfo(it)
                        withContext(Dispatchers.Default) {
                            discoveryResponseList.postValue(Success(it.components))
                            findCustomTopChatComponentsIfAny(it.components)
                            findBottomTabNavDataComponentsIfAny(it.components)
                            findAnchorTabComponentsIfAny(it.components)
                        }
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
    fun getDiscoveryAnchorTabLiveData(): LiveData<Result<ComponentsItem>> = discoveryAnchorTabLiveData

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
    private fun findAnchorTabComponentsIfAny(components: List<ComponentsItem>?) {
        val tabDataComponent = components?.find {
            it.name == ComponentNames.AnchorTabs.componentName && it.renderByDefault
        }
        if (tabDataComponent != null) {
            discoveryAnchorTabLiveData.postValue(Success(tabDataComponent))
        } else {
            discoveryAnchorTabLiveData.postValue(Fail(Throwable()))
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

    fun sendCouponInjectDataForLoggedInUsers() {
        launchCatchError(
                block = {
                    if (userSession.isLoggedIn) {
                        discoveryInjectCouponDataUseCase.sendDiscoveryInjectCouponData()
                    }
                },
                onError = {
                    discoveryPageInfo.value = Fail(it)
                }
        )
    }

    fun getScrollDepth(offset: Int, extent: Int, range: Int): Int {
        return if(range > 0) SCROLL_DEPTH * (offset + extent) / range else 0
    }

    fun getShareUTM(data:PageInfo) : String{
        var campaignCode = if(data.campaignCode.isNullOrEmpty()) "0" else data.campaignCode
        if(data.campaignCode != null && data.campaignCode.length > 11){
            campaignCode = data.campaignCode.substring(0,11)
        }
        return "${data.identifier}-${campaignCode}"
    }

    fun updateScroll(dx: Int, dy: Int, newState: Int) {
        _scrollState.value = ScrollData(dx,dy,newState)
    }

    fun resetScroll(){
        _scrollState.value = null
    }
}