package com.tokopedia.thankyou_native.data.mapper

import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.thankyou_native.domain.model.FeatureEngineData
import com.tokopedia.thankyou_native.domain.model.FeatureEngineItem
import com.tokopedia.thankyou_native.presentation.adapter.model.GyroRecommendation
import com.tokopedia.thankyou_native.presentation.adapter.model.GyroRecommendationListItem
import org.json.JSONObject

object FeatureRecommendationMapper {

    private val gson = Gson()

    fun getFeatureList(engineData: FeatureEngineData?): GyroRecommendation? {
        if (engineData == null)
            return null

        if (engineData.featureEngineItem.isNullOrEmpty())
            return null

        return GyroRecommendation(
                engineData.title,
                engineData.description,
                getGyroRecommendationItemList(engineData.featureEngineItem)
        )
    }

    private fun getGyroRecommendationItemList(featureEngineItems: ArrayList<FeatureEngineItem>): ArrayList<Visitable<*>> {
        return arrayListOf<Visitable<*>>().apply {
            featureEngineItems.forEach { featureEngineItem ->
                try {
                    val jsonObject = JSONObject(featureEngineItem.detail)
                    when (jsonObject[KEY_TYPE].toString().toLowerCase()) {
                        TYPE_LIST -> {
                            add(getFeatureRecommendationListItem(featureEngineItem))
                        }
                    }
                } catch (e: Exception) {
                }
            }
        }
    }

    private fun getFeatureRecommendationListItem(featureEngineItem: FeatureEngineItem): Visitable<*> {
        val featureListItem = gson.fromJson<GyroRecommendationListItem>(featureEngineItem.detail,
                GyroRecommendationListItem::class.java)
        featureListItem.id = featureEngineItem.id
        return featureListItem
    }

    const val KEY_TYPE = "type"
    const val TYPE_LIST = "list"

}