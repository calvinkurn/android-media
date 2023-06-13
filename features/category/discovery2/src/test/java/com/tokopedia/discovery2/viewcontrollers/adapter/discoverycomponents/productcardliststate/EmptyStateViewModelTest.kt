package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardliststate

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.emptystate.EmptyStateModel
import com.tokopedia.discovery2.repository.emptystate.EmptyStateRepository
import com.tokopedia.discovery2.usecase.emptystateusecase.EmptyStateUseCase
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class EmptyStateViewModelTest{
    @get:Rule
    var rule = InstantTaskExecutorRule()

    private val componentsItem: ComponentsItem = mockk()
    private val application: Application = mockk()
    private val viewModel: EmptyStateViewModel by lazy {
        spyk(EmptyStateViewModel(application, componentsItem, 99))
    }

    private val emptyStateUseCase: EmptyStateUseCase by lazy {
        mockk()
    }

    private val emptyStateRepository: EmptyStateRepository by lazy {
        mockk()
    }

    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(TestCoroutineDispatcher())
        every { componentsItem.data } returns null
    }

    @Test
    fun `position test`() {
        assert(viewModel.position == 99)
    }

    @Test
    fun `test for components`() {
        assert(viewModel.components === componentsItem)
    }

    @Test
    fun `test for application`(){
        assert(viewModel.application === application)
    }

    /**************************** getEmptyStateData() *******************************************/

    @Test
    fun getEmptyStateData() {
        viewModel.emptyStateUseCase = emptyStateUseCase
        viewModel.emptyStateRepository = emptyStateRepository
        val emptyStateModel = mockk<EmptyStateModel>(relaxed = true)
        every { viewModel.emptyStateRepository.getEmptyStateData(any()) } returns emptyStateModel

        assertEquals(viewModel.getEmptyStateData(), emptyStateModel)

    }
    /**************************** getEmptyStateData() *******************************************/



    /**************************** handleEmptyStateReset() *******************************************/

    @Test
    fun handleEmptyStateReset() {
        every { viewModel.emptyStateUseCase.resetChildComponents(any()) } returns true

        viewModel.handleEmptyStateReset()

        assertEquals(viewModel.syncData.value, true)

    }
    /**************************** handleEmptyStateReset() *******************************************/


    @After
    fun shutDown() {
        Dispatchers.resetMain()
    }
}
