package com.tokopedia.play_common.view.game.follow

import com.tokopedia.play_common.view.game.setupGiveaway
import com.tokopedia.play_common.view.game.setupQuiz

/**
 * Created by kenny.hadisaputra on 07/04/22
 */
fun InteractiveFollowDialogFragment.setupGiveaway(
    title: String,
    avatarUrl: String,
    badgeUrl: String,
    partnerName: String,
    isLoading: Boolean = false,
) {
    onView {
        getHeader().setupGiveaway(title)
        setAvatarUrl(avatarUrl)
        setBadgeUrl(badgeUrl)
        setPartnerName(partnerName)

        setLoading(isLoading)
    }
}

fun InteractiveFollowDialogFragment.setupQuiz(
    title: String,
    avatarUrl: String,
    badgeUrl: String,
    partnerName: String,
    isLoading: Boolean = false,
) {
    onView {
        getHeader().setupQuiz(title)
        setAvatarUrl(avatarUrl)
        setBadgeUrl(badgeUrl)
        setPartnerName(partnerName)

        setLoading(isLoading)
    }
}