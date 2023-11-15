package com.tokopedia.media.loader.tracker

import com.tokopedia.logger.utils.Priority

// tracker types
internal sealed class Type {
    object Request : Type()
    object Failure : Type()
}

// Please order based on priorities.
internal val tags = mapOf(
    /**
     * This tag comes from core team, which they want to track the failure callback from Glide.
     * As of now, the tag name is ambiguity, thus we have to migrate it a proper name next cycle.
     */
    Type.Failure to Pair("DEV_CDN_MONITORING", Priority.P1),

    /**
     * This is the enhancement of previous tag, which "MEDIALOADER_ANALYTIC". The previous tag
     * not comes from us, hence we need to ensure our data streamlined dedicated to Media team.
     * Previous tag comes from New Relic team, which not fit for us.
     *
     * Previous newrelic tag will be deprecated ("MEDIALOADER_ANALYTIC") next cycle.
     */
    Type.Request to Pair("MEDIALOADER_REQUEST", Priority.P2)
)

internal const val SOURCE_ID_MAX_LENGTH = 6
