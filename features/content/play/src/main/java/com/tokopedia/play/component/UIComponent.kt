package com.tokopedia.play.component

import androidx.annotation.IdRes
import androidx.lifecycle.LifecycleObserver
import kotlinx.coroutines.flow.Flow

/**
 * Created by jegul on 02/12/19
 */
interface UIComponent<T> : LifecycleObserver {

    @IdRes
    fun getContainerId(): Int

    fun getUserInteractionEvents(): Flow<T>
}