package com.tokopedia.navigation.util

class CompletableTask<T: Any>(
    val items: T,
    private val onCompleted: suspend (T) -> Unit
) {

    suspend fun completeTask() {
        onCompleted(items)
    }
}
