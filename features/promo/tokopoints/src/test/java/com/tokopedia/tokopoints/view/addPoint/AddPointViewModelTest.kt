package com.tokopedia.tokopoints.view.addPoint

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.tokopoints.view.model.addpointsection.SheetHowToGetV2
import com.tokopedia.tokopoints.view.util.Loading
import com.tokopedia.tokopoints.view.util.Resources
import com.tokopedia.tokopoints.view.util.Success
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import org.junit.Rule
import kotlin.reflect.KClass

class AddPointViewModelTest {

    val useCase = mockk<RewardUseCase>()
    lateinit var  viewModel : AddPointViewModel

    @get:Rule
    var rule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        Dispatchers.setMain(TestCoroutineDispatcher())
        viewModel = AddPointViewModel(useCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getRewardPoint success case`() {
        val observer = mockk<Observer<Resources<SheetHowToGetV2>>>{
            every { onChanged(any()) } just Runs
        }
        val data = mockk<SheetHowToGetV2>()
        coEvery{ useCase.execute() } returns mockk{
            every { sheetHowToGetV2 } returns data
        }
        viewModel.sheetLiveData.observeForever(observer)
        viewModel.getRewardPoint()
        verify(ordering = Ordering.ORDERED) {
            observer.onChanged(ofType(Loading::class as KClass<Loading<SheetHowToGetV2>>))
            observer.onChanged(ofType(Success::class as KClass<Success<SheetHowToGetV2>>))
        }

        val result = viewModel.sheetLiveData.value as Success
        assert(result.data == data)
    }

    @Test
    fun `getRewardPoint error case`() {
        val observer = mockk<Observer<Resources<SheetHowToGetV2>>>{
            every { onChanged(any()) } just Runs
        }
        viewModel.sheetLiveData.observeForever(observer)
        viewModel.getRewardPoint()
        verify(ordering = Ordering.ORDERED) {
            observer.onChanged(ofType(Loading::class as KClass<Loading<SheetHowToGetV2>>))
        }
    }
}