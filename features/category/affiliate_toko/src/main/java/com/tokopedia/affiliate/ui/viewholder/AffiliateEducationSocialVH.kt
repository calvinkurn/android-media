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
import com.tokopedia.affiliate.interfaces.AffiliateEducationSocialCTAClickInterface
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateEducationSocialUiModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

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

    @Inject
    lateinit var userSessionInterface: UserSessionInterface

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

    override fun bind(element: AffiliateEducationSocialUiModel?) {
        itemView.findViewById<ImageView>(R.id.header_image_social)
            ?.loadImage(element?.socialItem?.headerImage)
        itemView.findViewById<Typography>(R.id.share_channel)?.text =
            element?.socialItem?.socialChannel
        itemView.findViewById<Typography>(R.id.social_followers)?.text =
            element?.socialItem?.followCount
        itemView.findViewById<IconUnify>(R.id.social_icon).setImage(element?.socialItem?.icon)
        ViewCompat.setBackground(
            itemView.findViewById<CardView>(R.id.social_icon_container),
            roundedShape
        )
        itemView.findViewById<UnifyButton>(R.id.button_social_follow)?.setOnClickListener {
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
    private fun sendEducationClickEvent(creativeName: String?, eventId: String?, actionKeys: String, categoryKeys: String) {
        AffiliateAnalytics.sendEducationTracker(
            AffiliateAnalytics.EventKeys.SELECT_CONTENT,
            actionKeys,
            categoryKeys,
            eventId,
            position = 0,
            eventId,
            userSessionInterface.userId,
            creativeName
        )
    }
}
