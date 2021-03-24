package com.tokopedia.homenav.common.util

import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.abtest.AbTestPlatform.Companion.EXPERIMENT_NAME_TOKOPOINT

/**
 * Created by Lukas on 3/2/21.
 */

fun isABNewTokopoint(): Boolean{
    return try{
        RemoteConfigInstance.getInstance().abTestPlatform.getString(EXPERIMENT_NAME_TOKOPOINT) == EXPERIMENT_NAME_TOKOPOINT
    }catch (ex: Exception){
        false
    }
}