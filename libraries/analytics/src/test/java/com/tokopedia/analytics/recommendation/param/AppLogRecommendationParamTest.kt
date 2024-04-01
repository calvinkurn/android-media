package com.tokopedia.analytics.recommendation.param

import com.tokopedia.analytics.byteio.AppLogAnalytics
import com.tokopedia.analytics.byteio.AppLogParam
import com.tokopedia.analytics.byteio.EntranceForm
import com.tokopedia.analytics.byteio.SourcePageType
import com.tokopedia.analytics.byteio.recommendation.AppLogRecommendationCardModel
import com.tokopedia.analytics.byteio.recommendation.AppLogRecommendationProductModel
import com.tokopedia.analytics.byteio.recommendation.CardName
import com.tokopedia.analytics.recommendation.assertCompareRecommendationParam
import com.tokopedia.analytics.recommendation.assertRecommendationParam
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.spyk
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Before
import org.junit.Test

class AppLogRecommendationParamTest {

    @Before
    fun setup() {
        mockkObject(AppLogAnalytics)
        every { AppLogAnalytics.getCurrentData(AppLogParam.PAGE_NAME) } returns ACTUAL_PAGE_NAME
        MockKAnnotations.init(this)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    companion object {
        private const val ACTUAL_PAGE_NAME = "homepage"
        private const val RECOM_PAGE_NAME = "recomPageName"
        private const val REQUEST_ID = "request123"
        private const val PRODUCT_ID = "product123"
        private const val PARENT_PRODUCT_ID = "parent123"
        private const val CARD_ID = "card123"
        private const val VIDEO_ID = "video123"
        private const val INDEX = 0
        private const val REC_SESSION_ID = "recSessionId123"
        private const val REC_PARAMS = "recParams123"
        private const val SHOP_ID = "shopId123"
    }

    @Test
    fun `given trigger product event then construct product json object`() {
        val entranceForm = EntranceForm.PURE_GOODS_CARD
        val model = spyk(
            AppLogRecommendationProductModel.create(
                productId = PRODUCT_ID,
                position = INDEX,
                tabName = "for you",
                tabPosition = INDEX,
                moduleName = RECOM_PAGE_NAME,
                isAd = false,
                isUseCache = false,
                recSessionId = REC_SESSION_ID,
                recParams = REC_PARAMS,
                requestId = REQUEST_ID,
                shopId = SHOP_ID,
                entranceForm = entranceForm,
            )
        )

        val json = model.toShowClickJson()
        json.assertRecommendationParam(AppLogParam.LIST_NAME, "for_you")
        json.assertRecommendationParam(AppLogParam.LIST_NUM, INDEX + 1)
        json.assertRecommendationParam(AppLogParam.SOURCE_PAGE_TYPE, ACTUAL_PAGE_NAME)
        json.assertRecommendationParam(AppLogParam.ENTRANCE_FORM, EntranceForm.PURE_GOODS_CARD.str)
        json.assertRecommendationParam(AppLogParam.SOURCE_MODULE, "rec_${ACTUAL_PAGE_NAME}_outer_flow")
        json.assertRecommendationParam(AppLogParam.PRODUCT_ID, PRODUCT_ID)
        json.assertRecommendationParam(AppLogParam.IS_AD, 0)
        json.assertRecommendationParam(AppLogParam.IS_USE_CACHE, 0)
        json.assertRecommendationParam(AppLogParam.TRACK_ID, "${REQUEST_ID}_${PRODUCT_ID}_${INDEX + 1}")
        json.assertRecommendationParam(AppLogParam.REC_PARAMS, REC_PARAMS)
        json.assertRecommendationParam(AppLogParam.REQUEST_ID, REQUEST_ID)
        json.assertRecommendationParam(AppLogParam.SHOP_ID, SHOP_ID)
        json.assertRecommendationParam(AppLogParam.ITEM_ORDER, INDEX + 1)
        json.assertRecommendationParam(AppLogParam.REC_SESSION_ID, REC_SESSION_ID)
    }

    @Test
    fun `given trigger product event that eligible to send card event then construct card json object`() {
        val entranceForm = EntranceForm.PURE_GOODS_CARD
        val model = spyk(
            AppLogRecommendationProductModel.create(
                productId = PRODUCT_ID,
                position = INDEX,
                tabName = "for you",
                tabPosition = INDEX,
                moduleName = RECOM_PAGE_NAME,
                isAd = false,
                isUseCache = false,
                recSessionId = REC_SESSION_ID,
                recParams = REC_PARAMS,
                requestId = REQUEST_ID,
                shopId = SHOP_ID,
                entranceForm = entranceForm,
                cardName = CardName.REC_GOODS_CARD
            )
        )

        val productJson = model.toShowClickJson()
        val cardJson = model.asCardModel().toShowClickJson()

        cardJson.assertRecommendationParam(AppLogParam.CARD_NAME, CardName.REC_GOODS_CARD)
        cardJson.assertCompareRecommendationParam(productJson, AppLogParam.LIST_NAME)
        cardJson.assertCompareRecommendationParam(productJson, AppLogParam.LIST_NUM)
        cardJson.assertCompareRecommendationParam(productJson, AppLogParam.SOURCE_PAGE_TYPE)
        cardJson.assertCompareRecommendationParam(productJson, AppLogParam.SOURCE_MODULE)
        cardJson.assertCompareRecommendationParam(productJson, AppLogParam.PRODUCT_ID)
        cardJson.assertCompareRecommendationParam(productJson, AppLogParam.IS_AD)
        cardJson.assertCompareRecommendationParam(productJson, AppLogParam.IS_USE_CACHE)
        cardJson.assertCompareRecommendationParam(productJson, AppLogParam.TRACK_ID)
        cardJson.assertCompareRecommendationParam(productJson, AppLogParam.REC_PARAMS)
        cardJson.assertCompareRecommendationParam(productJson, AppLogParam.REQUEST_ID)
        cardJson.assertCompareRecommendationParam(productJson, AppLogParam.SHOP_ID)
        cardJson.assertCompareRecommendationParam(productJson, AppLogParam.ITEM_ORDER)
        cardJson.assertCompareRecommendationParam(productJson, AppLogParam.REC_SESSION_ID)
    }

    @Test
    fun `given trigger card event then construct card json object`() {
        val entranceForm = EntranceForm.PURE_GOODS_CARD
        val model = spyk(
            AppLogRecommendationCardModel.create(
                cardId = CARD_ID,
                productId = "0",
                position = INDEX,
                tabName = "for you",
                tabPosition = INDEX,
                moduleName = RECOM_PAGE_NAME,
                isAd = false,
                isUseCache = false,
                recSessionId = REC_SESSION_ID,
                recParams = REC_PARAMS,
                requestId = REQUEST_ID,
                shopId = SHOP_ID,
                entranceForm = entranceForm,
                cardName = CardName.REC_CONTENT_CARD.format("best_seller")
            )
        )

        val json = model.toShowClickJson()
        json.assertRecommendationParam(AppLogParam.CARD_NAME, CardName.REC_CONTENT_CARD.format("best_seller"))
        json.assertRecommendationParam(AppLogParam.LIST_NAME, "for_you")
        json.assertRecommendationParam(AppLogParam.LIST_NUM, INDEX + 1)
        json.assertRecommendationParam(AppLogParam.SOURCE_PAGE_TYPE, ACTUAL_PAGE_NAME)
        json.assertRecommendationParam(AppLogParam.SOURCE_MODULE, "rec_${ACTUAL_PAGE_NAME}_outer_flow")
        json.assertRecommendationParam(AppLogParam.PRODUCT_ID, "")
        json.assertRecommendationParam(AppLogParam.IS_AD, 0)
        json.assertRecommendationParam(AppLogParam.IS_USE_CACHE, 0)
        json.assertRecommendationParam(AppLogParam.TRACK_ID, "${REQUEST_ID}_${CARD_ID}_${INDEX + 1}")
        json.assertRecommendationParam(AppLogParam.REC_PARAMS, REC_PARAMS)
        json.assertRecommendationParam(AppLogParam.REQUEST_ID, REQUEST_ID)
        json.assertRecommendationParam(AppLogParam.SHOP_ID, SHOP_ID)
        json.assertRecommendationParam(AppLogParam.ITEM_ORDER, INDEX + 1)
        json.assertRecommendationParam(AppLogParam.REC_SESSION_ID, REC_SESSION_ID)
    }

    @Test
    fun `given organic vertical recommendation then source_module param should use rec_{pagename}_outer_flow format`() {
        val model = spyk(
            AppLogRecommendationProductModel.create(
                entranceForm = EntranceForm.PURE_GOODS_CARD,
                isAd = false,
                moduleName = RECOM_PAGE_NAME,
            )
        )

        val json = model.toShowClickJson()
        json.assertRecommendationParam(AppLogParam.SOURCE_MODULE, "rec_${ACTUAL_PAGE_NAME}_outer_flow")
    }

    @Test
    fun `given organic horizontal recommendation then source_module param should use rec_{pagename}_outer_{recomPagename}_module format`() {
        val model = spyk(
            AppLogRecommendationProductModel.create(
                entranceForm = EntranceForm.HORIZONTAL_GOODS_CARD,
                isAd = false,
                moduleName = RECOM_PAGE_NAME,
            )
        )

        val json = model.toShowClickJson()
        json.assertRecommendationParam(AppLogParam.SOURCE_MODULE, "rec_${ACTUAL_PAGE_NAME}_outer_${RECOM_PAGE_NAME}_module")
    }

    @Test
    fun `given ads vertical recommendation then source_module param should use rec_{pagename}_outer_flow format`() {
        val model = spyk(
            AppLogRecommendationProductModel.create(
                entranceForm = EntranceForm.PURE_GOODS_CARD,
                isAd = true,
                moduleName = RECOM_PAGE_NAME,
            )
        )

        val json = model.toShowClickJson()
        json.assertRecommendationParam(AppLogParam.SOURCE_MODULE, "ads_${ACTUAL_PAGE_NAME}_outer_flow")
    }

    @Test
    fun `given ads horizontal recommendation then source_module param should use rec_{pagename}_outer_{recomPagename}_module format`() {
        val model = spyk(
            AppLogRecommendationProductModel.create(
                entranceForm = EntranceForm.HORIZONTAL_GOODS_CARD,
                isAd = true,
                moduleName = RECOM_PAGE_NAME,
            )
        )

        val json = model.toShowClickJson()
        json.assertRecommendationParam(AppLogParam.SOURCE_MODULE, "ads_${ACTUAL_PAGE_NAME}_outer_${RECOM_PAGE_NAME}_module")
    }

    @Test
    fun `given single item recommendation then source_module param should use rec_{pagename}_outer_{recomPagename}_module format`() {
        val model = spyk(
            AppLogRecommendationProductModel.create(
                entranceForm = EntranceForm.DETAIL_GOODS_CARD,
                isAd = false,
                moduleName = RECOM_PAGE_NAME,
                isTrackAsHorizontalSourceModule = true,
            )
        )

        val json = model.toShowClickJson()
        json.assertRecommendationParam(AppLogParam.SOURCE_MODULE, "rec_${ACTUAL_PAGE_NAME}_outer_${RECOM_PAGE_NAME}_module")
    }

    @Test
    fun `given product card clicked then track_id param should use {requestId}_{productId}_{position} format`() {
        val model = spyk(
            AppLogRecommendationProductModel.create(
                entranceForm = EntranceForm.PURE_GOODS_CARD,
                cardName = CardName.REC_GOODS_CARD,
                requestId = REQUEST_ID,
                productId = PRODUCT_ID,
                position = INDEX
            )
        )

        val json = model.toShowClickJson()
        json.assertRecommendationParam(AppLogParam.TRACK_ID, "${REQUEST_ID}_${PRODUCT_ID}_${INDEX + 1}")
    }

    @Test
    fun `given video card clicked then track_id param should use {requestId}_{videoId}_{productId}_{position} format`() {
        val model = spyk(
            AppLogRecommendationCardModel.create(
                entranceForm = EntranceForm.PURE_GOODS_CARD,
                cardName = CardName.REC_VIDEO_CARD,
                requestId = REQUEST_ID,
                productId = PRODUCT_ID,
                cardId = VIDEO_ID,
                position = INDEX
            )
        )

        val json = model.toShowClickJson()
        json.assertRecommendationParam(AppLogParam.TRACK_ID, "${REQUEST_ID}_${VIDEO_ID}_${PRODUCT_ID}_${INDEX + 1}")
    }

    @Test
    fun `given content card clicked then track_id param should use {requestId}_{cardId}_{position} format`() {
        val model = spyk(
            AppLogRecommendationCardModel.create(
                entranceForm = EntranceForm.PURE_GOODS_CARD,
                cardName = CardName.REC_CONTENT_CARD.format("best_seller"),
                requestId = REQUEST_ID,
                cardId = CARD_ID,
                position = INDEX
            )
        )

        val json = model.toShowClickJson()
        json.assertRecommendationParam(AppLogParam.TRACK_ID, "${REQUEST_ID}_${CARD_ID}_${INDEX + 1}")
    }

    @Test
    fun `given banner card clicked then track_id param should use {requestId}_{cardId}_{position} format`() {
        val model = spyk(
            AppLogRecommendationCardModel.create(
                entranceForm = EntranceForm.PURE_GOODS_CARD,
                cardName = CardName.AD_FEED_CARD,
                requestId = REQUEST_ID,
                cardId = CARD_ID,
                position = INDEX
            )
        )

        val json = model.toShowClickJson()
        json.assertRecommendationParam(AppLogParam.TRACK_ID, "${REQUEST_ID}_${CARD_ID}_${INDEX + 1}")
    }

    @Test
    fun `given product mission card clicked then track_id param should use {requestId}_{productId}_{position} format`() {
        val model = spyk(
            AppLogRecommendationProductModel.create(
                entranceForm = EntranceForm.MISSION_HORIZONTAL_GOODS_CARD,
                cardName = CardName.MISSION_PRODUCT_CARD.format("title"),
                requestId = REQUEST_ID,
                productId = PRODUCT_ID,
                position = INDEX
            )
        )

        val json = model.toShowClickJson()
        json.assertRecommendationParam(AppLogParam.TRACK_ID, "${REQUEST_ID}_${PRODUCT_ID}_${INDEX + 1}")
    }

    @Test
    fun `given non product mission card clicked then track_id param should use {requestId}_{cardId}_{position} format`() {
        val model = spyk(
            AppLogRecommendationCardModel.create(
                entranceForm = EntranceForm.MISSION_HORIZONTAL_GOODS_CARD,
                cardName = CardName.MISSION_PAGE_CARD.format("title"),
                requestId = REQUEST_ID,
                cardId = CARD_ID,
                position = INDEX
            )
        )

        val json = model.toShowClickJson()
        json.assertRecommendationParam(AppLogParam.TRACK_ID, "${REQUEST_ID}_${CARD_ID}_${INDEX + 1}")
    }

    @Test
    fun `given zero values on model then param should be empty string`() {
        val model = spyk(
            AppLogRecommendationProductModel.create(
                entranceForm = EntranceForm.PURE_GOODS_CARD,
                cardName = CardName.REC_GOODS_CARD,
                productId = "0",
                tabPosition = -1,
            )
        )

        val json = model.toShowClickJson()
        json.assertRecommendationParam(AppLogParam.PRODUCT_ID, "")
        json.assertRecommendationParam(AppLogParam.LIST_NUM, "")
    }

    @Test
    fun `given multiple words values on list_name then convert whitespaces to underscore`() {
        val actualTabName = "for you"
        val expectedTabName = "for_you"

        val model = spyk(
            AppLogRecommendationProductModel.create(
                entranceForm = EntranceForm.PURE_GOODS_CARD,
                cardName = CardName.REC_GOODS_CARD,
                tabName = actualTabName
            )
        )

        val json = model.toShowClickJson()
        json.assertRecommendationParam(AppLogParam.LIST_NAME, expectedTabName)
    }

    @Test
    fun `given whitespaces on card_name then remove whitespaces`() {
        val actualCardName = CardName.MISSION_PAGE_CARD.format("title title")
        val expectedCardName = CardName.MISSION_PAGE_CARD.format("titletitle")

        val model = spyk(
            AppLogRecommendationCardModel.create(
                entranceForm = EntranceForm.MISSION_HORIZONTAL_GOODS_CARD,
                cardName = actualCardName,
                requestId = REQUEST_ID,
                cardId = CARD_ID,
                position = INDEX
            )
        )

        val json = model.toShowClickJson()
        json.assertRecommendationParam(AppLogParam.CARD_NAME, expectedCardName)
    }

    @Test
    fun `given isAd is true when rec_goods_card then card_name param should be ad_goods_card`() {
        val model = spyk(
            AppLogRecommendationProductModel.create(
                entranceForm = EntranceForm.PURE_GOODS_CARD,
                cardName = CardName.REC_GOODS_CARD,
                isAd = true
            )
        )

        val json = model.asCardModel().toShowClickJson()
        json.assertRecommendationParam(AppLogParam.CARD_NAME, CardName.AD_GOODS_CARD)
    }

    @Test
    fun `given parentProductID is not zero then productId param should use parentProductID`() {
        val model = spyk(
            AppLogRecommendationProductModel.create(
                entranceForm = EntranceForm.PURE_GOODS_CARD,
                cardName = CardName.REC_GOODS_CARD,
                requestId = REQUEST_ID,
                productId = PRODUCT_ID,
                parentProductId = PARENT_PRODUCT_ID,
                position = INDEX
            )
        )

        val json = model.toShowClickJson()
        json.assertRecommendationParam(AppLogParam.TRACK_ID, "${REQUEST_ID}_${PARENT_PRODUCT_ID}_${INDEX + 1}")
        json.assertRecommendationParam(AppLogParam.PRODUCT_ID, PARENT_PRODUCT_ID)
    }

    @Test
    fun `given parentProductID is zero then productId param should use productID`() {
        val model = spyk(
            AppLogRecommendationProductModel.create(
                entranceForm = EntranceForm.PURE_GOODS_CARD,
                cardName = CardName.REC_GOODS_CARD,
                requestId = REQUEST_ID,
                productId = PRODUCT_ID,
                parentProductId = "0",
                position = INDEX
            )
        )

        val json = model.toShowClickJson()
        json.assertRecommendationParam(AppLogParam.TRACK_ID, "${REQUEST_ID}_${PRODUCT_ID}_${INDEX + 1}")
        json.assertRecommendationParam(AppLogParam.PRODUCT_ID, PRODUCT_ID)
    }

    @Test
    fun `given product show or click then source_page_type param should use actual page name`() {
        val model = spyk(
            AppLogRecommendationProductModel.create(
                entranceForm = EntranceForm.PURE_GOODS_CARD,
            )
        )

        val json = model.toShowClickJson()
        json.assertRecommendationParam(AppLogParam.SOURCE_PAGE_TYPE, ACTUAL_PAGE_NAME)
    }

    @Test
    fun `given video show or click then source_page_type param is 'video'`() {
        val model = spyk(
            AppLogRecommendationCardModel.create(
                entranceForm = EntranceForm.PURE_GOODS_CARD,
                sourcePageType = SourcePageType.VIDEO
            )
        )

        val json = model.toShowClickJson()
        json.assertRecommendationParam(AppLogParam.SOURCE_PAGE_TYPE, SourcePageType.VIDEO)
    }
}
