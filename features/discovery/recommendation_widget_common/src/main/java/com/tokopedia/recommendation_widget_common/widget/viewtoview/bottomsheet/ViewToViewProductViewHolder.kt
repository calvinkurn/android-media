package com.tokopedia.recommendation_widget_common.widget.viewtoview.bottomsheet

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.recommendation_widget_common.R
import com.tokopedia.recommendation_widget_common.databinding.ItemViewToViewBinding
import com.tokopedia.utils.view.binding.viewBinding

class ViewToViewProductViewHolder(
    private val listener: ViewToViewListener,
    view: View,
) : RecyclerView.ViewHolder(view) {

    private var binding: ItemViewToViewBinding? by viewBinding()

    fun bind(data: ViewToViewDataModel) {
        itemView.addOnImpressionListener(data.recommendationItem) {
            listener.onProductImpressed(data, bindingAdapterPosition)
        }
        val binding = binding ?: return
        with(binding.root) {
            setProductModel(data.productModel)
            setOnClickListener {
                listener.onProductClicked(data, bindingAdapterPosition)
            }
            setAddToCartOnClickListener {
                listener.onAddToCartClicked(data, bindingAdapterPosition)
            }
            setAddVariantClickListener {
                listener.onAddToCartClicked(data, bindingAdapterPosition)
            }
        }
    }

    fun type() = LAYOUT

    companion object {
        val LAYOUT = R.layout.item_view_to_view
    }
}
