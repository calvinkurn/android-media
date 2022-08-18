package com.tokopedia.play.ui.productsheet.viewholder

import android.content.Context
import android.graphics.Paint
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.loadImageRounded
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.play.R
import com.tokopedia.play.databinding.ItemPlayProductLineBinding
import com.tokopedia.play.view.type.ComingSoon
import com.tokopedia.play.view.type.DiscountedPrice
import com.tokopedia.play.view.type.OriginalPrice
import com.tokopedia.play.view.type.OutOfStock
import com.tokopedia.play.view.type.StockAvailable
import com.tokopedia.play.view.uimodel.PlayProductUiModel
import com.tokopedia.play_common.util.extension.buildSpannedString
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.R as unifyR

/**
 * Created by jegul on 03/03/20
 */
class ProductLineViewHolder(
    binding: ItemPlayProductLineBinding,
    private val listener: Listener
) : BaseViewHolder(binding.root) {

    private val context: Context
        get() = itemView.context

    private val productBinding = binding.layoutPlayProduct


    private val imageRadius = itemView.resources.getDimensionPixelSize(R.dimen.play_product_image_radius).toFloat()
    private val separatorSpan = ForegroundColorSpan(
        MethodChecker.getColor(context, unifyR.color.Unify_NN500)
    )
    private val stockSpan = ForegroundColorSpan(
        MethodChecker.getColor(context, unifyR.color.Unify_RN500)
    )

    init {
        productBinding.tvOriginalPrice.paintFlags =
            productBinding.tvOriginalPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    }

    fun bind(item: PlayProductUiModel.Product) {
        productBinding.ivProductImage.loadImageRounded(item.imageUrl, imageRadius)
        productBinding.tvProductTitle.text = item.title

        productBinding.llInfo.showWithCondition(
            shouldShow = item.isPinned ||
                    (item.stock is StockAvailable && item.stock.stock <= MIN_STOCK)
        )
        productBinding.iconPinned.showWithCondition(item.isPinned)
        productBinding.tvInfo.text = getInfo(item)

        productBinding.tvNow.showWithCondition(item.isTokoNow)
        productBinding.ivNow.showWithCondition(item.isTokoNow)

        when (item.price) {
            is DiscountedPrice -> {
                productBinding.tvProductDiscount.show()
                productBinding.tvOriginalPrice.show()
                productBinding.tvProductDiscount.text = itemView.context.getString(R.string.play_discount_percent, item.price.discountPercent)
                productBinding.tvOriginalPrice.text = item.price.originalPrice
                productBinding.tvCurrentPrice.text = item.price.discountedPrice
            }
            is OriginalPrice -> {
                productBinding.tvProductDiscount.hide()
                productBinding.tvOriginalPrice.hide()
                productBinding.tvCurrentPrice.text = item.price.price
            }
        }

        when (item.stock) {
            OutOfStock -> {
                productBinding.shadowOutOfStock.show()
                productBinding.labelOutOfStock.show()
                productBinding.btnProductAtc.setDrawable(
                    getIconUnifyDrawable(itemView.context, IconUnify.ADD, ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_NN100))
                )
                productBinding.btnProductBuy.isEnabled = false
                productBinding.btnProductAtc.isEnabled = false
            }

            is StockAvailable -> {
                productBinding.shadowOutOfStock.gone()
                productBinding.labelOutOfStock.gone()
                productBinding.btnProductBuy.isEnabled = true
                productBinding.btnProductAtc.isEnabled = true
                productBinding.btnProductAtc.setDrawable(
                    getIconUnifyDrawable(itemView.context, IconUnify.ADD, ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_G500))
                )
            }
            is ComingSoon ->{
                productBinding.btnProductAtc.hide()
                productBinding.btnProductBuy.hide()
            }
        }

        productBinding.btnProductBuy.setOnClickListener {
            listener.onBuyProduct(item)
        }
        productBinding.btnProductAtc.setOnClickListener {
            listener.onAtcProduct(item)
        }
        itemView.setOnClickListener {
            if (!item.applink.isNullOrEmpty()) listener.onClicked(this, item)
        }
    }

    private fun getInfo(item: PlayProductUiModel.Product): CharSequence {
        return buildSpannedString {
            if (item.isPinned) {
                append(getString(R.string.play_product_pinned))
                append(' ')
            }

            if (item.stock !is StockAvailable ||
                item.stock.stock > MIN_STOCK) return@buildSpannedString

            if (item.isPinned) {
                val separator = getString(R.string.play_product_pinned_info_separator)
                append(separator, separatorSpan, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
                append(' ')
            }

            val stockText = getString(R.string.play_product_item_stock, item.stock.stock)
            append(stockText, stockSpan, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        }
    }

    companion object {

        fun create(
            parent: ViewGroup,
            listener: Listener,
        ) = ProductLineViewHolder(
            ItemPlayProductLineBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            ),
            listener,
        )

        private const val MIN_STOCK: Int = 5
    }

    interface Listener {
        fun onClicked(viewHolder: ProductLineViewHolder, product: PlayProductUiModel.Product)
        fun onBuyProduct(product: PlayProductUiModel.Product)
        fun onAtcProduct(product: PlayProductUiModel.Product)
    }
}
