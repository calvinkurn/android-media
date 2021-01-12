package com.tokopedia.imagepicker.common.util

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Checking for cache/internal storage
 */
object ImageCacheCleaner {
    @JvmStatic
    fun cleanUpStorageIfNeeded(lifecycleScope: CoroutineScope, context: Context) {
        lifecycleScope.launch {

        }
    }
}