package com.tokopedia.content.common.view

import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import com.tokopedia.content.common.R
import com.tokopedia.content.common.databinding.ViewContentTaggedProductBottomSheetCardBinding
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.unifycomponents.CardUnify
import kotlin.math.roundToInt
import com.tokopedia.content.common.R as contentcommonR

/**
 * Created by shruti.agarwal on 23/02/23
 */
class ContentTaggedProductBottomSheetItemView(
    context: Context,
    attrs: AttributeSet?
) : CardUnify(context, attrs) {

    private val binding = ViewContentTaggedProductBottomSheetCardBinding.inflate(
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

        bindStock(product.stock, product.price)
        bindCampaign(product.campaign)
        bindPrice(product.price)
    }

    private fun bindPrice(price: ContentTaggedProductUiModel.Price) {
        when (price) {
            is ContentTaggedProductUiModel.CampaignPrice -> {
                binding.tvProductDiscount.hide()
                binding.tvOriginalPrice.hide()
                binding.tvCurrentPrice.text = price.formattedPrice

                if (price.isMasked) {
                    binding.btnProductAtc.hide()
                    binding.btnProductBuy.hide()
                    binding.btnProductLongAtc.hide()
                }
            }

            is ContentTaggedProductUiModel.DiscountedPrice -> {
                binding.tvProductDiscount.show()
                binding.tvOriginalPrice.show()
                binding.tvProductDiscount.text = context.getString(
                    contentcommonR.string.feed_product_discount_percent,
                    price.discount
                )
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
            binding.pbStock.setValue(campaign.status.stockInPercent.roundToInt(), false)
            binding.pbStock.progressBarColor = intArrayOf(
                ContextCompat.getColor(
                    context,
                    R.color.content_dms_campaign_progress_0_color
                ),
                ContextCompat.getColor(
                    context,
                    R.color.content_dms_campaign_progress_100_color
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

    private fun bindStock(
        stock: ContentTaggedProductUiModel.Stock,
        price: ContentTaggedProductUiModel.Price
    ) {
        val isStockAvailable = stock is ContentTaggedProductUiModel.Stock.Available
        val isMaskedPrice = price is ContentTaggedProductUiModel.CampaignPrice && price.isMasked

        binding.btnProductBuy.isEnabled = isStockAvailable
        binding.btnProductAtc.isEnabled = isStockAvailable

        binding.viewOverlayOos.showWithCondition(!isStockAvailable && !isMaskedPrice)

        binding.labelOutOfStock.showWithCondition(!isStockAvailable)
        binding.labelSoon.showWithCondition(isMaskedPrice)
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
