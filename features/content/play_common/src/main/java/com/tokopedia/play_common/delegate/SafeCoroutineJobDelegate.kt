package com.tokopedia.play_common.delegate

import kotlinx.coroutines.Job
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Created by kenny.hadisaputra on 15/07/22
 */
fun reusableJob() = SafeCoroutineJobDelegate()

class SafeCoroutineJobDelegate : ReadWriteProperty<Any, Job?> {

    private var job: Job? = null

    override fun getValue(thisRef: Any, property: KProperty<*>): Job? {
        return job
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Job?) {
        job?.cancel()
        job = value
    }
}