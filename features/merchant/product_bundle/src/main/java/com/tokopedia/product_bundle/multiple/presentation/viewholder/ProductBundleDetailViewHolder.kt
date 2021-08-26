package com.tokopedia.product_bundle.multiple.presentation.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.product.detail.common.data.model.variant.ProductVariant
import com.tokopedia.product_bundle.R
import com.tokopedia.product_bundle.common.customview.DiscountPriceView
import com.tokopedia.product_bundle.common.customview.SpinnerView
import com.tokopedia.product_bundle.common.util.Utility
import com.tokopedia.product_bundle.common.customview.RoundedCornerImageView
import com.tokopedia.product_bundle.multiple.presentation.adapter.ProductBundleDetailAdapter.ProductBundleDetailItemClickListener
import com.tokopedia.product_bundle.multiple.presentation.model.ProductBundleDetail
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.currency.CurrencyFormatUtil
import kotlin.math.roundToInt

class ProductBundleDetailViewHolder(itemView: View, clickListener: ProductBundleDetailItemClickListener)
    : RecyclerView.ViewHolder(itemView) {

    private var productImageView: RoundedCornerImageView? = null
    private var productNameView: Typography? = null
    private var productVariantsView: SpinnerView? = null
    private var productPriceView: DiscountPriceView? = null

    init {
        this.productImageView = itemView.findViewById(R.id.riv_product_image)
        this.productNameView = itemView.findViewById(R.id.tv_product_name)
        this.productPriceView = itemView.findViewById(R.id.dpv_product_price)
        this.productVariantsView = itemView.findViewById(R.id.sv_product_variants)
        this.productVariantsView?.setOnClickListener {
            val productVariantObj = productVariantsView?.getTag(R.id.product_variant_tag)
            productVariantObj?.let { obj ->
                val productVariant = obj as ProductVariant
                clickListener.onProductVariantSpinnerClicked(productVariant)
            }
        }
    }

    fun bindData(bundleDetail: ProductBundleDetail) {
        productImageView?.loadImage(bundleDetail.productImageUrl)
        productNameView?.text = bundleDetail.productName
        // tag product variant to productVariantsView
        productVariantsView?.setTag(R.id.product_variant_tag, bundleDetail.productVariant)
        // hide product variant dropbox when there is no selection
        if (bundleDetail.hasVariant) productVariantsView?.visible()
        else productVariantsView?.gone()
        // set selected variant text
        productVariantsView?.text = bundleDetail.selectedVariantText
        // set product price
        productPriceView?.apply {
            price = CurrencyFormatUtil.convertPriceValueToIdrFormat(bundleDetail.bundlePrice, false)
            slashPrice = CurrencyFormatUtil.convertPriceValueToIdrFormat(bundleDetail.originalPrice, false)
            context?.run {
                discountAmount = String.format(this.getString(R.string.text_discount_in_percentage), bundleDetail.discountAmount)
            }
        }
    }
}