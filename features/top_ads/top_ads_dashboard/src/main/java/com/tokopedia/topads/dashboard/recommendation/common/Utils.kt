package com.tokopedia.topads.dashboard.recommendation.common

class Utils {
    fun toProgressPercent(count: Int): Int {
        return 100 - (count * 100) / 5
    }
}
