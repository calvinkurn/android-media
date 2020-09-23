package com.tokopedia.thankyou_native.data.mapper

import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.thankyou_native.domain.model.FeatureEngineItem
import com.tokopedia.thankyou_native.presentation.adapter.model.FeatureListItem
import org.json.JSONObject

object FeatureRecommendationMapper {

    private val gson = Gson()

    fun getFeatureList(featureEngineItems: ArrayList<FeatureEngineItem>): ArrayList<Visitable<*>> {
        return arrayListOf<Visitable<*>>().apply {
            featureEngineItems.forEach { featureEngineItem ->
                try {
                    val jsonObject = JSONObject(featureEngineItem.detail)
                    when (jsonObject[KEY_TYPE]) {
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
        val featureListItem = gson.fromJson<FeatureListItem>(featureEngineItem.detail,
                FeatureListItem::class.java)
        featureListItem.id = featureEngineItem.id
        return featureListItem
    }

    const val KEY_TYPE = "type"
    const val TYPE_LIST = "list"

}