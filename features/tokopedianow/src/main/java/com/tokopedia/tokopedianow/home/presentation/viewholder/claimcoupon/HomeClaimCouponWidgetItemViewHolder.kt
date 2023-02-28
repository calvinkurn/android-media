package com.tokopedia.tokopedianow.home.presentation.viewholder.claimcoupon

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.showIfWithBlock
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowClaimCouponWidgetItemBinding
import com.tokopedia.tokopedianow.home.presentation.uimodel.claimcoupon.HomeClaimCouponWidgetItemUiModel
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.utils.view.binding.viewBinding

class HomeClaimCouponWidgetItemViewHolder(
    itemView: View,
    private val listener: HomeClaimCouponWidgetItemListener? = null
) : AbstractViewHolder<HomeClaimCouponWidgetItemUiModel>(itemView) {

    companion object {
        const val COUPON_STATUS_COMPLETED = "Habis"
        const val COUPON_STATUS_CLAIM = "Klaim"
        const val COUPON_STATUS_LOGIN = "Login"

        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_claim_coupon_widget_item
    }

    private var binding: ItemTokopedianowClaimCouponWidgetItemBinding? by viewBinding()

    override fun bind(item: HomeClaimCouponWidgetItemUiModel) {
        binding?.apply {
            initImage(item)
            initButton(item)
            root.setOnClickListener {
                listener?.onCouponWidgetClicked(item.appLink)
            }
        }
    }

    private fun ItemTokopedianowClaimCouponWidgetItemBinding.initImage(
        item: HomeClaimCouponWidgetItemUiModel
    ) {
        iuCouponSmallImage.showIfWithBlock(item.isDouble) {
            loadImage(item.smallImageUrlMobile)
        }

        iuCouponLargeImage.showIfWithBlock(!item.isDouble) {
            loadImage(item.imageUrlMobile)
        }
    }

    private fun ItemTokopedianowClaimCouponWidgetItemBinding.initButton(
        item: HomeClaimCouponWidgetItemUiModel
    ) {
        val ctaText = item.ctaText
        btnClaim.buttonSize = if (item.isDouble) UnifyButton.Size.MICRO else UnifyButton.Size.SMALL
        btnClaim.text = if (ctaText == COUPON_STATUS_COMPLETED || ctaText.isBlank()) COUPON_STATUS_COMPLETED else ctaText
        btnClaim.isEnabled = ctaText == COUPON_STATUS_CLAIM
        if (btnClaim.isEnabled) {
            btnClaim.isInverse = true
            btnClaim.buttonVariant = UnifyButton.Variant.GHOST
            btnClaim.setTextColor(MethodChecker.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_N0))
        }
        else {
            btnClaim.isInverse = false
            btnClaim.buttonVariant = UnifyButton.Variant.FILLED
        }
        btnClaim.setOnClickListener {
            listener?.onClaimButtonClicked(item.id)
        }
    }

    interface HomeClaimCouponWidgetItemListener {
        fun onClaimButtonClicked(catalogId: String)
        fun onCouponWidgetClicked(appLink: String)
    }

}
