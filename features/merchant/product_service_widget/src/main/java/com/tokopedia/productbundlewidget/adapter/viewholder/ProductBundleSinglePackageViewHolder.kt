package com.tokopedia.productbundlewidget.adapter.viewholder

import android.graphics.PorterDuff
import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product_service_widget.R
import com.tokopedia.product_service_widget.databinding.ItemProductbundleSingleProductBinding
import com.tokopedia.productbundlewidget.model.BundleDetailUiModel
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.ColorMode
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class ProductBundleSinglePackageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_productbundle_single_product
    }

    private var viewBinding: ItemProductbundleSingleProductBinding? by viewBinding()

    fun bind(
        singleBundleDetailVariant: BundleDetailUiModel,
        isOverrideWidgetTheme: Boolean
    ) {
        viewBinding?.singleBundleVariantPackageContainer?.apply {
            text = singleBundleDetailVariant.minOrderWording
            buttonType = if (singleBundleDetailVariant.isSelected) UnifyButton.Type.MAIN else UnifyButton.Type.ALTERNATE

            if (isOverrideWidgetTheme) {
                if (singleBundleDetailVariant.isSelected) {
                    background.setTint(ContextCompat.getColor(context, R.color.dms_static_GN50))
                    background.setTintMode(PorterDuff.Mode.MULTIPLY)
                    applyColorMode(colorMode = ColorMode.LIGHT_MODE)
                } else {
                    val textColor = ContextCompat.getColor(context, R.color.dms_high_emphasis)
                    setTextColor(textColor)
                    applyColorMode(colorMode = ColorMode.LIGHT_MODE)
                }
            } else {
                if (singleBundleDetailVariant.isSelected) {
                    background.setTint(ContextCompat.getColor(context, unifyprinciplesR.color.Unify_GN50))
                    background.setTintMode(PorterDuff.Mode.MULTIPLY)
                } else {
                    val textColor = ContextCompat.getColor(context, unifyprinciplesR.color.Unify_NN950)
                    setTextColor(textColor)
                }
            }
        }
    }
}
