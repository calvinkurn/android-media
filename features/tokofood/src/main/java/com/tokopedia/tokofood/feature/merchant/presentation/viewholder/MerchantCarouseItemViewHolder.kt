package com.tokopedia.tokofood.feature.merchant.presentation.viewholder

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.tokofood.databinding.ItemMerchantInfoCarouselBinding
import com.tokopedia.tokofood.feature.merchant.presentation.model.CarouselData

class MerchantCarouseItemViewHolder(
        private val binding: ItemMerchantInfoCarouselBinding,
        private val clickListener: OnCarouselItemClickListener
) : RecyclerView.ViewHolder(binding.root) {

    interface OnCarouselItemClickListener {
        fun onCarouselItemClicked()
    }

    private var context: Context? = null

    init {
        context = binding.root.context
        binding.root.setOnClickListener {
            clickListener.onCarouselItemClicked()
        }
    }

    fun bindData(carouselData: CarouselData) {
        carouselData.imageResource?.run {
            binding.iuLeftDrawable.setImageResource(this)
            binding.iuLeftDrawable.show()
        }
        binding.tpgMerchantInfoHeader.text = carouselData.title
        binding.tpgMerchantInfoContent.text = carouselData.information
        context?.run {
            // normal text color by default
            var textColor = ContextCompat.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_NN950)
            if (carouselData.isWarning) {
                // red text color for warning
                textColor = ContextCompat.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_RN500)
            }
            binding.tpgMerchantInfoContent.setTextColor(textColor)
        }
    }
}