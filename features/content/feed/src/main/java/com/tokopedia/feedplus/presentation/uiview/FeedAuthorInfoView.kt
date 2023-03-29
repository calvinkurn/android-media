package com.tokopedia.feedplus.presentation.uiview

import com.tokopedia.applink.RouteManager
import com.tokopedia.feedplus.databinding.LayoutFeedAuthorInfoBinding
import com.tokopedia.feedplus.presentation.adapter.listener.FeedListener
import com.tokopedia.feedplus.presentation.model.FeedAuthorModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition

/**
 * Created By : Muhammad Furqan on 02/03/23
 */
class FeedAuthorInfoView(
    private val binding: LayoutFeedAuthorInfoBinding,
    private val feedListener: FeedListener
) {
    fun bindData(author: FeedAuthorModel, isLive: Boolean, showFollow: Boolean) {
        with(binding) {
            imgFeedOwnerProfile.setImageUrl(author.logoUrl)
            imgFeedOwnerProfile.showWithCondition(imageConditions(author.logoUrl))

            imgFeedOwnerBadge.setImageUrl(author.badgeUrl)
            imgFeedOwnerBadge.showWithCondition(imageConditions(author.badgeUrl))

            tvFeedOwnerName.text = author.name

            bindLiveLabel(isLive)
            bindFollow(author, showFollow)

            root.setOnClickListener {
                navigateToAuthorProfile(author.applink)
            }
        }
    }

    fun bindFollow(author: FeedAuthorModel, showFollow: Boolean) {
        if (showFollow) {
            binding.btnFeedFollow.setOnClickListener {
                if (author.isShop) {
                    feedListener.onFollowClicked(author.id, "", true)
                } else if (author.isUser) {
                    feedListener.onFollowClicked(author.id, author.encryptedUserId, false)
                }
            }
            binding.btnFeedFollow.show()
        } else {
            binding.btnFeedFollow.hide()
        }
    }

    fun bindLiveLabel(isLive: Boolean) {
        if (isLive) {
            binding.labelFeedLive.show()
        } else {
            binding.labelFeedLive.hide()
        }
    }

    private fun navigateToAuthorProfile(applink: String) {
        RouteManager.route(binding.root.context, applink)
    }

    private fun imageConditions(url: String) = url.isNotEmpty()
}
