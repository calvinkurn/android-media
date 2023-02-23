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
    private val listener: HomeCouponWidgetItemListener? = null
) : AbstractViewHolder<HomeClaimCouponWidgetItemUiModel>(itemView) {

    companion object {
        const val COUPON_STATUS_COMPLETED = "Habis"
        const val COUPON_STATUS_CLAIMED = "Klaim"

        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_claim_coupon_widget_item
    }

    private var binding: ItemTokopedianowClaimCouponWidgetItemBinding? by viewBinding()

    override fun bind(item: HomeClaimCouponWidgetItemUiModel) {
        setData(item)
    }

    private fun setData(item: HomeClaimCouponWidgetItemUiModel) {
        binding?.apply {
            initImage(item)
            initButton(item)
            root.setOnClickListener {
                listener?.onCouponWidgetClicked()
            }
        }
    }

    private fun ItemTokopedianowClaimCouponWidgetItemBinding.initImage(
        item: HomeClaimCouponWidgetItemUiModel
    ) {
        iuCouponImageDouble.showIfWithBlock(item.isDouble) {
            loadImage(item.smallImageUrlMobile)
        }

        iuCouponImage.showIfWithBlock(!item.isDouble) {
            loadImage(item.imageUrlMobile)
        }
    }

    private fun ItemTokopedianowClaimCouponWidgetItemBinding.initButton(
        item: HomeClaimCouponWidgetItemUiModel
    ) {
        val status = item.status
        btnClaim.buttonSize = if (item.isDouble) UnifyButton.Size.MICRO else UnifyButton.Size.SMALL
        btnClaim.text = if (status == COUPON_STATUS_COMPLETED || status.isBlank()) COUPON_STATUS_COMPLETED else status
        btnClaim.isEnabled = status == COUPON_STATUS_CLAIMED
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

    interface HomeCouponWidgetItemListener {
        fun onClaimButtonClicked(catalogId: String)
        fun onCouponWidgetClicked()
    }

}
