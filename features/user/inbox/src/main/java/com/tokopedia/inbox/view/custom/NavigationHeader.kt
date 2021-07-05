package com.tokopedia.inbox.view.custom

import android.view.View
import android.widget.ImageView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
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
        bindImageProfile(thumbnailUrl)
        bindUserName(userName)
    }

    private fun bindImageProfile(thumbnailUrl: String) {
        thumbnail?.scaleType = ImageView.ScaleType.CENTER_CROP
        thumbnail?.setImageUrl(thumbnailUrl)
    }

    private fun bindUserName(userName: String) {
        name?.text = MethodChecker.fromHtml(userName)
    }

    fun setBadgeCount(count: Int) {
        BadgeCounterBinder.bindBadgeCounter(badgeCounter, count)
    }

}