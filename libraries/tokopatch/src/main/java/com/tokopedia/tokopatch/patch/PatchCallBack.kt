package com.tokopedia.tokopatch.patch

import android.content.Context
import com.tokopedia.tokopatch.model.Patch

/**
 * Author errysuprayogi on 11,June,2020
 */
interface PatchCallBack {
    fun onPatchListFetched(
            context: Context,
            result: Boolean,
            isNet: Boolean,
            patches: List<Patch>
    )

    fun onPatchFetched(
            context: Context,
            result: Boolean,
            isNet: Boolean
    )

    fun onPatchApplied(
            context: Context,
            result: Boolean,
            patch: Patch
    )

    fun logNotify(
            context: Context,
            log: String,
            where: String
    )

    fun logMessage(context: Context, log: String)

    fun exceptionNotify(
            context: Context,
            throwable: Throwable,
            where: String
    )

    fun onStart()
    fun onFinish()
}