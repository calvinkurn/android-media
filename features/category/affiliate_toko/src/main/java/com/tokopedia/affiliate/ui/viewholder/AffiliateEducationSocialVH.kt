package com.tokopedia.affiliate.ui.viewholder

import android.content.res.ColorStateList
import android.view.View
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.RelativeCornerSize
import com.google.android.material.shape.RoundedCornerTreatment
import com.google.android.material.shape.ShapeAppearanceModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.affiliate.AffiliateAnalytics
import com.tokopedia.affiliate.INSTAGRAM
import com.tokopedia.affiliate.YOUTUBE
import com.tokopedia.affiliate.interfaces.AffiliateEducationSocialCTAClickInterface
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateEducationSocialUiModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSession

class AffiliateEducationSocialVH(
    itemView: View,
    private val socialCTAClickInterface: AffiliateEducationSocialCTAClickInterface?
) : AbstractViewHolder<AffiliateEducationSocialUiModel>(itemView) {

    companion object {
        private const val ROUND_RADIUS = 0.5f

        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.affiliate_education_social_item
    }

    private val roundedShape: MaterialShapeDrawable by lazy {
        val shape = ShapeAppearanceModel
            .builder()
            .setAllCorners(RoundedCornerTreatment())
            .setAllCornerSizes(RelativeCornerSize(ROUND_RADIUS))
            .build()
        MaterialShapeDrawable(shape).apply {
            this.fillColor = ColorStateList(
                arrayOf(
                    intArrayOf(android.R.attr.state_enabled)
                ),
                intArrayOf(
                    MethodChecker.getColor(
                        itemView.context,
                        com.tokopedia.unifyprinciples.R.color.Unify_Background
                    )
                )
            )
        }
    }

    private val headerImage = itemView.findViewById<ImageView>(R.id.header_image_social)
    private val shareChannel = itemView.findViewById<Typography>(R.id.share_channel)
    private val socialFollowers = itemView.findViewById<Typography>(R.id.social_followers)
    private val socialIcon = itemView.findViewById<IconUnify>(R.id.social_icon)
    private val socialIconContainer = itemView.findViewById<CardView>(R.id.social_icon_container)
    private val followButton = itemView.findViewById<UnifyButton>(R.id.button_social_follow)

    override fun bind(element: AffiliateEducationSocialUiModel?) {
        headerImage.loadImage(element?.socialItem?.headerImage)
        shareChannel.text = element?.socialItem?.socialChannel
        socialFollowers.text = element?.socialItem?.followCount
        followButton.text =
            if (element?.socialItem?.socialChannel in arrayOf(YOUTUBE, INSTAGRAM)) {
                getString(R.string.affiliate_follow_cta)
            } else {
                getString(R.string.affiliate_join_cta)
            }
        socialIcon.setImage(element?.socialItem?.icon)
        ViewCompat.setBackground(
            socialIconContainer,
            roundedShape
        )
        followButton.setOnClickListener {
            socialCTAClickInterface?.onSocialClick(
                element?.socialItem?.socialChannel.orEmpty(),
                element?.socialItem?.url.orEmpty()
            )
            sendEducationClickEvent(
                element?.socialItem?.socialChannel,
                element?.socialItem?.socialChannel,
                AffiliateAnalytics.ActionKeys.CLICK_SOCIAL_MEDIA_CARD,
                AffiliateAnalytics.CategoryKeys.AFFILIATE_EDUKASI_PAGE
            )
        }
    }

    private fun sendEducationClickEvent(
        creativeName: String?,
        eventId: String?,
        actionKeys: String,
        categoryKeys: String
    ) {
        AffiliateAnalytics.sendEducationTracker(
            AffiliateAnalytics.EventKeys.SELECT_CONTENT,
            actionKeys,
            categoryKeys,
            eventId,
            position = 0,
            eventId,
            UserSession(itemView.context).userId,
            creativeName
        )
    }
}
