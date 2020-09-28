package com.tokopedia.gtmutil.interfaces

import com.tokopedia.analytic.annotation.Level

interface GTMLogger {
    fun log(level: Level, info: String)
}