package com.tokopedia.remoteconfig.libra.home

import com.bytedance.dataplatform.BooleanExperiment
import com.bytedance.dataplatform.Experiment

/**
 * Created by @ilhamsuaib on 5/26/24.
 */

@Experiment(key = "android_enable_combine_inbox_notif", name = "android_enable_combine_inbox_notif", owner = "ilham.i", desc = "Enable combine inbox notif at home", methodName = "getHomeCombineInboxNotifEnabled")
class CombineInboxNotifFlag : BooleanExperiment() {
    override fun getDefault(): Boolean = true
    override fun isEnable(): Boolean = true
    override fun isSticky(): Boolean = true
}
