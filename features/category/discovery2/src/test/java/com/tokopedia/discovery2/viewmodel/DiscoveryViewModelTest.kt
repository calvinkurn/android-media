package com.tokopedia.discovery2.viewmodel

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.DeeplinkMapper
import com.tokopedia.applink.RouteManager
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.discovery.common.utils.URLParser
import com.tokopedia.discovery2.CONSTANT_0
import com.tokopedia.discovery2.CONSTANT_11
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DiscoveryResponse
import com.tokopedia.discovery2.data.PageInfo
import com.tokopedia.discovery2.data.customtopchatdatamodel.ChatExistingChat
import com.tokopedia.discovery2.data.customtopchatdatamodel.CustomChatResponse
import com.tokopedia.discovery2.data.productcarditem.DiscoATCRequestParams
import com.tokopedia.discovery2.datamapper.DiscoveryPageData
import com.tokopedia.discovery2.usecase.CustomTopChatUseCase
import com.tokopedia.discovery2.usecase.discoveryPageUseCase.DiscoveryDataUseCase
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity
import com.tokopedia.discovery2.viewmodel.livestate.GoToAgeRestriction
import com.tokopedia.discovery2.viewmodel.livestate.RouteToApplink
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.data.MiniCartItemKey
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
import junit.framework.TestCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DiscoveryViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()
    private lateinit var discoveryDataUseCase: DiscoveryDataUseCase
    private lateinit var getMiniCartListSimplifiedUseCase: GetMiniCartListSimplifiedUseCase
    private lateinit var addToCartUseCase: AddToCartUseCase
    private lateinit var updateCartUseCase: UpdateCartUseCase
    private lateinit var deleteCartUseCase: DeleteCartUseCase
    private lateinit var userSessionInterface: UserSessionInterface
    private lateinit var trackingQueue: TrackingQueue
    private lateinit var pageLoadTimePerformanceInterface: PageLoadTimePerformanceInterface

    private lateinit var viewModel: DiscoveryViewModel
    private var context: Context = mockk()

    private val customTopChatUseCase: CustomTopChatUseCase by lazy {
        mockk()
    }

    @Before
    @Throws(Exception::class)
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(TestCoroutineDispatcher())
        discoveryDataUseCase = mockk(relaxed = true)
        getMiniCartListSimplifiedUseCase = mockk(relaxed = true)
        addToCartUseCase = mockk(relaxed = true)
        updateCartUseCase = mockk(relaxed = true)
        deleteCartUseCase = mockk(relaxed = true)
        userSessionInterface = mockk(relaxed = true)
        trackingQueue = mockk(relaxed = true)
        pageLoadTimePerformanceInterface = mockk(relaxed = true)

        viewModel = spyk(
            DiscoveryViewModel(
                discoveryDataUseCase,
                getMiniCartListSimplifiedUseCase,
                addToCartUseCase,
                updateCartUseCase,
                deleteCartUseCase,
                userSessionInterface,
                trackingQueue,
                pageLoadTimePerformanceInterface
            )
        )

        mockkConstructor(RouteManager::class)
        mockkConstructor(URLParser::class)
        mockkObject(DeeplinkMapper)
    }

    /**************************** test for addProductToCart() *******************************************/
    @Test
    fun `test for addProductToCart when isGeneralCartATC is true and quantity is 1 and isGeneralCartATC is true and its success`() {
        val discoATCRequestParams: DiscoATCRequestParams = mockk(relaxed = true)
        every { discoATCRequestParams.productId } returns "1"
        every { discoATCRequestParams.shopId } returns "1"
        every { discoATCRequestParams.quantity } returns 1
        every { discoATCRequestParams.parentPosition } returns 1
        every { discoATCRequestParams.isGeneralCartATC } returns true

        val miniCartSimplifiedData: MiniCartSimplifiedData = mockk(relaxed = true)
        val miniCartItem: MiniCartItem = mockk(relaxed = true)
        val miniCartItemKey: MiniCartItemKey = mockk(relaxed = true)
        every { miniCartItemKey.id } returns "1"
        val mapItem: Map<MiniCartItemKey, MiniCartItem> = mapOf(miniCartItemKey to miniCartItem)
        every { miniCartSimplifiedData.miniCartItems } returns mapItem

        viewModel.addProductToCart(discoATCRequestParams)

        verify { addToCartUseCase.execute(any(), any()) }
    }

    @Test
    fun `test for addProductToCart when quantity is 1 and isGeneralCartATC is false`() {
        val discoATCRequestParams: DiscoATCRequestParams = mockk(relaxed = true)
        every { discoATCRequestParams.productId } returns "1"
        every { discoATCRequestParams.shopId } returns "1"
        every { discoATCRequestParams.quantity } returns 1
        every { discoATCRequestParams.parentPosition } returns 1
        every { discoATCRequestParams.isGeneralCartATC } returns false

        val miniCartSimplifiedData: MiniCartSimplifiedData = mockk(relaxed = true)
        val miniCartItemKey: MiniCartItemKey = mockk(relaxed = true)
        every { miniCartItemKey.id } returns "1"
        val miniCartItem: MiniCartItem = mockk(relaxed = true)
        val mapItem: Map<MiniCartItemKey, MiniCartItem> = mapOf(miniCartItemKey to miniCartItem)
        every { viewModel.miniCartSimplifiedData } returns miniCartSimplifiedData
        every { viewModel.miniCartSimplifiedData?.miniCartItems } returns mapItem

        viewModel.addProductToCart(discoATCRequestParams)

        verify { addToCartUseCase.execute(any(), any()) }
    }

    @Test
    fun `test for addProductToCart when quantity is 0 and isGeneralCartATC is false and product present in minicart so remove must be called`() {
        val discoATCRequestParams: DiscoATCRequestParams = mockk(relaxed = true)
        every { discoATCRequestParams.productId } returns "1"
        every { discoATCRequestParams.shopId } returns "1"
        every { discoATCRequestParams.quantity } returns 0
        every { discoATCRequestParams.parentPosition } returns 1
        every { discoATCRequestParams.isGeneralCartATC } returns false

        val miniCartSimplifiedData: MiniCartSimplifiedData = mockk(relaxed = true)
        val miniCartItemKey: MiniCartItemKey = MiniCartItemKey("1")
        val miniCartItem: MiniCartItem = mockk<MiniCartItem.MiniCartItemProduct>(relaxed = true)
        val mapItem: Map<MiniCartItemKey, MiniCartItem> = mapOf(miniCartItemKey to miniCartItem)
        viewModel.miniCartSimplifiedData = miniCartSimplifiedData
        every { miniCartSimplifiedData.miniCartItems } returns mapItem

        viewModel.addProductToCart(discoATCRequestParams)

        verify { deleteCartUseCase.execute(any(), any()) }
    }
    @Test
    fun `test for addProductToCart when quantity is greater than 0 and isGeneralCartATC is false and product present in minicart so update must be called`() {
        val discoATCRequestParams: DiscoATCRequestParams = mockk(relaxed = true)
        every { discoATCRequestParams.productId } returns "1"
        every { discoATCRequestParams.shopId } returns "1"
        every { discoATCRequestParams.quantity } returns 2
        every { discoATCRequestParams.parentPosition } returns 1
        every { discoATCRequestParams.isGeneralCartATC } returns false

        val miniCartSimplifiedData: MiniCartSimplifiedData = mockk(relaxed = true)
        val miniCartItemKey: MiniCartItemKey = MiniCartItemKey("1")
        val miniCartItem: MiniCartItem = mockk<MiniCartItem.MiniCartItemProduct>(relaxed = true)
        val mapItem: Map<MiniCartItemKey, MiniCartItem> = mapOf(miniCartItemKey to miniCartItem)
        viewModel.miniCartSimplifiedData = miniCartSimplifiedData
        every { miniCartSimplifiedData.miniCartItems } returns mapItem

        viewModel.addProductToCart(discoATCRequestParams)

        verify { updateCartUseCase.execute(any(), any()) }
    }

    /**************************** test for getQueryParameterMapFromBundle() *******************************************/
    @Test
    fun `test for getQueryParameterMapFromBundle when discoComponentQuery returns non null value`() {
        val bundle: Bundle = mockk(relaxed = true)
        com.tokopedia.discovery2.datamapper.discoComponentQuery = null
        every { bundle.getString(DiscoveryActivity.SOURCE, "") } returns "a"
        every { bundle.getString(DiscoveryActivity.COMPONENT_ID, "") } returns "b"
        every { bundle.getString(DiscoveryActivity.ACTIVE_TAB, "") } returns "c"
        every { bundle.getString(DiscoveryActivity.TARGET_COMP_ID, "") } returns "d"
        every { bundle.getString(DiscoveryActivity.PRODUCT_ID, "") } returns "e"
        every { bundle.getString(DiscoveryActivity.PIN_PRODUCT, "") } returns "f"
        every { bundle.getString(DiscoveryActivity.EMBED_CATEGORY, "") } returns "g"
        every { bundle.getString(DiscoveryActivity.RECOM_PRODUCT_ID, "") } returns "h"
        every { bundle.getString(DiscoveryActivity.CATEGORY_ID, "") } returns "i"
        every { bundle.getString(DiscoveryActivity.DYNAMIC_SUBTITLE, "") } returns "j"
        every { bundle.getString(DiscoveryActivity.TARGET_TITLE_ID, "") } returns "k"
        every { bundle.getString(DiscoveryActivity.CAMPAIGN_ID, "") } returns "l"
        every { bundle.getString(DiscoveryActivity.VARIANT_ID, "") } returns "m"
        every { bundle.getString(DiscoveryActivity.SHOP_ID, "") } returns "n"
        every { bundle.getString(DiscoveryActivity.QUERY_PARENT, "") } returns "o"

        val discoComponentQuery: MutableMap<String, String?> =
            mutableMapOf(DiscoveryActivity.CATEGORY_ID to "p")
        com.tokopedia.discovery2.datamapper.discoComponentQuery = discoComponentQuery

        val map: MutableMap<String, String?> = mutableMapOf(
            DiscoveryActivity.SOURCE to "a",
            DiscoveryActivity.COMPONENT_ID to "b",
            DiscoveryActivity.ACTIVE_TAB to "c",
            DiscoveryActivity.TARGET_COMP_ID to "d",
            DiscoveryActivity.PRODUCT_ID to "e",
            DiscoveryActivity.PIN_PRODUCT to "f",
            DiscoveryActivity.EMBED_CATEGORY to "g",
            DiscoveryActivity.RECOM_PRODUCT_ID to "h",
            DiscoveryActivity.CATEGORY_ID to "p",
            DiscoveryActivity.DYNAMIC_SUBTITLE to "j",
            DiscoveryActivity.TARGET_TITLE_ID to "k",
            DiscoveryActivity.CAMPAIGN_ID to "l",
            DiscoveryActivity.VARIANT_ID to "m",
            DiscoveryActivity.SHOP_ID to "n",
            DiscoveryActivity.QUERY_PARENT to "o"
        )

        viewModel.getQueryParameterMapFromBundle(bundle)

        TestCase.assertEquals(viewModel.getQueryParameterMapFromBundle(bundle), map)
    }

    @Test
    fun `test for getQueryParameterMapFromBundle when discoComponentQuery returns non null value and bundle is null`() {
        com.tokopedia.discovery2.datamapper.discoComponentQuery = null

        val discoComponentQuery: MutableMap<String, String?> =
            mutableMapOf(DiscoveryActivity.CATEGORY_ID to "p")
        com.tokopedia.discovery2.datamapper.discoComponentQuery = discoComponentQuery

        val map: MutableMap<String, String?> = mutableMapOf(
            DiscoveryActivity.SOURCE to null,
            DiscoveryActivity.COMPONENT_ID to null,
            DiscoveryActivity.ACTIVE_TAB to null,
            DiscoveryActivity.TARGET_COMP_ID to null,
            DiscoveryActivity.PRODUCT_ID to null,
            DiscoveryActivity.PIN_PRODUCT to null,
            DiscoveryActivity.EMBED_CATEGORY to null,
            DiscoveryActivity.RECOM_PRODUCT_ID to null,
            DiscoveryActivity.CATEGORY_ID to "p",
            DiscoveryActivity.DYNAMIC_SUBTITLE to null,
            DiscoveryActivity.TARGET_TITLE_ID to null,
            DiscoveryActivity.CAMPAIGN_ID to null,
            DiscoveryActivity.VARIANT_ID to null,
            DiscoveryActivity.SHOP_ID to null,
            DiscoveryActivity.QUERY_PARENT to null
        )

        viewModel.getQueryParameterMapFromBundle(null)

        TestCase.assertEquals(viewModel.getQueryParameterMapFromBundle(null), map)
    }

    @Test
    fun `test for getQueryParameterMapFromBundle when discoComponentQuery is null`() {
        val bundle: Bundle = mockk(relaxed = true)
        com.tokopedia.discovery2.datamapper.discoComponentQuery = null
        every { bundle.getString(DiscoveryActivity.SOURCE, "") } returns "a"
        every { bundle.getString(DiscoveryActivity.COMPONENT_ID, "") } returns "b"
        every { bundle.getString(DiscoveryActivity.ACTIVE_TAB, "") } returns "c"
        every { bundle.getString(DiscoveryActivity.TARGET_COMP_ID, "") } returns "d"
        every { bundle.getString(DiscoveryActivity.PRODUCT_ID, "") } returns "e"
        every { bundle.getString(DiscoveryActivity.PIN_PRODUCT, "") } returns "f"
        every { bundle.getString(DiscoveryActivity.EMBED_CATEGORY, "") } returns "g"
        every { bundle.getString(DiscoveryActivity.RECOM_PRODUCT_ID, "") } returns "h"
        every { bundle.getString(DiscoveryActivity.CATEGORY_ID, "") } returns "i"
        every { bundle.getString(DiscoveryActivity.DYNAMIC_SUBTITLE, "") } returns "j"
        every { bundle.getString(DiscoveryActivity.TARGET_TITLE_ID, "") } returns "k"
        every { bundle.getString(DiscoveryActivity.CAMPAIGN_ID, "") } returns "l"
        every { bundle.getString(DiscoveryActivity.VARIANT_ID, "") } returns "m"
        every { bundle.getString(DiscoveryActivity.SHOP_ID, "") } returns "n"
        every { bundle.getString(DiscoveryActivity.QUERY_PARENT, "") } returns "o"

        val map: MutableMap<String, String?> = mutableMapOf(
            DiscoveryActivity.SOURCE to "a",
            DiscoveryActivity.COMPONENT_ID to "b",
            DiscoveryActivity.ACTIVE_TAB to "c",
            DiscoveryActivity.TARGET_COMP_ID to "d",
            DiscoveryActivity.PRODUCT_ID to "e",
            DiscoveryActivity.PIN_PRODUCT to "f",
            DiscoveryActivity.EMBED_CATEGORY to "g",
            DiscoveryActivity.RECOM_PRODUCT_ID to "h",
            DiscoveryActivity.CATEGORY_ID to "i",
            DiscoveryActivity.DYNAMIC_SUBTITLE to "j",
            DiscoveryActivity.TARGET_TITLE_ID to "k",
            DiscoveryActivity.CAMPAIGN_ID to "l",
            DiscoveryActivity.VARIANT_ID to "m",
            DiscoveryActivity.SHOP_ID to "n",
            DiscoveryActivity.QUERY_PARENT to "o"
        )

        viewModel.getQueryParameterMapFromBundle(bundle)

        TestCase.assertEquals(viewModel.getQueryParameterMapFromBundle(bundle), map)
    }

    @Test
    fun `test for getQueryParameterMapFromBundle when discoComponentQuery returns null value`() {
        val bundle: Bundle = mockk(relaxed = true)
        com.tokopedia.discovery2.datamapper.discoComponentQuery = null
        every { bundle.getString(DiscoveryActivity.SOURCE, "") } returns "a"
        every { bundle.getString(DiscoveryActivity.COMPONENT_ID, "") } returns "b"
        every { bundle.getString(DiscoveryActivity.ACTIVE_TAB, "") } returns "c"
        every { bundle.getString(DiscoveryActivity.TARGET_COMP_ID, "") } returns "d"
        every { bundle.getString(DiscoveryActivity.PRODUCT_ID, "") } returns "e"
        every { bundle.getString(DiscoveryActivity.PIN_PRODUCT, "") } returns "f"
        every { bundle.getString(DiscoveryActivity.EMBED_CATEGORY, "") } returns "g"
        every { bundle.getString(DiscoveryActivity.RECOM_PRODUCT_ID, "") } returns "h"
        every { bundle.getString(DiscoveryActivity.CATEGORY_ID, "") } returns "i"
        every { bundle.getString(DiscoveryActivity.DYNAMIC_SUBTITLE, "") } returns "j"
        every { bundle.getString(DiscoveryActivity.TARGET_TITLE_ID, "") } returns "k"
        every { bundle.getString(DiscoveryActivity.CAMPAIGN_ID, "") } returns "l"
        every { bundle.getString(DiscoveryActivity.VARIANT_ID, "") } returns "m"
        every { bundle.getString(DiscoveryActivity.SHOP_ID, "") } returns "n"
        every { bundle.getString(DiscoveryActivity.QUERY_PARENT, "") } returns "o"
        val map: MutableMap<String, String?> = mutableMapOf(
            DiscoveryActivity.SOURCE to "a",
            DiscoveryActivity.COMPONENT_ID to "b",
            DiscoveryActivity.ACTIVE_TAB to "c",
            DiscoveryActivity.TARGET_COMP_ID to "d",
            DiscoveryActivity.PRODUCT_ID to "e",
            DiscoveryActivity.PIN_PRODUCT to "f",
            DiscoveryActivity.EMBED_CATEGORY to "g",
            DiscoveryActivity.RECOM_PRODUCT_ID to "h",
            DiscoveryActivity.CATEGORY_ID to "i",
            DiscoveryActivity.DYNAMIC_SUBTITLE to "j",
            DiscoveryActivity.TARGET_TITLE_ID to "k",
            DiscoveryActivity.CAMPAIGN_ID to "l",
            DiscoveryActivity.VARIANT_ID to "m",
            DiscoveryActivity.SHOP_ID to "n",
            DiscoveryActivity.QUERY_PARENT to "o"
        )
        val discoComponentQuery: MutableMap<String, String?> = mutableMapOf()
        com.tokopedia.discovery2.datamapper.discoComponentQuery = discoComponentQuery

        viewModel.getQueryParameterMapFromBundle(bundle)

        TestCase.assertEquals(viewModel.getQueryParameterMapFromBundle(bundle), map)
        com.tokopedia.discovery2.datamapper.discoComponentQuery = null
    }

    @Test
    fun `test for getQueryParameterMapFromBundle when discoComponentQuery returns null value and bundle is null`() {
        com.tokopedia.discovery2.datamapper.discoComponentQuery = null

        val map: MutableMap<String, String?> = mutableMapOf(
            DiscoveryActivity.SOURCE to null,
            DiscoveryActivity.COMPONENT_ID to null,
            DiscoveryActivity.ACTIVE_TAB to null,
            DiscoveryActivity.TARGET_COMP_ID to null,
            DiscoveryActivity.PRODUCT_ID to null,
            DiscoveryActivity.PIN_PRODUCT to null,
            DiscoveryActivity.EMBED_CATEGORY to null,
            DiscoveryActivity.RECOM_PRODUCT_ID to null,
            DiscoveryActivity.CATEGORY_ID to "",
            DiscoveryActivity.DYNAMIC_SUBTITLE to null,
            DiscoveryActivity.TARGET_TITLE_ID to null,
            DiscoveryActivity.CAMPAIGN_ID to null,
            DiscoveryActivity.VARIANT_ID to null,
            DiscoveryActivity.SHOP_ID to null,
            DiscoveryActivity.QUERY_PARENT to null
        )

        viewModel.getQueryParameterMapFromBundle(null)

        TestCase.assertEquals(viewModel.getQueryParameterMapFromBundle(null), map)
    }

    /**************************** test for getDiscoveryData() *******************************************/
    @Test
    fun `test for getDiscoveryData when redirectionUrl is not null`() {
        val discoveryPageData: DiscoveryPageData = mockk(relaxed = true)
        val pageInfo: PageInfo = mockk(relaxed = true)
        val localCacheModel: LocalCacheModel = mockk(relaxed = true)
        coEvery { pageInfo.redirectionUrl } returns "tokopedia://discovery/test-campaign-7"
        coEvery { pageInfo.showChooseAddress } returns true
        coEvery { discoveryPageData.pageInfo } returns pageInfo
        coEvery {
            discoveryDataUseCase.getDiscoveryPageDataUseCase(any(), any(), any(), any(), any())
        } returns discoveryPageData

        viewModel.getDiscoveryData(mutableMapOf(), localCacheModel)

        TestCase.assertEquals(
            viewModel.getDiscoveryLiveStateData().value,
            RouteToApplink("tokopedia://discovery/test-campaign-7")
        )
        TestCase.assertEquals(viewModel.checkAddressVisibility().value, true)
        TestCase.assertEquals(viewModel.getAddressVisibilityValue(), true)
    }

    @Test
    fun `test for getDiscoveryData  when redirectionUrl is empty`() {
        val discoveryPageData: DiscoveryPageData = mockk(relaxed = true)
        val pageInfo: PageInfo = mockk(relaxed = true)
        val localCacheModel: LocalCacheModel = mockk(relaxed = true)
        coEvery { pageInfo.redirectionUrl } returns ""
        coEvery { pageInfo.isAdult } returns 1
        coEvery { pageInfo.identifier } returns "xyz"
        coEvery { discoveryPageData.pageInfo } returns pageInfo
        coEvery {
            discoveryDataUseCase.getDiscoveryPageDataUseCase(any(), any(), any(), any(), any())
        } returns discoveryPageData

        viewModel.getDiscoveryData(mutableMapOf(), localCacheModel)

        TestCase.assertEquals(
            viewModel.getDiscoveryLiveStateData().value,
            GoToAgeRestriction("xyz", 0)
        )
    }

    @Test
    fun `test for getDiscoveryData when getDiscoveryPageDataUseCase throws error`() {
        val localCacheModel: LocalCacheModel = mockk(relaxed = true)
        coEvery {
            discoveryDataUseCase.getDiscoveryPageDataUseCase(any(), any(), any(), any(), any())
        } throws Exception("error")

        viewModel.getDiscoveryData(mutableMapOf(), localCacheModel)

        TestCase.assertEquals(viewModel.getDiscoveryPageInfo().value != null, true)
    }

    /**************************** test for getMapOfQueryParameter() *******************************************/
    @Test
    fun `test for getMapOfQueryParameter`() {
        val uri: Uri = mockk(relaxed = true)
        every { uri.getQueryParameter(DiscoveryActivity.SOURCE) } returns "a"
        every { uri.getQueryParameter(DiscoveryActivity.COMPONENT_ID) } returns "b"
        every { uri.getQueryParameter(DiscoveryActivity.ACTIVE_TAB) } returns "c"
        every { uri.getQueryParameter(DiscoveryActivity.TARGET_COMP_ID) } returns "d"
        every { uri.getQueryParameter(DiscoveryActivity.PRODUCT_ID) } returns "e"
        every { uri.getQueryParameter(DiscoveryActivity.PIN_PRODUCT) } returns "f"
        every { uri.getQueryParameter(DiscoveryActivity.EMBED_CATEGORY) } returns "g"
        every { uri.getQueryParameter(DiscoveryActivity.RECOM_PRODUCT_ID) } returns "h"
        every { uri.getQueryParameter(DiscoveryActivity.CATEGORY_ID) } returns "i"
        every { uri.getQueryParameter(DiscoveryActivity.DYNAMIC_SUBTITLE) } returns "j"
        every { uri.getQueryParameter(DiscoveryActivity.TARGET_TITLE_ID) } returns "k"
        every { uri.getQueryParameter(DiscoveryActivity.CAMPAIGN_ID) } returns "l"
        every { uri.getQueryParameter(DiscoveryActivity.VARIANT_ID) } returns "m"
        every { uri.getQueryParameter(DiscoveryActivity.SHOP_ID) } returns "n"
        every { uri.query } returns "o"

        val map: MutableMap<String, String?> = mutableMapOf(
            DiscoveryActivity.SOURCE to "a",
            DiscoveryActivity.COMPONENT_ID to "b",
            DiscoveryActivity.ACTIVE_TAB to "c",
            DiscoveryActivity.TARGET_COMP_ID to "d",
            DiscoveryActivity.PRODUCT_ID to "e",
            DiscoveryActivity.PIN_PRODUCT to "f",
            DiscoveryActivity.EMBED_CATEGORY to "g",
            DiscoveryActivity.RECOM_PRODUCT_ID to "h",
            DiscoveryActivity.CATEGORY_ID to "i",
            DiscoveryActivity.DYNAMIC_SUBTITLE to "j",
            DiscoveryActivity.TARGET_TITLE_ID to "k",
            DiscoveryActivity.CAMPAIGN_ID to "l",
            DiscoveryActivity.VARIANT_ID to "m",
            DiscoveryActivity.SHOP_ID to "n",
            DiscoveryActivity.QUERY_PARENT to "o"
        )

        viewModel.getMapOfQueryParameter(uri)

        TestCase.assertEquals(viewModel.getMapOfQueryParameter(uri), map)
    }

    /**************************** test for scrollToPinnedComponent() *******************************************/
    @Test
    fun `test for scrollToPinnedComponent when componentList is non empty`() {
        val componentsItem: ComponentsItem = mockk(relaxed = true)
        every { componentsItem.id } returns "2"
        val list = ArrayList<ComponentsItem>()
        list.add(componentsItem)

        viewModel.scrollToPinnedComponent(list, "2")

        TestCase.assertEquals(viewModel.scrollToPinnedComponent(list, "2"), 0)
    }

    @Test
    fun `test for scrollToPinnedComponent when componentList is empty`() {
        val list: ArrayList<ComponentsItem> = arrayListOf()

        viewModel.scrollToPinnedComponent(list, "2")

        TestCase.assertEquals(viewModel.scrollToPinnedComponent(list, "2"), -1)
    }

    /**************************** test for getShareUTM() *******************************************/
    @Test
    fun `test for getShareUTM when campaignCode is not empty`() {
        val pageInfo: PageInfo = mockk(relaxed = true)
        val campCode = "PG_17 June 22_Kejar Diskon Tokopedia 17 Juni 2022 14:00-16:00_764002"
        every { pageInfo.campaignCode } returns campCode

        viewModel.getShareUTM(pageInfo)

        TestCase.assertEquals(
            viewModel.getShareUTM(pageInfo),
            "-${campCode.substring(CONSTANT_0, CONSTANT_11)}"
        )
    }

    @Test
    fun `test for getShareUTM when campaignCode is empty`() {
        val pageInfo: PageInfo = mockk(relaxed = true)
        val campCode = ""
        every { pageInfo.campaignCode } returns campCode

        viewModel.getShareUTM(pageInfo)

        TestCase.assertEquals(viewModel.getShareUTM(pageInfo), "-0")
    }

    @Test
    fun `test for getShareUTM when campaignCode is null`() {
        val pageInfo: PageInfo = mockk(relaxed = true)
        every { pageInfo.campaignCode } returns null

        viewModel.getShareUTM(pageInfo)

        TestCase.assertEquals(viewModel.getShareUTM(pageInfo), "-0")
    }

    /**************************** test for getMiniCart() *******************************************/
    @Test
    fun `test for getMiniCart`() {
        val shopIds: ArrayList<String> = arrayListOf("1", "2")
        every { userSessionInterface.isLoggedIn } returns true

        viewModel.getMiniCart(shopIds, "2")

        verify { getMiniCartListSimplifiedUseCase.setParams(any(), any()) }
    }

    /**************************** test for getScrollDepth() *******************************************/
    @Test
    fun `test for getScrollDepth when range is greater than 0`() {
        viewModel.getScrollDepth(offset = 2, extent = 2, range = 2)

        TestCase.assertEquals(viewModel.getScrollDepth(offset = 2, extent = 2, range = 2), 200)
    }

    @Test
    fun `test for getScrollDepth when range is smaller than 0`() {
        viewModel.getScrollDepth(offset = 2, extent = 2, range = -2)

        TestCase.assertEquals(viewModel.getScrollDepth(offset = 2, extent = 2, range = -2), 0)
    }

    /**************************** test for updateScroll() *******************************************/
    @Test
    fun `test for updateScroll`() {
        viewModel.updateScroll(100, 100, 2, true)

        TestCase.assertEquals(viewModel.scrollState.value != null, true)
    }

    /**************************** test for resetScroll() *******************************************/
    @Test
    fun `test for resetScroll`() {
        viewModel.resetScroll()

        TestCase.assertEquals(viewModel.scrollState.value == null, true)
    }

    /**************************** test for openCustomTopChat() *******************************************/
    @Test
    fun `test for openCustomTopChat when isLoggedIn is false`() {
        every { userSessionInterface.isLoggedIn } returns false
        mockkStatic(RouteManager::class)
        every { RouteManager.route(any(), any()) } returns true

        viewModel.openCustomTopChat(
            context = context,
            appLinks = "tokopedia://discovery/test-campaign-2",
            shopId = 2
        )

        verify { RouteManager.route(context, ApplinkConst.LOGIN) }
    }

    @Test
    fun `test for openCustomTopChat when isLoggedIn is true`() {
        viewModel.customTopChatUseCase = customTopChatUseCase
        mockkStatic(RouteManager::class)
        every { RouteManager.route(any(), any()) } returns true
        val customChatResponse: CustomChatResponse = mockk(relaxed = true)
        val chatExistingChat: ChatExistingChat = mockk(relaxed = true)
        every { customChatResponse.chatExistingChat } returns chatExistingChat
        every { chatExistingChat.messageId } returns 1
        every { userSessionInterface.isLoggedIn } returns true
        coEvery { customTopChatUseCase.getCustomTopChatMessageId(any()) } returns customChatResponse

        viewModel.openCustomTopChat(
            context = context,
            appLinks = "tokopedia://discovery/test-campaign-7",
            shopId = 23
        )

        verify { RouteManager.route(any(), any()) }
    }

    @Test
    fun `test for openCustomTopChat when isLoggedIn is true and getCustomTopChatMessageId throws Exception`() {
        viewModel.customTopChatUseCase = customTopChatUseCase
        mockkStatic(RouteManager::class)
        every { RouteManager.route(any(), any()) } returns true
        every { userSessionInterface.isLoggedIn } returns true
        coEvery { customTopChatUseCase.getCustomTopChatMessageId(any()) } throws Exception("error")

        viewModel.openCustomTopChat(
            context = context,
            appLinks = "tokopedia://discovery/test-campaign-7",
            shopId = 23
        )

        verify(inverse = true) { RouteManager.route(any(), any()) }
    }

    @Test
    fun `test for DiscoverySamePage being opened if no recomProdId present`() {
        viewModel.checkForSamePageOpened(mutableMapOf())
        verify(inverse = true) { discoveryDataUseCase.getDiscoResponseIfPresent(any()) }
    }

    @Test
    fun `test for DiscoverySamePage being opened if recomProdId present and data mismatch`() {
        val map: MutableMap<String, String?> = mutableMapOf()
        map[DiscoveryActivity.RECOM_PRODUCT_ID] = "123"
        val mockResp: DiscoveryResponse = mockk()
        every { discoveryDataUseCase.getDiscoResponseIfPresent(any()) } returns mockResp
        val map2: MutableMap<String, String?> = mutableMapOf()
        map2[DiscoveryActivity.RECOM_PRODUCT_ID] = "101"
        every { mockResp.queryParamMap } returns map2
        viewModel.checkForSamePageOpened(map)
        verify { discoveryDataUseCase.getDiscoResponseIfPresent(any()) }
        verify { discoveryDataUseCase.clearPage(any()) }
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkConstructor(RouteManager::class)
        unmockkConstructor(URLParser::class)
        unmockkObject(DeeplinkMapper)
        com.tokopedia.discovery2.datamapper.discoComponentQuery = null
    }
}
