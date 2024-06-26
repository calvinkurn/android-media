package com.tokopedia.sellerorder.detail.presentation.adapter

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.imageassets.TokopediaImageUrl
import com.tokopedia.imageassets.utils.loadProductImage
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.clearCustomTarget
import com.tokopedia.media.loader.clearImage
import com.tokopedia.media.loader.utils.MediaBitmapEmptyTarget
import com.tokopedia.order_management_common.presentation.viewholder.AddOnSummaryViewHolder
import com.tokopedia.order_management_common.presentation.viewholder.AddOnViewHolder
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.databinding.ItemSomProductBundlingProductBinding
import com.tokopedia.sellerorder.detail.presentation.adapter.factory.SomDetailAdapterFactoryImpl
import com.tokopedia.sellerorder.detail.presentation.model.ProductBundleUiModel
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created By @ilhamsuaib on 05/07/21
 */

class SomDetailProductBundlingAdapter(
    private val actionListener: SomDetailAdapterFactoryImpl.ActionListener?,
    private val addOnListener: AddOnViewHolder.Listener,
    private val recyclerViewSharedPool: RecyclerView.RecycledViewPool
) : RecyclerView.Adapter<SomDetailProductBundlingAdapter.ViewHolder>() {

    var products = emptyList<ProductBundleUiModel.ProductUiModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_som_product_bundling_product, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(products[position])
    }

    override fun onViewRecycled(holder: ViewHolder) {
        holder.onViewRecycled()
    }

    override fun getItemCount(): Int = products.size

    inner class ViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView),
        AddOnSummaryViewHolder.Delegate.Mediator,
        AddOnSummaryViewHolder.Delegate by AddOnSummaryViewHolder.Delegate.Impl() {

        private val binding by viewBinding<ItemSomProductBundlingProductBinding>()

        private var productImageLoadTarget: MediaBitmapEmptyTarget<Bitmap>? = null

        init {
            registerAddOnSummaryDelegate(this)
        }

        override fun getAddOnSummaryLayout(): View? {
            return itemView.findViewById(R.id.layoutProductBundleAddOn)
        }

        override fun getRecycleViewSharedPool(): RecyclerView.RecycledViewPool? {
            return recyclerViewSharedPool
        }

        override fun getAddOnSummaryListener(): AddOnViewHolder.Listener {
            return addOnListener
        }

        fun bind(product: ProductBundleUiModel.ProductUiModel) {
            binding?.run {
                root.setOnClickListener {
                    actionListener?.onClickProduct(product.detail.orderDetailId.toLongOrZero())
                }
                productImageLoadTarget = imgSomBundleProduct.loadProductImage(
                    url = product.detail.thumbnail,
                    archivedUrl = TokopediaImageUrl.IMG_ARCHIVED_PRODUCT_SMALL
                ) { productImageLoadTarget = null }
                tvSomBundleProductName.text = product.detail.name
                tvSomBundlePrice.text =
                    StringBuilder("${product.detail.quantity} x ${product.detail.priceText}")
                if (product.detail.note.isNotEmpty()) {
                    tvSomBundleNotes.visible()
                    tvSomBundleNotes.text = product.detail.note.replace(
                        "\\n",
                        System.getProperty("line.separator") ?: ""
                    )
                } else {
                    tvSomBundleNotes.gone()
                }
            }
            bindAddonSummary(product.addOnSummaryUiModel)
        }

        fun onViewRecycled() {
            binding?.imgSomBundleProduct.clearImage()
            binding?.imgSomBundleProduct.clearCustomTarget(productImageLoadTarget)
        }
    }
}
