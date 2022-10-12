package com.tokopedia.macrobenchmark_util.env

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.tokopedia.macrobenchmark_util.env.mock.MockModelConfig
import com.tokopedia.macrobenchmark_util.env.mock.config.MacroMockConfig.createConfigMap
import com.tokopedia.macrobenchmark_util.env.interceptor.mock.MockDataHelper

class MockTestActivity : AppCompatActivity(){
    companion object {
        private const val URI_MACROBENCHMARK_MOCK = "macrobenchmark-mock"
    }

    var mockModelList: MutableList<MockModelConfig> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent != null) {
            val uri = intent.data
            uri?.let {
                if (uri.pathSegments[1] == URI_MACROBENCHMARK_MOCK) {
                    prepareMockConfig(uri.pathSegments[2]
                    )
                    setupMock()
                }
            }
        }
    }

    private fun prepareMockConfig(configName: String) {
        val configMap = createConfigMap(this, intent)
        configMap[configName]?.let { mockModelList.add(it) }
    }

    private fun setupMock() {
        mockModelList.forEach {
            it.createMockModel(this@MockTestActivity, intent)
        }
        MockDataHelper.setMock(this.applicationContext, mockModelList)
    }
}