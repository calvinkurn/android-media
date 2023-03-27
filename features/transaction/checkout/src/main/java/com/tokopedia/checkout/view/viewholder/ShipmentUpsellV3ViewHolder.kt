package com.tokopedia.checkout.view.viewholder

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Group
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.checkout.R
import com.tokopedia.checkout.view.ShipmentAdapterActionListener
import com.tokopedia.checkout.view.uimodel.ShipmentNewUpsellModel
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.unifycomponents.ContainerUnify
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography

class ShipmentUpsellV3ViewHolder(
    itemView: View,
    private val actionListener: ShipmentAdapterActionListener? = null
) : RecyclerView.ViewHolder(itemView) {


    companion object {

        @JvmField
        val LAYOUT = R.layout.item_upsell_v3

        private const val CARD_VIEW_MAX_RADIUS = 100
    }

    private val checkoutUpsellOuterContainer: ContainerUnify =
        itemView.findViewById(R.id.checkout_upsell_outer_container)
    private val checkoutUpsellContainer: ConstraintLayout =
        itemView.findViewById(R.id.checkout_upsell_container)
    private val checkoutUpsellImage: ImageUnify =
        itemView.findViewById(R.id.checkout_upsell_image)
    private val checkoutUpsellLogoContainer: CardUnify2 =
        itemView.findViewById(R.id.checkout_upsell_logo_container)
    private val checkoutUpsellLogo: ImageUnify =
        itemView.findViewById(R.id.checkout_upsell_logo)
    private val checkoutUpsellTitle: Typography =
        itemView.findViewById(R.id.checkout_upsell_title)
    private val checkoutUpsellDescription: Typography =
        itemView.findViewById(R.id.checkout_upsell_description)
    private val checkoutUpsellIcon: IconUnify =
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
        checkoutUpsellImage.setImageDrawable(
            ContextCompat.getDrawable(
                itemView.context,
                R.drawable.checkout_module_upsell_v3_background
            )
        )
        if (data.isLoading) {
            val transparentColor = android.R.color.transparent
            checkoutUpsellOuterContainer.setCustomContainerColor(Pair(transparentColor, transparentColor))
            checkoutUpsellContentGroup.gone()
            checkoutUpsellImage.invisible()
            checkoutUpsellIcon.invisible()
            checkoutUpsellLogoLoader.type = LoaderUnify.TYPE_CIRCLE
            checkoutUpsellTitle1Loader.type = LoaderUnify.TYPE_RECT
            checkoutUpsellTitle2Loader.type = LoaderUnify.TYPE_RECT
            checkoutUpsellDescriptionLoader.type = LoaderUnify.TYPE_RECT
            checkoutUpsellLoadingGroup.visible()
        } else {
            checkoutUpsellOuterContainer.setContainerColor(ContainerUnify.GREEN)
            checkoutUpsellContainer.setBackgroundColor(
                ContextCompat.getColor(
                    itemView.context,
                    R.color.Unify_Background
                )
            )
            checkoutUpsellImage.visible()
            checkoutUpsellImage.cornerRadius = Int.ZERO
            checkoutUpsellLogoContainer.radius = CARD_VIEW_MAX_RADIUS.toPx().toFloat()
            checkoutUpsellLogo.setImageUrl(data.image)
            checkoutUpsellTitle.text =
                HtmlLinkHelper(itemView.context, data.description).spannedString
            if (data.isSelected) {
                checkoutUpsellDescription.gone()
                checkoutUpsellIcon.setImageDrawable(
                    getIconUnifyDrawable(
                        itemView.context,
                        IconUnify.CLOSE
                    )
                )
                checkoutUpsellContainer.setOnClickListener {
                    actionListener?.onClickCancelNewUpsellCard(data)
                }
                checkoutUpsellContentGroup.visible()
            } else {
                checkoutUpsellDescription.text = "${data.priceWording}/${data.duration}"
                checkoutUpsellDescription.visible()
                checkoutUpsellIcon.setImageDrawable(
                    getIconUnifyDrawable(
                        itemView.context,
                        IconUnify.CHEVRON_RIGHT
                    )
                )
                checkoutUpsellContainer.setOnClickListener {
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
}
