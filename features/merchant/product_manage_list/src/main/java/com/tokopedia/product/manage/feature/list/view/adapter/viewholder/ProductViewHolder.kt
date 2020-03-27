package com.tokopedia.product.manage.feature.list.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler.loadImageFitCenter
import com.tokopedia.kotlin.extensions.view.getNumberFormatted
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.feature.list.view.model.ProductViewModel
import kotlinx.android.synthetic.main.item_manage_product_list.view.*

class ProductViewHolder(
    view: View,
    private val listener: ProductViewHolderView
): AbstractViewHolder<ProductViewModel>(view) {

    companion object {
        @LayoutRes
        var LAYOUT = R.layout.item_manage_product_list
    }

    override fun bind(product: ProductViewModel) {
        setTitleAndPrice(product)
        showProductStock(product)

        showProductLabel(product)
        showVariantLabel(product)
        showProductButton(product)

        showProductImage(product)
        showStockHintImage(product)
        showProductCheckBox(product)

        setOnClickListeners(product)
    }

    private fun setTitleAndPrice(product: ProductViewModel) {
        itemView.textTitle.text = product.title
        itemView.textPrice.text = product.priceFormatted
    }

    private fun showProductStock(product: ProductViewModel) {
        if(product.isNotVariant()) {
            itemView.textStockCount.text = product.stock?.getNumberFormatted()
            itemView.textStockCount.show()
            itemView.textStock.show()
        } else {
            itemView.textStockCount.hide()
            itemView.textStock.hide()
        }
    }

    private fun showProductLabel(product: ProductViewModel) {
        itemView.labelBanned.showWithCondition(product.isViolation())
        itemView.labelInactive.showWithCondition(product.isInactive())
        itemView.labelActive.showWithCondition(product.isActive())
    }

    private fun showVariantLabel(product: ProductViewModel) {
        itemView.labelVariant.showWithCondition(product.isVariant())
    }

    private fun showProductButton(product: ProductViewModel) {
        if(product.multiSelectActive) {
            itemView.btnContactCS.hide()
            itemView.btnEditVariant.hide()
            itemView.btnEditPrice.hide()
            itemView.btnEditStock.hide()
            itemView.btnMoreOptions.hide()
        } else {
            itemView.btnContactCS.showWithCondition(product.isViolation())
            itemView.btnEditVariant.showWithCondition(product.isVariant() && product.isNotViolation())
            itemView.btnEditPrice.showWithCondition(product.isNotVariant() && product.isNotViolation())
            itemView.btnEditStock.showWithCondition(product.isNotVariant() && product.isNotViolation())
            itemView.btnMoreOptions.show()
        }
    }

    private fun showStockHintImage(product: ProductViewModel) {
        itemView.imageStockInformation
            .showWithCondition(product.isEmpty() && product.isNotViolation())
    }

    private fun showProductImage(product: ProductViewModel) {
        loadImageFitCenter(itemView.context, itemView.imageProduct, product.imageUrl)
    }

    private fun setOnClickListeners(product: ProductViewModel) {
        setOnItemClickListener(product)
        itemView.checkBoxSelect.setOnClickListener { onClickCheckBox() }
        itemView.btnMoreOptions.setOnClickListener { listener.onClickMoreOptionsButton(product) }
        itemView.imageStockInformation.setOnClickListener { listener.onClickStockInformation() }
        itemView.btnEditPrice.setOnClickListener { listener.onClickEditPriceButton(product) }
        itemView.btnEditStock.setOnClickListener { listener.onClickEditStockButton(product) }
        itemView.btnEditVariant.setOnClickListener { listener.onClickEditVariantButton(product) }
        itemView.btnContactCS.setOnClickListener { listener.onClickContactCsButton(product)}
    }

    private fun setOnItemClickListener(product: ProductViewModel) {
        itemView.setOnClickListener {
            if (product.multiSelectActive) {
                toggleCheckBox()
                onClickCheckBox()
            } else {
                onClickProductItem(product)
            }
        }
    }

    private fun showProductCheckBox(product: ProductViewModel) {
        itemView.checkBoxSelect.isChecked = product.isChecked
        itemView.checkBoxSelect.showWithCondition(product.multiSelectActive)
    }

    private fun toggleCheckBox() {
        itemView.checkBoxSelect.apply { isChecked = !isChecked }
    }

    private fun onClickCheckBox() {
        val isChecked = itemView.checkBoxSelect.isChecked
        listener.onClickProductCheckBox(isChecked, adapterPosition)
    }

    private fun onClickProductItem(product: ProductViewModel) {
        listener.onClickProductItem(product)
    }

    interface ProductViewHolderView {
        fun onClickStockInformation()
        fun onClickMoreOptionsButton(product: ProductViewModel)
        fun onClickProductItem(product: ProductViewModel)
        fun onClickProductCheckBox(isChecked: Boolean, position: Int)
        fun onClickEditPriceButton(product: ProductViewModel)
        fun onClickEditStockButton(product: ProductViewModel)
        fun onClickEditVariantButton(product: ProductViewModel)
        fun onClickContactCsButton(product: ProductViewModel)
    }
}