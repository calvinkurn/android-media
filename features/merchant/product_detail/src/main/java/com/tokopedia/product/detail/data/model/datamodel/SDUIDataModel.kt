package com.tokopedia.product.detail.data.model.datamodel

import android.os.Bundle
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory
import org.json.JSONException
import org.json.JSONObject
import timber.log.Timber

data class SDUIDataModel(
    val type: String,
    val name: String,
    val jsonString: String
) : DynamicPdpDataModel {

    val jsonObject: JSONObject? = try {
        JSONObject(jsonString)
    } catch (ex: JSONException) {
        Timber.d(ex)
        null
    }

    override val impressHolder: ImpressHolder = ImpressHolder()

    override fun type(): String = type
    override fun type(typeFactory: DynamicProductDetailAdapterFactory?): Int {
        return typeFactory?.type(this)!!
    }

    override fun name(): String = name

    override fun equalsWith(newData: DynamicPdpDataModel): Boolean {
        return if (newData is SDUIDataModel) {
            return newData.jsonString == jsonString
        } else {
            false
        }
    }

    override fun newInstance(): DynamicPdpDataModel {
        return this.copy()
    }

    override fun getChangePayload(newData: DynamicPdpDataModel): Bundle? {
        return null
    }
}
