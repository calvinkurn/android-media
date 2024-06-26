package com.tokopedia.logisticcart.utils

import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.ProductData
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.RatesResponseStateConverter
import com.tokopedia.logisticcart.shipping.model.ShippingCourierUiModel
import com.tokopedia.logisticcart.shipping.model.ShippingDurationUiModel
import com.tokopedia.logisticcart.shipping.model.ShippingRecommendationData
import junit.framework.Assert.assertTrue
import org.junit.Test

class RatesResponseStateConverterTest {

    @Test
    fun determineRatesResponseCondition() {
        val data = ShippingRecommendationData().apply {
            shippingDurationUiModels = listOf(
                ShippingDurationUiModel().apply {
                    shippingCourierViewModelList = listOf(
                        ShippingCourierUiModel().apply {
                            productData = ProductData(
                                shipperProductId = 37
                            )
                        }
                    )
                }
            )
        }

        val actual = RatesResponseStateConverter().fillState(data, listOf(), 37, 0)
        assertTrue(actual.shippingDurationUiModels[0].isSelected)
        assertTrue(actual.shippingDurationUiModels[0].shippingCourierViewModelList[0].isSelected)
    }
}
