package com.tokopedia.home.size

import android.content.Context
import com.tokopedia.test.application.environment.interceptor.size.SizeModelConfig

class HomeSizeResponseConfig: SizeModelConfig() {
    companion object {
        const val KEY_QUERY_DYNAMIC_HOME_CHANNEL = "homeData"
    }

    override fun createModelConfig(context: Context): SizeModelConfig {
        addSizeModel(KEY_QUERY_DYNAMIC_HOME_CHANNEL, FIND_BY_QUERY_NAME)
        return this
    }

}