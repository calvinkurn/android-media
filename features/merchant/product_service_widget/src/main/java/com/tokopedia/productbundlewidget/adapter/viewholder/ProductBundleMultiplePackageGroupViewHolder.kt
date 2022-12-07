package com.tokopedia.productbundlewidget.adapter.viewholder

import android.graphics.PorterDuff
import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.media.loader.loadImage
import com.tokopedia.product_service_widget.R
import com.tokopedia.product_service_widget.databinding.ItemProductbundleMultipleProductGroupBinding
import com.tokopedia.productbundlewidget.model.BundleProductUiModel
import com.tokopedia.utils.view.binding.viewBinding

class ProductBundleMultiplePackageGroupViewHolder(
    itemView: View
): RecyclerView.ViewHolder(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_productbundle_multiple_product_group
    }

    private var viewBinding: ItemProductbundleMultipleProductGroupBinding? by viewBinding()

    fun bind(
        item: List<BundleProductUiModel>,
        onMoreProductClick: () -> Unit
    ) {
        val context = itemView.context
        val bundleProductItem = item.firstOrNull()
        val overlayColor = MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_Static_White_32)
        viewBinding?.apply {
            tvBundleProductCount.text = context.getString(R.string.bundlewidget_other_product_count_format, item.size)
            ivBundleProductMultiple.loadImage(bundleProductItem?.productImageUrl)
            ivBundleProductMultiple.setColorFilter(overlayColor, PorterDuff.Mode.MULTIPLY)
        }
        itemView.setOnClickListener { onMoreProductClick.invoke() }
    }
}
