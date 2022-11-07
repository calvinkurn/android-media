@file:OptIn(ExperimentalCoroutinesApi::class)
package com.tokopedia.media.util.internal

import com.tokopedia.media.util.*
import com.tokopedia.media.util.FlowAssertionError
import com.tokopedia.media.util.event.Event
import com.tokopedia.media.util.toEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlinx.coroutines.channels.ChannelResult

class TestChannel<T>(
    channel: Channel<T> = Channel(UNLIMITED),
    private val job: Job? = null,
    private val timeout: Float?,
) : TestChannelDefault<T> {

    private var ignoreTerminalEvents = false
    private var ignoreRemainingEvents = false

    private suspend fun <T> withTimeout(block: suspend () -> T): T {
        return if (timeout != null) {
            withTimeout(timeout) { block() }
        } else {
            block()
        }
    }

    private val channel = object : Channel<T> by channel {
        override fun tryReceive(): ChannelResult<T> {
            val result = channel.tryReceive()
            val event = result.toEvent()

            if (event is Event.Error || event is Event.Complete) {
                ignoreRemainingEvents = true
            }

            return result
        }

        override suspend fun receive(): T = try {
            channel.receive()
        } catch (e: Throwable) {
            ignoreRemainingEvents = true
            throw e
        }
    }

    override fun add(item: T) {
        if (!channel.trySend(item).isSuccess) {
            throw IllegalStateException("Added when closed")
        }
    }

    override suspend fun cancel() {
        if (!channel.isClosedForSend) {
            ignoreTerminalEvents = true
        }

        channel.cancel()
        job?.cancelAndJoin()
    }

    override fun close(cause: Throwable?) {
        if (!channel.isClosedForSend) {
            ignoreTerminalEvents = true
        }

        channel.close(cause)
        job?.cancel()
    }

    override fun expectNoEvents() {
        channel.expectNoEvents()
    }

    override fun expectMostRecentItem(): T = channel.expectMostRecentItem()

    override suspend fun awaitEvent(): Event<T> = withTimeout { channel.awaitEvent() }

    override suspend fun awaitItem(): T = withTimeout { channel.awaitItem() }

    override suspend fun awaitComplete() = withTimeout { channel.awaitComplete() }

    override suspend fun awaitError(): Throwable = withTimeout { channel.awaitError() }

    override fun ensureAllEventsConsumed() {
        if (ignoreRemainingEvents) return

        val unconsumed = mutableListOf<Event<T>>()
        var cause: Throwable? = null
        while (true) {
            val event = channel.takeEventUnsafe() ?: break
            if (!(ignoreTerminalEvents && event.isTerminal)) unconsumed += event
            if (event is Event.Error) {
                cause = event.throwable
                break
            } else if (event is Event.Complete) {
                break
            }
        }
        if (unconsumed.isNotEmpty()) {
            throw FlowAssertionError(
                buildString {
                    append("Unconsumed events found:")
                    for (event in unconsumed) {
                        append("\n - $event")
                    }
                },
                cause
            )
        }
    }
}
