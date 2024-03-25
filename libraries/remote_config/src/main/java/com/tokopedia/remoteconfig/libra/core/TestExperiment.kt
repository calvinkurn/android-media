package com.tokopedia.remoteconfig.libra.core

import com.bytedance.dataplatform.Experiment
import com.bytedance.dataplatform.IntegerExperiment

@Experiment(key = "test_key", name = "toko_test_ep", owner = "chenmo.96@bytedance.com", desc = "this is a test ep")
class TestExperiment : IntegerExperiment() {
    override fun getDefault(): Int {
        return 2
    }

    override fun isEnable(): Boolean {
        return true
    }

    override fun isSticky(): Boolean {
        return false
    }
}
