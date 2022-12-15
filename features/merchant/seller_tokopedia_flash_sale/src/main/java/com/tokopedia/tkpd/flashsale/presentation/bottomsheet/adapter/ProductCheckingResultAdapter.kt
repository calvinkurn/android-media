package com.tokopedia.tkpd.flashsale.presentation.bottomsheet.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.seller_tokopedia_flash_sale.databinding.StfsItemProductCheckResultBinding
import com.tokopedia.tkpd.flashsale.domain.entity.ProductCheckingResult

class ProductCheckingResultAdapter: RecyclerView.Adapter<ProductCheckingResultViewHolder>() {

    private var data: List<ProductCheckingResult> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductCheckingResultViewHolder {
        val binding = StfsItemProductCheckResultBinding.inflate(LayoutInflater.from(parent.context),
            parent, false)
        return ProductCheckingResultViewHolder(binding)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ProductCheckingResultViewHolder, position: Int) {
        data.getOrNull(position)?.let { menu ->
            holder.bind(menu)
        }
    }

    fun setDataList(newData: List<ProductCheckingResult>) {
        data = newData
        notifyItemRangeChanged(Int.ZERO, newData.size)
    }
}
