package com.tokopedia.play.view.viewcomponent.partnerinfo

import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.play.R
import com.tokopedia.play.view.uimodel.recom.PartnerFollowableStatus
import com.tokopedia.play.view.uimodel.recom.PlayPartnerFollowStatus
import com.tokopedia.play.view.uimodel.recom.PlayPartnerInfo
import com.tokopedia.play_common.util.extension.updateLayoutParams
import com.tokopedia.play_common.viewcomponent.ViewComponent
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.content.common.R as contentcommonR

class PartnerInfoViewComponent(
    container: ViewGroup,
    private val listener: Listener
) : ViewComponent(container, R.id.view_partner_info) {

    private val ivIcon: AppCompatImageView = findViewById(contentcommonR.id.iv_icon)
    private val ivBadge: AppCompatImageView = findViewById(contentcommonR.id.iv_badge)
    private val tvPartnerName: Typography = findViewById(contentcommonR.id.tv_partner_name)
    private val btnFollow: UnifyButton = findViewById(contentcommonR.id.btn_follow)

    init {
        btnFollow.setOnClickListener {
            listener.onFollowButtonClicked(this)
        }
    }

    fun setInfo(info: PlayPartnerInfo) {
        ivIcon.loadImage(info.iconUrl) //TODO: check placeholder, error state, etc from ImageUnify
        if (info.badgeUrl.isNotBlank()) {
            ivBadge.loadImage(info.badgeUrl) //TODO: check placeholder, error state, etc from ImageUnify
            ivBadge.show()
        } else {
            ivBadge.hide()
        }
        tvPartnerName.text = info.name
        setFollowStatus(info.status, info.isLoadingFollow)
        setupListener(info.appLink)
    }

    private fun setFollowStatus(
        followStatus: PlayPartnerFollowStatus,
        isLoading: Boolean
    ) {
        if (followStatus is PlayPartnerFollowStatus.Followable && followStatus.followStatus == PartnerFollowableStatus.NotFollowed) {
            btnFollow.isLoading = isLoading
            btnFollow.isEnabled = !isLoading
            btnFollow.show()
            listener.onFollowImpressed(this)
        } else {
            btnFollow.hide()
        }
    }

    private fun setupListener(applink: String) {
        rootView.setOnClickListener {
            listener.onPartnerInfoClicked(this, applink)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        btnFollow.updateLayoutParams {
            width = ViewGroup.LayoutParams.WRAP_CONTENT
        }
    }

    interface Listener {
        fun onPartnerInfoClicked(view: PartnerInfoViewComponent, applink: String)
        fun onFollowButtonClicked(view: PartnerInfoViewComponent)
        fun onFollowImpressed(view: PartnerInfoViewComponent)
    }
}
