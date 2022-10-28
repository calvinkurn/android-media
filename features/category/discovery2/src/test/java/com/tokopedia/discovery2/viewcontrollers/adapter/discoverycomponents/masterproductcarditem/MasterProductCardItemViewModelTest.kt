package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.masterproductcarditem

import android.app.Application
import android.content.Context
import android.content.res.Resources
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery.common.utils.URLParser
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.Constant.ProductTemplate.GRID
import com.tokopedia.discovery2.Constant.ProductTemplate.LIST
import com.tokopedia.discovery2.StockWording
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.data.Properties
import com.tokopedia.discovery2.data.campaignnotifymeresponse.CampaignNotifyMeResponse
import com.tokopedia.discovery2.datamapper.getComponent
import com.tokopedia.discovery2.usecase.campaignusecase.CampaignNotifyUserCase
import com.tokopedia.discovery2.usecase.productCardCarouselUseCase.ProductCardItemUseCase
import com.tokopedia.discovery2.usecase.topAdsUseCase.TopAdsTrackingUseCase
import com.tokopedia.user.session.UserSession
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

class MasterProductCardItemViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val componentsItem: ComponentsItem = mockk(relaxed = true)
    private val application: Application = mockk()
    private var viewModel: MasterProductCardItemViewModel = spyk(MasterProductCardItemViewModel(application, componentsItem, 99))
    private var context:Context = mockk()

    private val productCardItemUseCase: ProductCardItemUseCase by lazy {
        mockk()
    }

    private val discoveryTopAdsTrackingUseCase: TopAdsTrackingUseCase by lazy {
        mockk()
    }

    private val campaignNotifyUserCase: CampaignNotifyUserCase by lazy {
        mockk()
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(TestCoroutineDispatcher())

        mockkStatic(::getComponent)
        mockkConstructor(UserSession::class)
        mockkConstructor(URLParser::class)
        every { anyConstructed<URLParser>().paramKeyValueMapDecoded } returns HashMap()
        every { application.applicationContext } returns context

    }

    @After
    fun shutDown() {
        Dispatchers.resetMain()
        unmockkConstructor(UserSession::class)
        unmockkConstructor(URLParser::class)
        unmockkStatic(::getComponent)
    }

    @Test
    fun `test for components`(){
        assert(viewModel.components === componentsItem)
    }

    @Test
    fun `test for position`(){
        assert(viewModel.position == 99)
    }

    @Test
    fun `test for application`(){
        assert(viewModel.application === application)
    }

    /**************************** test for component position *******************************************/
    @Test
    fun `test for component position`(){
        viewModel.onAttachToViewHolder()

        assert(viewModel.getComponentPosition().value == viewModel.position)
    }

    /**************************** test for onAttachToViewHolder *******************************************/
    @Test
    fun `test for onAttachToViewHolder when campaignSoldCount is 0`(){
        val list = ArrayList<DataItem>()
        val item = DataItem(campaignSoldCount = "0", threshold = "20", customStock = "10")
        list.add(item)
        every { componentsItem.data } returns list
        val resource: Resources = mockk(relaxed = true)
        every { application.resources } returns resource
        every { application.applicationContext.resources } returns resource

        viewModel.onAttachToViewHolder()

        assert(viewModel.getProductModelValue().value != null)
    }

    @Test
    fun `test for onAttachToViewHolder when customStock is 0`(){
        val list = ArrayList<DataItem>()
        val item = DataItem(campaignSoldCount = "50", threshold = "20", customStock = "0")
        list.add(item)
        every { componentsItem.data } returns list
        val resource: Resources = mockk(relaxed = true)
        every { application.resources } returns resource
        every { application.applicationContext.resources } returns resource

        viewModel.onAttachToViewHolder()

        assert(viewModel.getProductModelValue().value != null)
    }

    @Test
    fun `test for onAttachToViewHolder when customStock is 1`(){
        val list = ArrayList<DataItem>()
        val item = DataItem(campaignSoldCount = "50", threshold = "20", customStock = "1")
        list.add(item)
        every { componentsItem.data } returns list
        val resource: Resources = mockk(relaxed = true)
        every { application.resources } returns resource
        every { application.applicationContext.resources } returns resource

        viewModel.onAttachToViewHolder()

        assert(viewModel.getProductModelValue().value != null)
    }

    @Test
    fun `test for onAttachToViewHolder when customStock is smaller than threshold`(){
        val list = ArrayList<DataItem>()
        val item = DataItem(campaignSoldCount = "50", threshold = "20", customStock = "10")
        list.add(item)
        every { componentsItem.data } returns list
        val resource: Resources = mockk(relaxed = true)
        every { application.resources } returns resource
        every { application.applicationContext.resources } returns resource

        viewModel.onAttachToViewHolder()

        assert(viewModel.getProductModelValue().value != null)
    }

    @Test
    fun `test for onAttachToViewHolder when customStock is greater than threshold`(){
        val list = ArrayList<DataItem>()
        val item = DataItem(campaignSoldCount = "50", threshold = "20", customStock = "30")
        list.add(item)
        every { componentsItem.data } returns list
        val resource: Resources = mockk(relaxed = true)
        every { application.resources } returns resource
        every { application.applicationContext.resources } returns resource

        viewModel.onAttachToViewHolder()

        assert(viewModel.getProductModelValue().value != null)
    }

    @Test
    fun `test for onAttachToViewHolder when stockWording color is empty`(){
        val list = ArrayList<DataItem>()
        val tempStockWording = StockWording(color = "", title = "xyz")
        val item = DataItem(stockWording = tempStockWording)
        list.add(item)
        every { componentsItem.data } returns list
        val resource: Resources = mockk(relaxed = true)
        every { application.resources } returns resource

        viewModel.onAttachToViewHolder()

        assert(viewModel.getDataItemValue().value != null)
    }

    /**************************** end for onAttachToViewHolder *******************************************/

    /**************************** test for sendTopAdsView *******************************************/
    @Test
    fun `test for sendTopAdsView`() {
        viewModel.discoveryTopAdsTrackingUseCase = discoveryTopAdsTrackingUseCase
        val list = ArrayList<DataItem>()
        val item = DataItem(isTopads = true, topadsViewUrl = "www.tokopedia.com")
        list.add(item)
        every { componentsItem.data } returns list
        val resource: Resources = mockk(relaxed = true)
        every { application.resources } returns resource
        every { discoveryTopAdsTrackingUseCase.hitImpressions(any(),any(),any(),any(),any()) } just runs

        viewModel.onAttachToViewHolder()
        viewModel.sendTopAdsView()

        verify { discoveryTopAdsTrackingUseCase.hitImpressions(any(),any(),any(),any(),any()) }
    }

    /**************************** test for sendTopAdsClick *******************************************/
    @Test
    fun `test for sendTopAdsClick`() {
        viewModel.discoveryTopAdsTrackingUseCase = discoveryTopAdsTrackingUseCase
        val list = ArrayList<DataItem>()
        val item = DataItem(isTopads = true, topadsClickUrl = "www.tokopedia.com")
        list.add(item)
        every { componentsItem.data } returns list
        val resource: Resources = mockk(relaxed = true)
        every { application.resources } returns resource
        every { discoveryTopAdsTrackingUseCase.hitClick(any(),any(),any(),any(),any()) } just runs

        viewModel.onAttachToViewHolder()
        viewModel.sendTopAdsClick()

        verify { discoveryTopAdsTrackingUseCase.hitClick(any(),any(),any(),any(),any()) }
    }

    /**************************** test for Login *******************************************/
    @Test
    fun `isUser Logged in when isLoggedIn is false`() {
        every { constructedWith<UserSession>(OfTypeMatcher<Context>(Context::class)).isLoggedIn } returns false

        assert(!viewModel.isUserLoggedIn())
    }
    @Test
    fun `isUser Logged in when isLoggedIn is true`() {
        every { constructedWith<UserSession>(OfTypeMatcher<Context>(Context::class)).isLoggedIn } returns true

        assert(viewModel.isUserLoggedIn())
    }
    /**************************** end of Login *******************************************/

    /**************************** test for userId *******************************************/
    @Test
    fun `get user id of logged in user when userId is empty`() {
        every { constructedWith<UserSession>(OfTypeMatcher<Context>(Context::class)).userId } returns ""

        assert(viewModel.getUserID() == "")
    }

    @Test
    fun `get user id of logged in user when userId is not empty`() {
        every { constructedWith<UserSession>(OfTypeMatcher<Context>(Context::class)).userId } returns "1012"

        assert(viewModel.getUserID() == "1012")
    }
    /**************************** end of userId *******************************************/

    /**************************** test for getProductDataItem() *******************************************/
    @Test
    fun `get product data item when data is null`() {
        every { componentsItem.data } returns null

        assert(viewModel.getProductDataItem() == null)

    }
    @Test
    fun `get product data item when list is empty`() {
        val list = ArrayList<DataItem>()
        every { componentsItem.data } returns list

        assert(viewModel.getProductDataItem() == null)
    }
    @Test
    fun `get product data item when list is not empty`() {
        val list = ArrayList<DataItem>()
        every { componentsItem.data } returns list
        val item:DataItem = mockk()
        list.add(item)

        assert(viewModel.getProductDataItem() === item)

    }
    /**************************** end of getProductDataItem() *******************************************/

    /**************************** test for updateProductQuantity() *******************************************/
    @Test
    fun `update quantity confirmation test`(){
        val list = ArrayList<DataItem>()
        every { componentsItem.data } returns list
        val item = DataItem()
        list.add(item)

        viewModel.updateProductQuantity(10)

        assert(item.quantity == 10)
    }

    /**************************** test for handleATCFailed() *******************************************/
    @Test
    fun `handle ATCFailed quantity recheck and reload`(){
        val viewmodel = spyk(MasterProductCardItemViewModel(application, componentsItem, 99))
        every { viewmodel.onAttachToViewHolder() } just Runs
        val list = ArrayList<DataItem>()
        every { componentsItem.data } returns list
        val item = DataItem()
        list.add(item)

        viewmodel.handleATCFailed()

        assert(item.quantity == 0)
        verify { viewmodel.onAttachToViewHolder() }
    }

    /**************************** end of handleATCFailed() *******************************************/

    /**************************** test for getComponentName() *******************************************/
    @Test
    fun `product card type name when name is null`() {
        every { componentsItem.name } returns null

        assert(viewModel.getComponentName().isEmpty())
    }

    @Test
    fun `product card type name when name is not null`() {
        every { componentsItem.name } returns "XYZ"

        assert(viewModel.getComponentName() == "XYZ")
    }
    /**************************** end of getComponentName() *******************************************/

    /**************************** test for getProductCardOptionsModel() *******************************************/
    @Test
    fun `test for getProductCardOptionsModel when data is null`() {
        viewModel.getProductCardOptionsModel()

        assert(!viewModel.getProductCardOptionsModel().hasWishlist)
    }

    @Test
    fun `test for getProductCardOptionsModel when data is not null`() {
        val list = ArrayList<DataItem>()
        val item = DataItem()
        list.add(item)
        every { componentsItem.data } returns list

        viewModel.getProductCardOptionsModel()

        assert(viewModel.getProductCardOptionsModel().hasWishlist)
    }

    /**************************** test for getThreeDotsWishlistOptionsModel() *******************************************/
    @Test
    fun `test for getThreeDotsWishlistOptionsModel when data is null`() {
        viewModel.getProductCardOptionsModel()

        assert(viewModel.getThreeDotsWishlistOptionsModel().productName.isEmpty())
    }

    @Test
    fun `test for getThreeDotsWishlistOptionsModel when data is not null`() {
        val list = ArrayList<DataItem>()
        val item = DataItem(name = "tokopedia")
        list.add(item)
        every { componentsItem.data } returns list

        viewModel.getProductCardOptionsModel()

        assert(viewModel.getThreeDotsWishlistOptionsModel().productName == "tokopedia")
    }

    /**************************** test for getTemplateType() *******************************************/
    @Test
    fun `test for getTemplateType when template is LIST`() {
        val tempProperties = Properties(template = LIST)
        every { componentsItem.properties } returns tempProperties
        val tempViewModel: MasterProductCardItemViewModel = spyk(MasterProductCardItemViewModel(application, componentsItem, 99))

        viewModel.getTemplateType()

        TestCase.assertEquals(tempViewModel.getTemplateType(), LIST)
    }

    @Test
    fun `test for getTemplateType properties is null`() {
        every { componentsItem.properties } returns null
        val tempViewModel: MasterProductCardItemViewModel = spyk(MasterProductCardItemViewModel(application, componentsItem, 99))

        TestCase.assertEquals(tempViewModel.getTemplateType(), GRID)
    }

    fun initLoggedInCallback(){
        viewModel.campaignNotifyUserCase = campaignNotifyUserCase
        viewModel.productCardItemUseCase = productCardItemUseCase
        val resource: Resources = mockk(relaxed = true)
        every { application.resources } returns resource
        every { application.applicationContext.resources } returns resource
    }

    /**************************** test for loggedInCallback() *******************************************/
    @Test
    fun `test for loggedInCallback when campaignResponse success is true`() {
        initLoggedInCallback()
        val list = ArrayList<DataItem>()
        val item = DataItem(notifyMe = true)
        list.add(item)
        every { componentsItem.data } returns list
        every { constructedWith<UserSession>(OfTypeMatcher<Context>(Context::class)).isLoggedIn } returns true
        val checkCampaignNotifyMeResponse = CampaignNotifyMeResponse.CheckCampaignNotifyMeResponse(success = true)
        val campaignNotifyMeResponse = CampaignNotifyMeResponse(checkCampaignNotifyMeResponse = checkCampaignNotifyMeResponse)
        coEvery {
            campaignNotifyUserCase.subscribeToCampaignNotifyMe(any())
        } returns campaignNotifyMeResponse
        coEvery {
            productCardItemUseCase.notifyProductComponentUpdate(any(),any())
        } returns true

        viewModel.onAttachToViewHolder()
        viewModel.loggedInCallback()

        assert(viewModel.showNotifyToastMessage().value?.first == false)
    }

    @Test
    fun `test for loggedInCallback when campaignResponse success is false`() {
        initLoggedInCallback()
        val list = ArrayList<DataItem>()
        val item = DataItem(notifyMe = true)
        list.add(item)
        every { componentsItem.data } returns list
        every { constructedWith<UserSession>(OfTypeMatcher<Context>(Context::class)).isLoggedIn } returns true
        val checkCampaignNotifyMeResponse = CampaignNotifyMeResponse.CheckCampaignNotifyMeResponse(success = false)
        val campaignNotifyMeResponse = CampaignNotifyMeResponse(checkCampaignNotifyMeResponse = checkCampaignNotifyMeResponse)
        coEvery {
            campaignNotifyUserCase.subscribeToCampaignNotifyMe(any())
        } returns campaignNotifyMeResponse
        coEvery {
            productCardItemUseCase.notifyProductComponentUpdate(any(),any())
        } returns true

        viewModel.onAttachToViewHolder()
        viewModel.loggedInCallback()

        assert(viewModel.showNotifyToastMessage().value?.first == true)
    }

    @Test
    fun `test for loggedInCallback when dataItem's notifyMe is false`() {
        initLoggedInCallback()
        val list = ArrayList<DataItem>()
        val item = DataItem(notifyMe = false)
        list.add(item)
        every { componentsItem.data } returns list
        every { constructedWith<UserSession>(OfTypeMatcher<Context>(Context::class)).isLoggedIn } returns true
        val checkCampaignNotifyMeResponse = CampaignNotifyMeResponse.CheckCampaignNotifyMeResponse(success = true)
        val campaignNotifyMeResponse = CampaignNotifyMeResponse(checkCampaignNotifyMeResponse = checkCampaignNotifyMeResponse)
        coEvery {
            campaignNotifyUserCase.subscribeToCampaignNotifyMe(any())
        } returns campaignNotifyMeResponse
        coEvery {
            productCardItemUseCase.notifyProductComponentUpdate(any(),any())
        } returns true

        viewModel.onAttachToViewHolder()
        viewModel.loggedInCallback()

        assert(viewModel.notifyMeCurrentStatus().value == true)
    }

    @Test
    fun `test for loggedInCallback when isLoggedIn is false`() {
        initLoggedInCallback()
        val list = ArrayList<DataItem>()
        val item = DataItem(notifyMe = true)
        list.add(item)
        every { componentsItem.data } returns list
        every { constructedWith<UserSession>(OfTypeMatcher<Context>(Context::class)).isLoggedIn } returns false

        viewModel.onAttachToViewHolder()
        viewModel.loggedInCallback()

        assert(viewModel.getShowLoginData().value == true)
    }

    /**************************** test for getParentPositionForCarousel() *******************************************/
    @Test
    fun `test for getParentPositionForCarousel when ComponentNames is ProductCardSprintSaleCarouselItem`() {
        val tempComponentsItem: ComponentsItem = spyk(ComponentsItem(position = 3))
        every { componentsItem.name } returns ComponentNames.ProductCardSprintSaleCarouselItem.componentName
        every { getComponent(componentsItem.id, componentsItem.pageEndPoint) } returns tempComponentsItem

        viewModel.getParentPositionForCarousel()

        TestCase.assertEquals(viewModel.getParentPositionForCarousel(), tempComponentsItem.position)
    }

    @Test
    fun `test for getParentPositionForCarousel when ComponentNames is ProductCardCarouselItem`() {
        val tempComponentsItem: ComponentsItem = spyk(ComponentsItem(position = 3))
        every { componentsItem.name } returns ComponentNames.ProductCardCarouselItem.componentName
        every { getComponent(componentsItem.id, componentsItem.pageEndPoint) } returns tempComponentsItem

        viewModel.getParentPositionForCarousel()

        TestCase.assertEquals(viewModel.getParentPositionForCarousel(), tempComponentsItem.position)
    }

    @Test
    fun `test for getParentPositionForCarousel when getComponent is null`() {
        every { componentsItem.name } returns ComponentNames.ProductCardCarouselItem.componentName
        every { getComponent(componentsItem.id, componentsItem.pageEndPoint) } returns null

        viewModel.getParentPositionForCarousel()

        TestCase.assertEquals(viewModel.getParentPositionForCarousel(), -1)
    }

    @Test
    fun `test for getParentPositionForCarousel when ComponentNames is ShopCardItem`() {
        val tempComponentsItem: ComponentsItem = spyk(ComponentsItem(position = 3))
        every { componentsItem.name } returns ComponentNames.AnchorTabsItem.componentName
        every { getComponent(componentsItem.id, componentsItem.pageEndPoint) } returns tempComponentsItem

        viewModel.getParentPositionForCarousel()

        TestCase.assertEquals(viewModel.getParentPositionForCarousel(), -1)
    }

    /**************************** test for scrollToTargetSimilarProducts() *******************************************/


    @Test
    fun `test for scrollToTargetSimilarProducts when data is null`(){
        val componentsItem: ComponentsItem = mockk(relaxed = true)
        val viewModel = spyk(MasterProductCardItemViewModel(application, componentsItem, 99))
        every { componentsItem.data } returns null
        viewModel.scrollToTargetSimilarProducts()
        assert(viewModel.getScrollSimilarProductComponentID().value == null)
    }

    @Test
    fun `test for scrollToTargetSimilarProducts when data is present but targetId is not`(){
        val componentsItem: ComponentsItem = mockk(relaxed = true)
        val viewModel = spyk(MasterProductCardItemViewModel(application, componentsItem, 99))
        val dataItem : DataItem = spyk()
        every { componentsItem.data } returns listOf(dataItem)
        every { dataItem.targetComponentId } returns null
        viewModel.scrollToTargetSimilarProducts()
        assert(viewModel.getScrollSimilarProductComponentID().value == null)
    }

    @Test
    fun `test for scrollToTargetSimilarProducts when data is present but targetId is empty`(){
        val componentsItem: ComponentsItem = mockk(relaxed = true)
        val viewModel = spyk(MasterProductCardItemViewModel(application, componentsItem, 99))
        val dataItem : DataItem = spyk()
        every { componentsItem.data } returns listOf(dataItem)
        every { dataItem.targetComponentId } returns ""
        viewModel.scrollToTargetSimilarProducts()
        assert(viewModel.getScrollSimilarProductComponentID().value == null)
    }

    @Test
    fun `test for scrollToTargetSimilarProducts when data is present but targetId is present`(){
        val componentsItem: ComponentsItem = mockk(relaxed = true)
        val viewModel = spyk(MasterProductCardItemViewModel(application, componentsItem, 99))
        val dataItem : DataItem = spyk()
        every { componentsItem.data } returns listOf(dataItem)
        every { dataItem.targetComponentId } returns "3"
        viewModel.scrollToTargetSimilarProducts()
        assert(viewModel.getScrollSimilarProductComponentID().value == "3")
    }


}