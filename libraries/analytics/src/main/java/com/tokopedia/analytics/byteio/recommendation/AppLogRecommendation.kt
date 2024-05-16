package com.tokopedia.analytics.byteio.recommendation

import com.tokopedia.analytics.byteio.AppLogAnalytics
import com.tokopedia.analytics.byteio.AppLogAnalytics.addEnterFrom
import com.tokopedia.analytics.byteio.AppLogAnalytics.addEnterFromInfo
import com.tokopedia.analytics.byteio.AppLogAnalytics.addEnterMethod
import com.tokopedia.analytics.byteio.AppLogAnalytics.addEntranceForm
import com.tokopedia.analytics.byteio.AppLogAnalytics.addPage
import com.tokopedia.analytics.byteio.AppLogAnalytics.addRequestId
import com.tokopedia.analytics.byteio.AppLogAnalytics.addSourcePageType
import com.tokopedia.analytics.byteio.AppLogAnalytics.addTrackId
import com.tokopedia.analytics.byteio.AppLogAnalytics.intValue
import com.tokopedia.analytics.byteio.AppLogParam
import com.tokopedia.analytics.byteio.AppLogParam.ENTER_FROM
import com.tokopedia.analytics.byteio.AppLogParam.ENTER_METHOD
import com.tokopedia.analytics.byteio.AppLogParam.SOURCE_MODULE
import com.tokopedia.analytics.byteio.EntranceForm
import com.tokopedia.analytics.byteio.EventName
import com.tokopedia.analytics.byteio.SourcePageType
import com.tokopedia.analytics.byteio.TrackConfirmCart
import com.tokopedia.analytics.byteio.TrackConfirmCartResult
import com.tokopedia.kotlin.extensions.view.orZero
import org.json.JSONObject

/**
 * Byte.io recommendation tracking
 * https://bytedance.sg.larkoffice.com/docx/MSiydFty1o0xIYxUe4LltuRHgue
 */
object AppLogRecommendation {

    fun sendProductShowAppLog(model: AppLogRecommendationProductModel) {
        AppLogAnalytics.send(EventName.PRODUCT_SHOW, model.toShowClickJson())
        if (model.shouldSendCardEvent()) {
            AppLogAnalytics.send(EventName.CARD_SHOW, model.asCardModel().toShowClickJson())
        }
    }

    fun sendProductClickAppLog(model: AppLogRecommendationProductModel) {
        AppLogAnalytics.send(EventName.PRODUCT_CLICK, model.toShowClickJson())
        if (model.shouldSendCardEvent()) {
            AppLogAnalytics.send(EventName.CARD_CLICK, model.asCardModel().toShowClickJson())
        }
        if (shouldSendRecTrigger(model.entranceForm, model.isEligibleForRecTrigger)) {
            AppLogAnalytics.send(EventName.REC_TRIGGER, model.toRecTriggerJson())
        }
        model.setGlobalParams()
    }

    fun sendCardShowAppLog(model: AppLogRecommendationCardModel) {
        AppLogAnalytics.send(EventName.CARD_SHOW, model.toShowClickJson())
    }

    fun sendCardClickAppLog(model: AppLogRecommendationCardModel) {
        AppLogAnalytics.send(EventName.CARD_CLICK, model.toShowClickJson())
        if (shouldSendRecTrigger(model.entranceForm)) {
            AppLogAnalytics.send(EventName.REC_TRIGGER, model.toRecTriggerJson())
        }
        model.setGlobalParams()
    }

    fun sendEnterPageAppLog() {
        AppLogAnalytics.send(
            EventName.ENTER_PAGE,
            JSONObject().apply {
                put(ENTER_FROM, AppLogAnalytics.getLastData(ENTER_FROM))
                put(ENTER_METHOD, AppLogAnalytics.getLastData(ENTER_METHOD))
                addPage()
            }
        )
    }

    fun sendConfirmCartAppLog(model: AppLogRecommendationProductModel, product: TrackConfirmCart) {
        AppLogAnalytics.send(EventName.CONFIRM_CART, JSONObject().apply {
            addPage()
            addEnterFrom()
            put(
                AppLogParam.ENTRANCE_INFO,
                generateEntranceInfoJson(model.sourceModule).toString()
            )

            put(AppLogParam.SOURCE_PAGE_TYPE, SourcePageType.PRODUCT_CARD)
            put(SOURCE_MODULE, model.sourceModule)
            put(AppLogParam.TRACK_ID, model.trackId)
            put(AppLogParam.REQUEST_ID, model.requestId)
            put(AppLogParam.ENTRANCE_FORM, model.entranceForm)

            put(AppLogParam.PRODUCT_ID, product.productId)
            put("product_category", product.productCategory)
            put("product_type", product.productType.type)
            put("original_price_value", product.originalPrice)
            put("sale_price_value", product.salePrice)
            put("button_type", "able_to_cart")
            put("sku_id", product.skuId)
            put("currency", "IDR")
            put("add_sku_num", product.addSkuNum)
            put("buy_type", 1) // ATC will use code 1, otherwise Buy Now use 0
        })
    }

    fun sendConfirmCartResultAppLog(
        model: AppLogRecommendationProductModel,
        product: TrackConfirmCartResult
    ) {
        AppLogAnalytics.send(EventName.CONFIRM_CART_RESULT, JSONObject().apply {
            addPage()
            addEnterFrom()
            addEnterFromInfo()
            put(
                AppLogParam.ENTRANCE_INFO,
                generateEntranceInfoJson(model.sourceModule).toString()
            )

            put(AppLogParam.SOURCE_PAGE_TYPE, SourcePageType.PRODUCT_CARD)
            put(SOURCE_MODULE, model.sourceModule)
            put(AppLogParam.TRACK_ID, model.trackId)
            put(AppLogParam.REQUEST_ID, model.requestId)
            put(AppLogParam.ENTRANCE_FORM, model.entranceForm)

            put(AppLogParam.PRODUCT_ID, product.productId)
            put("product_category", product.productCategory)
            put("product_type", product.productType.type)
            put("original_price_value", product.originalPrice)
            put("sale_price_value", product.salePrice)
            put("button_type", "able_to_cart")
            put("sku_id", product.skuId)
            put("currency", "IDR")
            put("add_sku_num", product.addSkuNum)
            put("buy_type", 1) // ATC will use code 1, otherwise Buy Now use 0
            put("cart_item_id", product.cartItemId)
            put("is_success", product.isSuccess?.intValue.orZero())
            put("fail_reason", product.failReason)
        })
    }

    private fun generateEntranceInfoJson(sourceModule: String): JSONObject {
        return JSONObject().also {
            it.put(SOURCE_MODULE, sourceModule)
            it.addEntranceForm()
            it.put(AppLogParam.IS_AD, AppLogAnalytics.getLastData(AppLogParam.IS_AD))
            it.addSourcePageType()
            it.addRequestId()
            it.addTrackId()
            it.addEnterMethod()
            it.addEnterFromInfo()
        }
    }

    private fun AppLogRecommendationProductModel.setGlobalParams() {
        AppLogAnalytics.setGlobalParams(
            entranceForm = entranceForm,
            enterMethod = enterMethod ?: AppLogAnalytics.getDataBeforeStep(ENTER_METHOD)?.toString(),
            sourceModule = sourceModule,
            isAd = isAd,
            trackId = trackId,
            sourcePageType = SourcePageType.PRODUCT_CARD,
            requestId = requestId
        )
        additionalParam.setAdditionalToGlobalParam()
    }

    private fun AppLogRecommendationCardModel.setGlobalParams() {
        AppLogAnalytics.setGlobalParams(
            entranceForm = entranceForm,
            enterMethod = enterMethod ?: AppLogAnalytics.getDataBeforeStep(ENTER_METHOD)?.toString(),
            sourceModule = sourceModule,
            isAd = isAd,
            trackId = trackId,
            sourcePageType = sourcePageType,
            requestId = requestId,
        )
    }

    private fun shouldSendRecTrigger(
        entranceForm: String,
        eligibleForRecTrigger: Boolean = false
    ): Boolean {
        return entranceForm == EntranceForm.PURE_GOODS_CARD.str ||
            entranceForm == EntranceForm.CONTENT_GOODS_CARD.str ||
            (entranceForm == EntranceForm.DETAIL_GOODS_CARD.str &&
                eligibleForRecTrigger)
    }

    private fun AppLogRecommendationProductModel.shouldSendCardEvent(): Boolean {
        return entranceForm == EntranceForm.MISSION_HORIZONTAL_GOODS_CARD.str ||
            entranceForm == EntranceForm.PURE_GOODS_CARD.str
    }
}
