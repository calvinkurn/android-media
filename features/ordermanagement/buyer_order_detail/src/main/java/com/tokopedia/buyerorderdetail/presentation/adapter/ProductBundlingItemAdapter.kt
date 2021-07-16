package com.tokopedia.buyerorderdetail.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.common.constants.BuyerOrderDetailActionButtonKey
import com.tokopedia.buyerorderdetail.common.utils.Utils
import com.tokopedia.buyerorderdetail.presentation.model.ActionButtonsUiModel
import com.tokopedia.buyerorderdetail.presentation.model.ProductListUiModel
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

class ProductBundlingItemAdapter(private val listener: ViewHolder.Listener,
                                 private val itemList: List<ProductListUiModel.ProductBundlingItemUiModel>): RecyclerView.Adapter<ProductBundlingItemAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_buyer_order_detail_product_bundling_list_item, parent, false)
        return ViewHolder(view, listener)
    }

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList.getOrNull(position))
    }

    class ViewHolder(itemView: View,
                     private val listener: Listener): RecyclerView.ViewHolder(itemView) {

        private val bundleItemThumbnailImage: ImageUnify? = itemView.findViewById(R.id.iv_item_bom_detail_bundling_thumbnail)
        private val bundleItemProductNameText: Typography? = itemView.findViewById(R.id.tv_item_bom_detail_bundling_product_name)
        private val bundleItemProductPriceQuantityText: Typography? = itemView.findViewById(R.id.tv_item_bom_detail_bundling_product_price_quantity)
        private val bundleItemProductNoteText: Typography? = itemView.findViewById(R.id.tv_item_bom_detail_bundling_product_note)
        private val bundleItemProductActionButton: UnifyButton? = itemView.findViewById(R.id.btn_item_bom_detail_bundling_action)

        private var element: ProductListUiModel.ProductBundlingItemUiModel? = null

        fun bind(model: ProductListUiModel.ProductBundlingItemUiModel?) {
            model?.let {
                element = it
                setBundleItemThumbnail(it.productThumbnailUrl)
                setBundleItemProductName(it.productName)
                setBundleItemProductPriceQuantity(it.quantity, it.priceText)
                setupBundleItemProductNote(it.productNote)
                setItemOnClickListener(it.orderId, it.orderDetailId, it.orderStatusId)
                setupBundleItemButton(model.button, model.isProcessing)
            }
        }

        private fun setBundleItemThumbnail(thumbnailUrl: String) {
            bundleItemThumbnailImage?.setImageUrl(thumbnailUrl)
        }

        private fun setBundleItemProductName(productName: String) {
            bundleItemProductNameText?.text = productName
        }

        private fun setBundleItemProductPriceQuantity(quantity: Int, priceText: String) {
            // TODO: Check for logic to show quantity
            bundleItemProductPriceQuantityText?.text = itemView.context.getString(R.string.label_product_price_and_quantity, quantity, priceText)
        }

        private fun setupBundleItemProductNote(productNote: String) {
            bundleItemProductNoteText?.run {
                showWithCondition(productNote.isNotBlank())
                text = Utils.composeItalicNote(productNote)
            }
        }

        private fun setupBundleItemButton(actionButton: ActionButtonsUiModel.ActionButton, processing: Boolean) {
            bundleItemProductActionButton?.run {
                isLoading = processing
                text = actionButton.label
                buttonVariant = Utils.mapButtonVariant(actionButton.variant)
                buttonType = Utils.mapButtonType(actionButton.type)
                showWithCondition(actionButton.label.isNotBlank())
                setOnClickListener {
                    onItemActionClicked(actionButton.key)
                }
            }
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
            fun onBundleItemAddToCart(uiModel: ProductListUiModel.ProductBundlingItemUiModel)
            fun onBundleItemSeeSimilarProducts(uiModel: ProductListUiModel.ProductBundlingItemUiModel)
        }
    }
}