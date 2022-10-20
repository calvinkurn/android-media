package com.tokopedia.tokopoints.view.couponlisting

import android.os.Bundle
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.tokopoints.view.model.CouponFilterBase
import com.tokopedia.tokopoints.view.util.ErrorMessage
import com.tokopedia.tokopoints.view.util.Loading
import com.tokopedia.tokopoints.view.util.Resources
import com.tokopedia.tokopoints.view.util.Success
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Rule
import kotlin.reflect.KClass

@ExperimentalCoroutinesApi
class StackedCouponActivtyViewModelTest {

    lateinit var viewModel: StackedCouponActivtyViewModel

    val bundle = mockk<Bundle>{
        every { getString("slug") } returns ""
    }
    val respository = mockk<StackedCouponRepository>()

    @get:Rule
    var rule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        Dispatchers.setMain(TestCoroutineDispatcher())
        viewModel = StackedCouponActivtyViewModel(bundle, respository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getFilter success case`() {
        runBlockingTest {
            val observer = mockk<Observer<Resources<CouponFilterBase>>> {
                every { onChanged(any()) } just Runs
            }
            viewModel.couponFilterViewModel.observeForever(observer)
            val data = mockk<CouponFilterBase>()
            coEvery { respository.getFilter("") } returns mockk {
                every { getData<CouponFilterBase>(CouponFilterBase::class.java) } returns data
                every { getError(CouponFilterBase::class.java) } returns null
            }
            viewModel.getFilter()
            verifyOrder {
                observer.onChanged(ofType(Loading::class as KClass<Loading<CouponFilterBase>>))
                observer.onChanged(ofType(Success::class as KClass<Success<CouponFilterBase>>))
            }
            val result = viewModel.couponFilterViewModel.value as Success
            assert(data == result.data)
        }
    }

    @Test
    fun `getFilter error case`() {
        runBlockingTest {
            val observer = mockk<Observer<Resources<CouponFilterBase>>> {
                every { onChanged(any()) } just Runs
            }
            viewModel.couponFilterViewModel.observeForever(observer)
            viewModel.getFilter()
            verifyOrder {
                observer.onChanged(ofType(Loading::class as KClass<Loading<CouponFilterBase>>))
                observer.onChanged(ofType(ErrorMessage::class as KClass<ErrorMessage<CouponFilterBase>>))
            }
        }
    }
}
