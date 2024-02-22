package com.tokopedia.sellerorder.detail.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.order_management_common.presentation.uimodel.AddOnSummaryUiModel
import com.tokopedia.order_management_common.presentation.viewholder.BmgmAddOnSummaryViewHolder
import com.tokopedia.order_management_common.presentation.viewholder.BmgmAddOnViewHolder
import com.tokopedia.order_management_common.util.setupCardDarkMode
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.databinding.DetailProductCardItemBinding
import com.tokopedia.sellerorder.detail.presentation.adapter.factory.SomDetailAdapterFactoryImpl
import com.tokopedia.sellerorder.detail.presentation.model.NonProductBundleUiModel
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created By @ilhamsuaib on 05/07/21
 */

class SomDetailNonProductBundleCardViewHolder(
    private val actionListener: SomDetailAdapterFactoryImpl.ActionListener?,
    private val addOnListener: BmgmAddOnViewHolder.Listener,
    private val recyclerViewSharedPool: RecyclerView.RecycledViewPool,
    itemView: View?
) : AbstractViewHolder<NonProductBundleUiModel>(itemView),
    BmgmAddOnSummaryViewHolder.Delegate.Mediator,
    BmgmAddOnSummaryViewHolder.Delegate by BmgmAddOnSummaryViewHolder.Delegate.Impl() {

    companion object {
        val RES_LAYOUT = R.layout.detail_product_card_item
    }

    private val binding by viewBinding<DetailProductCardItemBinding>()
    private var productDetailViewHolder: PartialSomDetailNonProductBundleDetailViewHolder? = null

    init {
        registerAddOnSummaryDelegate(this)
    }

    override fun bind(element: NonProductBundleUiModel) {
        productDetailViewHolder = getProductDetailViewHolder()
        productDetailViewHolder?.bind(element.product)
        setupDividerAddonSummary(element.addOnSummaryUiModel)
        bindAddonSummary(element.addOnSummaryUiModel)
        setupContainerBackground()
    }

    override fun getAddOnSummaryLayout(): View? {
        return itemView.findViewById(R.id.layoutProductAddOn)
    }

    override fun getRecycleViewSharedPool(): RecyclerView.RecycledViewPool? {
        return recyclerViewSharedPool
    }

    override fun getAddOnSummaryListener(): BmgmAddOnViewHolder.Listener {
        return addOnListener
    }

    private fun setupContainerBackground() {
        binding?.detailProductCardItem?.setupCardDarkMode()
    }

    private fun setupDividerAddonSummary(addOnSummaryUiModel: AddOnSummaryUiModel?) {
        binding?.dividerAddOn?.showWithCondition(addOnSummaryUiModel != null)
    }

    private fun getProductDetailViewHolder(): PartialSomDetailNonProductBundleDetailViewHolder {
        return productDetailViewHolder ?: PartialSomDetailNonProductBundleDetailViewHolder(
            binding?.layoutProductDetail, actionListener
        )
    }
}
