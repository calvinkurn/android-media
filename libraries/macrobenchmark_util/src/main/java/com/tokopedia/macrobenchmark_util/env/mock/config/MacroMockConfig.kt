package com.tokopedia.macrobenchmark_util.env.mock.config

import android.content.Context
import android.content.Intent
import com.tokopedia.macrobenchmark_util.env.mock.MockModelConfig

object MacroMockConfig {
    object Home {
        const val configName = "home"

        fun config(context: Context, intent: Intent): MockModelConfig {
            val homeMockResponseConfig = HomeMockResponseConfig()
            homeMockResponseConfig.createMockModel(context, intent)
            return homeMockResponseConfig
        }
    }

    object Search {
        const val configName = "search"

        fun config(context: Context, intent: Intent): MockModelConfig {
            val searchMockResponseConfig = SearchMockResponseConfig()
            searchMockResponseConfig.createMockModel(context, intent)
            return searchMockResponseConfig
        }
    }

    fun createConfigMap(context: Context, intent: Intent) = mutableMapOf(
        Pair(Home.configName, Home.config(context, intent)),
        Pair(Search.configName, Search.config(context, intent))
    )
}