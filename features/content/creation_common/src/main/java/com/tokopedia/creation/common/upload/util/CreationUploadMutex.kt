package com.tokopedia.creation.common.upload.util

import kotlinx.coroutines.sync.Mutex

/**
 * Created By : Jonathan Darwin on September 25, 2023
 */
object CreationUploadMutex {

    private val mutex = Mutex()

    fun get() = mutex
}
