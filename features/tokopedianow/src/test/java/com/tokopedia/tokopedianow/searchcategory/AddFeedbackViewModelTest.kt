package com.tokopedia.tokopedianow.searchcategory

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchersProvider
import com.tokopedia.tokopedianow.searchcategory.domain.model.AddProductFeedbackModel
import com.tokopedia.tokopedianow.searchcategory.presentation.viewmodel.AddFeedbackViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.coroutines.UseCase
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class AddFeedbackViewModelTest {

    private lateinit var viewModel:AddFeedbackViewModel
    private var addFeedbackUseCase:UseCase<AddProductFeedbackModel> = mockk()
    private val successCode = "200"
    private val errorCode = "300"

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup(){
        viewModel = AddFeedbackViewModel(
            CoroutineDispatchersProvider,
            addFeedbackUseCase
        )
    }

    @Test
    fun `add feedback success`(){
        val response = mockk<AddProductFeedbackModel>(relaxed = true)
        every {response.tokonowAddFeedback?.header?.errorCode} returns successCode
        val query = ""
        every { addFeedbackUseCase.execute(any(),any(),any()) }answers {
            firstArg<(AddProductFeedbackModel) -> Unit>().invoke(response)
        }
        viewModel.addProductFeedback(query)
        Assert.assertEquals((viewModel.addFeedbackResult.value as Success).data,response)
    }

    @Test
    fun `ad feedback null response error`(){
        val response = AddProductFeedbackModel(null)
        val query = ""
        every { addFeedbackUseCase.execute(any(),any(),any()) }answers {
            firstArg<(AddProductFeedbackModel) -> Unit>().invoke(response)
        }
        viewModel.addProductFeedback(query)
        Assert.assertEquals(viewModel.addFeedbackResult.value is Fail,true)
    }

    @Test
    fun `ad feedback null header error`(){
        val response = AddProductFeedbackModel(AddProductFeedbackModel.TokonowAddFeedback())
        val query = ""
        every { addFeedbackUseCase.execute(any(),any(),any()) }answers {
            firstArg<(AddProductFeedbackModel) -> Unit>().invoke(response)
        }
        viewModel.addProductFeedback(query)
        Assert.assertEquals(viewModel.addFeedbackResult.value is Fail,true)
    }

    @Test
    fun `ad feedback error code not equal to success code`(){
        val response = mockk<AddProductFeedbackModel>(relaxed = true)
        every {response.tokonowAddFeedback?.header?.errorCode} returns errorCode
        val query = ""
        every { addFeedbackUseCase.execute(any(),any(),any()) }answers {
            firstArg<(AddProductFeedbackModel) -> Unit>().invoke(response)
        }
        viewModel.addProductFeedback(query)
        Assert.assertEquals(viewModel.addFeedbackResult.value is Fail,true)
    }

    @Test
    fun `add feedback failure`(){
        val error = Throwable("Internet not found")
        val query = ""
        every { addFeedbackUseCase.execute(any(),any(),any()) }answers {
            secondArg<(Throwable) -> Unit>().invoke(error)
        }
        viewModel.addProductFeedback(query)
        Assert.assertEquals((viewModel.addFeedbackResult.value as Fail).throwable,error)
        Assert.assertEquals((viewModel.addFeedbackResult.value as Fail).throwable.message,error.message)
    }


}
