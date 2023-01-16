package com.tokopedia.recommendation_widget_common.widget.viewtoview.bottomsheet

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.productcard.ATCNonVariantListener
import com.tokopedia.recommendation_widget_common.R
import com.tokopedia.recommendation_widget_common.databinding.ItemViewToViewBinding
import com.tokopedia.recommendation_widget_common.databinding.ItemViewToViewShimmeringBinding
import com.tokopedia.utils.view.binding.viewBinding

sealed class ViewToViewViewHolder(
    view: View,
) : RecyclerView.ViewHolder(view) {

    abstract fun bind(element: ViewToViewDataModel?)
    abstract fun type() : Int

    class Loading(view: View): ViewToViewViewHolder(view) {
        private var binding: ItemViewToViewShimmeringBinding? by viewBinding()

        override fun bind(element: ViewToViewDataModel?) {
            if(element is ViewToViewDataModel.Loading) bind(element)
        }

        fun bind(data: ViewToViewDataModel.Loading) {
            val button = binding?.button ?: return
            if(data.hasAtc) button.show() else button.hide()
        }

        override fun type() = LAYOUT

        companion object {
            val LAYOUT = R.layout.item_view_to_view_shimmering
        }
    }

    class Product(
        private val listener: ViewToViewListener,
        view: View,
    ): ViewToViewViewHolder(view) {
        private var binding: ItemViewToViewBinding? by viewBinding()

        override fun bind(element: ViewToViewDataModel?) {
            if(element is ViewToViewDataModel.Product) bind(element)
        }

        fun bind(data: ViewToViewDataModel.Product) {
            val binding = binding ?: return
            with(binding.root) {
                setProductModel(data.productModel)
                setAddToCartNonVariantClickListener(object : ATCNonVariantListener {
                    override fun onQuantityChanged(quantity: Int) {

                    }
                })
                addOnImpressionListener(data, object : ViewHintListener {
                    override fun onViewHint() {
                        listener.onProductImpressed(data, bindingAdapterPosition)
                    }
                })
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

        override fun type() = LAYOUT

        companion object {
            val LAYOUT = R.layout.item_view_to_view
        }
    }
}
