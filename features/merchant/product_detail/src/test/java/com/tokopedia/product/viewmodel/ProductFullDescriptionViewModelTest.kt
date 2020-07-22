package com.tokopedia.product.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.product.detail.common.ProductDetailCommonConstant
import com.tokopedia.product.detail.data.model.spesification.ProductSpecificationResponse
import com.tokopedia.product.detail.usecase.GetProductSpecificationUseCase
import com.tokopedia.product.detail.view.viewmodel.ProductFullDescriptionViewModel
import com.tokopedia.product.util.TestDispatcherProvider
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.slot
import org.junit.*

/**
 * Created by Yehezkiel on 22/07/20
 */
class ProductFullDescriptionViewModelTest {
    @RelaxedMockK
    lateinit var getProductSpecificationUseCase: GetProductSpecificationUseCase

    private val viewModel by lazy {
        ProductFullDescriptionViewModel(TestDispatcherProvider(), getProductSpecificationUseCase)
    }

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @After
    fun clear() {
        viewModel.specificationResponseData.removeObserver { }
    }

    //region Specification
    @Test
    fun `on success get product specification`() {
        val catalogId = "1"
        val slotCatalogId = slot<RequestParams>()
        val productSpecificationSuccessData = ProductSpecificationResponse()
        viewModel.specificationResponseData.observeForever { }

        //Given
        coEvery {
            getProductSpecificationUseCase.executeOnBackground(capture(slotCatalogId))
        } returns productSpecificationSuccessData

        //When
        viewModel.setCatalogId(catalogId)

        //Then
        coVerify {
            getProductSpecificationUseCase.executeOnBackground(any())
        }

        Assert.assertTrue(viewModel.specificationResponseData.value is Success)
        Assert.assertTrue(catalogId == slotCatalogId.captured.getString(ProductDetailCommonConstant.PARAM_CATALOG_ID, ""))
    }

    @Test
    fun `on fail get product specification`() {
        val catalogId = "1"
        val slotCatalogId = slot<RequestParams>()
        viewModel.specificationResponseData.observeForever { }

        //Given
        coEvery {
            getProductSpecificationUseCase.executeOnBackground(capture(slotCatalogId))
        } throws Throwable()

        //When
        viewModel.setCatalogId(catalogId)

        //Then
        coVerify {
            getProductSpecificationUseCase.executeOnBackground(any())
        }

        Assert.assertTrue(viewModel.specificationResponseData.value is Fail)
        Assert.assertTrue(catalogId == slotCatalogId.captured.getString(ProductDetailCommonConstant.PARAM_CATALOG_ID, ""))
    }
    //endregion

}