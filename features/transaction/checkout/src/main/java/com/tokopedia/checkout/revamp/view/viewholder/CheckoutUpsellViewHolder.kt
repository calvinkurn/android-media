package com.tokopedia.checkout.revamp.view.viewholder

import android.view.ViewGroup
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
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class CheckoutUpsellViewHolder(
    private val binding: ItemCheckoutUpsellBinding,
    private val actionListener: CheckoutAdapterListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(data: ShipmentNewUpsellModel) {
        if (!data.isShow) {
            binding.root.gone()
            binding.root.layoutParams = RecyclerView.LayoutParams(0, 0)
            return
        } else {
            binding.root.visible()
            binding.root.layoutParams = RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            if (data.isLoading) {
                renderLoading()
            } else {
                renderContent(data)
            }
        }
    }

    private fun renderLoading() {
        binding.run {
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
            checkoutUpsellBackgroundImage.cornerRadius = Int.ZERO
            checkoutUpsellBackgroundImage.visible()
            checkoutUpsellLogoContainer.radius = CARD_VIEW_MAX_RADIUS.toPx().toFloat()
            checkoutUpsellLogo.setImageUrl(data.image)
            tvCheckoutUpsellTitle.text =
                HtmlLinkHelper(itemView.context, data.description).spannedString
            if (data.isSelected) {
                checkoutUpsellBackgroundImage.loadImage(R.drawable.checkout_module_upsell_new_short_background)
                tvCheckoutUpsellDescription.gone()
                checkoutUpsellIcon.loadImage(R.drawable.checkout_module_upsell_opt_out)
                checkoutUpsellCardOverlay.setOnClickListener {
                    actionListener.onClickCancelNewUpsellCard(data)
                }
                checkoutUpsellContentGroup.visible()
            } else {
                checkoutUpsellBackgroundImage.loadImage(R.drawable.checkout_module_upsell_new_background)
                tvCheckoutUpsellDescription.text =
                    itemView.context.getString(
                        R.string.checkout_label_upsell_description,
                        data.priceWording,
                        data.duration
                    )
                tvCheckoutUpsellDescription.visible()
                checkoutUpsellIcon.loadImage(
                    getIconUnifyDrawable(
                        itemView.context,
                        IconUnify.CHEVRON_RIGHT,
                        unifyprinciplesR.color.Unify_NN500
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
