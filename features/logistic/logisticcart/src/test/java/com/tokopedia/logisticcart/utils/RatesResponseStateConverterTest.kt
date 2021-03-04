package com.tokopedia.logisticcart.utils

import com.tokopedia.logisticcart.shipping.features.shippingduration.view.RatesResponseStateConverter
import com.tokopedia.logisticcart.shipping.model.ShippingCourierUiModel
import com.tokopedia.logisticcart.shipping.model.ShippingDurationUiModel
import com.tokopedia.logisticcart.shipping.model.ShippingRecommendationData
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.ProductData
import junit.framework.Assert.*
import org.junit.Test

class RatesResponseStateConverterTest {

    @Test
    fun determineRatesResponseCondition() {
        val data = ShippingRecommendationData().apply {
            shippingDurationViewModels = listOf(
                    ShippingDurationUiModel().apply {
                        shippingCourierViewModelList = listOf(
                                ShippingCourierUiModel().apply {
                                    productData = ProductData().apply {
                                        shipperProductId = 37
                                    }
                                }
                        )
                    }
            )
        }

        val actual = RatesResponseStateConverter().fillState(data, listOf(), 37, 0)
        assertTrue(actual.shippingDurationViewModels[0].isSelected)
        assertTrue(actual.shippingDurationViewModels[0].shippingCourierViewModelList[0].isSelected)
    }
}