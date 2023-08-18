package com.tokopedia.buyerorderdetail.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.databinding.ItemOwocProductBundlingListItemBinding
import com.tokopedia.buyerorderdetail.presentation.adapter.diffutil.OwocProductBundlingItemDiffUtilCallback
import com.tokopedia.buyerorderdetail.presentation.model.OwocProductListUiModel
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage

class OwocProductBundlingItemAdapter(
    private val listener: ViewHolder.Listener
) : RecyclerView.Adapter<OwocProductBundlingItemAdapter.ViewHolder>() {

    private val itemList = arrayListOf<OwocProductListUiModel.ProductUiModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemOwocProductBundlingListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding, listener)
    }

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList.getOrNull(position))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position)
        } else {
            payloads.firstOrNull()?.let {
                if (it is Pair<*, *>) {
                    val (oldItem, newItem) = it
                    if (oldItem is OwocProductListUiModel.ProductUiModel &&
                        newItem is OwocProductListUiModel.ProductUiModel
                    ) {
                        if (oldItem != newItem) {
                            holder.bind(newItem)
                        }
                    }
                }
            }
        }
    }

    fun setItems(newItemList: List<OwocProductListUiModel.ProductUiModel>) {
        if (newItemList.isEmpty()) return
        val diffCallback = OwocProductBundlingItemDiffUtilCallback(itemList, newItemList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        diffResult.dispatchUpdatesTo(this)
        itemList.clear()
        itemList.addAll(newItemList)
    }

    class ViewHolder(
        private val binding: ItemOwocProductBundlingListItemBinding,
        private val listener: Listener
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(model: OwocProductListUiModel.ProductUiModel?) {
            model?.let {
                with(binding) {
                    setBundleItemProductName(it.productName)
                    setBundleItemProductPriceQuantity(it.quantity, it.priceText)
                    setBundleItemThumbnail(it.productThumbnailUrl)
                }
                setItemOnClickListener(it.orderId, it.orderDetailId)
            }
        }

        private fun ItemOwocProductBundlingListItemBinding.setBundleItemThumbnail(thumbnailUrl: String) {
            ivItemOwocBundlingThumbnail.loadImage(thumbnailUrl)
        }

        private fun ItemOwocProductBundlingListItemBinding.setBundleItemProductName(productName: String) {
            tvItemOwocBundlingProductName.text = productName
        }

        private fun ItemOwocProductBundlingListItemBinding.setBundleItemProductPriceQuantity(quantity: Int, priceText: String) {
            tvItemOwocBundlingProductPriceQuantity.show()
            tvItemOwocBundlingProductPriceQuantity.text = itemView.context.getString(
                R.string.label_product_price_and_quantity,
                quantity,
                priceText
            )
        }

        private fun setItemOnClickListener(orderId: String, orderDetailId: String) {
            itemView.setOnClickListener {
                listener.onBundleItemClicked(orderId, orderDetailId)
            }
        }

        interface Listener {
            fun onBundleItemClicked(orderId: String, orderDetailId: String)
        }
    }
}
