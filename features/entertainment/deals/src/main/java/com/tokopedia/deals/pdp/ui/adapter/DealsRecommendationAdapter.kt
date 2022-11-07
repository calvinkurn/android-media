package com.tokopedia.deals.pdp.ui.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.applink.internal.ApplinkConstInternalDeals
import com.tokopedia.deals.databinding.ItemDealsCardShortBinding
import com.tokopedia.deals.common.model.response.EventProductDetail
import com.tokopedia.deals.common.utils.DealsUtils
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.media.loader.loadImage

class DealsRecommendationAdapter(private val recommendationListener: RecommendationListener?) : RecyclerView.Adapter<DealsRecommendationAdapter.ViewHolder>() {

    private var products: MutableList<EventProductDetail> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemDealsCardShortBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       holder.bind(products[position])
    }

    fun addProducts(products: List<EventProductDetail>) {
        this.products.clear()
        this.products = products.toMutableList()
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemDealsCardShortBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: EventProductDetail) {
            with(binding) {
                tgDealIntro.text = product.displayName
                ivProduct.loadImage(product.imageApp)
                ivBrand.loadImage(product.brand.featuredThumbnailImage)
                tgBrandName.text = product.brand.title

                if (product.displayTags.isNotEmpty()) {
                    tgHotDeal.show()
                } else {
                    tgHotDeal.hide()
                }

                if (product.mrp.toIntSafely() != Int.ZERO &&
                    product.mrp.toIntSafely() != product.salesPrice.toIntSafely()
                ) {
                    tgMrp.show()
                    tgMrp.text = DealsUtils.convertToCurrencyString(product.mrp.toLong())
                    tgMrp.paintFlags = tgMrp.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                } else {
                    tgMrp.invisible()
                }

                if (product.savingPercentage.isEmpty() || product.savingPercentage.startsWith(ZERO_PERCENT)) {
                    tgOff.invisible()
                } else {
                    tgOff.show()
                    tgOff.text = product.savingPercentage
                }

                tgSalesPrice.text = DealsUtils.convertToCurrencyString(product.salesPrice.toLong())

                ivBrand.setOnClickListener {
                    val brandUrl = ApplinkConstInternalDeals.DEALS_BRAND_DETAIL_PAGE + "?" + product.brand.seoUrl
                    recommendationListener?.onClickDealsBrand(brandUrl)
                }

                itemView.setOnClickListener {
                    recommendationListener?.onClickDealsProduct(product.appUrl, product, adapterPosition)
                }

                itemView.addOnImpressionListener(product) {
                    recommendationListener?.onImpressProduct(product, adapterPosition)
                }
            }
        }
    }

    companion object {
        private const val ZERO_PERCENT = "0%"
    }

    interface RecommendationListener {
        fun onClickDealsBrand(brandUrl: String)
        fun onClickDealsProduct(pdpUrl: String, product: EventProductDetail, index: Int)
        fun onImpressProduct(product: EventProductDetail, index: Int)
    }
}
