package com.tokopedia.remoteconfig.libra.core

import com.bytedance.dataplatform.Experiment
import com.bytedance.dataplatform.StringExperiment

@Experiment(key = "btm_sdk_config", name = "btm_sdk_config", owner = "wujiecheng.0814@bytedance.com", desc = "btm", methodName = "getBtmSettingString")
class BtmSettingExperiment : StringExperiment() {
    override fun getDefault(): String {
        return ""
    }

    override fun isEnable(): Boolean {
        return true
    }

    override fun isSticky(): Boolean {
        return false
    }
}
