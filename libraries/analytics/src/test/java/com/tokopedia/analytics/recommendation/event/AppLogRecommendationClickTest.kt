package com.tokopedia.analytics.recommendation.event

import com.tokopedia.analytics.byteio.AppLogAnalytics
import com.tokopedia.analytics.byteio.EntranceForm
import com.tokopedia.analytics.byteio.EventName
import com.tokopedia.analytics.byteio.recommendation.AppLogRecommendation
import com.tokopedia.analytics.byteio.recommendation.AppLogRecommendationCardModel
import com.tokopedia.analytics.byteio.recommendation.AppLogRecommendationProductModel
import com.tokopedia.analytics.recommendation.verifyRecommendationEvent
import com.tokopedia.analytics.recommendation.verifyRecommendationEventNot
import io.mockk.MockKAnnotations
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.spyk
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Before
import org.junit.Test

class AppLogRecommendationClickTest {

    @Before
    fun setup() {
        mockkObject(AppLogAnalytics)
        MockKAnnotations.init(this)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `given product clicked then send tiktokec_product_click event`() {
        val model = mockk<AppLogRecommendationProductModel>(relaxed = true)

        AppLogRecommendation.sendProductClickAppLog(model)

        verifyRecommendationEvent(EventName.PRODUCT_CLICK)
    }

    @Test
    fun `given card clicked then send tiktokec_card_click event`() {
        val model = mockk<AppLogRecommendationCardModel>(relaxed = true)

        AppLogRecommendation.sendCardClickAppLog(model)

        verifyRecommendationEvent(EventName.CARD_CLICK)
    }

    @Test
    fun `given product clicked when entrance form is pure_goods_card then send tiktokec_product_click, tiktokec_card_click, tiktokec_rec_trigger events`() {
        val model = spyk(
            AppLogRecommendationProductModel.create(
                entranceForm = EntranceForm.PURE_GOODS_CARD
            )
        )

        AppLogRecommendation.sendProductClickAppLog(model)

        val expectedEvents = hashSetOf(
            EventName.PRODUCT_CLICK,
            EventName.CARD_CLICK,
            EventName.REC_TRIGGER,
        )

        verifyRecommendationEvent(expectedEvents)
    }

    @Test
    fun `given product clicked when entrance form is mission_horizontal_goods_card then send tiktokec_product_click and tiktokec_card_click events`() {
        val model = spyk(
            AppLogRecommendationProductModel.create(
                entranceForm = EntranceForm.MISSION_HORIZONTAL_GOODS_CARD
            )
        )

        AppLogRecommendation.sendProductClickAppLog(model)

        val expectedEvents = hashSetOf(
            EventName.PRODUCT_CLICK,
            EventName.CARD_CLICK
        )

        verifyRecommendationEvent(expectedEvents)
    }

    @Test
    fun `given product clicked when entrance form is detail_goods_card and isEligibleForRecTrigger is true then send tiktokec_product_click, tiktokec_rec_trigger events`() {
        val model = spyk(
            AppLogRecommendationProductModel.create(
                entranceForm = EntranceForm.DETAIL_GOODS_CARD,
                isEligibleForRecTrigger = true
            )
        )

        AppLogRecommendation.sendProductClickAppLog(model)

        val expectedEvents = hashSetOf(
            EventName.PRODUCT_CLICK,
            EventName.REC_TRIGGER
        )

        verifyRecommendationEvent(expectedEvents)
    }

    @Test
    fun `given product clicked when entrance form is detail_goods_card and isEligibleForRecTrigger is false then don't send tiktokec_rec_trigger`() {
        val model = spyk(
            AppLogRecommendationProductModel.create(
                entranceForm = EntranceForm.DETAIL_GOODS_CARD,
                isEligibleForRecTrigger = false
            )
        )

        AppLogRecommendation.sendProductClickAppLog(model)

        verifyRecommendationEvent(EventName.PRODUCT_CLICK)
        verifyRecommendationEventNot(EventName.REC_TRIGGER)
    }

    @Test
    fun `given product clicked when entrance form is horizontal_goods_card then only send tiktokec_product_click`() {
        val model = spyk(
            AppLogRecommendationProductModel.create(
                entranceForm = EntranceForm.HORIZONTAL_GOODS_CARD,
            )
        )

        AppLogRecommendation.sendProductClickAppLog(model)

        verifyRecommendationEvent(EventName.PRODUCT_CLICK)
        verifyRecommendationEventNot(EventName.REC_TRIGGER)
    }

    @Test
    fun `given card clicked when entrance form is content_goods_card then send tiktokec_card_click and tiktokec_rec_trigger events`() {
        val model = spyk(
            AppLogRecommendationCardModel.create(
                entranceForm = EntranceForm.CONTENT_GOODS_CARD,
            )
        )

        AppLogRecommendation.sendCardClickAppLog(model)

        val expectedEvents = hashSetOf(
            EventName.CARD_CLICK,
            EventName.REC_TRIGGER
        )

        verifyRecommendationEvent(expectedEvents)
    }
}
