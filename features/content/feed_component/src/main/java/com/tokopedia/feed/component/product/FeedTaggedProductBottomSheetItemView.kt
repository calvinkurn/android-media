package com.tokopedia.feed.component.product

import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.databinding.ViewFeedTaggedProductBottomSheetCardBinding
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.CardUnify

/**
 * Created by shruti.agarwal on 23/02/23
 */
class FeedTaggedProductBottomSheetItemView(
    context: Context,
    attrs: AttributeSet?
) : CardUnify(context, attrs) {

    private val binding = ViewFeedTaggedProductBottomSheetCardBinding.inflate(
        LayoutInflater.from(context),
        this,
    )
    private var mListener: Listener? = null

    init {
        binding.tvOriginalPrice.paintFlags =
            binding.tvOriginalPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    }

    fun setListener(listener: Listener?) {
        mListener = listener
    }

    fun bindData(product: FeedTaggedProductUiModel) {
        binding.ivProductImage.setImageUrl(product.imageUrl)
        binding.tvProductTitle.text = product.title

        bindPrice(product.price)

        binding.btnProductBuy.setOnClickListener {
            mListener?.onBuyProductButtonClicked(this, product)
        }

        binding.btnProductAtc.setOnClickListener {
            mListener?.onAddToCartProductButtonClicked(this, product)
        }

        setOnClickListener {
            if (product.appLink.isNotEmpty()) {
                mListener?.onProductCardClicked(this, product)
            }
        }
    }

    private fun bindPrice(price: FeedTaggedProductUiModel.Price) {
        when(price) {
            is FeedTaggedProductUiModel.DiscountedPrice -> {
                binding.tvProductDiscount.show()
                binding.tvOriginalPrice.show()
                binding.tvProductDiscount.text =
                    context.getString(R.string.feed_product_discount_percent, price.discount)
                binding.tvOriginalPrice.text = price.originalPrice
                binding.tvCurrentPrice.text = price.price
            }
            is FeedTaggedProductUiModel.NormalPrice -> {
                binding.tvProductDiscount.hide()
                binding.tvOriginalPrice.hide()
                binding.tvCurrentPrice.text = price.price
            }
        }
    }

    interface Listener {
        fun onProductCardClicked(
            view: FeedTaggedProductBottomSheetItemView,
            product: FeedTaggedProductUiModel
        )

        fun onAddToCartProductButtonClicked(
            view: FeedTaggedProductBottomSheetItemView,
            product: FeedTaggedProductUiModel
        )
        fun onBuyProductButtonClicked(
            view: FeedTaggedProductBottomSheetItemView,
            product: FeedTaggedProductUiModel
        )
    }
}
