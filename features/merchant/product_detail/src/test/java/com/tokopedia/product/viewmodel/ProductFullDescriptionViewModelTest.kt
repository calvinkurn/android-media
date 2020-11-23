package com.tokopedia.product.viewmodel

import com.tokopedia.product.detail.common.ProductDetailCommonConstant
import com.tokopedia.product.detail.data.model.spesification.ProductSpecificationResponse
import com.tokopedia.product.detail.usecase.GetProductSpecificationUseCase
import com.tokopedia.product.detail.view.viewmodel.ProductFullDescriptionViewModel
import com.tokopedia.product.util.BaseProductViewModelTest
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.slot
import org.junit.After
import org.junit.Assert
import org.junit.Test

/**
 * Created by Yehezkiel on 22/07/20
 */
class ProductFullDescriptionViewModelTest : BaseProductViewModelTest() {

    @RelaxedMockK
    lateinit var getProductSpecificationUseCase: GetProductSpecificationUseCase

    private val viewModel by lazy {
        ProductFullDescriptionViewModel(CoroutineTestDispatchersProvider, getProductSpecificationUseCase)
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