package com.tokopedia.play.component

import androidx.annotation.IdRes
import kotlinx.coroutines.channels.ReceiveChannel

/**
 * Created by jegul on 02/12/19
 */
interface UIComponent<T> {

    @IdRes
    fun getContainerId(): Int

    fun getUserInteractionEvents(): ReceiveChannel<T>
}