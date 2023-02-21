package com.tokopedia.checkout.view.subscriber

import com.tokopedia.logisticcart.scheduledelivery.domain.model.DeliveryProduct
import com.tokopedia.logisticcart.shipping.model.CourierItemData
import com.tokopedia.logisticcart.shipping.model.ScheduleDeliveryUiModel
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ScheduleDeliveryCourierRecommendationSubscriberTest {

    @MockK
    private lateinit var scheduleDeliveryCourierRecommendationSubscriber: GetScheduleDeliveryCourierRecommendationSubscriber
    private lateinit var shipmentCartItemModel: ShipmentCartItemModel

    @Before
    fun before() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `WHEN SAF chosen schedule delivery is no more available, schedule delivery data in shipmentCartItemData should reset`() {
        // Data from SAF
        shipmentCartItemModel = ShipmentCartItemModel(
            scheduleDate = "2022-09-20T00:00:00Z",
            timeslotId = 2022092014123,
            validationMetadata = "{\"timeslot_id\":2022092014123,\"schedule_date\":\"2022-09-20T00:00:00Z\",\"shipping_price\":10000}"
        )
        // Schedule from SAF is no more available, so return recommend schedule delivery data
        val courierItemData = CourierItemData(
            scheduleDeliveryUiModel = ScheduleDeliveryUiModel(
                isSelected = false,
                scheduleDate = "2022-10-20T00:00:00Z",
                timeslotId = 2022092014124
            )
        )

        every {
            scheduleDeliveryCourierRecommendationSubscriber.handleSyncShipmentCartItemModel(
                any(),
                any()
            )
        } answers { callOriginal() }

        // When
        scheduleDeliveryCourierRecommendationSubscriber.handleSyncShipmentCartItemModel(
            courierItemData,
            shipmentCartItemModel
        )

        // Then
        assertEquals("", shipmentCartItemModel.scheduleDate)
        assertEquals(0L, shipmentCartItemModel.timeslotId)
        assertEquals("", shipmentCartItemModel.validationMetadata)
    }

    @Test
    fun `WHEN SAF chosen schedule delivery no more available and recommend true, schedule delivery data in shipmentCartItemData should be replace with courierItemData`() {
        // Data from SAF
        shipmentCartItemModel = ShipmentCartItemModel(
            scheduleDate = "2022-09-20T00:00:00Z",
            timeslotId = 2022092014123,
            validationMetadata = "{\"timeslot_id\":2022092014123,\"schedule_date\":\"2022-09-20T00:00:00Z\",\"shipping_price\":10000}"
        )
        // Schedule from SAF is no more available, so return recommend schedule delivery data
        val courierItemData = CourierItemData(
            scheduleDeliveryUiModel = ScheduleDeliveryUiModel(
                isSelected = true,
                scheduleDate = "2022-10-20T00:00:00Z",
                timeslotId = 2022092014124,
                deliveryProduct = DeliveryProduct(
                    validationMetadata = "{\"timeslot_id\":2022092014124,\"schedule_date\":\"2022-10-20T00:00:00Z\",\"shipping_price\":10000}"
                )
            )
        )

        every {
            scheduleDeliveryCourierRecommendationSubscriber.handleSyncShipmentCartItemModel(
                any(),
                any()
            )
        } answers { callOriginal() }

        // When
        scheduleDeliveryCourierRecommendationSubscriber.handleSyncShipmentCartItemModel(
            courierItemData,
            shipmentCartItemModel
        )

        // Then
        assertEquals(courierItemData.scheduleDeliveryUiModel?.scheduleDate, shipmentCartItemModel.scheduleDate)
        assertEquals(courierItemData.scheduleDeliveryUiModel?.timeslotId, shipmentCartItemModel.timeslotId)
        assertEquals(courierItemData.scheduleDeliveryUiModel?.deliveryProduct?.validationMetadata, shipmentCartItemModel.validationMetadata)
    }
}
