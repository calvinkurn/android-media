package com.tokopedia.productbundlewidget.adapter.viewholder

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.GradientDrawable
import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.media.loader.loadImage
import com.tokopedia.product_service_widget.R
import com.tokopedia.product_service_widget.databinding.ItemProductbundleMultipleProductGroupBinding
import com.tokopedia.productbundlewidget.model.BundleProductUiModel
import com.tokopedia.unifycomponents.dpToPx
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class ProductBundleMultiplePackageGroupViewHolder(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_productbundle_multiple_product_group
    }

    private var viewBinding: ItemProductbundleMultipleProductGroupBinding? by viewBinding()

    fun bind(
        item: List<BundleProductUiModel>,
        onMoreProductClick: () -> Unit,
        isOverrideWidgetTheme: Boolean
    ) {
        val context = itemView.context
        val bundleProductItem = item.firstOrNull()
        val overlayColor = MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_Static_White_32)
        viewBinding?.apply {
            tvBundleProductCount.text = context.getString(R.string.bundlewidget_other_product_count_format, item.size)
            ivBundleProductMultiple.loadImage(bundleProductItem?.productImageUrl)
            ivBundleProductMultiple.setColorFilter(overlayColor, PorterDuff.Mode.MULTIPLY)
        }
        isOverrideWidgetTheme(isOverrideWidgetTheme)
        itemView.setOnClickListener { onMoreProductClick.invoke() }
    }

    private fun isOverrideWidgetTheme(isOverrideWidgetTheme: Boolean) {
        if (isOverrideWidgetTheme) {
            val strokeWidth = 1
            viewBinding?.let {
                it.tvBundleProductCount.setTextColor(ContextCompat.getColor(itemView.context, unifyprinciplesR.color.Unify_Static_White))
                it.tvBundleProductDesc.setTextColor(ContextCompat.getColor(itemView.context, unifyprinciplesR.color.Unify_Static_White))

                val bgDrawable = ContextCompat.getDrawable(itemView.context, R.drawable.bg_productbundle_image)
                if (bgDrawable is GradientDrawable) {
                    bgDrawable.apply {
                        setColor(MethodChecker.getColor(itemView.context, R.color.dms_high_emphasis))
                        setStroke(1.toPx(), MethodChecker.getColor(itemView.context, R.color.dms_static_light_RN500))
                        cornerRadius = 7f.dpToPx()
                    }
                    it.layoutBundleMultipleImage.background = bgDrawable
                }

//                val shapeDrawable = it.layoutBundleMultipleImage.background as GradientDrawable
//                shapeDrawable.setColor(ContextCompat.getColor(itemView.context, R.color.dms_high_emphasis))
//                shapeDrawable.cornerRadius = 7f.dpToPx()
//                shapeDrawable.setStroke(strokeWidth, Color.parseColor("#F0F3F7"))


//                val newStrokeDrawable = GradientDrawable()
//                newStrokeDrawable.shape = GradientDrawable.RECTANGLE
//                newStrokeDrawable.setStroke(strokeWidth, Color.parseColor("#F0F3F7"))
//                newStrokeDrawable.setColor(Color.parseColor("#212121"))
//                newStrokeDrawable.cornerRadius = 7f.dpToPx()
//                it.layoutBundleMultipleImage.background = newStrokeDrawable
            }
        }
    }
}
