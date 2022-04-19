package com.tokopedia.favorite.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Reputation(
        private val badgeLevel: Int = 0,
        private var badge: String? = null,
        private var score: Int = 0,
        private var reputationScore: String? = null,
        private var tooltip: String? = null,
        private var minBadgeScore: Int = 0
): Parcelable {

    override fun toString(): String {
        return """
            |Reputation{
                |badge_level = '$badgeLevel',
                |badge = '$badge',
                |score = '$score',
                |reputation_score = '$reputationScore',
                |tooltip = '$tooltip',
                |min_badge_score = '$minBadgeScore'
            |}
         |""".trimIndent()
    }

}
