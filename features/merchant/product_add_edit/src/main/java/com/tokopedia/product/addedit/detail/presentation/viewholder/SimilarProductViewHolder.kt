package com.tokopedia.product.addedit.detail.presentation.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.getCurrencyFormatted
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.databinding.ItemSimilarProductLayoutBinding
import com.tokopedia.product.addedit.detail.presentation.model.SimilarProduct

class SimilarProductViewHolder(
        private var clickListener: ClickListener? = null,
        private val binding: ItemSimilarProductLayoutBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bindData(similarProduct: SimilarProduct) {
        if (adapterPosition.isZero()) binding.duTop.visibility = View.INVISIBLE
        val imageUrl = similarProduct.imageURL.orEmpty()
        binding.iuProductImage.setImageUrl(imageUrl)
        binding.tpgProductPrice.text = similarProduct.displayPrice?.getCurrencyFormatted()
        binding.tpgProductTitle.text = similarProduct.title
        binding.tpgProductRating.text = similarProduct.rating?.orZero().toString()
        var totalSoldText = similarProduct.totalSold.orZero().toString()
        binding.root.context?.run {
            totalSoldText = this.getString(R.string.label_sold_product, totalSoldText)
        }
        binding.tpgProductStatistic.text = totalSoldText
        binding.root.setOnClickListener {
            clickListener?.onProductItemClickListener(adapterPosition)
        }
    }

    interface ClickListener {
        fun onProductItemClickListener(adapterPosition: Int)
    }
}