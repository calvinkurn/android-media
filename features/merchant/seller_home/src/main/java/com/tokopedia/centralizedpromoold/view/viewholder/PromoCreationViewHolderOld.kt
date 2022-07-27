package com.tokopedia.centralizedpromoold.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.centralizedpromoold.analytic.CentralizedPromoTrackingOld
import com.tokopedia.centralizedpromoold.view.model.PromoCreationUiModelOld
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.R.layout.centralized_promo_item_promo_creation_old
import com.tokopedia.sellerhome.databinding.CentralizedPromoItemPromoCreationOldBinding

class PromoCreationViewHolderOld(view: View?) : AbstractViewHolder<PromoCreationUiModelOld>(view) {

    var onFreeShippingImpression: (() -> Unit)? = null
    var onFreeShippingClicked: (() -> Unit)? = null
    var onProductCouponImpression: (() -> Unit)? = null
    var onProductCouponClicked: (() -> Unit)? = null
    var onTokoMemberImpression: (() -> Unit)? = null
    var onTokoMemberClicked: (() -> Unit)? = null
    var onFlashSaleTokoCLicked: ((String) -> Unit)? = null

    companion object {
        val RES_LAYOUT = centralized_promo_item_promo_creation_old
    }

    private val binding by lazy {
        CentralizedPromoItemPromoCreationOldBinding.bind(itemView)
    }

    override fun bind(element: PromoCreationUiModelOld) {
        binding.run {
            ImageHandler.loadImageWithId(ivRecommendedPromo, element.imageDrawable)
            tvRecommendedPromoTitle.text = element.title
            tvRecommendedPromoDescription.text = element.description

            newTagLabel.run {
                if (element.tagLabel.isNotBlank()) {
                    show()
                    setLabel(element.tagLabel)
                } else {
                    hide()
                }
            }

            if (element.extra.isNotBlank()) {
                tvRecommendedPromoExtra.text = element.extra
                tvRecommendedPromoExtra.show()
                tvRecommendedPromoDescription.setPadding(
                    root.context.resources.getDimension(
                        com.tokopedia.unifyprinciples.R.dimen.layout_lvl0).toInt(),
                    root.context.resources.getDimension(
                        com.tokopedia.unifyprinciples.R.dimen.layout_lvl0).toInt(),
                    root.context.resources.getDimension(
                        com.tokopedia.unifyprinciples.R.dimen.layout_lvl0).toInt(),
                    root.context.resources.getDimension(
                        com.tokopedia.unifyprinciples.R.dimen.layout_lvl4).toInt()
                )
            } else {
                tvRecommendedPromoExtra.text = ""
                tvRecommendedPromoDescription.setPadding(
                    root.context.resources.getDimension(
                        com.tokopedia.unifyprinciples.R.dimen.layout_lvl0).toInt(),
                    root.context.resources.getDimension(
                        com.tokopedia.unifyprinciples.R.dimen.layout_lvl0).toInt(),
                    root.context.resources.getDimension(
                        com.tokopedia.unifyprinciples.R.dimen.layout_lvl0).toInt(),
                    root.context.resources.getDimension(
                        com.tokopedia.unifyprinciples.R.dimen.layout_lvl2).toInt()
                )
            }

            root.addOnImpressionListener(element.impressHolder) {
                when {
                    isFreeShippingPromo(element.title) -> {
                        onFreeShippingImpression?.invoke()
                    }
                    isProductCouponPromo(element.title) -> {
                        onProductCouponImpression?.invoke()
                    }
                    isTokoMember(element.title) -> {
                        onTokoMemberImpression?.invoke()
                    }
                    else -> {
                        CentralizedPromoTrackingOld.sendImpressionPromoCreation(element.title)
                    }
                }
            }

            root.setOnClickListener {
                openApplink(element.applink, element.title)
            }
        }
    }

    private fun openApplink(url: String, title: String) {
        with(itemView) {
            if (RouteManager.route(context, url)) {
                trackClickPromo(title)
            }
        }
    }

    private fun trackClickPromo(title: String) {
        when {
            isFreeShippingPromo(title) -> {
                onFreeShippingClicked?.invoke()
            }
            isProductCouponPromo(title) -> {
                onProductCouponClicked?.invoke()
            }
            isFlashSaleToko(title) -> {
                onFlashSaleTokoCLicked?.invoke(title)
            }
            isTokoMember(title) -> {
                onTokoMemberClicked?.invoke()
            }
            else -> {
                CentralizedPromoTrackingOld.sendClickPromoCreation(title)
            }
        }
    }

    private fun isFreeShippingPromo(title: String): Boolean {
        return title == getString(R.string.centralized_promo_promo_creation_free_shipping_title)
    }

    private fun isProductCouponPromo(title: String): Boolean {
        return title == getString(R.string.centralized_promo_promo_creation_voucher_product_title)
    }

    private fun isTokoMember(title: String): Boolean {
        return title == getString(R.string.centralized_promo_promo_creation_tokomember_title)
    }

    private fun isFlashSaleToko(title: String): Boolean {
        return title == getString(R.string.centralized_promo_promo_creation_flash_sale_toko_title)
    }

}