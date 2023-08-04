package com.tokopedia.checkout.revamp.view.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.checkout.R
import com.tokopedia.checkout.databinding.ItemCheckoutUpsellBinding
import com.tokopedia.checkout.revamp.view.adapter.CheckoutAdapterListener
import com.tokopedia.checkout.view.uimodel.ShipmentNewUpsellModel
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.toPx

class CheckoutUpsellViewHolder(
    private val binding: ItemCheckoutUpsellBinding,
    private val actionListener: CheckoutAdapterListener
): RecyclerView.ViewHolder(binding.root) {

    fun bind(data: ShipmentNewUpsellModel) {
        if (data.isLoading) {
            renderLoading()
        } else {
            renderContent(data)
        }
    }

    private fun renderLoading() {
        binding.run {
//            checkoutUpsellCard.setCardUnifyBackgroundColor(
//                MethodChecker.getColor(
//                    itemView.context,
//                    com.tokopedia.unifyprinciples.R.color.Unify_Background
//                )
//            )
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
    }

    private fun renderContent(data: ShipmentNewUpsellModel) {
        binding.run {
//            checkoutUpsellCard.setCardUnifyBackgroundColor(
//                MethodChecker.getColor(
//                    itemView.context,
//                    com.tokopedia.unifyprinciples.R.color.Unify_Background
//                )
//            )
            checkoutUpsellBackgroundImage.cornerRadius = Int.ZERO
            checkoutUpsellBackgroundImage.visible()
            checkoutUpsellLogoContainer.radius = CARD_VIEW_MAX_RADIUS.toPx().toFloat()
            checkoutUpsellLogo.setImageUrl(data.image)
            checkoutUpsellTitle.text =
                HtmlLinkHelper(itemView.context, data.description).spannedString
            if (data.isSelected) {
                checkoutUpsellBackgroundImage.loadImage(R.drawable.checkout_module_upsell_new_short_background)
                checkoutUpsellDescription.gone()
                checkoutUpsellIcon.loadImage(R.drawable.checkout_module_upsell_opt_out)
                checkoutUpsellCardOverlay.setOnClickListener {
                    actionListener.onClickCancelNewUpsellCard(data)
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
                    actionListener.onClickApplyNewUpsellCard(data)
                }
                checkoutUpsellContentGroup.visible()
            }
            checkoutUpsellIcon.visible()
            checkoutUpsellLoadingGroup.gone()

            if (!data.hasSeenUpsell) {
                data.hasSeenUpsell = true
                actionListener.onViewNewUpsellCard(data)
            }
        }
    }

    companion object {
        val VIEW_TYPE = R.layout.item_upsell_new_improvement

        private const val CARD_VIEW_MAX_RADIUS = 100
    }
}
