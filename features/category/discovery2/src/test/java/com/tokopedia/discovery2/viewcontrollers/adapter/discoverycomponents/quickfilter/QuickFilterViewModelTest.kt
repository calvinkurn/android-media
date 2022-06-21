package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.quickfilter

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

class QuickFilterViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val componentsItem: ComponentsItem = mockk(relaxed = true)
    private val application: Application = mockk()
    private var viewModel: QuickFilterViewModel = spyk(QuickFilterViewModel(application, componentsItem, 99))

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(TestCoroutineDispatcher())

        mockkStatic(::getComponent)
        mockkConstructor(URLParser::class)
        every { anyConstructed<URLParser>().paramKeyValueMapDecoded } returns HashMap()
    }

    @After
    fun shutDown() {
        Dispatchers.resetMain()
        unmockkStatic(::getComponent)
        unmockkConstructor(URLParser::class)
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

    /**************************** test for getTargetComponent() *******************************************/
    @Test
    fun `test for getTargetComponent when properties dynamic is true`(){
        val parentComponentsItem: ComponentsItem = spyk(ComponentsItem(id = "2",dynamicOriginalId = "3"))
        val properties: Properties = mockk(relaxed = true)
        every { properties.targetId } returns "3"
        every { properties.dynamic } returns true
        every { componentsItem.parentComponentId } returns "2"
        every { getComponent(componentsItem.parentComponentId, componentsItem.pageEndPoint) } returns parentComponentsItem

        viewModel.getTargetComponent()

        TestCase.assertEquals(viewModel.getTargetComponent(),parentComponentsItem)

    }

    @Test
    fun `test for getTargetComponent when properties dynamic is false`(){
        val parentComponentsItem: ComponentsItem = spyk(ComponentsItem(id = "3",dynamicOriginalId = "3"))
        val properties: Properties = mockk(relaxed = true)
        every { properties.targetId } returns "3"
        every { properties.dynamic } returns false
        every { componentsItem.parentComponentId } returns "3"
        every { getComponent(componentsItem.parentComponentId, componentsItem.pageEndPoint) } returns parentComponentsItem

        viewModel.getTargetComponent()

        TestCase.assertEquals(viewModel.getTargetComponent(),parentComponentsItem)

    }

    @Test
    fun `test for getTargetComponent when properties dynamic is false and dynamicOriginalId is empty`(){
        val parentComponentsItem: ComponentsItem = spyk(ComponentsItem(id = "3"))
        val properties: Properties = mockk(relaxed = true)
        every { properties.targetId } returns "3"
        every { properties.dynamic } returns true
        every { componentsItem.parentComponentId } returns "3"
        every { getComponent(componentsItem.parentComponentId, componentsItem.pageEndPoint) } returns parentComponentsItem

        viewModel.getTargetComponent()

        TestCase.assertEquals(viewModel.getTargetComponent(),parentComponentsItem)

    }

    @Test
    fun `test for getTargetComponent when getComponent returns null`(){
        val properties: Properties = mockk(relaxed = true)
        every { properties.targetId } returns "3"
        every { properties.dynamic } returns true
        every { componentsItem.parentComponentId } returns "3"
        every { getComponent(any(),any()) } returns null

        viewModel.getTargetComponent()

        TestCase.assertEquals(viewModel.getTargetComponent(),null)

    }

}