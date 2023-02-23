package com.tokopedia.tokopedianow.home.presentation.viewholder.claimcoupon

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowClaimCouponWidgetBinding
import com.tokopedia.tokopedianow.home.presentation.adapter.HomeAdapter
import com.tokopedia.tokopedianow.home.presentation.adapter.HomeAdapterTypeFactory
import com.tokopedia.tokopedianow.home.presentation.adapter.differ.HomeListDiffer
import com.tokopedia.tokopedianow.home.presentation.uimodel.claimcoupon.HomeClaimCouponWidgetItemUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.claimcoupon.HomeClaimCouponWidgetUiModel
import com.tokopedia.utils.view.binding.viewBinding

class HomeClaimCouponWidgetViewHolder(
    itemView: View,
    private val claimCouponWidgetItemListener: HomeClaimCouponWidgetItemViewHolder.HomeCouponWidgetItemListener? = null
): AbstractViewHolder<HomeClaimCouponWidgetUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_claim_coupon_widget
    }

    private var binding: ItemTokopedianowClaimCouponWidgetBinding? by viewBinding()

    private val adapter by lazy {
        HomeAdapter(
            typeFactory = HomeAdapterTypeFactory(
                claimCouponWidgetItemListener = claimCouponWidgetItemListener
            ),
            differ = HomeListDiffer(),
        )
    }

    override fun bind(element: HomeClaimCouponWidgetUiModel?) {
        binding?.apply {
            root.adapter = adapter
            root.layoutManager = GridLayoutManager(root.context, 1)
            adapter.submitList(listOf(HomeClaimCouponWidgetItemUiModel(
                id = "121",
                smallImageUrlMobile = "https://upload.wikimedia.org/wikipedia/commons/b/b6/Image_created_with_a_mobile_phone.png",
                imageUrlMobile = "https://upload.wikimedia.org/wikipedia/commons/b/b6/Image_created_with_a_mobile_phone.png",
                status = "Klaim",
                isDouble = false
            ), HomeClaimCouponWidgetItemUiModel(
                id = "121",
                smallImageUrlMobile = "https://upload.wikimedia.org/wikipedia/commons/b/b6/Image_created_with_a_mobile_phone.png",
                imageUrlMobile = "https://upload.wikimedia.org/wikipedia/commons/b/b6/Image_created_with_a_mobile_phone.png",
                status = "Klaim",
                isDouble = false
            )))
        }
    }
}
