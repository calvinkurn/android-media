package com.tokopedia.media.util

import com.tokopedia.media.util.internal.Receiver
import com.tokopedia.media.util.internal.TestChannel
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect

suspend fun <T> Flow<T>.test(
    timeout: Float? = null,
    validate: suspend Receiver<T>.() -> Unit,
) {
    coroutineScope {
        collectIn(this, null).apply {
            if (timeout != null) {
                withTimeout(timeout) {
                    validate()
                }
            } else {
                validate()
            }
            cancel()
            ensureAllEventsConsumed()
        }
    }
}

private fun <T> Flow<T>.collectIn(scope: CoroutineScope, timeout: Float?): Receiver<T> {
    lateinit var channel: Channel<T>

    val unconfined = scope.coroutineContext

    val job = scope.launch(unconfined, start = CoroutineStart.UNDISPATCHED) {
        channel = collectIntoChannel(this)
    }

    return TestChannel(channel, job, timeout)
}

internal fun <T> Flow<T>.collectIntoChannel(scope: CoroutineScope): Channel<T> {
    val output = Channel<T>(Channel.UNLIMITED)

    val job = scope.launch(start = CoroutineStart.UNDISPATCHED) {
        try {
            collect { output.trySend(it) }
            output.close()
        } catch (e: Exception) {
            output.close(e)
        }
    }

    return object : Channel<T> by output {
        override fun cancel(cause: CancellationException?) {
            job.cancel()
            output.close(cause)
        }

        override fun close(cause: Throwable?): Boolean {
            job.cancel()
            return output.close(cause)
        }
    }
}
