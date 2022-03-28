package com.tokopedia.reviewcommon.extension

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

fun Job?.launchNewIfNotLaunched(scope: CoroutineScope, action: () -> Unit): Job {
    return this?.takeIf { !it.isCompleted } ?: scope.launch { action() }
}