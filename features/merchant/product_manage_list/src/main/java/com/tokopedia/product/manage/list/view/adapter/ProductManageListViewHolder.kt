package com.tokopedia.product.manage.list.view.adapter

import android.view.View
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.holder.BaseCheckableViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.product.manage.item.common.util.FreeReturnTypeDef
import com.tokopedia.product.manage.list.R
import com.tokopedia.product.manage.list.constant.ProductManagePreOrderDef
import com.tokopedia.product.manage.list.constant.ProductManageStockDef
import com.tokopedia.product.manage.list.constant.ProductManageWholesaleDef
import com.tokopedia.product.manage.list.constant.option.StatusProductOption
import com.tokopedia.product.manage.list.view.model.ProductManageViewModel
import com.tokopedia.seller.product.common.utils.CurrencyUtils

class ProductManageListViewHolder(view: View, checkableListener: CheckableInteractionListener,
                                  val listener: ProductManageViewHolderListener) :
        BaseCheckableViewHolder<ProductManageViewModel>(view, checkableListener), CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    companion object {
        @LayoutRes
        var LAYOUT = R.layout.item_manage_product_list
    }

    private val productImageView: ImageView = view.findViewById(R.id.image_view_product)
    private val titleTextView: TextView = view.findViewById(R.id.text_view_title);
    private val stockTextView: TextView = view.findViewById(R.id.text_view_stock);
    private val priceTextView: TextView = view.findViewById(R.id.text_view_price);
    private val featuredImageView: ImageView = view.findViewById(R.id.image_view_featured);
    private val cashbackTextView: TextView = view.findViewById(R.id.text_view_cashback);
    private val wholesaleTextView: TextView = view.findViewById(R.id.text_view_wholesale);
    private val preOrderTextView: TextView = view.findViewById(R.id.text_view_pre_order);
    private val freeReturnImageView: ImageView = view.findViewById(R.id.image_view_free_return);
    private val optionImageButton: View = view.findViewById(R.id.image_button_option);
    private val checkBoxProduct: CheckBox = view.findViewById(R.id.check_box_product);
    private val tagEmptyStock: TextView = view.findViewById(R.id.tag_empty_product);
    private val viewSuperVision: View = view.findViewById(R.id.view_product_on_supervision);
    private val textViewVariant: TextView = view.findViewById(R.id.text_view_variant);

    interface ProductManageViewHolderListener {
        fun onClickOptionItem(productManageViewModel: ProductManageViewModel)

        fun onProductClicked(productManageViewModel: ProductManageViewModel)
    }

    override fun getCheckable(): CompoundButton = checkBoxProduct

    override fun onClick(v: View?) {
        toggle()
    }

    override fun bind(productManageViewModel: ProductManageViewModel) {
        super.bind(productManageViewModel)
        ImageHandler.loadImageRounded2(
                productImageView.context,
                productImageView,
                productManageViewModel.imageUrl
        )

        val statusUnderSupervision = productManageViewModel.productStatus == StatusProductOption.UNDER_SUPERVISION
        val statusStockEmpty = productManageViewModel.productStatus == StatusProductOption.EMPTY
        val isProductVariant = productManageViewModel.isProductVariant

        optionImageButton.setOnClickListener {
            listener.onClickOptionItem(productManageViewModel)
        }

        itemView.setOnClickListener {
            listener.onProductClicked(productManageViewModel)
        }

        titleTextView.text = MethodChecker.fromHtml(productManageViewModel.productName)
        priceTextView.text = priceTextView.context.getString(
                R.string.pml_price_format_text, productManageViewModel.productCurrencySymbol,
                CurrencyUtils.getPriceFormatted(productManageViewModel.productCurrencyId,
                        productManageViewModel.productPricePlain)
        )

        if (productManageViewModel.productCashback > 0) {
            cashbackTextView.text = cashbackTextView.context.getString(
                    R.string.product_manage_item_cashback, productManageViewModel.productCashback)
            cashbackTextView.visibility = View.VISIBLE
        } else {
            cashbackTextView.visibility = View.GONE
        }

        tagEmptyStock.visibility = if (statusStockEmpty) View.VISIBLE else View.GONE
        viewSuperVision.visibility = if (statusUnderSupervision) View.VISIBLE else View.GONE
        preOrderTextView.visibility = if (productManageViewModel.productPreorder == ProductManagePreOrderDef.PRE_ORDER) View.VISIBLE else View.GONE
        freeReturnImageView.visibility = if (productManageViewModel.productReturnable == FreeReturnTypeDef.TYPE_ACTIVE) View.VISIBLE else View.GONE
        wholesaleTextView.visibility = if (productManageViewModel.productWholesale == ProductManageWholesaleDef.WHOLESALE) View.VISIBLE else View.GONE

        if (!statusStockEmpty && productManageViewModel.productUsingStock == ProductManageStockDef.USING_STOCK) {
            stockTextView.visibility = View.VISIBLE
            if (productManageViewModel.isProductVariant) {
                stockTextView.text = itemView.context.getString(R.string.pml_product_variant_stock_limited)
            } else {
                stockTextView.text = itemView.context.getString(R.string.product_manage_label_stock_counter, productManageViewModel.productStock)
            }
        } else {
            stockTextView.visibility = View.GONE
        }

        if (isProductVariant) {
            textViewVariant.visibility = View.VISIBLE
        } else {
            textViewVariant.visibility = View.GONE
        }
        checkBoxProduct.isEnabled = !statusUnderSupervision

        if (productManageViewModel.isFeatureProduct) {
            featuredImageView.visibility = View.VISIBLE
        } else {
            featuredImageView.visibility = View.GONE
        }

    }
}