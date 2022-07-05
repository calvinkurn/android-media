package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.playwidget

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
import com.tokopedia.play.widget.data.PlayWidget
import com.tokopedia.play.widget.ui.PlayWidgetState
import com.tokopedia.play.widget.util.PlayWidgetTools
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

class DiscoveryPlayWidgetViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val componentsItem: ComponentsItem = mockk(relaxed = true)
    private val application: Application = mockk()
    private var viewModel: DiscoveryPlayWidgetViewModel = spyk(DiscoveryPlayWidgetViewModel(application, componentsItem, 99))

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

    /**************************** test for hitPlayWidgetService() *******************************************/
    @Test
    fun `test for hitPlayWidgetService`(){
        val list = ArrayList<DataItem>()
        val item = DataItem(playWidgetPlayID = "2")
        list.add(item)
        every { componentsItem.data } returns list
        val playWidgetTools: PlayWidgetTools = mockk(relaxed = true)
        viewModel.playWidgetTools = playWidgetTools
        val playWidget: PlayWidget = mockk(relaxed = true)
        coEvery { playWidgetTools.getWidgetFromNetwork(any()) } returns playWidget
        val playWidgetState: PlayWidgetState = mockk(relaxed = true)
        coEvery { playWidgetTools.mapWidgetToModel(any()) } returns playWidgetState

        viewModel.hitPlayWidgetService()

        TestCase.assertEquals(viewModel.getPlayWidgetUILiveData().value , playWidgetState)
    }

    @Test
    fun `test for hitPlayWidgetService when throws error`(){
        val list = ArrayList<DataItem>()
        val item = DataItem(playWidgetPlayID = "2")
        list.add(item)
        every { componentsItem.data } throws Exception("error")

        viewModel.hitPlayWidgetService()

        TestCase.assertEquals(viewModel.getPlayWidgetUILiveData().value , null)
    }

    /**************************** test for updatePlayWidgetTotalView() *******************************************/
    @Test
    fun `test for updatePlayWidgetTotalView`(){
        `test for hitPlayWidgetService`()
        val playWidgetTools: PlayWidgetTools = mockk(relaxed = true)
        viewModel.playWidgetTools = playWidgetTools
        val playWidgetState: PlayWidgetState = mockk(relaxed = true)
        coEvery { playWidgetTools.updateTotalView(any(),any(),any()) } returns playWidgetState

        viewModel.updatePlayWidgetTotalView("4","3")

        TestCase.assertEquals(viewModel.getPlayWidgetUILiveData().value, playWidgetState)

    }

}