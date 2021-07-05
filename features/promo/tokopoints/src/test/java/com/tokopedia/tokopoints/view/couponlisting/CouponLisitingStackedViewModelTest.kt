package com.tokopedia.tokopoints.view.couponlisting

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.tokopoints.view.model.TokoPointPromosEntity
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
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import kotlin.reflect.KClass


class CouponLisitingStackedViewModelTest {


    lateinit var viewModel: CouponLisitingStackedViewModel
    private val mRepository = mockk<StackedCouponRepository>()
    @get:Rule
    var rule = InstantTaskExecutorRule()

    @get:Rule
    var thrown = ExpectedException.none()

    @Before
    fun setUp() {
        Dispatchers.setMain(TestCoroutineDispatcher())
        viewModel = CouponLisitingStackedViewModel(mRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun getCoupons() {
        val observer = mockk<Observer<Resources<TokoPointPromosEntity>>> {
            every { onChanged(any()) } just Runs
        }
        val id = 1
        viewModel.startAdapter.observeForever(observer)
        viewModel.getCoupons(id)

        coVerify(exactly = 1) { observer.onChanged(ofType(Loading::class as KClass<Loading<TokoPointPromosEntity>>)) }
        assert(viewModel.category == id)
    }

    @Test
    fun `getList success case 1`() {
        runBlockingTest {
            val observer = mockk<Observer<Resources<TokoPointPromosEntity>>> {
                every { onChanged(any()) } just Runs
            }
            viewModel.category = 1
            val response = mockk<TokoPointPromosEntity>{
                every { coupon } returns mockk{
                    every { coupons } returns mockk()
                }
            }
            val data = mockk<GraphqlResponse>() {
                every {  getData<TokoPointPromosEntity>(TokoPointPromosEntity::class.java) } returns response
                every { getError(TokoPointPromosEntity::class.java) } returns null
            }
            coEvery { mRepository.getCouponList(1,1) } returns data
            viewModel.startAdapter.observeForever(observer)
            viewModel.getList(1)

            coVerify(exactly = 1) { observer.onChanged(ofType(Success::class as KClass<Success<TokoPointPromosEntity>>)) }
            val result = viewModel.startAdapter.value as Success
            assert(response == result.data)
        }

    }

    @Test
    fun `getList error case 2`() {
        runBlockingTest {
            val observer = mockk<Observer<Resources<TokoPointPromosEntity>>> {
                every { onChanged(any()) } just Runs
            }
            viewModel.category = 1
            val data = mockk<GraphqlResponse>() {
                every {  getData<TokoPointPromosEntity>(TokoPointPromosEntity::class.java) } returns null
                every { getError(TokoPointPromosEntity::class.java) } returns null
            }
            viewModel.category = 1
            coEvery { mRepository.getCouponList(1,1) } returns data
            viewModel.startAdapter.observeForever(observer)
            viewModel.getList(1)

            coVerify(exactly = 1) { observer.onChanged(ofType(ErrorMessage::class as KClass<ErrorMessage<TokoPointPromosEntity>>)) }
        }

    }

    @Test
    fun `getList error case 1`() {
        runBlockingTest {
            val observer = mockk<Observer<Resources<TokoPointPromosEntity>>> {
                every { onChanged(any()) } just Runs
            }
            viewModel.startAdapter.observeForever(observer)
            viewModel.getList(1)

            coVerify(exactly = 1) { observer.onChanged(ofType(ErrorMessage::class as KClass<ErrorMessage<TokoPointPromosEntity>>)) }
        }

    }

    @Test
    fun getCouponInStack() {
        runBlockingTest {
            val observer = mockk<Observer<TokoPointPromosEntity>> {
                every { onChanged(any()) } just Runs
            }
            val data = mockk<TokoPointPromosEntity>{
                every { coupon } returns mockk{
                    every { coupons } returns mockk()
                }
            }
            coEvery { mRepository.getInStackedCouponList("123") } returns mockk{
                every {  getData<TokoPointPromosEntity>(TokoPointPromosEntity::class.java) } returns data
                every { getError(TokoPointPromosEntity::class.java) } returns null
            }
            viewModel.inStackedAdapter.observeForever(observer)
            viewModel.getCouponInStack("123")

            coVerify(exactly = 1) { observer.onChanged(any()) }
            assert(viewModel.inStackedAdapter.value == data)

        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `getCouponInStack error case`() {
        runBlockingTest {
            val observer = mockk<Observer<TokoPointPromosEntity>> {
                every { onChanged(any()) } just Runs
            }
            coEvery { mRepository.getInStackedCouponList("123") } returns mockk{
                every {  getData<TokoPointPromosEntity>(TokoPointPromosEntity::class.java) } returns null
                every { getError(TokoPointPromosEntity::class.java) } returns null
            }
            viewModel.inStackedAdapter.observeForever(observer)
            viewModel.getCouponInStack("123")

            coVerify(exactly = 0) { observer.onChanged(any()) }
        }
    }
}