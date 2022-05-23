package com.tokopedia.product.detail.bseducational

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.product.detail.bseducational.data.EducationalButtons
import com.tokopedia.product.detail.bseducational.data.ProductEducational
import com.tokopedia.product.detail.bseducational.data.ProductEducationalResponse
import com.tokopedia.product.detail.bseducational.usecase.GetProductEducationalUseCase
import com.tokopedia.product.detail.bseducational.view.ProductEducationalViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ProductEducationalViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @RelaxedMockK
    lateinit var getProductEducationalUseCase: GetProductEducationalUseCase

    val viewModel by lazy {
        createViewModel()
    }

    private fun createViewModel(): ProductEducationalViewModel {
        return ProductEducationalViewModel(
                "1",
                getProductEducationalUseCase,
                CoroutineTestDispatchersProvider
        )
    }

    @Test
    fun `success get educational bottomsheet`() {
        val mockSuccess = ProductEducational(response = ProductEducationalResponse(
                title = "hello",
                description = "desc",
                icon = "ic",
                educationalButtons = listOf(EducationalButtons(
                        buttonTitle = "buttontitle",
                        buttonColor = "buttoncolor",
                        buttonApplink = "buttonapplink",
                        buttonWebLink = "buttonweblink"
                ),
                        EducationalButtons(
                                buttonTitle = "buttontitle",
                                buttonColor = "buttoncolor",
                                buttonApplink = "buttonapplink",
                                buttonWebLink = "buttonweblink")
                ))
        )

        coEvery {
            getProductEducationalUseCase
                    .executeOnBackground(any())
        } returns mockSuccess

        Assert.assertTrue(viewModel.educationalData.value is Success)
        val successValue = (viewModel.educationalData.value as Success).data
        Assert.assertEquals(successValue.response.title, "hello")
        Assert.assertEquals(successValue.response.educationalButtons.size, 2)
    }

    @Test
    fun `fail get educational bottomsheet`() {
        coEvery {
            getProductEducationalUseCase
                    .executeOnBackground(any())
        } throws Throwable("error")

        Assert.assertTrue(viewModel.educationalData.value is Fail)
    }
}