package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.tokopoints

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.usecase.tokopointsusecase.TokopointsListDataUseCase
import io.mockk.*
import junit.framework.TestCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.*

class TokopointsViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val componentsItem: ComponentsItem = mockk(relaxed = true)
    private val application: Application = mockk()

    private val viewModel: TokopointsViewModel by lazy {
        spyk(TokopointsViewModel(application, componentsItem, 99))
    }

    private val tokopointsListDataUseCase: TokopointsListDataUseCase by lazy {
        mockk()
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(TestCoroutineDispatcher())
    }

    @Test
    fun `test for useCase`() {
        val viewModel: TokopointsViewModel =
                spyk(TokopointsViewModel(application, componentsItem, 99))

        val tokopointsListDataUseCase = mockk<TokopointsListDataUseCase>()
        viewModel.tokopointsListDataUseCase = tokopointsListDataUseCase

        assert(viewModel.tokopointsListDataUseCase === tokopointsListDataUseCase)
    }


    @Test
    fun `test for position passed`() {
        assert(viewModel.position == 99)
    }

    @Test
    fun `component value is present in live data`() {
        assert(viewModel.getTokopointsComponentData().value == componentsItem)
    }

    /**************************** test for fetchTokopointsListData() *******************************************/

    @Test
    fun `test for fetchTokopointsListData when getTokopointsDataUseCase returns error`() {
        viewModel.tokopointsListDataUseCase = tokopointsListDataUseCase
        coEvery {
            tokopointsListDataUseCase.getTokopointsDataUseCase(any(), any())
        } throws Exception("Error")

        viewModel.fetchTokopointsListData(componentsItem.pageEndPoint)

        TestCase.assertEquals(viewModel.getTokopointsItemsListData().value == null, true)

    }

    @Test
    fun `test for fetchTokopointsListData when getTokopointsDataUseCase returns false`() {
        viewModel.tokopointsListDataUseCase = tokopointsListDataUseCase
        coEvery {
            tokopointsListDataUseCase.getTokopointsDataUseCase(any(), any())
        } returns false
        viewModel.fetchTokopointsListData(componentsItem.pageEndPoint)

        TestCase.assertEquals(viewModel.getTokopointsItemsListData().value == null, true)
    }

    @Test
    fun `test for fetchTokopointsListData when getTokopointsDataUseCase returns true`() {
        viewModel.tokopointsListDataUseCase = tokopointsListDataUseCase
        coEvery {
            tokopointsListDataUseCase.getTokopointsDataUseCase(any(), any())
        } returns true
        viewModel.fetchTokopointsListData(componentsItem.pageEndPoint)

        TestCase.assertEquals(viewModel.getTokopointsItemsListData().value != null, true)
    }

    /**************************** end of fetchTokopointsListData() *******************************************/

    @After
    fun shutDown() {
        Dispatchers.resetMain()
    }
}