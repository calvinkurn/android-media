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

class AppLogRecommendationShowTest {

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
    fun `given product shown then send tiktokec_product_show event`() {
        val model = mockk<AppLogRecommendationProductModel>(relaxed = true)

        AppLogRecommendation.sendProductShowAppLog(model)

        verifyRecommendationEvent(EventName.PRODUCT_SHOW)
    }

    @Test
    fun `given card shown then send tiktokec_card_show event`() {
        val model = mockk<AppLogRecommendationCardModel>(relaxed = true)

        AppLogRecommendation.sendCardShowAppLog(model)

        verifyRecommendationEvent(EventName.CARD_SHOW)
    }

    @Test
    fun `given product shown when entrance form is pure_goods_card then send tiktokec_product_show and tiktokec_card_show events`() {
        val model = spyk(
            AppLogRecommendationProductModel.create(
                entranceForm = EntranceForm.PURE_GOODS_CARD
            )
        )

        AppLogRecommendation.sendProductShowAppLog(model)

        val expectedEvents = hashSetOf(
            EventName.PRODUCT_SHOW,
            EventName.CARD_SHOW,
        )

        verifyRecommendationEvent(expectedEvents)
    }

    @Test
    fun `given product shown when entrance form is mission_horizontal_goods_card then send tiktokec_product_show and tiktokec_card_show events`() {
        val model = spyk(
            AppLogRecommendationProductModel.create(
                entranceForm = EntranceForm.MISSION_HORIZONTAL_GOODS_CARD
            )
        )

        AppLogRecommendation.sendProductShowAppLog(model)

        val expectedEvents = hashSetOf(
            EventName.PRODUCT_SHOW,
            EventName.CARD_SHOW,
        )

        verifyRecommendationEvent(expectedEvents)
    }

    @Test
    fun `given product shown when entrance form is detail_goods_card then send tiktokec_product_show events`() {
        val model = spyk(
            AppLogRecommendationProductModel.create(
                entranceForm = EntranceForm.DETAIL_GOODS_CARD,
            )
        )

        AppLogRecommendation.sendProductShowAppLog(model)

        verifyRecommendationEvent(EventName.PRODUCT_SHOW)
    }

    @Test
    fun `given product shown when entrance form is horizontal_goods_card then only send tiktokec_product_show`() {
        val model = spyk(
            AppLogRecommendationProductModel.create(
                entranceForm = EntranceForm.HORIZONTAL_GOODS_CARD,
            )
        )

        AppLogRecommendation.sendProductShowAppLog(model)

        verifyRecommendationEvent(EventName.PRODUCT_SHOW)
    }

    @Test
    fun `given card shown when entrance form is content_goods_card then send tiktokec_card_show event`() {
        val model = spyk(
            AppLogRecommendationCardModel.create(
                entranceForm = EntranceForm.CONTENT_GOODS_CARD,
            )
        )

        AppLogRecommendation.sendCardShowAppLog(model)

        verifyRecommendationEvent(EventName.CARD_SHOW)
    }
}
