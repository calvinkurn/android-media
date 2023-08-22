package com.tokopedia.buyerorderdetail.presentation.adapter

import android.animation.LayoutTransition
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.common.constants.BuyerOrderDetailActionButtonKey
import com.tokopedia.buyerorderdetail.presentation.adapter.diffutil.ProductBundlingItemDiffUtilCallback
import com.tokopedia.buyerorderdetail.presentation.model.ProductListUiModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

class ProductBundlingItemAdapter(
    private val listener: ViewHolder.Listener
) : RecyclerView.Adapter<ProductBundlingItemAdapter.ViewHolder>() {

    private val itemList = arrayListOf<ProductListUiModel.ProductUiModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_buyer_order_detail_product_bundling_list_item, parent, false)
        return ViewHolder(view, listener)
    }

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList.getOrNull(position))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        holder.bind(itemList.getOrNull(position), payloads)
    }

    fun setItems(newItemList: List<ProductListUiModel.ProductUiModel>) {
        val diffCallback = ProductBundlingItemDiffUtilCallback(itemList, newItemList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        diffResult.dispatchUpdatesTo(this)
        itemList.clear()
        itemList.addAll(newItemList)
    }

    class ViewHolder(
        itemView: View,
        private val listener: Listener
    ) : AbstractViewHolder<ProductListUiModel.ProductUiModel>(itemView) {

        private val containerProductInfo = itemView.findViewById<ConstraintLayout>(R.id.container_product_info)
        private val bundleItemThumbnailImage: ImageUnify? = itemView.findViewById(R.id.iv_item_bom_detail_bundling_thumbnail)
        private val bundleItemProductNameText: Typography? = itemView.findViewById(R.id.tv_item_bom_detail_bundling_product_name)
        private val bundleItemProductPriceQuantityText: Typography? = itemView.findViewById(R.id.tv_item_bom_detail_bundling_product_price_quantity)

        private var element: ProductListUiModel.ProductUiModel? = null

        override fun bind(model: ProductListUiModel.ProductUiModel?) {
            model?.let {
                element = it
                setBundleItemThumbnail(it.productThumbnailUrl)
                setBundleItemProductName(it.productName)
                setBundleItemProductPriceQuantity(it.quantity, it.priceText)
                setItemOnClickListener(it.orderId, it.orderDetailId, it.orderStatusId)
            }
        }

        override fun bind(element: ProductListUiModel.ProductUiModel?, payloads: MutableList<Any>) {
            payloads.firstOrNull()?.let {
                if (it is Pair<*, *>) {
                    val (oldItem, newItem) = it
                    if (oldItem is ProductListUiModel.ProductUiModel && newItem is ProductListUiModel.ProductUiModel) {
                        containerProductInfo?.layoutTransition?.enableTransitionType(LayoutTransition.CHANGING)
                        this.element = newItem
                        if (oldItem.productThumbnailUrl != newItem.productThumbnailUrl) {
                            setBundleItemThumbnail(newItem.productThumbnailUrl)
                        }
                        if (oldItem.productName != newItem.productName) {
                            setBundleItemProductName(newItem.productName)
                        }
                        if (
                            oldItem.quantity != newItem.quantity ||
                            oldItem.priceText != newItem.priceText
                        ) {
                            setBundleItemProductPriceQuantity(newItem.quantity, newItem.priceText)
                        }
                        if (
                            oldItem.orderId != newItem.orderId ||
                            oldItem.orderDetailId != newItem.orderDetailId ||
                            oldItem.orderStatusId != newItem.orderStatusId
                        ) {
                            setItemOnClickListener(
                                newItem.orderId,
                                newItem.orderDetailId,
                                newItem.orderStatusId
                            )
                        }
                        containerProductInfo?.layoutTransition?.disableTransitionType(LayoutTransition.CHANGING)
                        return
                    }
                }
            }
            bind(element)
        }

        private fun setBundleItemThumbnail(thumbnailUrl: String) {
            bundleItemThumbnailImage?.setImageUrl(thumbnailUrl)
        }

        private fun setBundleItemProductName(productName: String) {
            bundleItemProductNameText?.text = productName
        }

        private fun setBundleItemProductPriceQuantity(quantity: Int, priceText: String) {
            bundleItemProductPriceQuantityText?.text = itemView.context.getString(R.string.label_product_price_and_quantity, quantity, priceText)
        }

        private fun setItemOnClickListener(orderId: String, orderDetailId: String, orderStatusId: String) {
            itemView.setOnClickListener {
                listener.onBundleItemClicked(orderId, orderDetailId, orderStatusId)
            }
        }

        private fun onItemActionClicked(key: String) {
            element?.let {
                when(key) {
                    BuyerOrderDetailActionButtonKey.BUY_AGAIN -> {
                        listener.onBundleItemAddToCart(it)
                    }
                    BuyerOrderDetailActionButtonKey.SEE_SIMILAR_PRODUCTS -> {
                        listener.onBundleItemSeeSimilarProducts(it)
                    }
                    else -> {}
                }
            }
        }

        interface Listener {
            fun onBundleItemClicked(orderId: String, orderDetailId: String, orderStatusId: String)
            fun onBundleItemAddToCart(uiModel: ProductListUiModel.ProductUiModel)
            fun onBundleItemSeeSimilarProducts(uiModel: ProductListUiModel.ProductUiModel)
        }
    }
}
