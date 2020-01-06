package com.tokopedia.play.ui.toolbar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
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

    private val flLiveBadge = view.findViewById<FrameLayout>(R.id.fl_live_badge)
    private val tvChannelName = view.findViewById<Typography>(R.id.tv_stream_name)
    private val tvPartner = view.findViewById<Typography>(R.id.tv_shop_name)
    private val tvFollow = view.findViewById<Typography>(R.id.tv_follow)
    private val viewDot = view.findViewById<View>(R.id.v_separator)

    init {
        view.findViewById<ImageView>(R.id.iv_back)
                .setOnClickListener {
                    listener.onBackButtonClicked(this)
                }

        view.findViewById<ImageView>(R.id.iv_more)
                .setOnClickListener {
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

    fun setLiveBadgeVisibility(isLive: Boolean) {
        if (isLive) flLiveBadge.visible() else flLiveBadge.gone()
    }

    fun setTitle(title: String) {
        tvChannelName.text = title
    }

    fun setPartnerInfo(partnerInfo: PartnerInfoUiModel) {
        tvPartner.text = partnerInfo.name
        tvFollow.text = if (partnerInfo.isFollowed)
            view.context.getString(R.string.play_following) else
            view.context.getString(R.string.play_follow)

        if (partnerInfo.type == PartnerType.ADMIN) {
            tvFollow.setOnClickListener {}
        } else {
            tvFollow.setOnClickListener {
                if (partnerInfo.isFollowed) {
                    listener.onFollowButtonClicked(this, partnerInfo.id, PartnerFollowAction.UnFollow)
                    tvFollow.text = view.context.getString(R.string.play_follow)
                } else {
                    listener.onFollowButtonClicked(this, partnerInfo.id, PartnerFollowAction.Follow)
                    tvFollow.text = view.context.getString(R.string.play_following)
                }
                partnerInfo.isFollowed = !partnerInfo.isFollowed
            }
        }

        tvPartner.setOnClickListener {
            listener.onPartnerNameClicked(this, partnerInfo.id, partnerInfo.type)
        }

        if (tvPartner.text.isEmpty() || tvPartner.text.isBlank()) {
            tvPartner.gone()
            tvFollow.gone()
            viewDot.gone()
        }
    }

    interface Listener {
        fun onBackButtonClicked(view: ToolbarView)
        fun onMoreButtonClicked(view: ToolbarView)
        fun onFollowButtonClicked(view: ToolbarView, partnerId: Long, action: PartnerFollowAction)
        fun onPartnerNameClicked(view: ToolbarView, partnerId: Long, type: PartnerType)
    }
}