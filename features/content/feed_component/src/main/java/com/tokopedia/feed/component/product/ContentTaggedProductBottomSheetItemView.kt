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
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.unifycomponents.CardUnify
import kotlin.math.roundToInt

/**
 * Created by shruti.agarwal on 23/02/23
 */
class ContentTaggedProductBottomSheetItemView(
    context: Context,
    attrs: AttributeSet?
) : CardUnify(context, attrs) {

    private val binding = ViewFeedTaggedProductBottomSheetCardBinding.inflate(
        LayoutInflater.from(context),
        this
    )
    private var mListener: Listener? = null

    init {
        binding.tvOriginalPrice.paintFlags =
            binding.tvOriginalPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    }

    fun setListener(listener: Listener?) {
        mListener = listener
    }

    fun bindData(product: ContentTaggedProductUiModel) {
        binding.ivProductImage.setImageUrl(product.imageUrl)
        binding.tvProductTitle.text = product.title

        bindPrice(product.price)
        bindCampaign(product.campaign)

        binding.btnProductBuy.setOnClickListener {
            mListener?.onBuyProductButtonClicked(this, product)
        }

        binding.btnProductAtc.setOnClickListener {
            mListener?.onAddToCartProductButtonClicked(this, product)
        }

        binding.btnProductLongAtc.setOnClickListener {
            mListener?.onAddToCartProductButtonClicked(this, product)
        }

        setOnClickListener {
            if (product.appLink.isNotEmpty()) {
                mListener?.onProductCardClicked(this, product)
            }
        }

        bindStock(product.stock)
    }

    private fun bindPrice(price: ContentTaggedProductUiModel.Price) {
        when (price) {
            is ContentTaggedProductUiModel.CampaignPrice -> {
                binding.tvProductDiscount.hide()
                binding.tvOriginalPrice.show()
                binding.tvOriginalPrice.text = price.originalFormattedPrice
                binding.tvCurrentPrice.text = price.formattedPrice
            }
            is ContentTaggedProductUiModel.DiscountedPrice -> {
                binding.tvProductDiscount.show()
                binding.tvOriginalPrice.show()
                binding.tvProductDiscount.text = context.getString(com.tokopedia.content.common.R.string.feed_product_discount_percent, price.discount)
                binding.tvOriginalPrice.text = price.originalFormattedPrice
                binding.tvCurrentPrice.text = price.formattedPrice
            }
            is ContentTaggedProductUiModel.NormalPrice -> {
                binding.tvProductDiscount.hide()
                binding.tvOriginalPrice.hide()
                binding.tvCurrentPrice.text = price.formattedPrice
            }
        }
    }

    private fun bindCampaign(campaign: ContentTaggedProductUiModel.Campaign) {
        if (campaign.status is ContentTaggedProductUiModel.CampaignStatus.Ongoing) {
            binding.pbStock.setValue(campaign.status.stockInPercent.roundToInt(), true)
            binding.pbStock.progressBarColor = intArrayOf(
                ContextCompat.getColor(
                    context,
                    R.color.feed_dms_asgc_progress_0_color
                ),
                ContextCompat.getColor(
                    context,
                    R.color.feed_dms_asgc_progress_100_color
                )
            )
            binding.tvStock.text = campaign.status.stockLabel
            binding.llStockContainer.show()
        } else {
            binding.llStockContainer.hide()
        }
        if (campaign.status is ContentTaggedProductUiModel.CampaignStatus.Upcoming) {
            binding.llProductActionButton.hide()
            binding.btnProductLongAtc.show()
        } else {
            binding.llProductActionButton.show()
            binding.btnProductLongAtc.hide()
        }
    }

    private fun bindStock(stock: ContentTaggedProductUiModel.Stock) {
        val isShown = stock is ContentTaggedProductUiModel.Stock.Available
        binding.btnProductBuy.isEnabled = isShown
        binding.btnProductAtc.isEnabled = isShown
        binding.viewOverlayOos.showWithCondition(!isShown)
        binding.labelOutOfStock.showWithCondition(!isShown)
    }

    interface Listener {
        fun onProductCardClicked(
            view: ContentTaggedProductBottomSheetItemView,
            product: ContentTaggedProductUiModel
        )

        fun onAddToCartProductButtonClicked(
            view: ContentTaggedProductBottomSheetItemView,
            product: ContentTaggedProductUiModel
        )

        fun onBuyProductButtonClicked(
            view: ContentTaggedProductBottomSheetItemView,
            product: ContentTaggedProductUiModel
        )
    }
}
