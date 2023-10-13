package com.tokopedia.thankyou_native.data.mapper

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.graphql.CommonUtils
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.thankyou_native.domain.model.FeatureEngineData
import com.tokopedia.thankyou_native.domain.model.FeatureEngineItem
import com.tokopedia.thankyou_native.domain.model.ThanksPageData
import com.tokopedia.thankyou_native.presentation.adapter.model.*
import com.tokopedia.tokomember.model.MembershipOrderData
import com.tokopedia.tokomember.trackers.TokomemberSource
import org.json.JSONObject
import timber.log.Timber

object FeatureRecommendationMapper {

    private val gson = Gson()

    fun getTopAdsParams(engineData: FeatureEngineData?): TopAdsRequestParams? {
        if (engineData != null && !engineData.featureEngineItem.isNullOrEmpty()) {
            engineData.featureEngineItem.forEach { featureEngineItem ->
                try {
                    val jsonObject = JSONObject(featureEngineItem.detail)
                    if (jsonObject[KEY_TYPE].toString().equals(TYPE_TDN_PRODUCT, true) ||
                        jsonObject[KEY_TYPE].toString().equals(TYPE_TDN_USER, true)
                    ) {
                        val requestParam = gson.fromJson(
                            featureEngineItem.detail,
                            TopAdsRequestParams::class.java
                        )
                        return requestParam
                    }
                } catch (e: Exception) {
                    Timber.e(e)
                }
            }
        }
        return null
    }

    fun isTokomemberWidgetShow(engineData: FeatureEngineData?): Boolean {
        if (engineData != null && !engineData.featureEngineItem.isNullOrEmpty()) {
            engineData.featureEngineItem.forEach { featureEngineItem ->
                try {
                    val jsonObject = CommonUtils.fromJson<JsonObject>(featureEngineItem.detail, JsonObject::class.java)
                    if (jsonObject[KEY_TYPE].asString.equals(TYPE_TOKOMEMBER, true)) {
                        return true
                    }
                } catch (e: Exception) {
                    Timber.e(e)
                }
            }
        }
        return false
    }

    fun getTokomemberRequestParams(thanksPageData: ThanksPageData, engineData: FeatureEngineData): TokoMemberRequestParam {
        val shopParams = ArrayList<MembershipOrderData>()
        var amount: Double
        var shopId = 0
        var isFirstElement = false
        var sectionSubTitle = ""
        var sectionTitle = ""

        thanksPageData.shopOrder.forEach {
            amount = 0.0
            it.purchaseItemList.forEach { orderItem ->
                amount += orderItem.totalPrice
            }
            shopId = it.storeId.toIntOrZero()
            shopParams.add(MembershipOrderData(shopId, amount.toFloat()))
        }
        if (!engineData.featureEngineItem.isNullOrEmpty()) {
            engineData.featureEngineItem.forEachIndexed { i, featureEngineItem ->
                try {
                    val jsonObject = JSONObject(featureEngineItem.detail)
                    if (jsonObject[KEY_TYPE].toString().equals(TYPE_TOKOMEMBER, true)) {
                        if (i == 0) {
                            isFirstElement = true
                            sectionTitle = jsonObject[KEY_TITLE].toString()
                            sectionSubTitle = jsonObject[KEY_SUBTITLE].toString()
                        }
                    }
                } catch (e: Exception) {
                    Timber.e(e)
                }
            }
        }
        return TokoMemberRequestParam(
            pageType = PaymentPageMapper.getPaymentPageType(thanksPageData.pageType),
            source = TokomemberSource.THANK_YOU,
            paymentID = thanksPageData.paymentID,
            orderData = shopParams,
            shopID = shopId,
            isFirstElement = isFirstElement,
            sectionTitle = sectionTitle,
            sectionSubtitle = sectionSubTitle
        )
    }

    fun getFeatureList(engineData: FeatureEngineData?): GyroRecommendation? {
        if (engineData == null) {
            return null
        }

        if (engineData.featureEngineItem.isNullOrEmpty()) {
            return null
        }

        // For gyro section header will be equivalent to title of first element
        getGyroRecommendationItemList(engineData.featureEngineItem).also {
            val firstGyroItem = (it.getOrNull(0) as GyroRecommendationListItem?)
            firstGyroItem?.let { gyroItem ->
                return GyroRecommendation(
                    gyroItem.sectionTitle ?: "",
                    gyroItem.sectionDescription ?: "",
                    it
                )
            }
        }
        return null
    }

    fun getWidgetOrder(engineData: FeatureEngineData?): String {
        if (engineData != null && !engineData.featureEngineItem.isNullOrEmpty()) {
            try {
                val jsonObject = JsonParser.parseString(engineData.featureEngineItem.first().detail).asJsonObject
                return if (jsonObject[KEY_TYPE].asString.equals(TYPE_CONFIG, true)) {
                    jsonObject[KEY_WIDGET_ORDER].asString
                } else {
                    ""
                }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }

        return ""
    }

    fun getBanner(engineData: FeatureEngineData?): BannerWidgetModel? {
        if (engineData != null && !engineData.featureEngineItem.isNullOrEmpty()) {
            try {
                val bannerItem = engineData.featureEngineItem.find {
                    val type = JsonParser.parseString(it.detail).asJsonObject[KEY_TYPE].asString
                    type.equals(TYPE_BANNER, true)
                } ?: return null

                val bannerDetail = JsonParser.parseString(bannerItem.detail).asJsonObject
                val bannerItems = mutableListOf<BannerItem>()
                val bannerData = JsonParser.parseString(bannerDetail[KEY_BANNER_DATA].asString).asJsonArray

                for (i in 0 until bannerData.size()) {
                    bannerItems.add(
                        BannerItem(
                            bannerData[i].asJsonObject[KEY_ASSET_URL].asString,
                            bannerData[i].asJsonObject[KEY_APPLINK].asString,
                            bannerItem.id.toString() + " - " + bannerDetail[KEY_TITLE].asString
                        )
                    )
                }

                return BannerWidgetModel(
                    title = bannerDetail[KEY_TITLE].asString,
                    items = bannerItems
                )
            } catch (e: Exception) {
                Timber.e(e)
                return null
            }
        }

        return null
    }

    private fun getGyroRecommendationItemList(featureEngineItems: ArrayList<FeatureEngineItem>): ArrayList<Visitable<*>> {
        return arrayListOf<Visitable<*>>().apply {
            featureEngineItems.forEach { featureEngineItem ->
                try {
                    val jsonObject = JSONObject(featureEngineItem.detail)
                    when (jsonObject[KEY_TYPE].toString().lowercase()) {
                        TYPE_LIST -> {
                            add(getFeatureRecommendationListItem(featureEngineItem))
                        }
                    }
                } catch (e: Exception) {
                    Timber.e(e)
                }
            }
        }
    }

    private fun getFeatureRecommendationListItem(featureEngineItem: FeatureEngineItem): Visitable<*> {
        val featureListItem = gson.fromJson(
            featureEngineItem.detail,
            GyroRecommendationListItem::class.java
        )
        featureListItem.id = featureEngineItem.id
        return featureListItem
    }

    private const val KEY_TYPE = "type"
    private const val KEY_TITLE = "section_title"
    private const val KEY_SUBTITLE = "section_desc"
    private const val TYPE_LIST = "list"
    private const val TYPE_TDN_USER = "tdn_user"
    private const val TYPE_CONFIG = "config"
    private const val TYPE_BANNER = "banner"
    private const val KEY_WIDGET_ORDER = "widget_order"
    private const val KEY_BANNER_DATA = "banner_data"
    private const val KEY_ASSET_URL = "asset_url"
    private const val KEY_APPLINK = "applink"
    private const val KEY_ID = "id"
    const val TYPE_TOKOMEMBER = "tokomember"
    const val TYPE_TDN_PRODUCT = "tdn_product"
}
