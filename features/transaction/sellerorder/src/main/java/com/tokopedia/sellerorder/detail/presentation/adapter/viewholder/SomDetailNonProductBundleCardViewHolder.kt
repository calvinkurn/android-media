package com.tokopedia.sellerorder.detail.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.showWithCondition
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
    private val recyclerViewSharedPool: RecyclerView.RecycledViewPool,
    itemView: View?
) : AbstractViewHolder<NonProductBundleUiModel>(itemView), SomDetailAddOnViewHolder.Listener {

    companion object {
        val RES_LAYOUT = R.layout.detail_product_card_item
    }

    private val binding by viewBinding<DetailProductCardItemBinding>()
    private var productDetailViewHolder: PartialSomDetailNonProductBundleDetailViewHolder? = null
    private var addOnSummaryViewHolder: PartialSomDetailAddOnSummaryViewHolder? = null

    override fun bind(element: NonProductBundleUiModel) {
        productDetailViewHolder = getProductDetailViewHolder()
        addOnSummaryViewHolder = getAddOnSummaryViewHolder()
        productDetailViewHolder?.bind(element.product)
        addOnSummaryViewHolder?.bind(element.addOnSummary)
        binding?.dividerAddOn?.showWithCondition(
            productDetailViewHolder?.isShowing() == true &&
                    addOnSummaryViewHolder?.isShowing() == true
        )
    }

    private fun getProductDetailViewHolder(): PartialSomDetailNonProductBundleDetailViewHolder {
        return productDetailViewHolder ?: PartialSomDetailNonProductBundleDetailViewHolder(
            binding?.layoutProductDetail, actionListener
        )
    }

    private fun getAddOnSummaryViewHolder(): PartialSomDetailAddOnSummaryViewHolder {
        return addOnSummaryViewHolder ?: PartialSomDetailAddOnSummaryViewHolder(
            somDetailAddOnListener = this,
            binding = binding?.layoutProductAddOn,
            recyclerViewSharedPool = recyclerViewSharedPool
        )
    }

    override fun onCopyAddOnDescriptionClicked(label: String, description: CharSequence) {
        actionListener?.onCopyAddOnDescription(label, description)
    }
}