package com.tokopedia.play.view.viewcomponent

import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Group
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.R
import com.tokopedia.play.ui.toolbar.model.PartnerFollowAction
import com.tokopedia.play.ui.toolbar.model.PartnerType
import com.tokopedia.play.view.uimodel.recom.*
import com.tokopedia.play_common.viewcomponent.ViewComponent
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by jegul on 03/08/20
 */
class ToolbarViewComponent(
        container: ViewGroup,
        @IdRes idRes: Int,
        private val listener: Listener
) : ViewComponent(container, idRes) {

    private val tvPartnerName = findViewById<Typography>(R.id.tv_partner_name)
    private val tvFollow = findViewById<Typography>(R.id.tv_follow)
    private val clPartner = findViewById<ConstraintLayout>(R.id.cl_partner)
    private val groupFollowable = findViewById<Group>(R.id.group_followable)
    private val ivMore = findViewById<ImageView>(R.id.iv_more)
    private val rlCart = findViewById<RelativeLayout>(R.id.rl_cart)
    private val tvBadgeCart = findViewById<TextView>(R.id.tv_badge_cart)
    private val ivCopyLink = findViewById<ImageView>(R.id.iv_copy_link)

    init {
        findViewById<ImageView>(R.id.iv_back)
                .setOnClickListener {
                    listener.onBackButtonClicked(this)
                }

        ivMore.setOnClickListener {
            listener.onMoreButtonClicked(this)
        }

        rlCart.setOnClickListener {
            listener.onCartButtonClicked(this)
        }
    }

    fun hideActionMore() {
        ivMore.hide()
    }

    fun setPartnerInfo(partnerInfo: PlayPartnerInfoUiModel) {
        tvPartnerName.text = partnerInfo.basicInfo.name
        setFollowStatus(partnerInfo.isFollowed)

        if (!partnerInfo.isFollowable) {
            tvFollow.setOnClickListener {}
        } else {
            tvFollow.setOnClickListener {
                if (partnerInfo.isFollowed) {
                    listener.onFollowButtonClicked(this, partnerInfo.basicInfo.id, PartnerFollowAction.UnFollow)
                } else {
                    listener.onFollowButtonClicked(this, partnerInfo.basicInfo.id, PartnerFollowAction.Follow)
                }
                partnerInfo.isFollowed = !partnerInfo.isFollowed
            }
        }

        if (partnerInfo.basicInfo.name.isEmpty() || partnerInfo.basicInfo.name.isBlank()) clPartner.hide()
        else {
            clPartner.show()
            if (!partnerInfo.isFollowable) groupFollowable.hide()
            else groupFollowable.show()

            tvPartnerName.setOnClickListener {
                listener.onPartnerNameClicked(this, partnerInfo.basicInfo.id, partnerInfo.basicInfo.type)
            }
        }
    }

    fun setFollowStatus(shouldFollow: Boolean) {
        tvFollow.text = getString(
                if (shouldFollow) R.string.play_following
                else R.string.play_follow
        )
    }

    fun setCartInfo(cartUiModel: PlayCartInfoUiModel) {
        if (cartUiModel.shouldShow) rlCart.show() else rlCart.gone()
        if (cartUiModel.count > 0) {
            tvBadgeCart.show()
            tvBadgeCart.text =  if (cartUiModel.count > 99) getString(R.string.play_mock_cart) else cartUiModel.count.toString()
        } else {
            tvBadgeCart.invisible()
        }
    }

    fun setShareInfo(shareInfoUiModel: PlayShareInfoUiModel) {
        setIsShareable(shareInfoUiModel.shouldShow)

        ivCopyLink.setOnClickListener {
            listener.onCopyButtonClicked(this, shareInfoUiModel.content)
        }
    }

    fun setIsShareable(isShow: Boolean) {
        if (isShow) ivCopyLink.show() else ivCopyLink.hide()
    }

    interface Listener {
        fun onBackButtonClicked(view: ToolbarViewComponent)
        fun onMoreButtonClicked(view: ToolbarViewComponent)
        fun onFollowButtonClicked(view: ToolbarViewComponent, partnerId: Long, action: PartnerFollowAction)
        fun onPartnerNameClicked(view: ToolbarViewComponent, partnerId: Long, type: PartnerType)
        fun onCartButtonClicked(view: ToolbarViewComponent)
        fun onCopyButtonClicked(view: ToolbarViewComponent, content: String)
    }
}