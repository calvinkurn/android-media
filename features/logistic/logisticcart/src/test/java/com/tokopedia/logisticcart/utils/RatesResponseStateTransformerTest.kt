package com.tokopedia.logisticcart.utils

import com.tokopedia.logisticcart.shipping.model.ShippingCourierViewModel
import com.tokopedia.logisticcart.shipping.model.ShippingDurationViewModel
import com.tokopedia.logisticcart.shipping.model.ShippingRecommendationData
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.ProductData
import junit.framework.Assert.*
import org.junit.Test

class RatesResponseStateTransformerTest {

    @Test
    fun determineRatesResponseCondition() {
        val data = ShippingRecommendationData().apply {
            shippingDurationViewModels = listOf(
                    ShippingDurationViewModel().apply {
                        shippingCourierViewModelList = listOf(
                                ShippingCourierViewModel().apply {
                                    productData = ProductData().apply {
                                        shipperProductId = 37
                                    }
                                }
                        )
                    }
            )
        }

        val actual = RatesResponseStateTransformer(listOf(), 37, 0).call(data)
        assertTrue(actual.shippingDurationViewModels[0].isSelected)
        assertTrue(actual.shippingDurationViewModels[0].shippingCourierViewModelList[0].isSelected)
    }
}