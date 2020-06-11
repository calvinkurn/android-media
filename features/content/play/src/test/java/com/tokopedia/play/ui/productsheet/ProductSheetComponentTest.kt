package com.tokopedia.play.ui.productsheet

import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.play.component.EventBusFactory
import com.tokopedia.play.extensions.isAnyHidden
import com.tokopedia.play.extensions.isAnyShown
import com.tokopedia.play.helper.TestCoroutineDispatchersProvider
import com.tokopedia.play.model.ModelBuilder
import com.tokopedia.play.view.event.ScreenStateEvent
import com.tokopedia.play.view.type.BottomInsetsState
import com.tokopedia.play.view.type.BottomInsetsType
import com.tokopedia.play.view.uimodel.ProductSheetUiModel
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.net.ConnectException
import java.net.UnknownHostException

/**
 * Created by jegul on 24/03/20
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProductSheetComponentTest {

    private lateinit var component: ProductSheetComponent
    private val owner = mockk<LifecycleOwner>(relaxed = true)

    private val testDispatcher = TestCoroutineDispatcher()
    private val coroutineScope = CoroutineScope(testDispatcher)

    private val modelBuilder = ModelBuilder()

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { owner.lifecycle } returns mockk(relaxed = true)

        component = ProductSheetComponentMock(mockk(relaxed = true), EventBusFactory.get(owner), coroutineScope)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when the data is loading, then placeholder should be shown`() = runBlockingTest(testDispatcher) {
        val mockResult = modelBuilder.buildPlayResultLoading<ProductSheetUiModel>()

        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.SetProductSheet(mockResult))
        verify { component.uiView.showPlaceholder() }
        confirmVerified(component.uiView)
    }

    @Test
    fun `when the data is failed to load, then product sheet should show error`() = runBlockingTest(testDispatcher) {
        val mockResult = modelBuilder.buildPlayResultFailure<ProductSheetUiModel>()

        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.SetProductSheet(mockResult))
        verify {
            component.uiView.showError(
                    isConnectionError = mockResult.error is ConnectException || mockResult.error is UnknownHostException,
                    onError = mockResult.onRetry
            )
        }
        confirmVerified(component.uiView)
    }

    @Test
    fun `given data is successfully retrieved, when product list is empty, then empty state should be shown`() = runBlockingTest(testDispatcher) {
        val mockResult = modelBuilder.buildPlayResultSuccess(
                modelBuilder.buildProductSheetUiModel()
        )

        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.SetProductSheet(mockResult))
        verify {
            component.uiView.showEmpty(mockResult.data.partnerId)
        }
        confirmVerified(component.uiView)
    }

    @Test
    fun `given data is successfully retrieved, when product list is not empty, then product list should be shown`() = runBlockingTest(testDispatcher) {
        val mockResult = modelBuilder.buildPlayResultSuccess(
                modelBuilder.buildProductSheetUiModel(
                        productList = listOf(modelBuilder.buildProductLineUiModel())
                )
        )

        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.SetProductSheet(mockResult))
        verify {
            component.uiView.setProductSheet(mockResult.data)
        }
        confirmVerified(component.uiView)
    }

    @Test
    fun `when product sheet should be shown, then product sheet should show`() = runBlockingTest(testDispatcher) {
        val mockBottomInsets = modelBuilder.buildBottomInsetsMap(
                productSheetState = modelBuilder.buildBottomInsetsState(isShown = true)
        )

        val mockStateHelper = modelBuilder.buildStateHelperUiModel(
                bottomInsets = mockBottomInsets
        )

        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.BottomInsetsChanged(mockBottomInsets, mockBottomInsets.isAnyShown, mockBottomInsets.isAnyHidden, mockStateHelper))
        verify { component.uiView.showWithHeight((mockBottomInsets[BottomInsetsType.ProductSheet] as BottomInsetsState.Shown).estimatedInsetsHeight) }
        confirmVerified(component.uiView)
    }

    @Test
    fun `when product sheet should be hidden, then product sheet should hide`() = runBlockingTest(testDispatcher) {
        val mockBottomInsets = modelBuilder.buildBottomInsetsMap(
                productSheetState = modelBuilder.buildBottomInsetsState(isShown = false)
        )

        val mockStateHelper = modelBuilder.buildStateHelperUiModel(
                bottomInsets = mockBottomInsets
        )

        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.BottomInsetsChanged(mockBottomInsets, mockBottomInsets.isAnyShown, mockBottomInsets.isAnyHidden, mockStateHelper))
        verify { component.uiView.hide() }
        confirmVerified(component.uiView)
    }

    class ProductSheetComponentMock(container: ViewGroup, bus: EventBusFactory, scope: CoroutineScope) : ProductSheetComponent(container, bus, scope, TestCoroutineDispatchersProvider) {
        override fun initView(container: ViewGroup): ProductSheetView {
            return mockk(relaxed = true)
        }
    }
}