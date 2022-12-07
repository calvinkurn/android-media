package com.tokopedia.media.util

import com.tokopedia.media.util.event.Event
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ChannelResult
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.selects.select

fun <T> ReceiveChannel<T>.expectMostRecentItem(): T {
    var previous: ChannelResult<T>? = null
    while (true) {
        val current = tryReceive()
        current.exceptionOrNull()?.let { throw it }
        if (current.isFailure) {
            break
        }
        previous = current
    }

    if (previous?.isSuccess == true) return previous.getOrThrow()

    throw AssertionError("No item was found")
}

fun <T> ReceiveChannel<T>.expectNoEvents() {
    val result = tryReceive()
    if (!result.isFailure) result.unexpectedResult("no events")
}

suspend fun <T> ReceiveChannel<T>.awaitEvent(): Event<T> {
    val timeout = contextTimeout()
    return try {
        withAppropriateTimeout(timeout) {
            val item = receive()
            Event.Item(item)
        }
    } catch (e: kotlinx.coroutines.TimeoutCancellationException) {
        throw AssertionError("No value produced in $timeout")
    } catch (e: TimeoutCancellationException) {
        throw AssertionError("No value produced in $timeout")
    } catch (e: CancellationException) {
        throw e
    } catch (e: ClosedReceiveChannelException) {
        Event.Complete
    } catch (e: Exception) {
        Event.Error(e)
    }
}

private suspend fun <T> withAppropriateTimeout(
    timeout: Float,
    block: suspend CoroutineScope.() -> T,
): T {
    return withWallclockTimeout(timeout, block)
}

private suspend fun <T> withWallclockTimeout(
    timeout: Float,
    block: suspend CoroutineScope.() -> T,
): T = coroutineScope {
    val blockDeferred = async(start = CoroutineStart.UNDISPATCHED, block = block)
    val timeoutJob = launch(Dispatchers.Default) { delay(timeout.toLong()) }

    select {
        blockDeferred.onAwait { result ->
            timeoutJob.cancel()
            result
        }
        timeoutJob.onJoin {
            blockDeferred.cancel()
            throw TimeoutCancellationException("Timed out waiting for $timeout")
        }
    }
}

internal fun <T> ReceiveChannel<T>.takeEventUnsafe(): Event<T>? {
    return tryReceive().toEvent()
}

suspend fun <T> ReceiveChannel<T>.awaitItem(): T =
    when (val result = awaitEvent()) {
        is Event.Item -> result.value
        else -> unexpectedEvent(result, "item")
    }

suspend fun <T> ReceiveChannel<T>.awaitComplete() {
    val event = awaitEvent()
    if (event != Event.Complete) {
        unexpectedEvent(event, "complete")
    }
}

suspend fun <T> ReceiveChannel<T>.awaitError(): Throwable {
    val event = awaitEvent()
    return (event as? Event.Error)?.throwable
        ?: unexpectedEvent(event, "error")
}

internal fun <T> ChannelResult<T>.toEvent(): Event<T>? {
    val cause = exceptionOrNull()
    return when {
        isSuccess -> Event.Item(getOrThrow())
        cause != null -> Event.Error(cause)
        isClosed -> Event.Complete
        else -> null
    }
}

private fun <T> ChannelResult<T>.unexpectedResult(expected: String): Nothing = unexpectedEvent(toEvent(), expected)

private fun unexpectedEvent(event: Event<*>?, expected: String): Nothing {
    val cause = (event as? Event.Error)?.throwable
    val eventAsString = event?.toString() ?: "no items"
    throw FlowAssertionError("Expected $expected but found $eventAsString", cause)
}
