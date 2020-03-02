package com.tokopedia.product.manage.feature.list.view.adapter

import android.view.View
import android.widget.CompoundButton
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.holder.BaseCheckableViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler.loadImageFitCenter
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.feature.list.view.model.ProductViewModel
import kotlinx.android.synthetic.main.item_manage_product_list.view.*

class ProductViewHolder(
    view: View,
    checkableListener: CheckableInteractionListener,
    val listener: ProductViewHolderView
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

        setOnClickListeners(product)
    }

    override fun onClick(v: View?) = toggle()

    override fun getCheckable(): CompoundButton? {
        return itemView.checkBoxSelect
    }

    private fun setTitleAndPrice(product: ProductViewModel) {
        itemView.textTitle.text = product.title
        itemView.textPrice.text = product.price
    }

    private fun showProductStock(product: ProductViewModel) {
        if(product.isNotVariant()) {
            val productStock = itemView.context
                .getString(R.string.product_manage_stock_format, product.stock)
            itemView.textStock.text = productStock
            itemView.textStock.show()
        }
    }

    private fun showProductLabel(product: ProductViewModel) {
        when {
            product.isBanned() -> itemView.labelBanned.show()
            product.isInactive() -> itemView.labelInactive.show()
            product.isActive() -> itemView.labelActive.show()
        }
    }

    private fun showVariantLabel(product: ProductViewModel) {
        if(product.isVariant()) itemView.labelVariant.show()
    }

    private fun showProductButton(product: ProductViewModel) {
        when {
            product.isBanned() -> itemView.btnContactCS.show()
            product.isVariant() -> itemView.btnEditVariant.show()
            else -> showEditPriceAndStockBtn()
        }
    }

    private fun showEditPriceAndStockBtn() {
        itemView.btnEditPrice.show()
        itemView.btnEditStock.show()
    }

    private fun showStockHintImage(product: ProductViewModel) {
        if(product.isStockEmpty()) itemView.imageStockInformation.show()
    }

    private fun showProductImage(product: ProductViewModel) {
        loadImageFitCenter(itemView.context, itemView.imageProduct, product.imageUrl)
    }

    private fun setOnClickListeners(product: ProductViewModel) {
        itemView.setOnClickListener { listener.onClickProductItem(product) }
        itemView.btnMoreOptions.setOnClickListener { listener.onClickMoreOptionsButton(product) }
        itemView.imageStockInformation.setOnClickListener { listener.onClickStockInformation() }
    }
    
    interface ProductViewHolderView {
        fun onClickStockInformation()
        fun onClickMoreOptionsButton(productManageViewModel: ProductViewModel)
        fun onClickProductItem(productManageViewModel: ProductViewModel)
    }
}