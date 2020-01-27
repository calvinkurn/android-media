package com.tokopedia.play.ui.toolbar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Group
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.play.R
import com.tokopedia.play.component.UIView
import com.tokopedia.play.ui.toolbar.model.PartnerFollowAction
import com.tokopedia.play.ui.toolbar.model.PartnerType
import com.tokopedia.play.view.uimodel.PartnerInfoUiModel
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by jegul on 09/12/19
 */
class ToolbarView(
        container: ViewGroup,
        private val listener: Listener
) : UIView(container) {

    private val view: View =
            LayoutInflater.from(container.context).inflate(R.layout.view_toolbar, container, true)
                    .findViewById(R.id.cl_toolbar)

    private val liveBadge = view.findViewById<View>(R.id.live_badge)
    private val tvChannelName = view.findViewById<Typography>(R.id.tv_stream_name)
    private val tvPartnerName = view.findViewById<Typography>(R.id.tv_partner_name)
    private val tvFollow = view.findViewById<Typography>(R.id.tv_follow)
    private val clPartner = view.findViewById<ConstraintLayout>(R.id.cl_partner)
    private val groupFollowable = view.findViewById<Group>(R.id.group_followable)
    private val ivMore = view.findViewById<ImageView>(R.id.iv_more)

    init {
        view.findViewById<ImageView>(R.id.iv_back)
                .setOnClickListener {
                    listener.onBackButtonClicked(this)
                }

        ivMore.setOnClickListener {
            listener.onMoreButtonClicked(this)
        }
    }

    override val containerId: Int = view.id


    override fun show() {
        view.show()
    }

    override fun hide() {
        view.hide()
    }

    internal fun setLiveBadgeVisibility(isLive: Boolean) {
        if (isLive) liveBadge.visible() else liveBadge.gone()
    }

    internal fun setTitle(title: String) {
        tvChannelName.text = title
    }

    internal fun hideActionMore() {
        ivMore.hide()
    }

    internal fun setPartnerInfo(partnerInfo: PartnerInfoUiModel) {
        tvPartnerName.text = partnerInfo.name
        setFollowStatus(partnerInfo.isFollowed)

        if (!partnerInfo.isFollowable) {
            tvFollow.setOnClickListener {}
        } else {
            tvFollow.setOnClickListener {
                if (partnerInfo.isFollowed) {
                    listener.onFollowButtonClicked(this, partnerInfo.id, PartnerFollowAction.UnFollow)
                } else {
                    listener.onFollowButtonClicked(this, partnerInfo.id, PartnerFollowAction.Follow)
                }
                partnerInfo.isFollowed = !partnerInfo.isFollowed
            }
        }

        if (partnerInfo.type == PartnerType.ADMIN || partnerInfo.name.isEmpty() || partnerInfo.name.isBlank()) clPartner.gone()
        else {
            clPartner.visible()
            if (!partnerInfo.isFollowable) groupFollowable.gone()
            else groupFollowable.visible()

            tvPartnerName.setOnClickListener {
                listener.onPartnerNameClicked(this, partnerInfo.id, partnerInfo.type)
            }
        }
    }

    fun setFollowStatus(shouldFollow: Boolean) {
        tvFollow.text = view.context.getString(
                if (shouldFollow) R.string.play_following
                else R.string.play_follow
        )
    }

    interface Listener {
        fun onBackButtonClicked(view: ToolbarView)
        fun onMoreButtonClicked(view: ToolbarView)
        fun onFollowButtonClicked(view: ToolbarView, partnerId: Long, action: PartnerFollowAction)
        fun onPartnerNameClicked(view: ToolbarView, partnerId: Long, type: PartnerType)
    }
}