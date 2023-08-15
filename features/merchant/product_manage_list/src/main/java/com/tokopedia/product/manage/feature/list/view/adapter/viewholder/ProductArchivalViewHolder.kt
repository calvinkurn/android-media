package com.tokopedia.product.manage.feature.list.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.common.feature.list.data.model.ProductUiModel
import com.tokopedia.product.manage.databinding.ItemManageProductListArchivalBinding
import com.tokopedia.utils.view.binding.viewBinding

class ProductArchivalViewHolder(
    private val view: View,
    private val listener: ProductViewHolder.ProductViewHolderView
) : AbstractViewHolder<ProductUiModel>(view) {

    companion object {
        @LayoutRes
        var LAYOUT = R.layout.item_manage_product_list_archival
    }

    private val binding by viewBinding<ItemManageProductListArchivalBinding>()

    override fun bind(product: ProductUiModel) {
        showTicker(product)
        setupLabel(product)
        showProductImage(product)
        setTitleAndPrice(product)
        showMoreMenu(product)
        showProductCheckBox(product)
        binding?.btnContactCS?.setOnClickListener {
            listener.onClickContactCsButton(product)
        }

        binding?.btnMoreOptions?.setOnClickListener {
            listener.onClickMoreOptionsButton(product)
        }

        binding?.checkBoxSelect?.setOnClickListener { onClickCheckBox() }

    }

    private fun showMoreMenu(product: ProductUiModel) {
        binding?.btnMoreOptions?.showWithCondition(product.isInGracePeriod)
    }

    private fun setupLabel(product: ProductUiModel) {
        binding?.labelArchival?.showWithCondition(product.isArchived)
        binding?.labelArchivalPotential?.showWithCondition(product.isInGracePeriod)
    }

    private fun showProductImage(product: ProductUiModel) {
        binding?.imageProduct?.let {
            ImageHandler.loadImageFitCenter(itemView.context, it, product.imageUrl)
        }
    }

    private fun setTitleAndPrice(product: ProductUiModel) {
        binding?.textTitle?.text = product.title
    }

    private fun showTicker(product: ProductUiModel) {
        binding?.tickerProductManageViolation?.showWithCondition(product.isInGracePeriod)
        if (product.isInGracePeriod) {
            binding?.tickerProductManageViolation?.setTextDescription(
                getString(R.string.product_manage_text_potential_product_to_archived)
            )
        }
        binding?.tvInfoProductArchival?.showWithCondition(product.isArchived)
    }

    private fun showProductCheckBox(product: ProductUiModel) {
        if (product.isInGracePeriod) {
            binding?.checkBoxSelect?.isChecked = product.isChecked
            binding?.checkBoxSelect?.showWithCondition(product.multiSelectActive)
        }
    }

    private fun onClickCheckBox() {
        val isChecked = binding?.checkBoxSelect?.isChecked == true
        listener.onClickProductCheckBox(isChecked, adapterPosition)
    }
}
