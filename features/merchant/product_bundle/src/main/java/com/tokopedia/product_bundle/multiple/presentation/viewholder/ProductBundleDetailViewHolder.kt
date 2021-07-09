package com.tokopedia.product_bundle.multiple.presentation.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.product_bundle.R
import com.tokopedia.product_bundle.common.customview.DiscountPriceView
import com.tokopedia.product_bundle.common.customview.SpinnerView
import com.tokopedia.product_bundle.common.view.RoundedCornerImageView
import com.tokopedia.product_bundle.multiple.presentation.adapter.ProductBundleDetailAdapter.ProductBundleDetailItemClickListener
import com.tokopedia.product_bundle.multiple.presentation.model.ProductBundleDetail
import com.tokopedia.unifyprinciples.Typography

class ProductBundleDetailViewHolder(itemView: View, clickListener: ProductBundleDetailItemClickListener)
    : RecyclerView.ViewHolder(itemView) {

    private var productImageView: RoundedCornerImageView? = null
    private var productNameView: Typography? = null
    private var productVariantsView: SpinnerView? = null
    private var productPriceView: DiscountPriceView? = null

    init {
        productImageView = itemView.findViewById(R.id.riv_product_image)
        productNameView = itemView.findViewById(R.id.tv_product_name)
        productVariantsView = itemView.findViewById(R.id.sv_product_variants)
        productPriceView = itemView.findViewById(R.id.dpv_product_price)
    }

    fun bindData(bundleDetail: ProductBundleDetail) {
        productImageView?.loadImage(bundleDetail.productImageUrl)
        productNameView?.text = bundleDetail.productName

        productPriceView?.apply {
            val context = itemView.context
            price = String.format(context.getString(R.string.text_price_in_rupiah), bundleDetail.bundlePrice)
            discountAmount = String.format(context.getString(R.string.text_discount_in_percentage), bundleDetail.discountAmount)
            slashPrice = String.format(context.getString(R.string.text_price_in_rupiah), bundleDetail.originalPrice)
        }
    }
}