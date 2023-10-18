package com.tokopedia.tokopedianow.home.presentation.viewholder.claimcoupon

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.showIfWithBlock
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
import com.tokopedia.tokopedianow.home.presentation.uimodel.claimcoupon.HomeClaimCouponWidgetUiModel
import com.tokopedia.utils.view.binding.viewBinding

class HomeClaimCouponWidgetViewHolder(
    itemView: View,
    private val claimCouponWidgetItemListener: HomeClaimCouponWidgetItemViewHolder.HomeClaimCouponWidgetItemListener? = null,
    private val claimCouponWidgetItemTracker: HomeClaimCouponWidgetItemViewHolder.HomeClaimCouponWidgetItemTracker? = null,
    private val claimCouponWidgetListener: HomeClaimCouponWidgetListener? = null
): AbstractViewHolder<HomeClaimCouponWidgetUiModel>(itemView) {

    companion object {
        private const val SHIMMERING_ID = "shimmering"

        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_claim_coupon_widget
    }

    private var binding: ItemTokopedianowClaimCouponWidgetBinding? by viewBinding()

    private var widgets: MutableList<Visitable<*>> = mutableListOf()

    private val adapter by lazy {
        HomeAdapter(
            typeFactory = HomeAdapterTypeFactory(
                claimCouponWidgetItemListener = claimCouponWidgetItemListener,
                claimCouponWidgetItemTracker = claimCouponWidgetItemTracker
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
            adapter.submitList(widgets)
        }
    }

    private fun ItemTokopedianowClaimCouponWidgetBinding.showSuccessState(item: HomeClaimCouponWidgetUiModel) {
        initHeader(title = item.title)
        initList(couponList = item.claimCouponList)
        initLocalLoad(isError = false)
    }

    private fun ItemTokopedianowClaimCouponWidgetBinding.showErrorState(item: HomeClaimCouponWidgetUiModel) {
        initHeader(title = String.EMPTY)
        initList(couponList = null)
        initLocalLoad(
            id = item.id,
            slugs = item.slugs,
            isError = true
        )
    }

    private fun ItemTokopedianowClaimCouponWidgetBinding.showLoadingState(item: HomeClaimCouponWidgetUiModel) {
        val shimmeringUiModel = HomeClaimCouponWidgetItemShimmeringUiModel(
            id = SHIMMERING_ID,
            isDouble = item.isDouble,
            title = item.title
        )
        initHeader(title = String.EMPTY)
        initList(
            couponList = if (item.isDouble) {
                listOf(
                    shimmeringUiModel,
                    shimmeringUiModel
                )
            } else {
                listOf(
                    shimmeringUiModel
                )
            }
        )
        initLocalLoad(isError = false)
    }

    private fun ItemTokopedianowClaimCouponWidgetBinding.initHeader(
        title: String
    ) {
        dynamicHeaderCustomView.showIfWithBlock(title.isNotBlank()) {
            dynamicHeaderCustomView.setModel(TokoNowDynamicHeaderUiModel(title = title))
        }
    }

    private fun ItemTokopedianowClaimCouponWidgetBinding.initList(
        couponList: List<Visitable<*>>?
    ) {
        rvClaimWidgets.showIfWithBlock(!couponList.isNullOrEmpty()) {
            couponList?.let { couponList ->
                widgets.clear()
                widgets.addAll(couponList)
            }
        }
    }

    private fun ItemTokopedianowClaimCouponWidgetBinding.initLocalLoad(
        id: String = "",
        slugs: List<String> = listOf(),
        isError: Boolean
    ) {
        localLoad.showIfWithBlock(isError) {
            progressState = false
            title?.text = context.getString(R.string.tokopedianow_claim_coupon_widget_local_load_title)
            description?.text = context.getString(R.string.tokopedianow_claim_coupon_widget_local_load_description)
            refreshBtn?.setOnClickListener {
                progressState = true
                claimCouponWidgetListener?.onClickRefreshButton(id, slugs)
            }
        }
    }

    interface HomeClaimCouponWidgetListener {
        fun onClickRefreshButton(widgetId: String, slugs: List<String>)
    }
}
