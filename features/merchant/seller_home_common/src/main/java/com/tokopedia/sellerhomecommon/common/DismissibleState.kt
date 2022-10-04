package com.tokopedia.sellerhomecommon.common

/**
 * Created by @ilhamsuaib on 07/09/22.
 */

internal const val STATE_NONE = "none"
internal const val STATE_ALWAYS = "always"
internal const val STATE_TRIGGER = "trigger"

enum class DismissibleState(
    val value: String
) {
    NONE(STATE_NONE), ALWAYS(STATE_ALWAYS), TRIGGER(STATE_TRIGGER)
}