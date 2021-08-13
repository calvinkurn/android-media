package com.tokopedia.layanan_finansial.view.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.layanan_finansial.view.models.LayananFinansialModel
import com.tokopedia.layanan_finansial.view.models.LayananFinansialOuter
import com.tokopedia.layanan_finansial.view.models.LayananSectionModel
import com.tokopedia.layanan_finansial.view.usecase.LayananUsecase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class LayananFinansialViewModelTest {

    private val usecase = mockk<LayananUsecase>(relaxed = true)
    private lateinit var viewModel: LayananFinansialViewModel
    val observer = mockk<Observer<Result<LayananFinansialModel>>>(relaxed = true)

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        Dispatchers.setMain(TestCoroutineDispatcher())
        viewModel = LayananFinansialViewModel(usecase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }


    @Test
    fun `getDetail success`() {
        val data1 = mockk<LayananFinansialOuter>()
        every { data1.data?.sectionList?.get(any())?.title } returns "title"

        val data = LayananFinansialOuter(LayananFinansialModel(
                listOf(LayananSectionModel(
                        "title", "subtitle", "bg", "type", listOf()))
        ))
        coEvery { usecase.execute() } returns data
        viewModel.liveData.observeForever(observer)
        viewModel.getDetail()
        assert(viewModel.liveData.value is Success)
        assert((viewModel.liveData.value as Success).data.sectionList?.get(0)?.title == "title")
    }

    @Test
    fun `getDetail fail with null`() {
        val data = LayananFinansialOuter()

        coEvery { usecase.execute() } returns data
        viewModel.liveData.observeForever(observer)
        viewModel.getDetail()
        assert(viewModel.liveData.value is Fail)
    }

    @Test
    fun `getDetail fail`() {
        val observer = mockk<Observer<Result<LayananFinansialModel>>>()

        coEvery { usecase.execute() } throws mockk<MessageErrorException> {
            every { message } returns "title|message"
        }
        viewModel.liveData.observeForever(observer)
        viewModel.getDetail()
        assert(viewModel.liveData.value is Fail)
    }
}