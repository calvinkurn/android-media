package com.tokopedia.product_bundle.multiple.presentation.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.getCurrencyFormatted
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.product_bundle.common.customview.DiscountPriceView
import com.tokopedia.product_bundle.common.customview.SpinnerView
import com.tokopedia.product_bundle.multiple.presentation.adapter.ProductBundleDetailAdapter.ProductBundleDetailItemClickListener
import com.tokopedia.product_bundle.multiple.presentation.model.ProductBundleDetail
import com.tokopedia.product_service_widget.R
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

class ProductBundleDetailViewHolder(itemView: View, clickListener: ProductBundleDetailItemClickListener)
    : RecyclerView.ViewHolder(itemView) {

    private var productImageView: ImageUnify? = null
    private var productNameView: Typography? = null
    private var productVariantsView: SpinnerView? = null
    private var productPriceView: DiscountPriceView? = null
    private var tvVariantEmpty: View? = null

    init {
        this.productImageView = itemView.findViewById(R.id.riv_product_image)
        this.productNameView = itemView.findViewById(R.id.tv_product_name)
        this.tvVariantEmpty = itemView.findViewById(R.id.tv_variant_empty)
        this.productPriceView = itemView.findViewById(R.id.dpv_product_price)
        this.productVariantsView = itemView.findViewById(R.id.sv_product_variants)
        this.productNameView?.setOnClickListener {
            val productBundleDetailObj = productVariantsView?.getTag(R.id.product_bundle_detail_tag)
            productBundleDetailObj?.let { obj ->
                val productBundleDetail = obj as ProductBundleDetail
                clickListener.onProductNameViewClicked(productBundleDetail)
            }
        }
        this.productVariantsView?.setOnClickListener {
            val productBundleDetailObj = productVariantsView?.getTag(R.id.product_bundle_detail_tag)
            productBundleDetailObj?.let { obj ->
                val productBundleDetail = obj as ProductBundleDetail
                clickListener.onProductVariantSpinnerClicked(productBundleDetail)
            }
        }
    }

    fun bindData(bundleDetail: ProductBundleDetail, isVariantStockEmpty: Boolean) {
        val productName = itemView.context.getString(
            R.string.bundlewidget_product_title_format, bundleDetail.productQuantity, bundleDetail.productName)
        productImageView?.loadImage(bundleDetail.productImageUrl)
        productNameView?.text = MethodChecker.fromHtml(productName)
        // tag product bundle detail to product variant spinner and product name view
        productNameView?.setTag(R.id.product_bundle_detail_tag, bundleDetail)
        productVariantsView?.setTag(R.id.product_bundle_detail_tag, bundleDetail)
        // hide product variant dropbox when there is no selection
        if (bundleDetail.hasVariant) productVariantsView?.visible()
        else productVariantsView?.gone()
        // set selected variant text
        productVariantsView?.text = bundleDetail.selectedVariantText
        // set product price
        productPriceView?.apply {
            price = bundleDetail.bundlePrice.getCurrencyFormatted()
            slashPrice = bundleDetail.originalPrice.getCurrencyFormatted()
            context?.run {
                discountAmount = String.format(this.getString(R.string.text_discount_in_percentage), bundleDetail.discountAmount)
            }
        }
        // show variant empty label if stock is empty
        tvVariantEmpty?.isVisible = isVariantStockEmpty
    }
}
