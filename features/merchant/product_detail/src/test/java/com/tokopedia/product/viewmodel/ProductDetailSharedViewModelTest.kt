package com.tokopedia.product.viewmodel

import com.tokopedia.product.detail.view.fragment.ProductVideoDetailDataModel
import com.tokopedia.product.detail.view.viewmodel.ProductDetailSharedViewModel
import com.tokopedia.product.detail.view.widget.ProductVideoDataModel
import com.tokopedia.product.estimasiongkir.data.model.RatesEstimateRequest
import com.tokopedia.product.util.BaseProductViewModelTest
import org.junit.Assert
import org.junit.Test

/**
 * Created by Yehezkiel on 03/03/21
 */
class ProductDetailSharedViewModelTest : BaseProductViewModelTest() {

    private val sharedViewModel: ProductDetailSharedViewModel by lazy {
        ProductDetailSharedViewModel()
    }

    @Test
    fun `update video detail in video detail fragment`() {
        sharedViewModel.updateVideoDetailData(ProductVideoDetailDataModel())
        Assert.assertNotNull(sharedViewModel.productVideoDetailData)
    }

    @Test
    fun `update video detail in pdp fragment`() {
        sharedViewModel.updateVideoDataInPreviousFragment(listOf(ProductVideoDataModel()))
        Assert.assertNotNull(sharedViewModel.productVideoData)
    }

    @Test
    fun `update rates request data in rates bottom sheet`() {
        sharedViewModel.setRequestData(RatesEstimateRequest())
        Assert.assertNotNull(sharedViewModel.rateEstimateRequest)
    }

    @Test
    fun `update rates data when address changed in pdp fragment`() {
        sharedViewModel.setAddressChanged(true)
        Assert.assertNotNull(sharedViewModel.isAddressChanged)
    }
}