package com.tokopedia.iris.data.db.mapper

import com.google.gson.Gson
import com.tokopedia.iris.model.Configuration
import com.tokopedia.iris.model.PerfConfiguration

object ConfigurationMapper {

    fun parse(json: String) : Configuration? {
        return try{
            Gson().fromJson(json, Configuration::class.java)
        } catch (e: Exception) {
            Configuration()
        }
    }
    @Suppress("SwallowedException")
    fun parsePerf(json: String) : PerfConfiguration {
        return try{
            Gson().fromJson(json, PerfConfiguration::class.java)
        } catch (e: Exception) {
            PerfConfiguration()
        }
    }
}