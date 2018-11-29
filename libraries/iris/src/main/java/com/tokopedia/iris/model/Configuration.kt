package com.tokopedia.iris.model

import java.util.concurrent.TimeUnit

/**
 * @author okasurya on 10/18/18.
 */
data class Configuration(
        val maxRow: Int = 50,
        val intervals: Long = TimeUnit.MINUTES.toMillis(1),
        val isEnabled: Boolean = true
)