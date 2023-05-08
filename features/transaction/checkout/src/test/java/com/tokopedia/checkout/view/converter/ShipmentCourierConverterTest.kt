package com.tokopedia.checkout.view.converter

import com.tokopedia.checkout.view.DataProvider
import com.tokopedia.checkout.view.helper.ScheduleDeliveryResponseHelper
import com.tokopedia.logisticcart.shipping.features.shippingcourier.view.ShippingCourierConverter
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.ShippingDurationConverter
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

/**
 * Created by victor on 10/11/22
 */

class ShipmentCourierConverterTest {

    private var shippingCourierConverter = ShippingCourierConverter()
    private var shippingDurationConverter = ShippingDurationConverter()

    @Test
    fun `WHEN schedule delivery data is hidden THEN scheduleDeliveryUiModel should return null`() {
        // Given
        val ratesResponse = DataProvider.provideRatesV3EmptyServicesResponse()
        val ratesScheduleDeliveryResponse = DataProvider.provideScheduleDeliveryRatesResponse()
        val mockRatesScheduleDeliveryResponse =
            ScheduleDeliveryResponseHelper.getMockScheduleDeliveryRatesResponse(
                ratesScheduleDeliveryResponse,
                hidden = true
            )
        val shippingRecommendationData =
            shippingDurationConverter.convertModel(ratesResponse.ratesData)
        shippingRecommendationData.scheduleDeliveryData =
            mockRatesScheduleDeliveryResponse.ongkirGetScheduledDeliveryRates.scheduleDeliveryData
        val shipmentCartItemModel = ShipmentCartItemModel()

        // When
        val courierItemData = shippingCourierConverter.convertToCourierItemData(
            null,
            shippingRecommendationData,
            shipmentCartItemModel
        )

        // Then
        assertEquals(null, courierItemData.scheduleDeliveryUiModel)
    }

    @Test
    fun `WHEN schedule delivery data is none and recommend is false THEN scheduleDeliveryUiModel should return isSelected false`() {
        // Given
        val ratesResponse = DataProvider.provideRatesV3EmptyServicesResponse()
        val ratesScheduleDeliveryResponse = DataProvider.provideScheduleDeliveryRatesResponse()
        val shippingRecommendationData =
            shippingDurationConverter.convertModel(ratesResponse.ratesData)
        shippingRecommendationData.scheduleDeliveryData =
            ratesScheduleDeliveryResponse.ongkirGetScheduledDeliveryRates.scheduleDeliveryData
        val shipmentCartItemModel = ShipmentCartItemModel()

        // When
        val courierItemData = shippingCourierConverter.convertToCourierItemData(
            null,
            shippingRecommendationData,
            shipmentCartItemModel
        )

        // Then
        assertEquals(false, courierItemData.scheduleDeliveryUiModel!!.isSelected)
    }

    @Test
    fun `WHEN schedule delivery data is none and recommend is true THEN scheduleDeliveryUiModel should return isSelected true`() {
        // Given
        val ratesResponse = DataProvider.provideRatesV3EmptyServicesResponse()
        val ratesScheduleDeliveryResponse = DataProvider.provideScheduleDeliveryRatesResponse()
        val mockRatesScheduleDeliveryResponse =
            ScheduleDeliveryResponseHelper.getMockScheduleDeliveryRatesResponse(
                ratesScheduleDeliveryResponse,
                recommend = true
            )
        val shippingRecommendationData =
            shippingDurationConverter.convertModel(ratesResponse.ratesData)
        shippingRecommendationData.scheduleDeliveryData =
            mockRatesScheduleDeliveryResponse.ongkirGetScheduledDeliveryRates.scheduleDeliveryData
        val shipmentCartItemModel = ShipmentCartItemModel()

        // When
        val courierItemData = shippingCourierConverter.convertToCourierItemData(
            null,
            shippingRecommendationData,
            shipmentCartItemModel
        )

        // Then
        assertEquals(true, courierItemData.scheduleDeliveryUiModel!!.isSelected)
    }

    @Test
    fun `WHEN SAF schedule delivery data is still available THEN scheduleDeliveryUiModel should return isSelected true`() {
        // Given
        val ratesResponse = DataProvider.provideRatesV3EmptyServicesResponse()
        val ratesScheduleDeliveryResponse = DataProvider.provideScheduleDeliveryRatesResponse()
        val shippingRecommendationData =
            shippingDurationConverter.convertModel(ratesResponse.ratesData)
        shippingRecommendationData.scheduleDeliveryData =
            ratesScheduleDeliveryResponse.ongkirGetScheduledDeliveryRates.scheduleDeliveryData
        val shipmentCartItemModel = ShipmentCartItemModel(
            scheduleDate = "2022-09-20T00:00:00Z",
            timeslotId = 2022092014123,
            validationMetadata = "{\"timeslot_id\":2022092014123,\"schedule_date\":\"2022-09-20T00:00:00Z\",\"shipping_price\":10000}"
        )

        // When
        val courierItemData = shippingCourierConverter.convertToCourierItemData(
            null,
            shippingRecommendationData,
            shipmentCartItemModel
        )

        // Then
        assertEquals(true, courierItemData.scheduleDeliveryUiModel!!.isSelected)
    }

    @Test
    fun `WHEN SAF schedule delivery data is no more available and recommend false THEN scheduleDeliveryUiModel should return isSelected false`() {
        // Given
        val ratesResponse = DataProvider.provideRatesV3EmptyServicesResponse()
        val ratesScheduleDeliveryResponse = DataProvider.provideScheduleDeliveryRatesResponse()
        val shippingRecommendationData =
            shippingDurationConverter.convertModel(ratesResponse.ratesData)
        shippingRecommendationData.scheduleDeliveryData =
            ratesScheduleDeliveryResponse.ongkirGetScheduledDeliveryRates.scheduleDeliveryData
        val shipmentCartItemModel = ShipmentCartItemModel(
            scheduleDate = "2022-10-20T00:00:00Z",
            timeslotId = 2022092014124,
            validationMetadata = "{\"timeslot_id\":2022092014124,\"schedule_date\":\"2022-10-20T00:00:00Z\",\"shipping_price\":10000}"
        )

        // When
        val courierItemData = shippingCourierConverter.convertToCourierItemData(
            null,
            shippingRecommendationData,
            shipmentCartItemModel
        )

        // Then
        assertEquals(false, courierItemData.scheduleDeliveryUiModel!!.isSelected)
    }

    @Test
    fun `WHEN SAF schedule delivery data is no more available and recommend true THEN scheduleDeliveryUiModel should return isSelected true`() {
        // Given
        val ratesResponse = DataProvider.provideRatesV3EmptyServicesResponse()
        val ratesScheduleDeliveryResponse = DataProvider.provideScheduleDeliveryRatesResponse()
        val mockRatesScheduleDeliveryResponse =
            ScheduleDeliveryResponseHelper.getMockScheduleDeliveryRatesResponse(
                ratesScheduleDeliveryResponse,
                recommend = true
            )
        val shippingRecommendationData =
            shippingDurationConverter.convertModel(ratesResponse.ratesData)
        shippingRecommendationData.scheduleDeliveryData =
            mockRatesScheduleDeliveryResponse.ongkirGetScheduledDeliveryRates.scheduleDeliveryData
        val shipmentCartItemModel = ShipmentCartItemModel(
            scheduleDate = "2022-10-20T00:00:00Z",
            timeslotId = 2022092014124,
            validationMetadata = "{\"timeslot_id\":2022092014124,\"schedule_date\":\"2022-10-20T00:00:00Z\",\"shipping_price\":10000}"
        )

        // When
        val courierItemData = shippingCourierConverter.convertToCourierItemData(
            null,
            shippingRecommendationData,
            shipmentCartItemModel
        )

        // Then
        assertEquals(true, courierItemData.scheduleDeliveryUiModel!!.isSelected)
    }

    @Test
    fun `WHEN SAF schedule delivery data is still available THEN scheduleDeliveryUiModel should return selected schedule`() {
        // Given
        val ratesResponse = DataProvider.provideRatesV3EmptyServicesResponse()
        val ratesScheduleDeliveryResponse = DataProvider.provideScheduleDeliveryRatesResponse()
        val shippingRecommendationData =
            shippingDurationConverter.convertModel(ratesResponse.ratesData)
        shippingRecommendationData.scheduleDeliveryData =
            ratesScheduleDeliveryResponse.ongkirGetScheduledDeliveryRates.scheduleDeliveryData
        val shipmentCartItemModel = ShipmentCartItemModel(
            scheduleDate = "2022-09-20T00:00:00Z",
            timeslotId = 2022092014123,
            validationMetadata = "{\"timeslot_id\":2022092014123,\"schedule_date\":\"2022-09-20T00:00:00Z\",\"shipping_price\":10000}"
        )

        // When
        val courierItemData = shippingCourierConverter.convertToCourierItemData(
            null,
            shippingRecommendationData,
            shipmentCartItemModel
        )

        // Then
        assertNotNull(courierItemData.scheduleDeliveryUiModel)
        assertNotNull(courierItemData.scheduleDeliveryUiModel!!.deliveryProduct)
        assertEquals(
            shipmentCartItemModel.scheduleDate,
            courierItemData.scheduleDeliveryUiModel!!.scheduleDate
        )
        assertEquals(
            shipmentCartItemModel.timeslotId,
            courierItemData.scheduleDeliveryUiModel!!.timeslotId
        )
        assertEquals(
            shipmentCartItemModel.validationMetadata,
            courierItemData.scheduleDeliveryUiModel!!.deliveryProduct.validationMetadata
        )
    }

    @Test
    fun `WHEN SAF schedule delivery data is no more available THEN scheduleDeliveryUiModel should return recommend schedule`() {
        // Given
        val ratesResponse = DataProvider.provideRatesV3EmptyServicesResponse()
        val ratesScheduleDeliveryResponse = DataProvider.provideScheduleDeliveryRatesResponse()
        val shippingRecommendationData =
            shippingDurationConverter.convertModel(ratesResponse.ratesData)
        shippingRecommendationData.scheduleDeliveryData =
            ratesScheduleDeliveryResponse.ongkirGetScheduledDeliveryRates.scheduleDeliveryData
        val shipmentCartItemModel = ShipmentCartItemModel(
            scheduleDate = "2022-08-20T00:00:00Z",
            timeslotId = 2022092014122,
            validationMetadata = "{\"timeslot_id\":2022092014122,\"schedule_date\":\"2022-08-20T00:00:00Z\",\"shipping_price\":10000}"
        )

        // When
        val courierItemData = shippingCourierConverter.convertToCourierItemData(
            null,
            shippingRecommendationData,
            shipmentCartItemModel
        )

        // Then
        assertNotNull(courierItemData.scheduleDeliveryUiModel)
        assertNotNull(courierItemData.scheduleDeliveryUiModel!!.deliveryProduct)
        assertEquals("2022-09-20T00:00:00Z", courierItemData.scheduleDeliveryUiModel!!.scheduleDate)
        assertEquals(
            2022092014123,
            courierItemData.scheduleDeliveryUiModel!!.timeslotId
        )
        assertEquals(
            "{\"timeslot_id\":2022092014123,\"schedule_date\":\"2022-09-20T00:00:00Z\",\"shipping_price\":10000}",
            courierItemData.scheduleDeliveryUiModel!!.deliveryProduct.validationMetadata
        )
    }
}
