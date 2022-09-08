package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.tabs

import android.app.Application
import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery.common.utils.URLParser
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.data.Properties
import com.tokopedia.discovery2.usecase.tabsusecase.DynamicTabsUseCase
import com.tokopedia.user.session.UserSession
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.*

class TabsViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val componentsItem: ComponentsItem = mockk(relaxed = true)
    private val application: Application = mockk()
    private var context:Context = mockk()
    private val viewModel: TabsViewModel = spyk(TabsViewModel(application, componentsItem, 99))

    private val dynamicTabsUseCase: DynamicTabsUseCase by lazy {
        mockk()
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(TestCoroutineDispatcher())

        mockkConstructor(URLParser::class)
        every { anyConstructed<URLParser>().paramKeyValueMapDecoded } returns HashMap()
        mockkConstructor(UserSession::class)
        every { application.applicationContext } returns context
    }

    @Test
    fun `test for useCase`() {
        val viewModel: TabsViewModel =
                spyk(TabsViewModel(application, componentsItem, 99))

        val dynamicTabsUseCase = mockk<DynamicTabsUseCase>()
        viewModel.dynamicTabsUseCase = dynamicTabsUseCase

        assert(viewModel.dynamicTabsUseCase === dynamicTabsUseCase)
    }



    @Test
    fun `test for position passed`(){
        assert(viewModel.position == 99)
    }

    @Test
    fun `test for application`(){
        assert(viewModel.application === application)
    }

    @Test
    fun `test for reInitTabComponentData`(){
        viewModel.reInitTabComponentData()

        assert(viewModel.reInitTabComponentData() == componentsItem.reInitComponentItems())
    }

    @Test
    fun `test for reInitTabTargetComponents`(){
        viewModel.dynamicTabsUseCase = dynamicTabsUseCase
        coEvery { dynamicTabsUseCase.updateTargetProductComponent(any(),any()) } returns true

        viewModel.reInitTabTargetComponents()

        assert(dynamicTabsUseCase.updateTargetProductComponent(componentsItem.id, componentsItem.pageEndPoint))
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

    /**************************** test for onTabClick() *******************************************/
    @Test
    fun `Test for onTabClick`() {
        viewModel.onTabClick()

        assert(viewModel.syncData.value == true)
    }

    /**************************** test for getArrowVisibilityStatus() *******************************************/
    @Test
    fun `Test for getArrowVisibilityStatus when categoryDetail is not null`() {
        val tempProperties = spyk(Properties(categoryDetail = true))
        every { componentsItem.properties } returns tempProperties
        viewModel.getArrowVisibilityStatus()

        assert(viewModel.getArrowVisibilityStatus())
    }

    @Test
    fun `Test for getArrowVisibilityStatus when categoryDetail is null`() {
        val tempProperties = spyk(Properties())
        every { componentsItem.properties } returns tempProperties
        viewModel.getArrowVisibilityStatus()

        assert(!viewModel.getArrowVisibilityStatus())
    }

    @Test
    fun `Test for getArrowVisibilityStatus when Properties is null`() {
        every { componentsItem.properties } returns null
        viewModel.getArrowVisibilityStatus()

        assert(!viewModel.getArrowVisibilityStatus())
    }

    /**************************** test for updateTabItems() *******************************************/
    @Test
    fun `Test for updateTabItems when background equals plain`() {
        val tempProperties = Properties(background = "plain")
        every { componentsItem.properties } returns tempProperties
        val componentItemList = mockk<ArrayList<ComponentsItem>>(relaxed = true)
        every { componentsItem.getComponentsItem() } returns componentItemList

        viewModel.onAttachToViewHolder()

        assert(viewModel.getUnifyTabLiveData().value == componentItemList)
    }

    @Test
    fun `Test for updateTabItems when Properties is null`() {
        val componentItemList = mockk<ArrayList<ComponentsItem>>(relaxed = true)
        every { componentsItem.getComponentsItem() } returns componentItemList
        every { componentsItem.properties } returns null

        viewModel.onAttachToViewHolder()

        assert(viewModel.getColorTabComponentLiveData().value == componentsItem.getComponentsItem())
    }

    @Test
    fun `Test for updateTabItems when background not equals plain`() {
        val componentItemList = mockk<ArrayList<ComponentsItem>>(relaxed = true)
        every { componentsItem.getComponentsItem() } returns componentItemList
        val tempProperties = spyk(Properties(background = "xyz"))
        every { componentsItem.properties } returns tempProperties

        viewModel.onAttachToViewHolder()

        assert(viewModel.getColorTabComponentLiveData().value == componentsItem.getComponentsItem())
    }

    /**************************** test for fetchDynamicTabData() *******************************************/
    @Test
    fun `Test for fetchDynamicTabData when getTabData returns true`() {
        viewModel.dynamicTabsUseCase = dynamicTabsUseCase
        val tempProperties = spyk(Properties(dynamic = true))
        every { componentsItem.properties } returns tempProperties
        val componentItemList = arrayListOf<ComponentsItem>()
        every { componentsItem.getComponentsItem() } returns componentItemList
        coEvery { dynamicTabsUseCase.getTabData(any(),any())}  returns true

        viewModel.onAttachToViewHolder()

        assert(viewModel.syncData.value == true)
    }

    @Test
    fun `Test for fetchDynamicTabData when getTabData returns false`() {
        viewModel.dynamicTabsUseCase = dynamicTabsUseCase
        val tempProperties = spyk(Properties(dynamic = true))
        every { componentsItem.properties } returns tempProperties
        val componentItemList = arrayListOf<ComponentsItem>()
        every { componentsItem.getComponentsItem() } returns componentItemList
        coEvery { dynamicTabsUseCase.getTabData(any(),any())}  returns false

        viewModel.onAttachToViewHolder()

        assert(viewModel.syncData.value == false)
    }

    @Test
    fun `Test for fetchDynamicTabData when getTabData throws exception`() {
        viewModel.dynamicTabsUseCase = dynamicTabsUseCase
        val tempProperties = spyk(Properties(dynamic = true))
        every { componentsItem.properties } returns tempProperties
        val componentItemList = arrayListOf<ComponentsItem>()
        every { componentsItem.getComponentsItem() } returns componentItemList
        coEvery { dynamicTabsUseCase.getTabData(any(),any())}  throws Exception("Error")

        viewModel.onAttachToViewHolder()

        assert(viewModel.syncData.value == null)
    }

    /**************************** test for getTabItemData() *******************************************/
    @Test
    fun `Test for getTabItemData when list is not empty`() {
        val list = ArrayList<DataItem>()
        val item = DataItem()
        list.add(item)
        every { componentsItem.data } returns list

        viewModel.getTabItemData(0)

        assert(viewModel.getTabItemData(0) == item)
    }

    @Test
    fun `Test for getTabItemData when list is empty`() {
        val list = ArrayList<DataItem>()
        every { componentsItem.data } returns list

        viewModel.getTabItemData(0)

        assert(viewModel.getTabItemData(0) == null)
    }

    /**************************** test for setSelectedState() *******************************************/
    @Test
    fun `Test for setSelectedState when list is not empty`() {
        val list = ArrayList<DataItem>()
        val item = DataItem(isSelected = true)
        list.add(item)
        every { componentsItem.data } returns list
        val componentItemList = ArrayList<ComponentsItem>()
        componentItemList.add(componentsItem)
        every { componentsItem.getComponentsItem() } returns componentItemList

        assert(!viewModel.setSelectedState(0,true))
    }

    @Test
    fun `Test for setSelectedState when isSelected is null`() {
        val list = ArrayList<DataItem>()
        val item = DataItem()
        list.add(item)
        every { componentsItem.data } returns list
        val componentItemList = ArrayList<ComponentsItem>()
        componentItemList.add(componentsItem)
        every { componentsItem.getComponentsItem() } returns componentItemList

        assert(viewModel.setSelectedState(0,true))
    }

    @Test
    fun `Test for setSelectedState when DataList is empty`() {
        val list = ArrayList<DataItem>()
        every { componentsItem.data } returns list

        assert(viewModel.setSelectedState(0,true))
    }


    @After
    fun shutDown() {
        Dispatchers.resetMain()

        unmockkConstructor(UserSession::class)
        unmockkConstructor(URLParser::class)
    }
}