package com.tokopedia.statistic.common.utils

import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.statistic.common.Const

/**
 * Created By @ilhamsuaib on 20/08/21
 */
class StatisticRemoteConfig(
    private val remoteConfig: RemoteConfig
) {

    fun isCustomDateFilterEnabled(): Boolean {
        return remoteConfig.getBoolean(Const.RemoteConfigKey.CUSTOM_DATE_FILTER_ENABLED, false)
    }
}