package com.tokopedia.tokopedianow.home.presentation.viewholder.claimcoupon

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.common.model.TokoNowDynamicHeaderUiModel
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowClaimCouponWidgetBinding
import com.tokopedia.tokopedianow.home.presentation.adapter.HomeAdapter
import com.tokopedia.tokopedianow.home.presentation.adapter.HomeAdapterTypeFactory
import com.tokopedia.tokopedianow.home.presentation.adapter.differ.HomeListDiffer
import com.tokopedia.tokopedianow.home.presentation.decoration.ClaimCouponWidgetItemDecoration
import com.tokopedia.tokopedianow.home.presentation.decoration.ClaimCouponWidgetItemDecoration.Companion.DOUBLE_SPAN_COUNT
import com.tokopedia.tokopedianow.home.presentation.decoration.ClaimCouponWidgetItemDecoration.Companion.SINGLE_SPAN_COUNT
import com.tokopedia.tokopedianow.home.presentation.uimodel.claimcoupon.HomeClaimCouponWidgetItemShimmeringUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.claimcoupon.HomeClaimCouponWidgetItemUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.claimcoupon.HomeClaimCouponWidgetUiModel
import com.tokopedia.utils.view.binding.viewBinding

class HomeClaimCouponWidgetViewHolder(
    itemView: View,
    private val claimCouponWidgetItemListener: HomeClaimCouponWidgetItemViewHolder.HomeClaimCouponWidgetItemListener? = null,
    private val claimCouponWidgetListener: HomeClaimCouponWidgetListener? = null
): AbstractViewHolder<HomeClaimCouponWidgetUiModel>(itemView) {

    companion object {
        private const val SHIMMERING_ID = "shimmering"

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

    init {
        binding?.apply {
            rvClaimWidgets.addItemDecoration(
                ClaimCouponWidgetItemDecoration(
                    space = root.context.resources.getDimension(R.dimen.tokopedianow_space_item_claim_coupon_widget).toIntSafely()
                )
            )
        }
    }

    override fun bind(item: HomeClaimCouponWidgetUiModel) {
        binding?.apply {
            rvClaimWidgets.adapter = adapter
            rvClaimWidgets.layoutManager = GridLayoutManager(root.context, if (item.isDouble) DOUBLE_SPAN_COUNT else SINGLE_SPAN_COUNT)
            when(item.state) {
                TokoNowLayoutState.SHOW -> showSuccessState(item)
                TokoNowLayoutState.LOADING -> showLoadingState(item)
                else -> showErrorState(item)
            }
        }
    }

    private fun ItemTokopedianowClaimCouponWidgetBinding.showSuccessState(item: HomeClaimCouponWidgetUiModel) {
        localLoad.hide()
        dynamicHeaderCustomView.show()
        dynamicHeaderCustomView.setModel(TokoNowDynamicHeaderUiModel(title = item.title))

        adapter.submitList(listOf(HomeClaimCouponWidgetItemUiModel(
            id = 121,
            smallImageUrlMobile = "https://images.tokopedia.net/img/android/now/PN-RICH.jpg",
            imageUrlMobile = "https://images.tokopedia.net/img/android/now/PN-RICH.jpg",
            couponCode = "Klaim",
            isDouble = false,
            appLink = "tokopedia://now"
        ), HomeClaimCouponWidgetItemUiModel(
            id = 123,
            smallImageUrlMobile = "https://images.tokopedia.net/img/android/now/PN-RICH.jpg",
            imageUrlMobile = "https://images.tokopedia.net/img/android/now/PN-RICH.jpg",
            couponCode = "Klaim",
            isDouble = false,
            appLink = "tokopedia://now"
        )))
    }

    private fun ItemTokopedianowClaimCouponWidgetBinding.showErrorState(item: HomeClaimCouponWidgetUiModel) {
        dynamicHeaderCustomView.hide()
        rvClaimWidgets.hide()

        localLoad.apply {
            show()
            progressState = false
            title?.text = context.getString(R.string.tokopedianow_claim_coupon_widget_local_load_title)
            description?.text = context.getString(R.string.tokopedianow_claim_coupon_widget_local_load_description)
            refreshBtn?.setOnClickListener {
                progressState = true
                claimCouponWidgetListener?.onClickRefreshButton(item.slugs)
            }
        }
    }

    private fun ItemTokopedianowClaimCouponWidgetBinding.showLoadingState(item: HomeClaimCouponWidgetUiModel) {
        dynamicHeaderCustomView.hide()
        localLoad.hide()

        val shimmeringUiModel = mutableListOf(
            HomeClaimCouponWidgetItemShimmeringUiModel(
                id = SHIMMERING_ID,
                isDouble = item.isDouble
            ),
            HomeClaimCouponWidgetItemShimmeringUiModel(
                id = SHIMMERING_ID,
                isDouble = item.isDouble
            )
        )

        if (item.isDouble) {
            adapter.submitList(
                shimmeringUiModel
            )
        } else {
            shimmeringUiModel.removeLast()
            adapter.submitList(
                shimmeringUiModel
            )
        }
    }

    interface HomeClaimCouponWidgetListener {
        fun onClickRefreshButton(slugs: List<String>)
    }
}
