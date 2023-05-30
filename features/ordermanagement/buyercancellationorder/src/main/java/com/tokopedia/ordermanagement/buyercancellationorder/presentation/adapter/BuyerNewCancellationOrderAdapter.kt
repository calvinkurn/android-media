package com.tokopedia.ordermanagement.buyercancellationorder.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.media.loader.loadImage
import com.tokopedia.ordermanagement.buyercancellationorder.databinding.ItemProductCancellationOrderBinding
import com.tokopedia.ordermanagement.buyercancellationorder.presentation.adapter.uimodel.BuyerCancellationProductUiModel

class BuyerNewCancellationOrderAdapter(
    private val buyerCancellationProductList: List<BuyerCancellationProductUiModel>
) : RecyclerView.Adapter<BuyerNewCancellationOrderAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemProductCancellationOrderBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = buyerCancellationProductList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (buyerCancellationProductList.isNotEmpty()) {
            holder.bind(buyerCancellationProductList[position])
        }
    }


    inner class ViewHolder(private val binding: ItemProductCancellationOrderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: BuyerCancellationProductUiModel) {
            with(binding) {
                icShopGrade.loadImage(item.shopIcon)
                ivProductThumbnail.loadImage(item.productThumbnailUrl)
                tvOrderNumber.text = item.orderNumberLabel
                tvShopName.text = item.shopName
                tvInvoiceNumber.text = item.invoiceNumber
                tvProductName.text = item.productName
                tvMoreInfoProduct.text = item.moreProductInfo
            }
        }
    }
}
