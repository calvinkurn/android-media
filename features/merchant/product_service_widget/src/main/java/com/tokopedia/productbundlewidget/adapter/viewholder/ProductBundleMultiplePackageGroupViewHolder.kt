package com.tokopedia.productbundlewidget.adapter.viewholder

import android.graphics.PorterDuff
import android.view.View
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.media.loader.loadImage
import com.tokopedia.product_service_widget.R
import com.tokopedia.product_service_widget.databinding.ItemProductbundleMultipleProductGroupBinding
import com.tokopedia.productbundlewidget.model.BundleProductUiModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.binding.viewBinding

class ProductBundleMultiplePackageGroupViewHolder(
    itemView: View
): RecyclerView.ViewHolder(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_productbundle_multiple_product_group
    }

    private var viewBinding: ItemProductbundleMultipleProductGroupBinding? by viewBinding()
    private var tvBundleProductCountz: Typography? = null
    private var imageBundleProduct: ImageUnify? = null
    private var bundleProductsContainer: ConstraintLayout? = null

    init {
        viewBinding?.apply {
            tvBundleProductCountz = tvBundleProductCount
            imageBundleProduct = ivBundleProductMultiple
            bundleProductsContainer = multipleBundleProductsContainer
        }
    }

    fun bind(
        item: List<BundleProductUiModel>,
        onViewImpression: (position: Int) -> Unit,
        onClickImpression: (position: Int) -> Unit
    ) {
        val context = itemView.context
        val bundleProductItem = item.firstOrNull()
        tvBundleProductCountz?.text = context.getString(R.string.bundlewidget_other_product_count_format, item.size)
        imageBundleProduct?.loadImage(bundleProductItem?.productImageUrl)
        val black = MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN500)
        imageBundleProduct?.setColorFilter(black, PorterDuff.Mode.MULTIPLY)

        //itemView.addOnImpressionListener(bundleProductItem) { onViewImpression.invoke(adapterPosition) }
        itemView.setOnClickListener { onClickImpression.invoke(adapterPosition) }
    }
}
