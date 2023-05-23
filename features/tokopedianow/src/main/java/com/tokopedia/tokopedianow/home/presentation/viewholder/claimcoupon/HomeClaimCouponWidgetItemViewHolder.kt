package com.tokopedia.tokopedianow.home.presentation.viewholder.claimcoupon

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.showIfWithBlock
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowClaimCouponWidgetItemBinding
import com.tokopedia.tokopedianow.home.presentation.uimodel.claimcoupon.HomeClaimCouponWidgetItemUiModel
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.utils.view.binding.viewBinding

class HomeClaimCouponWidgetItemViewHolder(
    itemView: View,
    private val listener: HomeClaimCouponWidgetItemListener? = null,
    private val tracker: HomeClaimCouponWidgetItemTracker? = null
) : AbstractViewHolder<HomeClaimCouponWidgetItemUiModel>(itemView) {

    companion object {
        const val COUPON_STATUS_COMPLETED = "Habis"
        const val COUPON_STATUS_CLAIM = "Klaim"
        const val COUPON_STATUS_CLAIMED = "Diklaim"
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
                listener?.onClickCouponWidget(item.appLink)
                tracker?.onClickCouponWidgetTracker(
                    couponStatus = item.ctaText,
                    position = layoutPosition,
                    slugText = item.slugText,
                    couponName = item.couponName,
                    warehouseId = item.warehouseId
                )
            }
            root.addOnImpressionListener(item) {
                tracker?.onImpressCouponTracker(
                    couponStatus = item.ctaText,
                    position = layoutPosition,
                    slugText = item.slugText,
                    couponName = item.couponName,
                    warehouseId = item.warehouseId,
                    isDouble = item.isDouble
                )
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
        btnClaim.apply {
            buttonSize = if (item.isDouble) UnifyButton.Size.MICRO else UnifyButton.Size.SMALL
            text = if (ctaText == COUPON_STATUS_COMPLETED || ctaText.isBlank()) COUPON_STATUS_COMPLETED else ctaText
            isEnabled = ctaText == COUPON_STATUS_CLAIM
            if (isEnabled) {
                isInverse = true
                buttonVariant = UnifyButton.Variant.GHOST
                setTextColor(MethodChecker.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_NN0))
            }
            else {
                isInverse = false
                buttonVariant = UnifyButton.Variant.FILLED
            }
            setOnClickListener {
                listener?.onClickClaimButton(
                    widgetId = item.widgetId,
                    catalogId = item.id,
                    couponStatus = item.ctaText,
                    position = layoutPosition,
                    slugText = item.slugText,
                    couponName = item.couponName,
                    warehouseId = item.warehouseId
                )
                tracker?.onClickClaimButtonTracker(
                    couponStatus = item.ctaText,
                    position = layoutPosition,
                    slugText = item.slugText,
                    couponName = item.couponName,
                    warehouseId = item.warehouseId
                )
            }
        }
    }

    interface HomeClaimCouponWidgetItemListener {
        fun onClickClaimButton(
            widgetId: String,
            catalogId: String,
            couponStatus: String,
            position: Int,
            slugText: String,
            couponName: String,
            warehouseId: String
        )
        fun onClickCouponWidget(
            appLink: String
        )
    }

    interface HomeClaimCouponWidgetItemTracker {
        fun onImpressCouponTracker(
            couponStatus: String,
            position: Int,
            slugText: String,
            couponName: String,
            warehouseId: String,
            isDouble: Boolean
        )
        fun onClickCouponWidgetTracker(
            couponStatus: String,
            position: Int,
            slugText: String,
            couponName: String,
            warehouseId: String
        )
        fun onClickClaimButtonTracker(
            couponStatus: String,
            position: Int,
            slugText: String,
            couponName: String,
            warehouseId: String
        )
    }

}
