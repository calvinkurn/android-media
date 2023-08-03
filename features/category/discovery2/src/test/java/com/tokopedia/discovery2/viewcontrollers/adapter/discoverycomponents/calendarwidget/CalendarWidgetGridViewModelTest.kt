package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.calendarwidget

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.usecase.productCardCarouselUseCase.ProductCardsUseCase
import io.mockk.*
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class CalendarWidgetGridViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val componentsItem: ComponentsItem by lazy {
        mockk(relaxed = true)
    }
    private val application: Application = mockk()

    private val viewModel: CalendarWidgetGridViewModel =
        spyk(CalendarWidgetGridViewModel(application, componentsItem, 99))
    private val calenderWidgetUseCase: ProductCardsUseCase = mockk()

    @Before
    @Throws(Exception::class)
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(TestCoroutineDispatcher())
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test for useCase`() {
        val viewModel: CalendarWidgetGridViewModel =
            spyk(CalendarWidgetGridViewModel(application, componentsItem, 99))

        val calenderWidgetUseCase = mockk<ProductCardsUseCase>()
        viewModel.calenderWidgetUseCase = calenderWidgetUseCase

        assert(viewModel.calenderWidgetUseCase === calenderWidgetUseCase)
    }

    @Test
    fun `test for onAttachToViewHolder`() {
        runBlocking {
            viewModel.calenderWidgetUseCase = calenderWidgetUseCase
            every { componentsItem.properties?.calendarType } returns "dynamic"
            coEvery {
                calenderWidgetUseCase.loadFirstPageComponents(
                    any(),
                    any()
                )
            } returns true

            viewModel.onAttachToViewHolder()

            assertEquals(viewModel.syncData.value, true)
        }
    }

    @Test
    fun `test for position passed`() {
        assert(viewModel.position == 99)
    }

    @Test
    fun `test for components passed`() {
        assert(viewModel.components === componentsItem)
    }
}
