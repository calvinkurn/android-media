package com.tokopedia.play_common.view.game.follow

import android.content.Context
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.play_common.R
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
) {
    updateView {
        getHeader().setupGiveaway(title)
        setAvatarUrl(avatarUrl)
        setBadgeUrl(badgeUrl)
        setPartnerName(partnerName)
    }
}

fun InteractiveFollowDialogFragment.setupQuiz(
    title: String,
    avatarUrl: String,
    badgeUrl: String,
    partnerName: String,
) {
    updateView {
        getHeader().setupQuiz(title)
        setAvatarUrl(avatarUrl)
        setBadgeUrl(badgeUrl)
        setPartnerName(partnerName)
    }
}