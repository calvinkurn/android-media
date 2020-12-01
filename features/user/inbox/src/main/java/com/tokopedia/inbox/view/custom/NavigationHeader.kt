package com.tokopedia.inbox.view.custom

import android.view.View
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.inbox.R
import com.tokopedia.inbox.common.config.InboxConfig
import com.tokopedia.inbox.view.binder.BadgeCounterBinder
import com.tokopedia.inboxcommon.RoleType
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * for grouping view
 * see [R.layout.partial_inbox_nav_content_view]
 */
class NavigationHeader @Inject constructor(
        private val userSession: UserSessionInterface
) {

    var thumbnail: ImageUnify? = null
    var name: Typography? = null
    var badgeCounter: Typography? = null

    fun bindNavHeaderView(view: View?) {
        thumbnail = view?.findViewById(R.id.iv_user_thumbnail)
        name = view?.findViewById(R.id.txt_user_name)
        badgeCounter = view?.findViewById(R.id.unread_header_counter)
    }

    fun bindValue() {
        when (InboxConfig.role) {
            RoleType.SELLER -> bindValue(
                    userName = userSession.shopName,
                    thumbnailUrl = userSession.shopAvatar
            )
            RoleType.BUYER -> bindValue(
                    userName = userSession.name,
                    thumbnailUrl = userSession.profilePicture
            )
        }
    }

    private fun bindValue(
            userName: String,
            thumbnailUrl: String
    ) {
        ImageHandler.LoadImage(thumbnail, thumbnailUrl)
        name?.text = userName
    }

    fun setBadgeCount(count: Int) {
        BadgeCounterBinder.bindBadgeCounter(badgeCounter, count)
    }

}