package com.tokopedia.tokopedianow.home.presentation.viewholder.claimcoupon

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowClaimCouponWidgetShimmeringItemBinding
import com.tokopedia.tokopedianow.home.presentation.uimodel.claimcoupon.HomeClaimCouponWidgetItemShimmeringUiModel
import com.tokopedia.utils.view.binding.viewBinding

class HomeClaimCouponWidgetItemShimmeringViewHolder (
    itemView: View
) : AbstractViewHolder<HomeClaimCouponWidgetItemShimmeringUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_claim_coupon_widget_shimmering_item
    }

    private var binding: ItemTokopedianowClaimCouponWidgetShimmeringItemBinding? by viewBinding()

    override fun bind(item: HomeClaimCouponWidgetItemShimmeringUiModel) {
        binding?.apply {
            if (item.title.isBlank()) {
                topSpace.hide()
            } else {
                topSpace.show()
            }

            if (item.isDouble) {
                luCouponLargeImage.hide()
                luCouponSmallImage.show()
            } else {
                luCouponLargeImage.show()
                luCouponSmallImage.hide()
            }
        }
    }

}
