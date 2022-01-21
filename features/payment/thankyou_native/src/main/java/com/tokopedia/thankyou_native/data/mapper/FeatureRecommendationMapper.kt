package com.tokopedia.thankyou_native.data.mapper

import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.thankyou_native.domain.model.FeatureEngineData
import com.tokopedia.thankyou_native.domain.model.FeatureEngineItem
import com.tokopedia.thankyou_native.presentation.adapter.model.GyroRecommendation
import com.tokopedia.thankyou_native.presentation.adapter.model.GyroRecommendationListItem
import com.tokopedia.thankyou_native.presentation.adapter.model.TopAdsRequestParams
import org.json.JSONObject

object FeatureRecommendationMapper {

    private val gson = Gson()

    fun getTopAdsParams(engineData: FeatureEngineData?): TopAdsRequestParams? {
        if (engineData != null && !engineData.featureEngineItem.isNullOrEmpty()) {
            engineData.featureEngineItem.forEach { featureEngineItem ->
                try {
                    val jsonObject = JSONObject(featureEngineItem.detail)
                    if (jsonObject[KEY_TYPE].toString().equals(TYPE_TDN_PRODUCT, true)
                        || jsonObject[KEY_TYPE].toString().equals(TYPE_TDN_USER, true)) {
                        val requestParam = gson.fromJson(
                            featureEngineItem.detail,
                            TopAdsRequestParams::class.java
                        )
                        return requestParam
                    }

                } catch (e: Exception) { }
            }
        }
        return null
    }

    fun getFeatureList(engineData: FeatureEngineData?): GyroRecommendation? {
        if (engineData == null)
            return null

        if (engineData.featureEngineItem.isNullOrEmpty())
            return null

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

    private fun getGyroRecommendationItemList(featureEngineItems: ArrayList<FeatureEngineItem>): ArrayList<Visitable<*>> {
        return arrayListOf<Visitable<*>>().apply {
            featureEngineItems.forEach { featureEngineItem ->
                try {
                    val jsonObject = JSONObject(featureEngineItem.detail)
                    when (jsonObject[KEY_TYPE].toString().lowercase()) {
                        TYPE_LIST, TYPE_TOKOMEMBER -> {
                            add(getFeatureRecommendationListItem(featureEngineItem))
                        }
                    }
                } catch (e: Exception) { }
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
    const val TYPE_LIST = "list"
    const val TYPE_TOKOMEMBER = "tokomember"
    private const val TYPE_TDN_USER = "tdn_user"
    const val TYPE_TDN_PRODUCT = "tdn_product"

}