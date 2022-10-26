package com.tokopedia.play.view.custom

import android.content.Context
import android.graphics.Paint
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.loader.loadImageRounded
import com.tokopedia.play.R
import com.tokopedia.play.databinding.ViewProductBottomSheetCardBinding
import com.tokopedia.play.view.type.*
import com.tokopedia.play.view.uimodel.PlayProductUiModel
import com.tokopedia.play.view.uimodel.recom.tagitem.ProductSectionUiModel
import com.tokopedia.play_common.util.extension.buildSpannedString
import com.tokopedia.unifycomponents.CardUnify
import com.tokopedia.unifycomponents.UnifyButton

/**
 * Created by kenny.hadisaputra on 19/08/22
 */
class ProductBottomSheetCardView(
    context: Context,
    attrs: AttributeSet?
) : CardUnify(context, attrs) {

    private val binding = ViewProductBottomSheetCardBinding.inflate(
        LayoutInflater.from(context),
        this,
    )

    private val imageRadius = resources.getDimensionPixelSize(R.dimen.play_product_image_radius).toFloat()
    private val separatorSpan = ForegroundColorSpan(
        MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN500)
    )
    private val stockSpan = ForegroundColorSpan(
        MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_RN500)
    )

    private var mListener: Listener? = null

    init {
        binding.tvOriginalPrice.paintFlags =
            binding.tvOriginalPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    }

    fun setListener(listener: Listener?) {
        mListener = listener
    }

    fun setItem(item: PlayProductUiModel.Product, section: ProductSectionUiModel.Section) {
        binding.ivProductImage.loadImageRounded(item.imageUrl, imageRadius)
        binding.tvProductTitle.text = item.title

        binding.llInfo.showWithCondition(
            shouldShow = item.isPinned ||
                    (item.stock is StockAvailable && item.stock.stock <= STOCK_THRESHOLD)
        )
        binding.iconPinned.showWithCondition(item.isPinned)
        binding.tvInfo.text = getInfo(item)

        binding.tvNow.showWithCondition(item.isTokoNow)
        binding.ivNow.showWithCondition(item.isTokoNow)

        when (item.price) {
            is DiscountedPrice -> {
                binding.tvProductDiscount.show()
                binding.tvOriginalPrice.show()
                binding.tvProductDiscount.text = context.getString(R.string.play_discount_percent, item.price.discountPercent)
                binding.tvOriginalPrice.text = item.price.originalPrice
                binding.tvCurrentPrice.text = item.price.discountedPrice
            }
            is OriginalPrice -> {
                binding.tvProductDiscount.hide()
                binding.tvOriginalPrice.hide()
                binding.tvCurrentPrice.text = item.price.price
            }
        }

        when (item.stock) {
            OutOfStock -> {
                binding.shadowOutOfStock.show()
                binding.labelOutOfStock.show()
            }

            is StockAvailable -> {
                binding.shadowOutOfStock.hide()
                binding.labelOutOfStock.hide()
            }
            is ComingSoon ->{
                binding.shadowOutOfStock.hide()
                binding.labelOutOfStock.hide()
            }
        }

        binding.btnProductBuy.setOnClickListener {
            mListener?.onBuyProduct(this, item, section)
        }
        binding.btnProductAtc.setOnClickListener {
            mListener?.onAtcProduct(this, item, section)
        }
        setOnClickListener {
            if (!item.applink.isNullOrEmpty()) mListener?.onClicked(this, item, section)
        }

        //Buttons
        binding.btnProductAtc.showWithCondition(item.buttonUiModels.isNotEmpty())
        binding.btnProductBuy.showWithCondition(item.buttonUiModels.isNotEmpty())
        binding.btnProductBuy.generateButton(item.buttonUiModels.firstOrNull().orDefault())
        binding.btnProductAtc.generateButton(item.buttonUiModels.lastOrNull().orDefault())
    }

    private fun getInfo(item: PlayProductUiModel.Product): CharSequence {
        return buildSpannedString {
            if (item.isPinned) {
                append(context.getString(R.string.play_product_pinned))
                append(' ')
            }

            if (item.stock !is StockAvailable ||
                item.stock.stock > STOCK_THRESHOLD
            ) return@buildSpannedString

            if (item.isPinned) {
                val separator = context.getString(R.string.play_product_pinned_info_separator)
                append(separator, separatorSpan, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
                append(' ')
            }

            val stockText = context.getString(R.string.play_product_item_stock, item.stock.stock.toString())
            append(stockText, stockSpan, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        }
    }

    /**
     * Move to another file?
     */
    private fun UnifyButton.generateButton(button: ProductButtonUiModel){
        //Setup Text
        text = button.text

        //Setup Icon if any, for now its only for ATC
        val iconColor = if(button.color == ProductButtonColor.DISABLED_BUTTON)
                            com.tokopedia.unifyprinciples.R.color.Unify_NN100
                        else com.tokopedia.unifyprinciples.R.color.Unify_G500

        when (button.type) {
            ProductButtonType.ATC ->
                setDrawable(
                    getIconUnifyDrawable(context, IconUnify.ADD, ContextCompat.getColor(context, iconColor)))
        }

        //Setup Color, default?
        when (button.color) {
            ProductButtonColor.PRIMARY_BUTTON -> {
                buttonVariant = UnifyButton.Variant.FILLED
                buttonType = UnifyButton.Type.MAIN
                isEnabled = true
            }
            ProductButtonColor.SECONDARY_BUTTON -> {
                buttonVariant = UnifyButton.Variant.GHOST
                buttonType = UnifyButton.Type.MAIN
                isEnabled = true
            }
            ProductButtonColor.DISABLED_BUTTON -> {
                buttonVariant = UnifyButton.Variant.FILLED
                buttonType = UnifyButton.Type.MAIN
                isEnabled = false
            }
            ProductButtonColor.SECONDARY_GRAY_BUTTON -> {
                buttonVariant = UnifyButton.Variant.GHOST
                buttonType = UnifyButton.Type.MAIN
                isEnabled = false
            }
        }
    }

    companion object {
        private const val STOCK_THRESHOLD = 5
    }

    interface Listener {
        fun onClicked(
            view: ProductBottomSheetCardView,
            product: PlayProductUiModel.Product,
            section: ProductSectionUiModel.Section,
        )
        fun onBuyProduct(
            view: ProductBottomSheetCardView,
            product: PlayProductUiModel.Product,
            section: ProductSectionUiModel.Section,
        )
        fun onAtcProduct(
            view: ProductBottomSheetCardView,
            product: PlayProductUiModel.Product,
            section: ProductSectionUiModel.Section,
        )
    }
}
