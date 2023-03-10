package com.tokopedia.discovery2.viewmodel

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery
import com.tokopedia.atc_common.AtcFromExternalSource
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.cartcommon.data.request.updatecart.UpdateCartRequest
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.common_sdk_affiliate_toko.model.AffiliatePageDetail
import com.tokopedia.common_sdk_affiliate_toko.model.AffiliateSdkPageSource
import com.tokopedia.common_sdk_affiliate_toko.model.AffiliateSdkProductInfo
import com.tokopedia.common_sdk_affiliate_toko.utils.AffiliateAtcSource
import com.tokopedia.common_sdk_affiliate_toko.utils.AffiliateCookieHelper
import com.tokopedia.discovery.common.model.ProductCardOptionsModel
import com.tokopedia.discovery.common.utils.URLParser
import com.tokopedia.discovery2.CONSTANT_0
import com.tokopedia.discovery2.CONSTANT_11
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.Utils.Companion.RPC_FILTER_KEY
import com.tokopedia.discovery2.Utils.Companion.toDecodedString
import com.tokopedia.discovery2.analytics.DISCOVERY_DEFAULT_PAGE_TYPE
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.data.PageInfo
import com.tokopedia.discovery2.data.ScrollData
import com.tokopedia.discovery2.data.productcarditem.DiscoATCRequestParams
import com.tokopedia.discovery2.data.productcarditem.DiscoveryAddToCartDataModel
import com.tokopedia.discovery2.data.productcarditem.DiscoveryRemoveFromCartDataModel
import com.tokopedia.discovery2.data.productcarditem.DiscoveryUpdateCartDataModel
import com.tokopedia.discovery2.datamapper.DiscoveryPageData
import com.tokopedia.discovery2.datamapper.discoComponentQuery
import com.tokopedia.discovery2.datamapper.discoveryPageData
import com.tokopedia.discovery2.usecase.CustomTopChatUseCase
import com.tokopedia.discovery2.usecase.discoveryPageUseCase.DiscoveryDataUseCase
import com.tokopedia.discovery2.usecase.discoveryPageUseCase.DiscoveryInjectCouponDataUseCase
import com.tokopedia.discovery2.usecase.quickcouponusecase.QuickCouponUseCase
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity.Companion.ACTIVE_TAB
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity.Companion.AFFILIATE_UNIQUE_ID
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity.Companion.CAMPAIGN_ID
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity.Companion.CATEGORY_ID
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity.Companion.CHANNEL
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity.Companion.COMPONENT_ID
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity.Companion.DYNAMIC_SUBTITLE
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity.Companion.EMBED_CATEGORY
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity.Companion.PIN_PRODUCT
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity.Companion.PRODUCT_ID
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity.Companion.QUERY_PARENT
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity.Companion.RECOM_PRODUCT_ID
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity.Companion.SHOP_ID
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity.Companion.SOURCE
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity.Companion.TARGET_COMP_ID
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity.Companion.TARGET_TITLE_ID
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity.Companion.VARIANT_ID
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.masterproductcarditem.WishListManager
import com.tokopedia.discovery2.viewcontrollers.adapter.factory.ComponentsList
import com.tokopedia.discovery2.viewmodel.livestate.DiscoveryLiveState
import com.tokopedia.discovery2.viewmodel.livestate.GoToAgeRestriction
import com.tokopedia.discovery2.viewmodel.livestate.RouteToApplink
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.data.getMiniCartItemProduct
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.minicart.common.domain.usecase.MiniCartSource
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import kotlinx.coroutines.*
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
                                             private val pageLoadTimePerformanceInterface: PageLoadTimePerformanceInterface?,
                                             private var affiliateCookieHelper : AffiliateCookieHelper) : BaseViewModel(), CoroutineScope {

    private val discoveryPageInfo = MutableLiveData<Result<PageInfo>>()
    private val discoveryFabLiveData = MutableLiveData<Result<ComponentsItem>>()
    private val discoveryResponseList = MutableLiveData<Result<List<ComponentsItem>>>()
    private val discoveryLiveStateData = MutableLiveData<DiscoveryLiveState>()
    private val discoveryBottomNavLiveData = MutableLiveData<Result<ComponentsItem>>()
    private val discoveryAnchorTabLiveData = MutableLiveData<Result<ComponentsItem>>()

    var miniCartSimplifiedData: MiniCartSimplifiedData? = null

    val miniCartAdd: LiveData<Result<DiscoveryAddToCartDataModel>>
        get() = _miniCartAdd
    private val _miniCartAdd = SingleLiveEvent<Result<DiscoveryAddToCartDataModel>>()

    val miniCart: LiveData<Result<MiniCartSimplifiedData>>
        get() = _miniCart
    private val _miniCart = MutableLiveData<Result<MiniCartSimplifiedData>>()

    val miniCartUpdate: LiveData<Result<DiscoveryUpdateCartDataModel>>
        get() = _miniCartUpdate
    private val _miniCartUpdate = SingleLiveEvent<Result<DiscoveryUpdateCartDataModel>>()

    val miniCartRemove: LiveData<Result<DiscoveryRemoveFromCartDataModel>>
        get() = _miniCartRemove
    private val _miniCartRemove = SingleLiveEvent<Result<DiscoveryRemoveFromCartDataModel>>()

    val miniCartOperationFailed:LiveData<Pair<Int,Int>>
        get() = _miniCartOperationFailed
    private val _miniCartOperationFailed = SingleLiveEvent<Pair<Int,Int>>()

    private val _scrollState  = MutableLiveData<ScrollData>()
    val scrollState: LiveData<ScrollData> = _scrollState

    var pageIdentifier: String = ""
    var pageType: String = ""
    var pagePath: String = ""
    var campaignCode: String = ""
    private var chooseAddressVisibilityLiveData = MutableLiveData<Boolean>()
    var isAffiliateInitialized = false
    private var randomUUIDAffiliate: String? = null
    private var bottomTabNavDataComponent : ComponentsItem?  = null

    @Inject
    lateinit var customTopChatUseCase: CustomTopChatUseCase

    @Inject
    lateinit var quickCouponUseCase: QuickCouponUseCase

    @Inject
    lateinit var discoveryInjectCouponDataUseCase: DiscoveryInjectCouponDataUseCase

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()


    private fun getMiniCartItem(productId: String): MiniCartItem.MiniCartItemProduct? {
        val items = miniCartSimplifiedData?.miniCartItems.orEmpty()
        return items.getMiniCartItemProduct(productId)
    }

    fun addProductToCart(
        discoATCRequestParams: DiscoATCRequestParams
    ) {
        val miniCartItem = if (!discoATCRequestParams.isGeneralCartATC)
            getMiniCartItem(discoATCRequestParams.productId) else null
        when {
            miniCartItem == null -> addItemToCart(
                discoATCRequestParams
            )
            discoATCRequestParams.quantity.isZero() -> removeItemCart(
                miniCartItem,
                discoATCRequestParams
            )
            else -> updateItemCart(
                miniCartItem,
                discoATCRequestParams
            )
        }
    }


    private fun addItemToCart(
        discoATCRequestParams: DiscoATCRequestParams
    ) {
        val addToCartRequestParams = AddToCartUseCase.getMinimumParams(
            productId = discoATCRequestParams.productId,
            shopId = discoATCRequestParams.shopId ?: "",
            quantity = discoATCRequestParams.quantity,
            atcExternalSource = if (isAffiliateInitialized)
                AtcFromExternalSource.ATC_FROM_DISCOVERY
            else
                AtcFromExternalSource.ATC_FROM_OTHERS
        )
        addToCartUseCase.setParams(addToCartRequestParams)
        addToCartUseCase.execute({
            _miniCartAdd.postValue(Success(DiscoveryAddToCartDataModel(it, discoATCRequestParams)))
            handleATCAffiliate(discoATCRequestParams)
        }, {
            _miniCartAdd.postValue(Fail(it))
            _miniCartOperationFailed.postValue(
                Pair(
                    discoATCRequestParams.parentPosition,
                    discoATCRequestParams.position
                )
            )
        })
    }

    private fun handleATCAffiliate(discoATCRequestParams: DiscoATCRequestParams) {
        if (isAffiliateInitialized) {
            var affiliateUID = ""
            var affiliateChannel = ""
            discoComponentQuery?.let {
                affiliateUID = it[AFFILIATE_UNIQUE_ID] ?: ""
                affiliateChannel = it[CHANNEL] ?: ""
            }
            val productInfo =
                AffiliateSdkProductInfo(
                    discoATCRequestParams.requestingComponent.data?.firstOrNull()?.categoryDeptId
                        ?: "",
                    false,
                    discoATCRequestParams.quantity
                )
            val pageDetail = AffiliatePageDetail(
                source = AffiliateSdkPageSource.DirectATC(
                    AffiliateAtcSource.DISCOVERY_PAGE,
                    discoATCRequestParams.shopId,
                    productInfo
                ),
                pageName = pageIdentifier,
                pageId = "0"
            )
            launchCatchError(
                block = {
                    affiliateCookieHelper.initCookie(
                        affiliateUUID = affiliateUID,
                        affiliateChannel = affiliateChannel,
                        affiliatePageDetail = pageDetail
                    )
                },
                onError = {

                }
            )
        }
    }

    private fun updateItemCart(
            miniCartItem: MiniCartItem.MiniCartItemProduct,
            discoATCRequestParams: DiscoATCRequestParams
    ) {
        miniCartItem.quantity = discoATCRequestParams.quantity
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
            _miniCartUpdate.value = Success(
                DiscoveryUpdateCartDataModel(
                    it,
                    discoATCRequestParams,
                    miniCartItem.cartId
                )
            )
        }, {
            _miniCartUpdate.postValue(Fail(it))
            _miniCartOperationFailed.postValue(
                Pair(
                    discoATCRequestParams.parentPosition,
                    discoATCRequestParams.position
                )
            )
        })
    }

    fun getMiniCart(shopId: List<String>, warehouseId: String?) {
        if(!shopId.isNullOrEmpty() && warehouseId.toLongOrZero() != 0L && userSession.isLoggedIn) {
            launchCatchError(block = {
                getMiniCartUseCase.setParams(shopId, MiniCartSource.TokonowDiscoveryPage)
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

    private fun removeItemCart(
        miniCartItem: MiniCartItem.MiniCartItemProduct,
        discoATCRequestParams: DiscoATCRequestParams
    ) {
        deleteCartUseCase.setParams(
            cartIdList = listOf(miniCartItem.cartId)
        )
        deleteCartUseCase.execute({
            _miniCartRemove.postValue(
                Success(
                    DiscoveryRemoveFromCartDataModel(
                        miniCartItem.productId,
                        it.data.message.joinToString(separator = ", "),
                        discoATCRequestParams,
                        cartId = miniCartItem.cartId
                    )
                )
            )
        }, {
            _miniCartRemove.postValue(Fail(it))
            _miniCartOperationFailed.postValue(
                Pair(
                    discoATCRequestParams.parentPosition,
                    discoATCRequestParams.position
                )
            )
        })
    }

    fun getDiscoveryData(queryParameterMap: MutableMap<String, String?>, userAddressData: LocalCacheModel?) {
        pageLoadTimePerformanceInterface?.stopPreparePagePerformanceMonitoring()
        launchCatchError(
                block = {
                    pageLoadTimePerformanceInterface?.startNetworkRequestPerformanceMonitoring()
                    var queryParameterMapWithRpc = mutableMapOf<String, String>()
                    var queryParameterMapWithoutRpc = mutableMapOf<String, String>()
                    if (discoveryPageData[pageIdentifier] != null) {
                        discoveryPageData[pageIdentifier]?.let {
                            queryParameterMapWithRpc = it.queryParamMapWithRpc
                            queryParameterMapWithoutRpc = it.queryParamMapWithoutRpc
                        }
                    } else {
                        setParameterMap(queryParameterMap, queryParameterMapWithRpc, queryParameterMapWithoutRpc)
                    }
                    val data = discoveryDataUseCase.getDiscoveryPageDataUseCase(pageIdentifier, queryParameterMap, queryParameterMapWithRpc, queryParameterMapWithoutRpc, userAddressData)
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
                EMBED_CATEGORY to intentUri.getQueryParameter(EMBED_CATEGORY),
                RECOM_PRODUCT_ID to intentUri.getQueryParameter(RECOM_PRODUCT_ID),
                DYNAMIC_SUBTITLE to intentUri.getQueryParameter(DYNAMIC_SUBTITLE),
                TARGET_TITLE_ID to intentUri.getQueryParameter(TARGET_TITLE_ID),
                CAMPAIGN_ID to intentUri.getQueryParameter(CAMPAIGN_ID),
                VARIANT_ID to intentUri.getQueryParameter(VARIANT_ID),
                SHOP_ID to intentUri.getQueryParameter(SHOP_ID),
                QUERY_PARENT to intentUri.query,
                AFFILIATE_UNIQUE_ID to intentUri.getQueryParameter(AFFILIATE_UNIQUE_ID),
                CHANNEL to intentUri.getQueryParameter(CHANNEL),

                )
    }

    fun scrollToPinnedComponent(listComponent: List<ComponentsItem>, pinnedComponentId: String?): Pair<Int,Boolean> {
        var isTabsAbovePinnedComponent = false
        listComponent.forEachIndexed { index, componentsItem ->
            if(componentsItem.name == ComponentsList.Tabs.componentName){
                isTabsAbovePinnedComponent = true
            }
            if (componentsItem.id == pinnedComponentId) {
                return Pair(index,isTabsAbovePinnedComponent)
            }
        }
        return Pair(PINNED_COMPONENT_FAIL_STATUS,isTabsAbovePinnedComponent)
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
                EMBED_CATEGORY to bundle?.getString(EMBED_CATEGORY, ""),
                RECOM_PRODUCT_ID to bundle?.getString(RECOM_PRODUCT_ID,""),
                DYNAMIC_SUBTITLE to bundle?.getString(DYNAMIC_SUBTITLE,""),
                TARGET_TITLE_ID to bundle?.getString(TARGET_TITLE_ID,""),
                CAMPAIGN_ID to bundle?.getString(CAMPAIGN_ID,""),
                VARIANT_ID to bundle?.getString(VARIANT_ID,""),
                SHOP_ID to bundle?.getString(SHOP_ID,""),
                QUERY_PARENT to bundle?.getString(QUERY_PARENT,""),
                AFFILIATE_UNIQUE_ID to bundle?.getString(AFFILIATE_UNIQUE_ID, "")?.toDecodedString(),
                CHANNEL to bundle?.getString(CHANNEL, ""),
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
        if(data.campaignCode != null && data.campaignCode.length > CONSTANT_11){
            campaignCode = data.campaignCode.substring(CONSTANT_0, CONSTANT_11)
        }
        return "${data.identifier}-${campaignCode}"
    }

    fun updateScroll(dx: Int, dy: Int, newState: Int, userPressed: Boolean) {
        _scrollState.value = ScrollData(dx,dy,newState,!userPressed)
    }

    fun resetScroll(){
        _scrollState.value = null
    }

    fun checkForSamePageOpened(queryParameterMapFromBundle: MutableMap<String, String?>) {
        if (!queryParameterMapFromBundle[RECOM_PRODUCT_ID].isNullOrEmpty())
            discoveryDataUseCase.getDiscoResponseIfPresent(pageIdentifier)?.queryParamMap?.let {
                if (queryParameterMapFromBundle[RECOM_PRODUCT_ID] != it[RECOM_PRODUCT_ID]) {
                    discoveryDataUseCase.clearPage(pageIdentifier)
                }
            }
    }

    private fun setParameterMap(queryParameterMap: MutableMap<String, String?>, queryParameterMapWithRpc: MutableMap<String, String>, queryParameterMapWithoutRpc: MutableMap<String, String>) {
        launchCatchError(
            (this + Dispatchers.Default).coroutineContext,
            block = {
                val queryMap =
                    URLParser(ApplinkConstInternalDiscovery.INTERNAL_DISCOVERY + "?" + queryParameterMap[QUERY_PARENT]).paramKeyValueMapDecoded
                for ((key, value) in queryMap) {
                    if (!value.isNullOrEmpty()) {
                        if (key.startsWith(RPC_FILTER_KEY)) {
                            val keyWithoutPrefix = key.removePrefix(RPC_FILTER_KEY)
                            queryParameterMapWithRpc[keyWithoutPrefix] = value
                        } else {
                            queryParameterMapWithoutRpc[key] = value
                        }
                    }
                }
            },
            onError = {
                it
            }
        )
    }

    fun initAffiliateSDK() {
        isAffiliateInitialized = true
        var affiliateUID = ""
        var affiliateChannel = ""
        discoComponentQuery?.let {
            affiliateUID = it[AFFILIATE_UNIQUE_ID] ?: ""
            affiliateChannel = it[CHANNEL] ?: ""
        }
        val pageDetail = AffiliatePageDetail(
            source = AffiliateSdkPageSource.Discovery(),
            pageName = pageIdentifier,
            pageId = "0"
        )
        launchCatchError(
            block = {
                affiliateCookieHelper.initCookie(
                    affiliateUUID = affiliateUID,
                    affiliateChannel = affiliateChannel,
                    affiliatePageDetail = pageDetail
                )
            },
            onError = {

            }
        )
    }

    fun createAffiliateLink(applink: String): String {
        return if (isAffiliateInitialized)
            affiliateCookieHelper.createAffiliateLink(applink, getTrackerIDForAffiliate())
        else
            applink
    }

    fun getTrackerIDForAffiliate(): String {
        if(randomUUIDAffiliate == null){
            randomUUIDAffiliate = Utils.generateRandomUUID()
        }
        return randomUUIDAffiliate ?: ""
    }
}
