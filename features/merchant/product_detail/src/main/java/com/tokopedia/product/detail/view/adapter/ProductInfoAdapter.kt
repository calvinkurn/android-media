package com.tokopedia.product.detail.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.data.model.pdplayout.Content
import com.tokopedia.product.detail.common.data.model.pdplayout.ProductInfoData
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import kotlinx.android.synthetic.main.item_informasi_product.view.*

class ProductInfoAdapter(private val listOfData: List<Content>,
                         private val infoData: ProductInfoData,
                         private val listener: DynamicProductDetailListener) : RecyclerView.Adapter<ProductInfoAdapter.ItemProductInfoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemProductInfoViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_informasi_product, parent, false)
        return ItemProductInfoViewHolder(view, listener)
    }

    override fun getItemCount(): Int = listOfData.size

    override fun onBindViewHolder(holder: ItemProductInfoViewHolder, position: Int) {
        holder.bind(listOfData[position], infoData)
    }

    inner class ItemProductInfoViewHolder(private val view: View,
                                          private val listener: DynamicProductDetailListener) : RecyclerView.ViewHolder(view) {

        fun bind(data: Content, infoData: ProductInfoData) {
            view.title_info.text = data.title
            view.desc_info.text = data.subtitle

            if (data.applink.isNotEmpty()) {
                view.desc_info.setTextColor(MethodChecker.getColor(view.context, R.color.tkpd_main_green))
                view.desc_info.setOnClickListener {
                    listener.onSubtitleInfoClicked(data.applink, infoData.etalaseId, infoData.shopId.toIntOrZero(), infoData.categoryId)
                }
            }
        }

    }
}