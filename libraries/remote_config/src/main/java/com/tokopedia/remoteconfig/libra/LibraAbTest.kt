package com.tokopedia.remoteconfig.libra

import android.app.Application
import com.bytedance.applog.AppLog
import com.bytedance.bdinstall.Level
import com.bytedance.dataplatform.ExperimentManager
import com.bytedance.dataplatform.IExposureService
import com.bytedance.dataplatform.INetService
import com.bytedance.dataplatform.ISerializationService
import com.bytedance.dataplatform.remote_config.Experiments
import com.bytedance.ttnet.INetworkApi
import com.bytedance.ttnet.utils.RetrofitUtils
import com.google.gson.Gson
import java.lang.reflect.Type

object LibraAbTest {

    @JvmStatic
    fun init(app: Application) {
        ExperimentManager.init(app, "https://libra-sg.tiktokv.com/common", true, null,
            object : ISerializationService {
                private val gson = Gson()

                override fun object2Json(instance: Any?): String {
                    return gson.toJson(instance)
                }

                override fun <T : Any?> parseObject(input: String?, clazz: Type?): T {
                    return gson.fromJson(input, clazz)
                }
            }, object : IExposureService {
                override fun expose(exposureInfo: String?) {
                    AppLog.setExternalAbVersion(exposureInfo);
                }
            }, object : INetService {

                override fun request(url: String?): String? {
                    try {
                        val newUrl =
                            AppLog.addNetCommonParams(app, url, true, Level.L0)
                        val networkApi = RetrofitUtils.createSsService<INetworkApi>(newUrl,
                            INetworkApi::class.java)
                        val call =
                            networkApi.doGet(true, -1, "", null, null, null)
                        return call.execute().body().toString()
                    } catch (e: Exception) {
                    }
                    return null
                }
            })

        ExperimentManager.setAppLogService({ event, params ->
            AppLog.onEventV3(event, params)
        },true, true)
    }
}
