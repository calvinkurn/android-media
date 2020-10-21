package com.tokopedia.home_recom.model.datamodel

import com.tokopedia.home_recom.model.entity.ProductDetailData
import com.tokopedia.home_recom.view.adapter.HomeRecommendationTypeFactoryImpl
import io.mockk.mockk
import org.junit.Assert.*
import org.junit.Test

/**
 * Created by Lukas on 2019-07-15
 */
class ProductInfoDataModelTest{
    private val visitor = HomeRecommendationTypeFactoryImpl(mockk(), mockk(), mockk(), mockk())
    private fun productInfoDataModelFactory(productDetailData: ProductDetailData) = ProductInfoDataModel(productDetailData)

    @Test
    fun testProductInfoDataModel(){
        //given
        val mock = mockk<ProductDetailData>()
        val layout = productInfoDataModelFactory(mock).type(visitor)
        //then
        assertEquals(layout, ProductInfoDataModel.LAYOUT)
    }
}