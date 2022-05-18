package com.tokopedia.ordermanagement.buyercancellationorder.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.media.loader.loadImage
import com.tokopedia.ordermanagement.buyercancellationorder.data.getcancellationreason.BuyerGetCancellationReasonData
import com.tokopedia.ordermanagement.buyercancellationorder.databinding.BottomsheetProductItemBinding

/**
 * Created by fwidjaja on 12/06/20.
 */
class BuyerListOfProductsBottomSheetAdapter : RecyclerView.Adapter<BuyerListOfProductsBottomSheetAdapter.ViewHolder>() {
    var listProducts = listOf<BuyerGetCancellationReasonData.Data.GetCancellationReason.OrderDetailsCancellation>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(BottomsheetProductItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return listProducts.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (listProducts.isNotEmpty()) {
            holder.bind(listProducts[position])
        }
    }

    inner class ViewHolder(private val binding: BottomsheetProductItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: BuyerGetCancellationReasonData.Data.GetCancellationReason.OrderDetailsCancellation) {
            with(binding) {
                ivProduct.loadImage(data.picture)
                labelProductName.text = data.productName
                labelPrice.text = data.productPrice
            }
        }
    }
}