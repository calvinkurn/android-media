package com.tokopedia.iris.data.db.mapper

import com.google.gson.Gson
import com.tokopedia.iris.model.Configuration
import com.tokopedia.iris.model.PerfConfiguration
import com.tokopedia.iris.model.PerfWhitelistConfiguration

object ConfigurationMapper {

    fun parse(json: String): Configuration? {
        return try {
            Gson().fromJson(json, Configuration::class.java)
        } catch (e: Exception) {
            Configuration()
        }
    }

    @Suppress("SwallowedException")
    fun parsePerf(json: String): PerfConfiguration {
        return try {
            Gson().fromJson(json, PerfConfiguration::class.java)
        } catch (e: Exception) {
            PerfConfiguration()
        }
    }

    fun parseWhitelistPerf(json: String): PerfWhitelistConfiguration {
        return try {
            Gson().fromJson(json, PerfWhitelistConfiguration::class.java)
        } catch (e: Exception) {
            PerfWhitelistConfiguration()
        }
    }
}
