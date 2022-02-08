package com.tokopedia.sellerorder.detail.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.databinding.DetailProductCardItemBinding
import com.tokopedia.sellerorder.detail.presentation.adapter.SomDetailAdapter
import com.tokopedia.sellerorder.detail.presentation.model.NonProductBundleUiModel
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created By @ilhamsuaib on 05/07/21
 */

class SomDetailNonProductBundleCardViewHolder(
    private val actionListener: SomDetailAdapter.ActionListener?,
    itemView: View?
) : AbstractViewHolder<NonProductBundleUiModel>(itemView), SomDetailAddOnViewHolder.Listener {

    companion object {
        val RES_LAYOUT = R.layout.detail_product_card_item
    }

    private val binding by viewBinding<DetailProductCardItemBinding>()
    private var productDetailViewHolder: PartialSomDetailNonProductBundleDetailViewHolder? = null
    private var addOnSummaryViewHolder: PartialSomDetailAddOnSummaryViewHolder? = null

    override fun bind(element: NonProductBundleUiModel) {
        val productDetailViewHolder = getProductDetailViewHolder(element)
        val addOnSummaryViewHolder = getAddOnSummaryViewHolder(element)
        productDetailViewHolder.bind()
        addOnSummaryViewHolder.bind()
        binding?.dividerAddOn?.showWithCondition(
            shouldShow = productDetailViewHolder.isShowing() && addOnSummaryViewHolder.isShowing()
        )
    }

    private fun getProductDetailViewHolder(element: NonProductBundleUiModel): PartialSomDetailNonProductBundleDetailViewHolder {
        return productDetailViewHolder?.apply {
            this.element = element.product
        } ?: PartialSomDetailNonProductBundleDetailViewHolder(
            binding?.layoutProductDetail, actionListener, element.product
        )
    }

    private fun getAddOnSummaryViewHolder(element: NonProductBundleUiModel): PartialSomDetailAddOnSummaryViewHolder {
        return addOnSummaryViewHolder?.apply {
            this.element = element.addOnSummary
        } ?: PartialSomDetailAddOnSummaryViewHolder(
            somDetailAddOnListener = this,
            binding = binding?.layoutProductAddOn,
            element = element.addOnSummary
        )
    }

    override fun onCopyAddOnDescriptionClicked(label: String, description: CharSequence) {
        actionListener?.onCopyAddOnDescription(label, description)
    }
}