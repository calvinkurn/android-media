package com.tokopedia.analytics.topads

import com.tokopedia.analytics.byteio.topads.AdsLogConst
import com.tokopedia.analytics.byteio.topads.AppLogTopAds
import com.tokopedia.analytics.byteio.topads.models.AdsLogRealtimeClickModel
import com.tokopedia.analytics.byteio.topads.models.AdsLogShowModel
import com.tokopedia.analytics.byteio.topads.models.AdsLogShowOverModel
import org.json.JSONObject
import org.junit.Assert.assertEquals

fun JSONObject.assertRealtimeClickSRP(realtimeClickModel: AdsLogRealtimeClickModel) {
    val adExtraDataJSONObject: JSONObject = this[AdsLogConst.Param.AD_EXTRA_DATA] as JSONObject
    val expectedResult = JSONObject().apply {
        put(AdsLogConst.Param.CHANNEL, AppLogTopAds.getChannel())
        put(AdsLogConst.Param.ENTER_FROM, AppLogTopAds.getEnterFrom())
        put(AdsLogConst.Param.MALL_CARD_TYPE, realtimeClickModel.adExtraData.mallCardType)
        put(AdsLogConst.Param.PRODUCT_ID, realtimeClickModel.adExtraData.productId)
        put(AdsLogConst.Param.PRODUCT_NAME, realtimeClickModel.adExtraData.productName)
    }

    assertEquals(expectedResult.toString(), adExtraDataJSONObject.toString())

    assertEquals(this[AdsLogConst.Param.CATEGORY], AdsLogConst.EVENT_V3)
    assertEquals(this[AdsLogConst.Param.IS_AD_EVENT], "1")
    assertEquals(this[AdsLogConst.Param.NT], 1) //1 is mobile
    assertEquals(this[AdsLogConst.Param.VALUE], realtimeClickModel.adsValue)
    assertEquals(this[AdsLogConst.Param.LOG_EXTRA], realtimeClickModel.logExtra)
    assertEquals(this[AdsLogConst.Param.GROUP_ID], "0")
    assertEquals(this[AdsLogConst.REFER], realtimeClickModel.refer)
    assert(this[AdsLogConst.Param.SYSTEM_START_TIMESTAMP] != 0)
    assertEquals(this[AdsLogConst.TAG], AppLogTopAds.getTagValue(AppLogTopAds.currentPageName))
}

fun JSONObject.assertRealtimeClickOutsideSRP(realtimeClickModel: AdsLogRealtimeClickModel) {
    val adExtraDataJSONObject: JSONObject = this[AdsLogConst.Param.AD_EXTRA_DATA] as JSONObject
    val expectedResult = JSONObject().apply {
        put(AdsLogConst.Param.MALL_CARD_TYPE, realtimeClickModel.adExtraData.mallCardType)
        put(AdsLogConst.Param.PRODUCT_ID, realtimeClickModel.adExtraData.productId)
        put(AdsLogConst.Param.PRODUCT_NAME, realtimeClickModel.adExtraData.productName)
    }

    assertEquals(expectedResult.toString(), adExtraDataJSONObject.toString())

    assertEquals(this[AdsLogConst.Param.CATEGORY], AdsLogConst.EVENT_V3)
    assertEquals(this[AdsLogConst.Param.IS_AD_EVENT], "1")
    assertEquals(this[AdsLogConst.Param.NT], 1) //1 is mobile
    assertEquals(this[AdsLogConst.Param.VALUE], realtimeClickModel.adsValue)
    assertEquals(this[AdsLogConst.Param.LOG_EXTRA], realtimeClickModel.logExtra)
    assertEquals(this[AdsLogConst.Param.GROUP_ID], "0")
    assertEquals(this[AdsLogConst.REFER], realtimeClickModel.refer)
    assert(this[AdsLogConst.Param.SYSTEM_START_TIMESTAMP] != 0)
    assertEquals(this[AdsLogConst.TAG], AppLogTopAds.getTagValue(AppLogTopAds.currentPageName))
}

fun JSONObject.assertShowSRP(adsLogShowModel: AdsLogShowModel) {
    val adExtraDataJSONObject: JSONObject = this[AdsLogConst.Param.AD_EXTRA_DATA] as JSONObject
    val expectedResult = JSONObject().apply {
        put(AdsLogConst.Param.CHANNEL, AppLogTopAds.getChannel())
        put(AdsLogConst.Param.ENTER_FROM, AppLogTopAds.getEnterFrom())
        put(AdsLogConst.Param.MALL_CARD_TYPE, adsLogShowModel.adExtraData.mallCardType)
        put(AdsLogConst.Param.PRODUCT_ID, adsLogShowModel.adExtraData.productId)
        put(AdsLogConst.Param.PRODUCT_NAME, adsLogShowModel.adExtraData.productName)
    }

    assertEquals(expectedResult.toString(), adExtraDataJSONObject.toString())

    assertEquals(this[AdsLogConst.Param.CATEGORY], AdsLogConst.EVENT_V3)
    assertEquals(this[AdsLogConst.Param.IS_AD_EVENT], "1")
    assertEquals(this[AdsLogConst.Param.NT], 1) //1 is mobile
    assertEquals(this[AdsLogConst.Param.VALUE], adsLogShowModel.adsValue)
    assertEquals(this[AdsLogConst.Param.LOG_EXTRA], adsLogShowModel.logExtra)
    assertEquals(this[AdsLogConst.Param.GROUP_ID], "0")
    assert(this[AdsLogConst.Param.SYSTEM_START_TIMESTAMP] != 0)
    assertEquals(this[AdsLogConst.TAG], AppLogTopAds.getTagValue(AppLogTopAds.currentPageName))
}


fun JSONObject.assertShowOutsideSRP(adsLogShowModel: AdsLogShowModel) {
    val adExtraDataJSONObject: JSONObject = this[AdsLogConst.Param.AD_EXTRA_DATA] as JSONObject
    val expectedResult = JSONObject().apply {
        put(AdsLogConst.Param.MALL_CARD_TYPE, adsLogShowModel.adExtraData.mallCardType)
        put(AdsLogConst.Param.PRODUCT_ID, adsLogShowModel.adExtraData.productId)
        put(AdsLogConst.Param.PRODUCT_NAME, adsLogShowModel.adExtraData.productName)
    }

    assertEquals(expectedResult.toString(), adExtraDataJSONObject.toString())

    assertEquals(this[AdsLogConst.Param.CATEGORY], AdsLogConst.EVENT_V3)
    assertEquals(this[AdsLogConst.Param.IS_AD_EVENT], "1")
    assertEquals(this[AdsLogConst.Param.NT], 1) //1 is mobile
    assertEquals(this[AdsLogConst.Param.VALUE], adsLogShowModel.adsValue)
    assertEquals(this[AdsLogConst.Param.LOG_EXTRA], adsLogShowModel.logExtra)
    assertEquals(this[AdsLogConst.Param.GROUP_ID], "0")
    assert(this[AdsLogConst.Param.SYSTEM_START_TIMESTAMP] != 0)
    assertEquals(this[AdsLogConst.TAG], AppLogTopAds.getTagValue(AppLogTopAds.currentPageName))
}

fun JSONObject.assertShowOverSRP(adsLogShowOverModel: AdsLogShowOverModel) {
    val adExtraDataJSONObject: JSONObject = this[AdsLogConst.Param.AD_EXTRA_DATA] as JSONObject
    val expectedResult = JSONObject().apply {
        put(AdsLogConst.Param.CHANNEL, AppLogTopAds.getChannel())
        put(AdsLogConst.Param.ENTER_FROM, AppLogTopAds.getEnterFrom())
        put(AdsLogConst.Param.MALL_CARD_TYPE, adsLogShowOverModel.adExtraData.mallCardType)
        put(AdsLogConst.Param.PRODUCT_ID, adsLogShowOverModel.adExtraData.productId)
        put(AdsLogConst.Param.SIZE_PERCENT, adsLogShowOverModel.adExtraData.sizePercent)
        put(AdsLogConst.Param.PRODUCT_NAME, adsLogShowOverModel.adExtraData.productName)
    }

    assertEquals(expectedResult.toString(), adExtraDataJSONObject.toString())

    assertEquals(this[AdsLogConst.Param.CATEGORY], AdsLogConst.EVENT_V3)
    assertEquals(this[AdsLogConst.Param.IS_AD_EVENT], "1")
    assertEquals(this[AdsLogConst.Param.NT], 1) //1 is mobile
    assertEquals(this[AdsLogConst.Param.VALUE], adsLogShowOverModel.adsValue)
    assertEquals(this[AdsLogConst.Param.LOG_EXTRA], adsLogShowOverModel.logExtra)
    assertEquals(this[AdsLogConst.Param.GROUP_ID], "0")
    assert(this[AdsLogConst.Param.SYSTEM_START_TIMESTAMP] != 0)
}


fun JSONObject.assertShowOverOutsideSRP(showOverModel: AdsLogShowOverModel) {
    val adExtraDataJSONObject: JSONObject = this[AdsLogConst.Param.AD_EXTRA_DATA] as JSONObject
    val expectedResult = JSONObject().apply {
        put(AdsLogConst.Param.MALL_CARD_TYPE, showOverModel.adExtraData.mallCardType)
        put(AdsLogConst.Param.PRODUCT_ID, showOverModel.adExtraData.productId)
        put(AdsLogConst.Param.PRODUCT_NAME, showOverModel.adExtraData.productName)
        put(AdsLogConst.Param.SIZE_PERCENT, showOverModel.adExtraData.sizePercent)
    }

    assertEquals(expectedResult.toString(), adExtraDataJSONObject.toString())

    assertEquals(this[AdsLogConst.Param.CATEGORY], AdsLogConst.EVENT_V3)
    assertEquals(this[AdsLogConst.Param.IS_AD_EVENT], "1")
    assertEquals(this[AdsLogConst.Param.NT], 1) //1 is mobile
    assertEquals(this[AdsLogConst.Param.VALUE], showOverModel.adsValue)
    assertEquals(this[AdsLogConst.Param.LOG_EXTRA], showOverModel.logExtra)
    assertEquals(this[AdsLogConst.Param.GROUP_ID], "0")
    assert(this[AdsLogConst.Param.SYSTEM_START_TIMESTAMP] != 0)
}
