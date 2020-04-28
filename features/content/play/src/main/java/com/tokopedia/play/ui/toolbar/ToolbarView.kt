package com.tokopedia.play.ui.toolbar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Group
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.play.R
import com.tokopedia.play.component.UIView
import com.tokopedia.play.ui.toolbar.model.PartnerFollowAction
import com.tokopedia.play.ui.toolbar.model.PartnerType
import com.tokopedia.play.view.uimodel.CartUiModel
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

    private val tvPartnerName = view.findViewById<Typography>(R.id.tv_partner_name)
    private val tvFollow = view.findViewById<Typography>(R.id.tv_follow)
    private val clPartner = view.findViewById<ConstraintLayout>(R.id.cl_partner)
    private val groupFollowable = view.findViewById<Group>(R.id.group_followable)
    private val ivMore = view.findViewById<ImageView>(R.id.iv_more)
    private val rlCart = view.findViewById<RelativeLayout>(R.id.rl_cart)
    private val tvBadgeCart = view.findViewById<TextView>(R.id.tv_badge_cart)

    init {
        view.findViewById<ImageView>(R.id.iv_back)
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

    override val containerId: Int = view.id

    override fun show() {
        view.show()
    }

    override fun hide() {
        view.hide()
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

        if (partnerInfo.name.isEmpty() || partnerInfo.name.isBlank()) clPartner.hide()
        else {
            clPartner.show()
            if (!partnerInfo.isFollowable) groupFollowable.hide()
            else groupFollowable.show()

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

    fun setCartInfo(cartUiModel: CartUiModel) {
        if (cartUiModel.isShow) rlCart.show() else rlCart.invisible()
        if (cartUiModel.count > 0) {
            tvBadgeCart.show()
            tvBadgeCart.text =  if (cartUiModel.count > 99) container.context.getString(R.string.play_mock_cart) else cartUiModel.count.toString()
        } else {
            tvBadgeCart.invisible()
        }
    }

    interface Listener {
        fun onBackButtonClicked(view: ToolbarView)
        fun onMoreButtonClicked(view: ToolbarView)
        fun onFollowButtonClicked(view: ToolbarView, partnerId: Long, action: PartnerFollowAction)
        fun onPartnerNameClicked(view: ToolbarView, partnerId: Long, type: PartnerType)
        fun onCartButtonClicked(view: ToolbarView)
    }
}