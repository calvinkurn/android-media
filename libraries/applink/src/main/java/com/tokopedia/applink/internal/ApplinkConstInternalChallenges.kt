package com.tokopedia.applink.internal

import com.tokopedia.applink.constant.DeeplinkConstant

object ApplinkConstInternalChallenges {
    const val HOST_CHALLENGES = "challenges"
    const val INTERNAL_CHALLENGES = "${DeeplinkConstant.SCHEME_INTERNAL}://$HOST_CHALLENGES"
    const val CHALLENGE_DETAILS = "$INTERNAL_CHALLENGES/challenge/{challenge-id}"
}