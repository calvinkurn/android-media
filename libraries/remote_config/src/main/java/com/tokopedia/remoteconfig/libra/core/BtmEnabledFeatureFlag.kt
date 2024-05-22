package com.tokopedia.remoteconfig.libra.core

import com.bytedance.dataplatform.BooleanExperiment
import com.bytedance.dataplatform.Experiment

/**
 * Created by @ilhamsuaib on 5/22/24.
 */

@Experiment(key = "android_enable_btm", name = "android_enable_btm", owner = "ilham.i@bytedance.com", desc = "enable or disable btm. default is true")
class BtmEnabledFeatureFlag : BooleanExperiment() {

    override fun getDefault(): Boolean = true

    override fun isEnable(): Boolean = true

    override fun isSticky(): Boolean = false //cache enabled
}
