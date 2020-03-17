package com.tokopedia.product.manage.feature.list.view.adapter.viewholder

import android.view.View
import android.widget.CompoundButton
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.holder.BaseCheckableViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler.loadImageFitCenter
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.feature.list.view.model.ProductViewModel
import kotlinx.android.synthetic.main.item_manage_product_list.view.*

class ProductViewHolder(
    view: View,
    checkableListener: CheckableInteractionListener,
    private val listener: ProductViewHolderView
): BaseCheckableViewHolder<ProductViewModel>(view, checkableListener),
    CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    companion object {
        @LayoutRes
        var LAYOUT = R.layout.item_manage_product_list
    }

    override fun bind(product: ProductViewModel) {
        super.bind(product)

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

    override fun onClick(v: View?) = toggle()

    override fun getCheckable(): CompoundButton? {
        return itemView.checkBoxSelect
    }

    private fun setTitleAndPrice(product: ProductViewModel) {
        itemView.textTitle.text = product.title
        itemView.textPrice.text = product.priceFormatted
    }

    private fun showProductStock(product: ProductViewModel) {
        if(product.isNotVariant()) {
            val productStock = itemView.context
                .getString(R.string.product_manage_stock_format, product.stock)
            itemView.textStock.text = productStock
            itemView.textStock.show()
        } else {
            itemView.textStock.hide()
        }
    }

    private fun showProductLabel(product: ProductViewModel) {
        itemView.labelBanned.showViewIf(product.isViolation())
        itemView.labelInactive.showViewIf(product.isInactive())
        itemView.labelActive.showViewIf(product.isActive())
    }

    private fun showVariantLabel(product: ProductViewModel) {
        itemView.labelVariant.showViewIf(product.isVariant())
    }

    private fun showProductButton(product: ProductViewModel) {
        if(product.multiSelectActive) {
            itemView.btnContactCS.hide()
            itemView.btnEditVariant.hide()
            itemView.btnEditPrice.hide()
            itemView.btnEditStock.hide()
            itemView.btnMoreOptions.hide()
        } else {
            itemView.btnContactCS.showViewIf(product.isViolation())
            itemView.btnEditVariant.showViewIf(product.isVariant())
            itemView.btnEditPrice.showViewIf(product.isNotVariant())
            itemView.btnEditStock.showViewIf(product.isNotVariant())
            itemView.btnMoreOptions.show()
        }
    }

    private fun showStockHintImage(product: ProductViewModel) {
        itemView.imageStockInformation.showViewIf(product.isEmpty())
    }

    private fun showProductImage(product: ProductViewModel) {
        loadImageFitCenter(itemView.context, itemView.imageProduct, product.imageUrl)
    }

    private fun setOnClickListeners(product: ProductViewModel) {
        itemView.setOnClickListener { listener.onClickProductItem(product) }
        itemView.btnMoreOptions.setOnClickListener { listener.onClickMoreOptionsButton(product) }
        itemView.imageStockInformation.setOnClickListener { listener.onClickStockInformation() }
        itemView.btnEditPrice.setOnClickListener { listener.onClickEditPriceButton(product) }
        itemView.btnEditStock.setOnClickListener { listener.onClickEditStockButton(product) }
        itemView.btnEditVariant.setOnClickListener { listener.onClickEditVariantButton(product) }
        itemView.btnContactCS.setOnClickListener { listener.onClickContactCsButton(product)}
    }

    private fun showProductCheckBox(product: ProductViewModel) {
        itemView.checkBoxSelect.showViewIf(product.multiSelectActive)
    }

    private fun View.showViewIf(predicate: Boolean) {
        if(predicate) show() else hide()
    }

    interface ProductViewHolderView {
        fun onClickStockInformation()
        fun onClickMoreOptionsButton(product: ProductViewModel)
        fun onClickProductItem(product: ProductViewModel)
        fun onClickEditPriceButton(product: ProductViewModel)
        fun onClickEditStockButton(product: ProductViewModel)
        fun onClickEditVariantButton(product: ProductViewModel)
        fun onClickContactCsButton(product: ProductViewModel)
    }
}