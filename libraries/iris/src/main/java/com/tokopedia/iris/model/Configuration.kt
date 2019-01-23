package com.tokopedia.iris.model

import com.tokopedia.iris.DEFAULT_MAX_ROW
import com.tokopedia.iris.DEFAULT_SERVICE_TIME
import java.util.concurrent.TimeUnit

/**
 * @author okasurya on 10/18/18.
 */
data class Configuration(
        val maxRow: Int = DEFAULT_MAX_ROW,
        val intervals: Long =  TimeUnit.SECONDS.toMillis(DEFAULT_SERVICE_TIME), // TimeUnit.MINUTES.toMillis(DEFAULT_SERVICE_TIME) default 15minutes
        val isEnabled: Boolean = true
)