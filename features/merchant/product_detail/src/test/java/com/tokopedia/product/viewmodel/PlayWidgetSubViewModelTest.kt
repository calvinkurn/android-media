package com.tokopedia.product.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.play.widget.data.PlayWidget
import com.tokopedia.play.widget.data.PlayWidgetReminder
import com.tokopedia.play.widget.domain.PlayWidgetUseCase
import com.tokopedia.play.widget.ui.PlayWidgetState
import com.tokopedia.play.widget.ui.model.PlayWidgetBackgroundUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetConfigUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetReminderType
import com.tokopedia.play.widget.ui.model.PlayWidgetUiModel
import com.tokopedia.play.widget.ui.model.switch
import com.tokopedia.play.widget.util.PlayWidgetTools
import com.tokopedia.product.detail.common.data.model.pdplayout.BasicInfo
import com.tokopedia.product.detail.common.data.model.pdplayout.DynamicProductInfoP1
import com.tokopedia.product.detail.common.data.model.product.Category
import com.tokopedia.product.detail.common.data.model.variant.ProductVariant
import com.tokopedia.product.detail.common.data.model.variant.VariantChild
import com.tokopedia.product.detail.view.viewmodel.product_detail.impl.PlayWidgetSubViewModel
import com.tokopedia.product.detail.view.viewmodel.product_detail.mediator.GetProductDetailDataModelMediator
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.ext.getOrAwaitValue
import com.tokopedia.unit.test.ext.verifyErrorEquals
import com.tokopedia.unit.test.ext.verifySuccessEquals
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.unmockkAll
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by yovi.putra on 28/03/23"
 * Project name: android-tokopedia-core
 **/

@ExperimentalCoroutinesApi
class PlayWidgetSubViewModelTest {

    @RelaxedMockK
    lateinit var playWidgetTools: PlayWidgetTools

    @RelaxedMockK
    lateinit var mediator: GetProductDetailDataModelMediator

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var viewModel: PlayWidgetSubViewModel

    private val playWidgetExpectedState by lazy {
        PlayWidgetState(
            model = PlayWidgetUiModel(
                "title",
                "action title",
                "applink",
                true,
                PlayWidgetConfigUiModel(
                    true,
                    1000,
                    true,
                    1,
                    1,
                    2,
                    1
                ),
                PlayWidgetBackgroundUiModel("", "", "", listOf(), ""),
                listOf()
            ),
            isLoading = false
        )
    }

    @Before
    fun beforeTest() {
        MockKAnnotations.init(this)

        viewModel = PlayWidgetSubViewModel(playWidgetTools = playWidgetTools)
        viewModel.register(CoroutineScope(CoroutineTestDispatchersProvider.main), mediator)
    }

    @After
    fun afterTest() {
        unmockkAll()
    }

    @Test
    fun `get play widget data success`() {
        val widgetType = PlayWidgetUseCase.WidgetType.PDPWidget(emptyList(), emptyList())

        val expectedResponse = PlayWidget()

        coEvery { mediator.getPdpLayout() } returns null
        coEvery { mediator.getVariant() } returns null
        coEvery {
            playWidgetTools.getWidgetFromNetwork(widgetType = widgetType)
        } returns expectedResponse

        coEvery {
            playWidgetTools.mapWidgetToModel(expectedResponse)
        } returns playWidgetExpectedState

        viewModel.getPlayWidgetData()

        viewModel.playWidgetModel.verifySuccessEquals(Success(playWidgetExpectedState))
    }

    @Test
    fun `get play widget data success with variant data available`() {
        val widgetType = PlayWidgetUseCase.WidgetType.PDPWidget(emptyList(), emptyList())
        val expectedResponse = PlayWidget()

        coEvery {
            viewModel.mediator.getVariant()
        } returns ProductVariant(parentId = "1")

        coEvery {
            playWidgetTools.getWidgetFromNetwork(
                widgetType = widgetType
            )
        } returns expectedResponse

        coEvery {
            playWidgetTools.mapWidgetToModel(
                expectedResponse
            )
        } returns playWidgetExpectedState

        viewModel.getPlayWidgetData()
        coVerify { viewModel.mediator.getVariant() }
        assert(viewModel.mediator.getVariant() != null)
    }

    @Test
    fun `get play widget data success with basic category detail available`() {
        val widgetType = PlayWidgetUseCase.WidgetType.PDPWidget(emptyList(), emptyList())
        val expectedResponse = PlayWidget()

        coEvery {
            viewModel.mediator.getPdpLayout()
        } returns DynamicProductInfoP1(
            basic = BasicInfo(category = Category(detail = listOf(Category.Detail())))
        )

        coEvery {
            viewModel.mediator.getVariant()
        } returns ProductVariant(parentId = "1", children = listOf(VariantChild()))

        coEvery {
            playWidgetTools.getWidgetFromNetwork(
                widgetType = widgetType
            )
        } returns expectedResponse

        coEvery {
            playWidgetTools.mapWidgetToModel(
                expectedResponse
            )
        } returns playWidgetExpectedState

        viewModel.getPlayWidgetData()

        coVerify { viewModel.mediator.getPdpLayout() }
        coVerify { viewModel.mediator.getVariant() }
        assert(viewModel.mediator.getPdpLayout()?.basic?.category != null)
    }

    @Test
    fun `get play widget data error cause by get widget from network`() {
        val widgetType = PlayWidgetUseCase.WidgetType.PDPWidget(emptyList(), emptyList())

        coEvery {
            playWidgetTools.getWidgetFromNetwork(widgetType)
        } throws Throwable()

        viewModel.getPlayWidgetData()

        val result = viewModel.playWidgetModel.getOrAwaitValue()
        assertTrue(result is Fail)
    }

    @Test
    fun `get play widget data error cause by map widget to model`() {
        val widgetType = PlayWidgetUseCase.WidgetType.PDPWidget(emptyList(), emptyList())
        val expectedResponse = PlayWidget()

        coEvery {
            playWidgetTools.getWidgetFromNetwork(widgetType = widgetType)
        } returns expectedResponse

        coEvery {
            playWidgetTools.mapWidgetToModel(expectedResponse)
        } throws Throwable()

        viewModel.getPlayWidgetData()

        assertTrue(viewModel.playWidgetModel.getOrAwaitValue() is Fail)
    }

    @Test
    fun `play widget toggle reminder success`() {
        val fakeState = PlayWidgetState(
            model = PlayWidgetUiModel(
                "title",
                "action title",
                "applink",
                true,
                PlayWidgetConfigUiModel(
                    true,
                    1000,
                    true,
                    1,
                    1,
                    2,
                    1
                ),
                PlayWidgetBackgroundUiModel("", "", "", listOf(), ""),
                listOf()
            ),
            isLoading = false
        )
        val fakeChannelId = "123"
        val fakeReminderType = PlayWidgetReminderType.Reminded

        val expectedReminder = PlayWidgetReminder()
        val expectedMapReminder = true

        coEvery {
            playWidgetTools.updateActionReminder(
                fakeState,
                fakeChannelId,
                fakeReminderType
            )
        } returns fakeState

        coEvery {
            playWidgetTools.updateToggleReminder(fakeChannelId, fakeReminderType)
        } returns expectedReminder

        coEvery {
            playWidgetTools.mapWidgetToggleReminder(expectedReminder)
        } returns expectedMapReminder

        viewModel.updatePlayWidgetToggleReminder(
            fakeState,
            fakeChannelId,
            fakeReminderType
        )

        viewModel.playWidgetModel.verifySuccessEquals(Success(fakeState))
        viewModel.playWidgetReminderSwitch.verifySuccessEquals(Success(fakeReminderType))
    }

    @Test
    fun `play widget toggle reminder fail cause by map reminder return false`() {
        val fakeState = PlayWidgetState(
            model = PlayWidgetUiModel(
                "title",
                "action title",
                "applink",
                true,
                PlayWidgetConfigUiModel(
                    true,
                    1000,
                    true,
                    1,
                    1,
                    2,
                    1
                ),
                PlayWidgetBackgroundUiModel("", "", "", listOf(), ""),
                listOf()
            ),
            isLoading = false
        )
        val fakeChannelId = "123"
        val fakeReminderType = PlayWidgetReminderType.Reminded

        val expectedReminder = PlayWidgetReminder()
        val expectedMapReminder = false

        coEvery {
            playWidgetTools.updateActionReminder(
                fakeState,
                fakeChannelId,
                fakeReminderType
            )
        } returns fakeState

        coEvery {
            playWidgetTools.updateToggleReminder(fakeChannelId, fakeReminderType)
        } returns expectedReminder

        coEvery {
            playWidgetTools.mapWidgetToggleReminder(expectedReminder)
        } returns expectedMapReminder

        coEvery {
            playWidgetTools.updateActionReminder(
                fakeState,
                fakeChannelId,
                fakeReminderType.switch()
            )
        } returns fakeState

        viewModel.updatePlayWidgetToggleReminder(
            fakeState,
            fakeChannelId,
            fakeReminderType
        )

        viewModel.playWidgetModel.verifySuccessEquals(Success(fakeState))
        viewModel.playWidgetReminderSwitch.verifyErrorEquals(Fail(Throwable()))
    }

    @Test
    fun `play widget toggle reminder fail cause by exception`() {
        val fakeState = PlayWidgetState(
            model = PlayWidgetUiModel(
                "title",
                "action title",
                "applink",
                true,
                PlayWidgetConfigUiModel(
                    true,
                    1000,
                    true,
                    1,
                    1,
                    2,
                    1
                ),
                PlayWidgetBackgroundUiModel("", "", "", listOf(), ""),
                listOf()
            ),
            isLoading = false
        )
        val fakeChannelId = "123"
        val fakeReminderType = PlayWidgetReminderType.Reminded

        val expectedThrowable = Throwable()

        coEvery {
            playWidgetTools.updateActionReminder(
                fakeState,
                fakeChannelId,
                fakeReminderType
            )
        } returns fakeState

        coEvery {
            playWidgetTools.updateToggleReminder(fakeChannelId, fakeReminderType)
        } throws expectedThrowable

        coEvery {
            playWidgetTools.updateActionReminder(
                fakeState,
                fakeChannelId,
                fakeReminderType.switch()
            )
        } returns fakeState

        viewModel.updatePlayWidgetToggleReminder(
            fakeState,
            fakeChannelId,
            fakeReminderType
        )

        viewModel.playWidgetModel.verifySuccessEquals(Success(fakeState))
        viewModel.playWidgetReminderSwitch.verifyErrorEquals(Fail(expectedThrowable))
    }
}
