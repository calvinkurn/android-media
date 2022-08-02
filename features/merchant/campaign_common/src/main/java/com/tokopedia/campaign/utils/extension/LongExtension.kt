package com.tokopedia.campaign.utils.extension

import java.util.Date


/**
 * Convert millis to Date object
 */
fun Long.toDate(): Date {
    return Date(this)
}

