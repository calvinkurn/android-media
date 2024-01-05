package com.tokopedia.play.view.custom

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.loader.loadImageRounded
import com.tokopedia.play.R
import com.tokopedia.play.databinding.ViewProductBottomSheetCardBinding
import com.tokopedia.play.view.type.*
import com.tokopedia.play.view.uimodel.PlayProductUiModel
import com.tokopedia.play.view.uimodel.isShowRating
import com.tokopedia.play.view.uimodel.isShowSoldQuantity
import com.tokopedia.play.view.uimodel.recom.tagitem.ProductSectionUiModel
import com.tokopedia.play.view.uimodel.recom.tagitem.isUpcoming
import com.tokopedia.play_common.util.extension.buildSpannedString

/**
 * Created by kenny.hadisaputra on 19/08/22
 */
class ProductBottomSheetCardView: FrameLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private val binding = ViewProductBottomSheetCardBinding.inflate(
        LayoutInflater.from(context),
        this, true
    )

    private val imageRadius =
        resources.getDimensionPixelSize(R.dimen.play_product_image_radius).toFloat()
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

    @SuppressLint("ResourceType")
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
                binding.tvProductDiscount.text =
                    context.getString(R.string.play_discount_percent, item.price.discountPercent)
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
            is ComingSoon -> {
                binding.shadowOutOfStock.hide()
                binding.labelOutOfStock.hide()
            }
        }

        val firstButton = item.buttons.firstOrNull().orDefault()
        val lastButton = item.buttons.lastOrNull().orDefault()

        binding.btnProductFirst.setOnClickListener {
            mListener?.onButtonTransactionProduct(this, item, section, firstButton.type.toAction)
        }

        binding.btnProductSecond.setOnClickListener {
            mListener?.onButtonTransactionProduct(this, item, section, lastButton.type.toAction)
        }

        binding.layoutRibbon.showWithCondition(item.label.rankFmt.isNotBlank())
        binding.layoutRibbon.rankFmt = item.label.rankFmt
        binding.layoutRibbon.setRibbonColors(item.label.rankColors)
        binding.layoutRibbon.startAnimation()

        binding.cardPlayPinned.setOnClickListener {
            if (!item.applink.isNullOrEmpty()) mListener?.onClicked(this, item, section)
        }

        // Buttons
        binding.btnProductFirst.showWithCondition(item.buttons.isNotEmpty())
        binding.btnProductSecond.showWithCondition(item.buttons.isNotEmpty())

        binding.btnProductFirst.text = firstButton.text
        binding.btnProductSecond.text = lastButton.text

        binding.btnProductFirst.generateButton(firstButton.color)
        binding.btnProductSecond.generateButton(lastButton.color)

        binding.lblProductNumber.showWithCondition(item.isNumerationShown)
        binding.lblProductNumber.text = item.number

        //Social Proof
        if (section.config.type.isUpcoming) {
            binding.ivPlayProductStars.hide()
            binding.tvPlayRatingAndSoldQuantity.hide()
        } else {
            binding.ivPlayProductStars.showWithCondition(item.isShowRating)
            binding.tvPlayRatingAndSoldQuantity.show()
            binding.tvPlayRatingAndSoldQuantity.text = buildString {
                append(item.rating)
                append(if (item.isShowSoldQuantity && item.isShowRating) " | " else "")
                append(item.soldQuantity)
            }
        }
    }

    private fun getInfo(item: PlayProductUiModel.Product): CharSequence {
        return buildSpannedString {
            if (item.isPinned) {
                append(context.getString(R.string.play_product_pinned))
                append(' ')
            }

            if (item.stock !is StockAvailable ||
                item.stock.stock > STOCK_THRESHOLD
            ) {
                return@buildSpannedString
            }

            if (item.isPinned) {
                val separator = context.getString(R.string.play_product_pinned_info_separator)
                append(separator, separatorSpan, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
                append(' ')
            }

            val stockText =
                context.getString(R.string.play_product_item_stock, item.stock.stock.toString())
            append(stockText, stockSpan, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        }
    }

    companion object {
        private const val STOCK_THRESHOLD = 5
    }

    interface Listener {
        fun onClicked(
            view: ProductBottomSheetCardView,
            product: PlayProductUiModel.Product,
            section: ProductSectionUiModel.Section
        )

        fun onButtonTransactionProduct(
            view: ProductBottomSheetCardView,
            product: PlayProductUiModel.Product,
            section: ProductSectionUiModel.Section,
            action: ProductAction
        )
    }
}
