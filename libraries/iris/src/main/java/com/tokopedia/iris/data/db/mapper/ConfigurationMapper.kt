package com.tokopedia.iris.data.db.mapper

import com.google.gson.Gson
import com.tokopedia.iris.model.Configuration

/**
 * Created by meta on 24/05/19.
 */
class ConfigurationMapper {

    fun parse(json: String) : Configuration? {
        return try{
            Gson().fromJson(json, Configuration::class.java)
        } catch (e: Exception) {
            Configuration()
        }
    }
}