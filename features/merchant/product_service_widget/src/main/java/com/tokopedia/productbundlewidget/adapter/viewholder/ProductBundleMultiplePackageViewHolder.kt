package com.tokopedia.productbundlewidget.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.media.loader.loadImage
import com.tokopedia.product_service_widget.R
import com.tokopedia.product_service_widget.databinding.ItemProductbundleMultipleProductBinding
import com.tokopedia.productbundlewidget.model.BundleProductUiModel
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.binding.viewBinding

class ProductBundleMultiplePackageViewHolder(
    itemView: View
): RecyclerView.ViewHolder(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_productbundle_multiple_product
    }

    private var viewBinding: ItemProductbundleMultipleProductBinding? by viewBinding()
    private var typographyBundleProductName: Typography? = null
    private var imageBundleProduct: ImageUnify? = null
    private var bundleProductsContainer: ConstraintLayout? = null

    init {
        viewBinding?.apply {
            typographyBundleProductName = tvBundleProductMultipleName
            imageBundleProduct = ivBundleProductMultiple
            bundleProductsContainer = multipleBundleProductsContainer
        }
    }

    fun bind(
        item: Pair<Int, BundleProductUiModel>,
        onViewImpression: (position: Int) -> Unit,
        onClickImpression: (position: Int) -> Unit
    ) {
        val context = itemView.context
        val bundleProductItem = item.second
        val productName = context.getString(
            R.string.bundlewidget_product_title_format, item.first, bundleProductItem.productName)
        typographyBundleProductName?.text = HtmlLinkHelper(context, productName).spannedString
        imageBundleProduct?.loadImage(bundleProductItem.productImageUrl)
        itemView.addOnImpressionListener(bundleProductItem) { onViewImpression.invoke(adapterPosition) }
        itemView.setOnClickListener { onClickImpression.invoke(adapterPosition) }
    }
}
