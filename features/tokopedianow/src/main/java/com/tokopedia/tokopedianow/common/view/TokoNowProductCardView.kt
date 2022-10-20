package com.tokopedia.tokopedianow.common.view

import android.content.Context
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import com.tokopedia.home_component.util.getHexColorFromIdColor
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showIfWithBlock
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.util.ViewUtil.getDpFromDimen
import com.tokopedia.tokopedianow.databinding.LayoutTokopedianowProductCardViewBinding

class TokoNowProductCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : CardView(context, attrs) {

    companion object {
        private const val VERTICAL_BIAS_RATING_TYPOGRAPHY = 1.0f
    }

    private var binding: LayoutTokopedianowProductCardViewBinding

    init {
        binding = LayoutTokopedianowProductCardViewBinding.inflate(LayoutInflater.from(context), this, true)
        /**
         * note :
         * - init category info
         * - set text and background color from BE (promo label and assigned value)
         */
        setupUi()
    }

    private fun setupUi() {
        val isNormal = false
        val isOos = false
        val isFlashSale = !isOos && !isNormal
        binding.apply {
            initImageFilterView(
                imageUrl = "https://slack-imgs.com/?c=1&o1=ro&url=https%3A%2F%2Fimages.tokopedia.net%2Fimg%2Fandroid%2Fnow%2FPN-RICH.jpg",
                brightness = if (isOos) 0.5f else 1f
            )
            initAssignedValueTypography(
                assignedValue = "Terbaru"
            )
            initMainPriceTypography(
                price = "1.500.000"
            )
            initPromoLabel(
                label = "88%",
                slashPrice = "8888"
            )
            initProductNameTypography(
                productName = "product pertama ini"
            )
            initRatingTypography(
                rating = "4.5",
                isFlashSale = isFlashSale,
                isNormal = isNormal
            )
            initOosLabel(
                label = "terjual",
                isOos = isOos
            )
            initProgressBar(
                isFlashSale = isFlashSale,
                progressStatus = "Terjangkau"
            )
        }
    }

    private fun LayoutTokopedianowProductCardViewBinding.initImageFilterView(
        imageUrl: String,
        brightness: Float
    ) {
        imageFilterView.loadImage(imageUrl)
        imageFilterView.brightness = brightness
    }

    private fun LayoutTokopedianowProductCardViewBinding.initAssignedValueTypography(
        assignedValue: String
    ) {
        assignedValueTypography.showIfWithBlock(assignedValue.isNotBlank()) {
            text = assignedValue
        }
    }

    private fun LayoutTokopedianowProductCardViewBinding.initMainPriceTypography(
        price: String
    ) {
        mainPriceTypography.showIfWithBlock(price.isNotBlank()) {
            text = price
        }
    }

    private fun LayoutTokopedianowProductCardViewBinding.initPromoLabel(
        label: String,
        slashPrice: String
    ) {
        promoLabel.showIfWithBlock(label.isNotBlank()) {
            text = label
            initSlashPriceTypography(slashPrice)
        }
    }

    private fun LayoutTokopedianowProductCardViewBinding.initSlashPriceTypography(
        slashPrice: String
    ) {
        slashPriceTypography.showIfWithBlock(slashPrice.isNotBlank()) {
            text = slashPrice
            paintFlags = Paint.STRIKE_THRU_TEXT_FLAG or Paint.ANTI_ALIAS_FLAG
        }
    }

    private fun LayoutTokopedianowProductCardViewBinding.initProductNameTypography(
        productName: String
    ) {
        productNameTypography.showIfWithBlock(productName.isNotBlank()) {
            text = productName
        }
    }

    private fun LayoutTokopedianowProductCardViewBinding.initRatingTypography(
        rating: String,
        isFlashSale: Boolean,
        isNormal: Boolean
    ) {
        ratingTypography.showIfWithBlock(rating.isNotBlank() && !isFlashSale) {
            ratingIcon.show()
            text = rating
            if (isNormal) {
                adjustRatingPosition()
            }
        }
    }

    private fun LayoutTokopedianowProductCardViewBinding.initSimilarProductTypography(
        isOos: Boolean
    ) {
        similarProductTypography.showIfWithBlock(isOos) {
            rightDrawable(
                drawable = getIconUnifyDrawable(
                    context = context,
                    iconId = IconUnify.CHEVRON_DOWN,
                    assetColor = ContextCompat.getColor(
                        context,
                        com.tokopedia.unifyprinciples.R.color.Unify_GN500
                    )
                )
            )
        }
    }

    private fun LayoutTokopedianowProductCardViewBinding.initOosLabel(label: String, isOos: Boolean) {
        oosLabel.showIfWithBlock(label.isNotBlank() && isOos) {
            text = label
            unlockFeature = true
            setLabelType(
                getHexColorFromIdColor(
                    context = context,
                    idColor = com.tokopedia.unifyprinciples.R.color.Unify_N700_68
                )
            )
            initWishlistButton(
                isOos = isOos
            )
            initSimilarProductTypography(
                isOos = isOos
            )
        }
    }

    private fun LayoutTokopedianowProductCardViewBinding.initWishlistButton(
        isOos: Boolean
    ) {
        wishlistButton.showIfWithBlock(isOos) {
            quantityEditor.hide()
        }
    }

    private fun LayoutTokopedianowProductCardViewBinding.initProgressBar(
        isFlashSale: Boolean,
        progressStatus: String
    ) {
        progressBar.showIfWithBlock(isFlashSale) {
            progressTypography.show()
            progressTypography.text = progressStatus
        }
    }

    /**
     * This function is used for phase 2
     */
//    private fun LayoutTokopedianowProductCardViewBinding.initCategoryInfo(
//        weight: String,
//        category: String,
//        isBold: Boolean,
//        color: String
//    ) {
//        val weightNotBlank = weight.isNotBlank()
//        val categoryNotBlank = category.isNotBlank()
//        categoryInfoTypography.showIfWithBlock(weightNotBlank || categoryNotBlank) {
//            val concatenatedSpannable = SpannableStringBuilder()
//            if (weightNotBlank) concatenatedSpannable.append(weight)
//            if (categoryNotBlank) {
//                concatenatedSpannable.append(MethodChecker.fromHtml("&#8226;"))
//                val categorySpannable = SpannableString("Halal")
//                if (isBold) {
//                    categorySpannable.setSpan(
//                        color,
//                        0,
//                        categorySpannable.length,
//                        Spannable.SPAN_INCLUSIVE_INCLUSIVE
//                    )
//                    categorySpannable.setSpan(
//                        StyleSpan(Typeface.BOLD),
//                        0,
//                        category.length,
//                        Spannable.SPAN_INCLUSIVE_INCLUSIVE
//                    )
//                }
//                concatenatedSpannable.append(categorySpannable)
//            }
//            categoryInfoTypography.setText(concatenatedSpannable, TextView.BufferType.SPANNABLE)
//        }
//    }

    private fun LayoutTokopedianowProductCardViewBinding.adjustRatingPosition() {
        val constraintSet = ConstraintSet()

        constraintSet.clone(root)

        constraintSet.clear(
            ratingTypography.id,
            ConstraintSet.END
        )

        constraintSet.clear(
            ratingIcon.id,
            ConstraintSet.START
        )

        constraintSet.setVerticalBias(
            ratingTypography.id,
            VERTICAL_BIAS_RATING_TYPOGRAPHY
        )

        constraintSet.connect(
            ratingTypography.id,
            ConstraintSet.START,
            ratingIcon.id,
            ConstraintSet.END,
            getDpFromDimen(context, R.dimen.tokopedianow_product_card_rating_typography_start_margin_normal_state).toIntSafely()
        )

        constraintSet.connect(
            ratingIcon.id,
            ConstraintSet.START,
            ConstraintSet.PARENT_ID,
            ConstraintSet.START,
            getDpFromDimen(context, R.dimen.tokopedianow_product_card_rating_icon_start_margin_normal_state).toIntSafely()
        )

        constraintSet.connect(
            ratingTypography.id,
            ConstraintSet.BOTTOM,
            ConstraintSet.PARENT_ID,
            ConstraintSet.BOTTOM,
            getDpFromDimen(context, R.dimen.tokopedianow_product_card_rating_typography_bottom_margin_normal_state).toIntSafely()
        )

        constraintSet.applyTo(root)
    }

    private fun TextView.rightDrawable(
        drawable: Drawable?
    ) {
        val widthLeft = getDpFromDimen(
            context = context,
            id = R.dimen.tokopedianow_product_card_similar_product_typography_width_left
        ).toIntSafely()

        val heightTop = getDpFromDimen(
            context = context,
            id = R.dimen.tokopedianow_product_card_similar_product_typography_height_top
        ).toIntSafely()

        val width = getDpFromDimen(
            context = context,
            id = R.dimen.tokopedianow_product_card_similar_product_typography_width_right
        ).toIntSafely()

        val height = getDpFromDimen(
            context = context,
            id = R.dimen.tokopedianow_product_card_similar_product_typography_height_bottom
        ).toIntSafely()

        drawable?.setBounds(
            widthLeft,
            heightTop,
            width,
            height
        )

        setCompoundDrawables(
            null,
            null,
            drawable,
            null
        )
    }
}
