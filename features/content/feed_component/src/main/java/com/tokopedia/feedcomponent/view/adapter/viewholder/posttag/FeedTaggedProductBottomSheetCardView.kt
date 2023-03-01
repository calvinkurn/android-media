package com.tokopedia.feedcomponent.view.adapter.viewholder.posttag

import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import android.view.LayoutInflater
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXProduct
import com.tokopedia.feedcomponent.databinding.ViewFeedTaggedProductBottomSheetCardBinding
import com.tokopedia.feedcomponent.view.viewmodel.posttag.ProductPostTagModelNew
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.unifycomponents.CardUnify

/**
 * Created by shruti.agarwal on 23/02/23
 */
class FeedTaggedProductBottomSheetCardView(
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

    fun setItem(item: ProductPostTagModelNew) {
        val product = item.product
        binding.ivProductImage.setImageUrl(product.coverURL)
        binding.tvProductTitle.text = product.name

        if (product.isDiscount) {
            binding.tvProductDiscount.show()
            binding.tvOriginalPrice.show()
            binding.tvProductDiscount.text =
                context.getString(R.string.feed_product_discount_percent, product.discount)
            binding.tvOriginalPrice.text = product.priceOriginalFmt
            binding.tvCurrentPrice.text = product.priceFmt
        } else {
            binding.tvProductDiscount.hide()
            binding.tvOriginalPrice.hide()
            binding.tvCurrentPrice.text = product.priceFmt
        }


        binding.btnProductFirst.setOnClickListener {
            mListener?.onButtonTransactionProduct(this, item.product)
        }

        binding.btnProductSecond.setOnClickListener {
            mListener?.onButtonTransactionProduct(this, item.product)
        }

        setOnClickListener {
            if (product.appLink.isNotEmpty()) mListener?.onClicked(this, item.product)
        }

        binding.btnProductFirst.showWithCondition(true)
        binding.btnProductSecond.showWithCondition(true)

    }

    interface Listener {
        fun onClicked(
            view: FeedTaggedProductBottomSheetCardView,
            product: FeedXProduct
        )

        fun onButtonTransactionProduct(
            view: FeedTaggedProductBottomSheetCardView,
            product: FeedXProduct
        )
    }
}
