package com.tokopedia.productbundlewidget.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product_service_widget.R
import com.tokopedia.product_service_widget.databinding.ItemProductbundleSingleProductBinding
import com.tokopedia.productbundlewidget.model.BundleDetailUiModel
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.utils.view.binding.viewBinding

class ProductBundleSinglePackageViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_productbundle_single_product
    }

    private var viewBinding: ItemProductbundleSingleProductBinding? by viewBinding()

    fun bind(singleBundleDetailVariant: BundleDetailUiModel) {
        viewBinding?.singleBundleVariantPackageContainer?.apply {
            text = singleBundleDetailVariant.minOrderWording
            buttonType = if (singleBundleDetailVariant.isSelected)
                UnifyButton.Type.MAIN else UnifyButton.Type.ALTERNATE
            setTextColor(ContextCompat.getColor(context, com.tokopedia.unifycomponents.R.color.Unify_NN900))
        }
    }
}
