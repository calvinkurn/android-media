package com.tokopedia.checkout.view.viewholder

import android.view.View
import androidx.constraintlayout.widget.Group
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.checkout.R
import com.tokopedia.checkout.view.ShipmentAdapterActionListener
import com.tokopedia.checkout.view.uimodel.ShipmentNewUpsellModel
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.unifycomponents.ContainerUnify
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography

class ShipmentNewUpsellImprovementViewHolder(
    itemView: View,
    private val actionListener: ShipmentAdapterActionListener? = null
) : RecyclerView.ViewHolder(itemView) {

    companion object {

        @JvmField
        val LAYOUT = R.layout.item_upsell_new_improvement

        private const val CARD_VIEW_MAX_RADIUS = 100
    }

    private val checkoutUpsellOuterContainer: ContainerUnify =
        itemView.findViewById(R.id.checkout_upsell_outer_container)
    private val checkoutUpsellCard: CardUnify2 =
        itemView.findViewById(R.id.checkout_upsell_card)
    private val checkoutUpsellCardOverlay: CardUnify2 =
        itemView.findViewById(R.id.checkout_upsell_card_overlay)
    private val checkoutUpsellBackgroundImage: ImageUnify =
        itemView.findViewById(R.id.checkout_upsell_background_image)
    private val checkoutUpsellLogoContainer: CardUnify2 =
        itemView.findViewById(R.id.checkout_upsell_logo_container)
    private val checkoutUpsellLogo: ImageUnify =
        itemView.findViewById(R.id.checkout_upsell_logo)
    private val checkoutUpsellTitle: Typography =
        itemView.findViewById(R.id.tv_checkout_upsell_title)
    private val checkoutUpsellDescription: Typography =
        itemView.findViewById(R.id.tv_checkout_upsell_description)
    private val checkoutUpsellIcon: ImageUnify =
        itemView.findViewById(R.id.checkout_upsell_icon)
    private val checkoutUpsellContentGroup: Group =
        itemView.findViewById(R.id.checkout_upsell_content_group)
    private val checkoutUpsellLogoLoader: LoaderUnify =
        itemView.findViewById(R.id.checkout_upsell_logo_loader)
    private val checkoutUpsellTitle1Loader: LoaderUnify =
        itemView.findViewById(R.id.checkout_upsell_title1_loader)
    private val checkoutUpsellTitle2Loader: LoaderUnify =
        itemView.findViewById(R.id.checkout_upsell_title2_loader)
    private val checkoutUpsellDescriptionLoader: LoaderUnify =
        itemView.findViewById(R.id.checkout_upsell_description_loader)
    private val checkoutUpsellLoadingGroup: Group =
        itemView.findViewById(R.id.checkout_upsell_loading_group)

    fun bind(data: ShipmentNewUpsellModel) {
        if (data.isLoading) {
            renderLoading()
        } else {
            renderContent(data)
        }
    }

    private fun renderLoading() {
        checkoutUpsellCard.setCardUnifyBackgroundColor(
            MethodChecker.getColor(
                itemView.context,
                com.tokopedia.unifyprinciples.R.color.Unify_Background
            )
        )
        checkoutUpsellCardOverlay.setOnClickListener(null)
        checkoutUpsellContentGroup.gone()
        checkoutUpsellBackgroundImage.loadImage(R.drawable.checkout_module_upsell_new_background)
        checkoutUpsellBackgroundImage.invisible()
        checkoutUpsellIcon.invisible()
        checkoutUpsellLogoLoader.type = LoaderUnify.TYPE_CIRCLE
        checkoutUpsellTitle1Loader.type = LoaderUnify.TYPE_RECT
        checkoutUpsellTitle2Loader.type = LoaderUnify.TYPE_RECT
        checkoutUpsellDescriptionLoader.type = LoaderUnify.TYPE_RECT
        checkoutUpsellLoadingGroup.visible()
    }

    private fun renderContent(data: ShipmentNewUpsellModel) {
        checkoutUpsellCard.setCardUnifyBackgroundColor(
            MethodChecker.getColor(
                itemView.context,
                com.tokopedia.unifyprinciples.R.color.Unify_Background
            )
        )
        checkoutUpsellOuterContainer.setContainerColor(ContainerUnify.GREEN)
        checkoutUpsellBackgroundImage.cornerRadius = Int.ZERO
        checkoutUpsellBackgroundImage.visible()
        checkoutUpsellLogoContainer.radius = CARD_VIEW_MAX_RADIUS.toPx().toFloat()
        checkoutUpsellLogo.setImageUrl(data.image)
        checkoutUpsellTitle.text = HtmlLinkHelper(itemView.context, data.description).spannedString
        if (data.isSelected) {
            checkoutUpsellBackgroundImage.loadImage(R.drawable.checkout_module_upsell_new_short_background)
            checkoutUpsellDescription.gone()
            checkoutUpsellIcon.loadImage(R.drawable.checkout_module_upsell_opt_out)
            checkoutUpsellCardOverlay.setOnClickListener {
                actionListener?.onClickCancelNewUpsellCard(data)
            }
            checkoutUpsellContentGroup.visible()
        } else {
            checkoutUpsellBackgroundImage.loadImage(R.drawable.checkout_module_upsell_new_background)
            checkoutUpsellDescription.text =
                itemView.context.getString(
                    R.string.checkout_label_upsell_description,
                    data.priceWording,
                    data.duration
                )
            checkoutUpsellDescription.visible()
            checkoutUpsellIcon.loadImage(
                getIconUnifyDrawable(
                    itemView.context,
                    IconUnify.CHEVRON_RIGHT
                )
            )
            checkoutUpsellCardOverlay.setOnClickListener {
                actionListener?.onClickApplyNewUpsellCard(data)
            }
            checkoutUpsellContentGroup.visible()
        }
        checkoutUpsellIcon.visible()
        checkoutUpsellLoadingGroup.gone()

        if (!data.hasSeenUpsell) {
            data.hasSeenUpsell = true
            actionListener?.onViewNewUpsellCard(data)
        }
    }
}
