package com.tokopedia.remoteconfig

import android.content.Context
import com.tokopedia.remoteconfig.abtest.AbTestPlatform

class RemoteConfigInstance(val context: Context, val brand: String) {

    companion object {
        val datas = mutableListOf<RemoteConfigInstance>()

        fun makeDatas(context: Context, brand: String): RemoteConfigInstance {
            val data = RemoteConfigInstance(context, brand)
            datas.add(data)
            return data
        }

        fun getABTestPlatform(context: Context): RemoteConfig {
            return AbTestPlatform(context)
        }
    }

}
