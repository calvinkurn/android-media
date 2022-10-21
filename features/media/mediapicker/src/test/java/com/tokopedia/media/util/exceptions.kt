package com.tokopedia.media.util

import kotlinx.coroutines.CancellationException

internal class FlowAssertionError(
    message: String,
    override val cause: Throwable?
) : AssertionError(message)

internal class TimeoutCancellationException internal constructor(
    message: String,
) : CancellationException(message)
