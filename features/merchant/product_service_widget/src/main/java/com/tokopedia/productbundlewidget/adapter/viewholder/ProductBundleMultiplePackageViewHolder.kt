package com.tokopedia.productbundlewidget.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
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
        item: BundleProductUiModel,
        onViewImpression: (position: Int) -> Unit,
        onClickImpression: (position: Int) -> Unit,
        isOverrideWidgetTheme: Boolean
    ) {
        overrideWidgetTheme(isOverrideWidgetTheme = isOverrideWidgetTheme)
        val context = itemView.context
        val productName = context.getString(
            R.string.bundlewidget_product_title_format, item.productCount, item.productName)
        typographyBundleProductName?.text = HtmlLinkHelper(context, productName).spannedString
        imageBundleProduct?.loadImage(item.productImageUrl)
        itemView.addOnImpressionListener(item) { onViewImpression.invoke(absoluteAdapterPosition) }
        itemView.setOnClickListener { onClickImpression.invoke(absoluteAdapterPosition) }
    }

    private fun overrideWidgetTheme(isOverrideWidgetTheme: Boolean) {
        if (isOverrideWidgetTheme) {
            typographyBundleProductName?.setTextColor(ContextCompat.getColor(itemView.context, R.color.dms_high_emphasis))
        }
    }
}
