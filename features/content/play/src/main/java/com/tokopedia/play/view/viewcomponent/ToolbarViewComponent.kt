package com.tokopedia.play.view.viewcomponent

import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.IdRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Group
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.R
import com.tokopedia.play.view.uimodel.recom.PartnerFollowableStatus
import com.tokopedia.play.view.uimodel.recom.PlayPartnerFollowStatus
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

    init {
        findViewById<ImageView>(R.id.iv_back)
                .setOnClickListener {
                    listener.onBackButtonClicked(this)
                }

        ivMore.setOnClickListener {
            listener.onMoreButtonClicked(this)
        }

        tvFollow.setOnClickListener {
            listener.onFollowButtonClicked(this)
        }

        tvPartnerName.setOnClickListener {
            listener.onPartnerNameClicked(this)
        }
    }

    fun hideActionMore() {
        ivMore.hide()
    }

    fun setPartnerName(name: String) {
        if (name.isEmpty() || name.isBlank()) clPartner.hide()
        else {
            clPartner.show()
            tvPartnerName.text = name
        }
    }

    fun setFollowStatus(followStatus: PlayPartnerFollowStatus) {
        if (followStatus is PlayPartnerFollowStatus.Followable) {
            tvFollow.text = getString(
                    if (followStatus.followStatus == PartnerFollowableStatus.Followed) R.string.play_following
                    else R.string.play_follow
            )
            groupFollowable.show()
        } else {
            groupFollowable.hide()
        }
    }

    interface Listener {
        fun onBackButtonClicked(view: ToolbarViewComponent)
        fun onMoreButtonClicked(view: ToolbarViewComponent)
        fun onFollowButtonClicked(view: ToolbarViewComponent)
        fun onPartnerNameClicked(view: ToolbarViewComponent)
    }
}