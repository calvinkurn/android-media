package com.tokopedia.homenav.common.util

import com.tokopedia.remoteconfig.RemoteConfigInstance

/**
 * Created by Lukas on 3/2/21.
 */
private const val EXPERIMENT_NAME_TOKOPOINT = "tokopoints_glmenu"

fun isABNewTokopoint(): Boolean{
    return true
    return try{
        RemoteConfigInstance.getInstance().abTestPlatform.getString(EXPERIMENT_NAME_TOKOPOINT) == EXPERIMENT_NAME_TOKOPOINT
    }catch (ex: Exception){
        false
    }
}