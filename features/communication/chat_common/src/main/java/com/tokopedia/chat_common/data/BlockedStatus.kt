package com.tokopedia.chat_common.data

/**
 * @author by nisie on 14/01/19.
 */
class BlockedStatus(
        var isBlocked: Boolean = false,
        var isPromoBlocked: Boolean = false,
        var blockedUntil: String = ""
)