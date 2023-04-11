package com.tokopedia.play_common.util

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import kotlin.random.Random

/**
 * Created by kenny.hadisaputra on 14/09/22
 */
class ActivityResultHelper private constructor(
    private val startActivityResultHandler: (Intent, Int) -> Unit,
) {
    private val resultMap = mutableMapOf<Int, () -> Unit>()

    private val randomInt: Int
        get() = Random.nextInt(0, 1000)

    private val lifecycleObserver = LifecycleEventObserver { source, event ->
        synchronized(this) {
            when (event) {
                Lifecycle.Event.ON_DESTROY -> resultMap.clear()
                else -> {}
            }
        }
    }

    constructor(fragment: Fragment) : this(
        { intent, reqCode -> fragment.startActivityForResult(intent, reqCode) }
    ) {
        fragment.lifecycle.addObserver(lifecycleObserver)
    }

    constructor(activity: AppCompatActivity) : this(
        { intent, reqCode -> activity.startActivityForResult(intent, reqCode) }
    ) {
        activity.lifecycle.addObserver(lifecycleObserver)
    }

    fun processResult(requestCode: Int) = synchronized(this) {
        resultMap[requestCode]?.invoke()
        resultMap.remove(requestCode)
    }

    fun generateRequestCode(
        onResult: () -> Unit
    ): Int = synchronized(this) {
        val reqCode = generateInternalRequestCode()
        resultMap[reqCode] = onResult
        reqCode
    }

    fun startActivity(
        intent: Intent,
        onResult: () -> Unit
    ) = synchronized(this) {
        val reqCode = generateInternalRequestCode()
        resultMap[reqCode] = onResult
        startActivityResultHandler(intent, reqCode)
    }

    private fun generateInternalRequestCode(): Int {
        var reqCode = randomInt
        while (resultMap.contains(reqCode)) {
            reqCode = randomInt
        }
        return reqCode
    }
}