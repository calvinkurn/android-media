package com.tokopedia.deals.ui.brand_detail

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.DateFormatUtils
import com.tokopedia.deals.R
import com.tokopedia.deals.data.entity.Product
import com.tokopedia.deals.databinding.ItemDealsBrandDetailBinding
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.utils.text.currency.CurrencyFormatHelper

class DealsBrandDetailAdapter(private val callback: DealsBrandDetailCallback) : RecyclerView.Adapter<DealsBrandDetailAdapter.DealsBrandDetailViewHolder>() {

    var listProduct = emptyList<Product>()

    inner class DealsBrandDetailViewHolder(
        private val callback: DealsBrandDetailCallback,
        private val binding: ItemDealsBrandDetailBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            with(binding) {
                imgItemBrandDetail.loadImage(product.imageApp)
                tgItemTitleBrandDetail.text = product.title
                tgItemValidDate.text = DateFormatUtils.getFormattedDate(product.maxEndDate.toString(), DATE_FORMAT)
                tgItemPriceBrandDetail.text = product.price
                if (product.savingPercentage.isNotEmpty() && !product.savingPercentage.startsWith(
                        ZERO_PERCENT
                    )
                ) {
                    labelItemPercentageBrandDetail.text = product.savingPercentage
                    tgSlashPriceBrandDetail.apply {
                        val rupiahResult = resources.getString(
                            R.string.deals_brand_detail_item_slash_price,
                            CurrencyFormatHelper.convertToRupiah(product.mrp.toString())
                        )
                        text = rupiahResult
                        paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    }
                } else {
                    labelItemPercentageBrandDetail.gone()
                    tgSlashPriceBrandDetail.gone()
                }

                root.setOnClickListener {
                    callback.clickProduct(position, product)
                }

                root.addOnImpressionListener(product) {
                    callback.impressionProduct(position, product)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return listProduct.size
    }

    override fun onBindViewHolder(holder: DealsBrandDetailViewHolder, position: Int) {
        holder.bind(listProduct[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DealsBrandDetailViewHolder {
        val binding = ItemDealsBrandDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DealsBrandDetailViewHolder(callback, binding)
    }

    companion object {
        const val DATE_FORMAT = "dd MMM yyyy"
        const val ZERO_PERCENT = "0%"
    }

    interface DealsBrandDetailCallback {
        fun clickProduct(position: Int, product: Product)
        fun impressionProduct(position: Int, product: Product)
    }
}
