package com.tokopedia.homenav.common.util

import android.content.Context
import com.tokopedia.remoteconfig.RemoteConfigInstance

/**
 * Created by Lukas on 3/2/21.
 */
private const val EXPERIMENT_NAME_TOKOPOINT = "Rollout X"

fun Context.isABNewTokopoint(): Boolean{
    return try{
        RemoteConfigInstance.getInstance().abTestPlatform.getString(EXPERIMENT_NAME_TOKOPOINT) == EXPERIMENT_NAME_TOKOPOINT
    }catch (ex: Exception){
        false
    }
}