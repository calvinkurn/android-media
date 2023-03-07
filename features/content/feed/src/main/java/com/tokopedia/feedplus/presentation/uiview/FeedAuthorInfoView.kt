package com.tokopedia.feedplus.presentation.uiview

import com.tokopedia.feedplus.databinding.LayoutFeedAuthorInfoBinding
import com.tokopedia.feedplus.presentation.model.FeedAuthorModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition

/**
 * Created By : Muhammad Furqan on 02/03/23
 */
class FeedAuthorInfoView(private val binding: LayoutFeedAuthorInfoBinding) {
    fun bindData(author: FeedAuthorModel, isLive: Boolean, showFollow: Boolean) {
        with(binding) {
            imgFeedOwnerProfile.setImageUrl(author.logoUrl)
            imgFeedOwnerProfile.showWithCondition(imageConditions(author.logoUrl))

            imgFeedOwnerBadge.setImageUrl(author.badgeUrl)
            imgFeedOwnerBadge.showWithCondition(imageConditions(author.badgeUrl))

            tvFeedOwnerName.text = author.name

            bindLiveLabel(isLive)
            bindFollow(showFollow)
        }
    }

    fun bindFollow(showFollow: Boolean) {
        binding.btnFeedFollow.showWithCondition(showFollow)
    }

    fun bindLiveLabel(isLive: Boolean) {
        if (isLive) {
            binding.labelFeedLive.show()
        } else {
            binding.labelFeedLive.hide()
        }
    }

    fun showClearView() {
        with(binding) {
            imgFeedOwnerProfile.hide()
            imgFeedOwnerBadge.hide()
            tvFeedOwnerName.hide()
            labelFeedLive.hide()
            btnFeedFollow.hide()
        }
    }

    fun hideClearView() {
        with(binding) {
            imgFeedOwnerProfile.show()
            imgFeedOwnerBadge.show()
            tvFeedOwnerName.show()
            labelFeedLive.show()
            btnFeedFollow.show()
        }
    }

    private fun imageConditions(url: String) = url.isNotEmpty()
}
