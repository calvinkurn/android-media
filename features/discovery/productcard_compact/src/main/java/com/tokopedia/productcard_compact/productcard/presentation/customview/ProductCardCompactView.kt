package com.tokopedia.productcard_compact.productcard.presentation.customview

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.setTextColorCompat
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showIfWithBlock
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.media.loader.loadImage
import com.tokopedia.productcard_compact.productcard.presentation.uimodel.LIGHT_RED
import com.tokopedia.productcard_compact.productcard.presentation.uimodel.TokoNowProductCardViewUiModel.LabelGroup
import com.tokopedia.productcard_compact.similarproduct.presentation.listener.ProductCardCompactSimilarProductTrackerListener
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.ProgressBarUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.productcard_compact.R
import com.tokopedia.productcard_compact.productcard.presentation.uimodel.LIGHT_GREEN
import com.tokopedia.productcard_compact.productcard.presentation.uimodel.TEXT_DARK_ORANGE
import com.tokopedia.productcard_compact.productcard.presentation.uimodel.TRANSPARENT_BLACK
import com.tokopedia.productcard_compact.productcard.presentation.uimodel.TokoNowProductCardViewUiModel
import com.tokopedia.productcard_compact.common.util.ViewUtil.doOnPreDraw
import com.tokopedia.productcard_compact.common.util.ViewUtil.getDpFromDimen
import com.tokopedia.productcard_compact.common.util.ViewUtil.getHexColorFromIdColor
import com.tokopedia.productcard_compact.common.util.ViewUtil.safeParseColor
import com.tokopedia.productcard_compact.databinding.LayoutProductCardCompactViewBinding
import com.tokopedia.productcard_compact.similarproduct.presentation.activity.ProductCardCompactSimilarProductActivity

class ProductCardCompactView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : CardView(context, attrs) {

    companion object {
        const val DEFAULT_MAX_LINES = 2
        const val MAX_LINES_NEEDED_TO_CHANGE = 1

        private const val VERTICAL_BIAS_RATING_TYPOGRAPHY = 0.998f
        private const val WORDING_SEGERA_HABIS = "Segera Habis"
        private const val BOUND_DEFAULT_VALUE = 0
        private const val NO_MARGIN = 0
        private const val NO_DISCOUNT_STRING = "0"
        private const val PERCENTAGE_CHAR = '%'
    }

    private var productCardCompactSimilarProductTrackerListener: ProductCardCompactSimilarProductTrackerListener? = null
    private var binding: LayoutProductCardCompactViewBinding

    init {
        binding = LayoutProductCardCompactViewBinding.inflate(
            LayoutInflater.from(context),
            this,
            true
        )
    }

    private fun LayoutProductCardCompactViewBinding.setupUi(
        model: TokoNowProductCardViewUiModel
    ) {
        initImageFilterView(
            imageUrl = model.imageUrl,
            brightness = model.getImageBrightness()
        )
        initQuantityEditor(
            isVariant = model.isVariant,
            minOrder = model.minOrder,
            maxOrder = model.maxOrder,
            orderQuantity = model.orderQuantity,
            isOos = model.isOos(),
            needToShowQuantityEditor = model.needToShowQuantityEditor
        )
        initAssignedValueTypography(
            labelGroup = model.getAssignedValueLabelGroup()
        )
        initMainPriceTypography(
            price = model.price
        )
        initPromoLabel(
            discount = model.discount,
            discountInt = model.discountInt,
            labelGroup = model.getPriceLabelGroup()
        )
        initSlashPriceTypography(
            slashPrice = model.slashPrice,
        )
        initProductNameTypography(
            name = model.name,
            needToChangeMaxLinesName = model.needToChangeMaxLinesName
        )
        initRatingTypography(
            rating = model.rating,
            isFlashSale = model.isFlashSale(),
            isNormal = model.isNormal()
        )
        initWeight(
            labelGroup = model.getWeightLabelGroup()
        )
        initOosLabel(
            labelGroup = model.getOosLabelGroup(),
            isOos = model.isOos()
        )
        initWishlistButton(
            isOos = model.isOos(),
            isShown = model.isWishlistShown,
            hasBeenWishlist = model.hasBeenWishlist,
            productId = model.productId,
        )
        initSimilarProductTypography(
            isOos = model.isOos(),
            isShown = model.isSimilarProductShown,
            productId = model.productId
        )
        initProgressBar(
            isFlashSale = model.isFlashSale(),
            progressBarLabel = model.progressBarLabel,
            progressBarPercentage = model.progressBarPercentage
        )
    }

    private fun LayoutProductCardCompactViewBinding.initImageFilterView(
        imageUrl: String,
        brightness: Float
    ) {
        imageFilterView.loadImage(imageUrl)
        imageFilterView.brightness = brightness
    }

    private fun LayoutProductCardCompactViewBinding.initQuantityEditor(
        isVariant: Boolean,
        minOrder: Int,
        maxOrder: Int,
        orderQuantity: Int,
        isOos: Boolean,
        needToShowQuantityEditor: Boolean
    ) {
        quantityEditor.showIfWithBlock(!isOos && needToShowQuantityEditor) {
            quantityEditor.isVariant = isVariant
            quantityEditor.minQuantity = minOrder
            quantityEditor.maxQuantity = maxOrder
            quantityEditor.setQuantity(
                quantity = orderQuantity
            )
        }
    }

    private fun LayoutProductCardCompactViewBinding.initAssignedValueTypography(
        labelGroup: LabelGroup?
    ) {
        assignedValueTypography.showIfWithBlock(labelGroup != null) {
            labelGroup?.let { labelGroup ->
                text = labelGroup.title
                if (labelGroup.isBestSellerPosition()) {
                    adjustBestSellerLabelBackground(labelGroup)
                } else {
                    adjustTextColor(labelGroup.type)
                }
            }
        }
    }

    private fun LayoutProductCardCompactViewBinding.initMainPriceTypography(
        price: String
    ) {
        mainPriceTypography.showIfWithBlock(price.isNotBlank()) {
            text = price
        }
    }

    private fun LayoutProductCardCompactViewBinding.initPromoLabel(
        discount: String,
        discountInt: Int,
        labelGroup: LabelGroup?
    ) {
        val isDiscountNotBlankOrZero = (discount.isNotBlank() && discount != NO_DISCOUNT_STRING) || !discountInt.isZero()
        promoLayout.showIfWithBlock(isDiscountNotBlankOrZero || labelGroup != null) {
            if (isDiscountNotBlankOrZero) {
                promoLabel.text = if (discountInt.isZero()) {
                    if (discount.last() != PERCENTAGE_CHAR) "$discount${PERCENTAGE_CHAR}" else discount
                } else {
                    context.getString(R.string.product_card_compact_product_card_percentage_format, discountInt)
                }
                promoLabel.adjustLabelType(LIGHT_RED)
            } else {
                labelGroup?.let { labelGroup ->
                    promoLabel.text = labelGroup.title
                    promoLabel.adjustLabelType(labelGroup.type)
                }
            }
        }
    }

    private fun LayoutProductCardCompactViewBinding.initSlashPriceTypography(
        slashPrice: String
    ) {
        slashPriceTypography.showIfWithBlock(slashPrice.isNotBlank()) {
            text = slashPrice
            paintFlags = Paint.STRIKE_THRU_TEXT_FLAG or Paint.ANTI_ALIAS_FLAG
        }
    }

    private fun LayoutProductCardCompactViewBinding.initProductNameTypography(
        name: String,
        needToChangeMaxLinesName: Boolean,
    ) {
        productNameTypography.showIfWithBlock(name.isNotBlank()) {
            text = name
            maxLines = if (needToChangeMaxLinesName && promoLayout.isVisible) {
                MAX_LINES_NEEDED_TO_CHANGE
            } else {
                DEFAULT_MAX_LINES
            }
        }
    }

    private fun LayoutProductCardCompactViewBinding.initRatingTypography(
        rating: String,
        isFlashSale: Boolean,
        isNormal: Boolean
    ) {
        ratingIcon.hide()
        ratingTypography.showIfWithBlock(rating.isNotBlank() && !isFlashSale) {
            ratingIcon.show()
            text = rating
            adjustRatingPosition(isNormal)
        }
    }

    private fun LayoutProductCardCompactViewBinding.initSimilarProductTypography(
        isOos: Boolean,
        isShown: Boolean,
        productId: String
    ) {
        similarProductTypography.showIfWithBlock(isOos && isShown) {
            adjustChevronIcon(
                drawable = getIconUnifyDrawable(
                    context = context,
                    iconId = IconUnify.CHEVRON_DOWN,
                    assetColor = ContextCompat.getColor(
                        context,
                        com.tokopedia.unifyprinciples.R.color.Unify_GN500
                    )
                )
            )
            setOnClickListener {
                val intent = ProductCardCompactSimilarProductActivity.createNewIntent(context, productId, productCardCompactSimilarProductTrackerListener)
                context.startActivity(intent)
            }
        }
    }

    private fun LayoutProductCardCompactViewBinding.initWeight(
        labelGroup: LabelGroup?
    ) {
        categoryInfoTypography.showIfWithBlock(labelGroup != null) {
            labelGroup?.let { labelGroup ->
                text = labelGroup.title
            }
        }
    }

    private fun LayoutProductCardCompactViewBinding.initOosLabel(
        labelGroup: LabelGroup?,
        isOos: Boolean
    ) {
        oosLabel.showIfWithBlock(labelGroup != null && isOos) {
            labelGroup?.let { labelGroup ->
                text = labelGroup.title
                unlockFeature = true
                adjustBackgroundColor(
                    colorType = labelGroup.type
                )
            }
        }
    }

    private fun LayoutProductCardCompactViewBinding.initWishlistButton(
        isOos: Boolean,
        isShown: Boolean,
        hasBeenWishlist: Boolean,
        productId: String
    ) {
        wishlistButton.showIfWithBlock(isShown && isOos) {
            wishlistButton.bind(
                isSelected = hasBeenWishlist,
                productId = productId
            )
        }
    }

    private fun LayoutProductCardCompactViewBinding.initProgressBar(
        isFlashSale: Boolean,
        progressBarLabel: String,
        progressBarPercentage: Int
    ) {
        progressBar.showIfWithBlock(isFlashSale) {
            setValue(progressBarPercentage, false)
            progressBarHeight = ProgressBarUnify.SIZE_SMALL
            progressBarColor = intArrayOf(
                ContextCompat.getColor(
                    context,
                    com.tokopedia.unifycomponents.R.color.Unify_RN600
                ),
                ContextCompat.getColor(
                    context,
                    com.tokopedia.unifycomponents.R.color.Unify_RN500
                )
            )
            adjustFireIcon(progressBarLabel)
        }

        progressTypography.showIfWithBlock(isFlashSale) {
            text = progressBarLabel
            val colorRes = if (progressBarLabel.equals(WORDING_SEGERA_HABIS, ignoreCase = true)) {
                com.tokopedia.unifyprinciples.R.color.Unify_RN500
            } else {
                R.color.product_card_compact_dms_progress_bar_label_text_color
            }
            setTextColor(
                ContextCompat.getColor(
                    context,
                    colorRes
                )
            )
        }
    }

    private fun LayoutProductCardCompactViewBinding.adjustRatingPosition(isNormal: Boolean) {
        val constraintSet = ConstraintSet()

        constraintSet.clone(root)

        if (isNormal) {
            constraintSet.connect(
                ratingTypography.id,
                ConstraintSet.BOTTOM,
                ConstraintSet.PARENT_ID,
                ConstraintSet.BOTTOM,
                NO_MARGIN
            )

            constraintSet.setVerticalBias(
                ratingTypography.id,
                VERTICAL_BIAS_RATING_TYPOGRAPHY
            )
        } else {
            constraintSet.clear(
                ratingTypography.id,
                ConstraintSet.BOTTOM
            )
        }

        constraintSet.applyTo(root)
    }

    private fun ProgressBarUnify.adjustFireIcon(
        progressBarLabel: String
    ) {
        if (progressBarLabel.equals(WORDING_SEGERA_HABIS, ignoreCase = true)) {
            setProgressIcon(
                icon = ContextCompat.getDrawable(
                    context,
                    R.drawable.product_card_compact_ic_product_card_fire_filled
                ),
                width = getDpFromDimen(
                    context = context,
                    id = R.dimen.product_card_compact_product_card_progress_bar_width
                ).toIntSafely(),
                height = getDpFromDimen(
                    context = context,
                    id = R.dimen.product_card_compact_product_card_progress_bar_height
                ).toIntSafely()
            )

            setMargin(
                getDpFromDimen(
                    context = context,
                    id = R.dimen.product_card_compact_product_card_progress_bar_margin_start
                ).toIntSafely(),
                getDpFromDimen(
                    context = context,
                    id = R.dimen.product_card_compact_product_card_progress_bar_margin_top
                ).toIntSafely(),
                getDpFromDimen(
                    context = context,
                    id = R.dimen.product_card_compact_product_card_progress_bar_margin_end
                ).toIntSafely(),
                getDpFromDimen(
                    context = context,
                    id = R.dimen.product_card_compact_product_card_progress_bar_margin_bottom
                ).toIntSafely()
            )
        }
    }

    private fun Typography.adjustChevronIcon(
        drawable: Drawable?
    ) {
        val widthRight = getDpFromDimen(
            context = context,
            id = R.dimen.product_card_compact_product_card_similar_product_typography_width_right
        ).toIntSafely()

        val heightBottom = getDpFromDimen(
            context = context,
            id = R.dimen.product_card_compact_product_card_similar_product_typography_height_bottom
        ).toIntSafely()

        drawable?.setBounds(
            BOUND_DEFAULT_VALUE,
            BOUND_DEFAULT_VALUE,
            widthRight,
            heightBottom
        )

        setCompoundDrawables(
            null,
            null,
            drawable,
            null
        )
    }

    private fun Typography.adjustBestSellerLabelBackground(
        labelGroup: LabelGroup
    ) {
        background = ContextCompat.getDrawable(
            context,
            R.drawable.product_card_compact_bg_product_card_best_seller
        )
        backgroundTintList = ColorStateList.valueOf(
            safeParseColor(
                color = labelGroup.type,
                defaultColor = ContextCompat.getColor(
                    context,
                    com.tokopedia.unifyprinciples.R.color.Unify_NN600
                )
            )
        )
    }

    private fun Typography.adjustTextColor(
        colorType: String
    ) {
        when (colorType) {
            TEXT_DARK_ORANGE -> {
                setTextColorCompat(com.tokopedia.unifyprinciples.R.color.Unify_YN500)
            }
        }
    }

    private fun Label.adjustBackgroundColor(
        colorType: String
    ) {
        when (colorType) {
            TRANSPARENT_BLACK -> {
                setLabelType(
                    getHexColorFromIdColor(
                        context = context,
                        idColor = R.color.product_card_compact_dms_product_card_status_label_background
                    )
                )
            }
        }
    }

    private fun Label.adjustLabelType(
        labelType: String
    ) {
        when (labelType) {
            LIGHT_GREEN -> setLabelType(Label.HIGHLIGHT_LIGHT_GREEN)
            LIGHT_RED -> setLabelType(Label.HIGHLIGHT_LIGHT_RED)
        }
    }

    fun setData(model: TokoNowProductCardViewUiModel) {
        if (model.usePreDraw) {
            binding.root.doOnPreDraw {
                binding.setupUi(model)
            }
        } else {
            binding.setupUi(model)
        }
    }

    fun setOnClickQuantityEditorListener(
        onClickListener: (Int) -> Unit
    ) {
        binding.quantityEditor.onClickListener = onClickListener
    }

    fun setOnClickQuantityEditorVariantListener(
        onClickVariantListener: (Int) -> Unit
    ) {
        binding.quantityEditor.onClickVariantListener = onClickVariantListener
    }

    fun setSimilarProductTrackerListener(
        productCardCompactSimilarProductTrackerListener: ProductCardCompactSimilarProductTrackerListener?
    ){
        this.productCardCompactSimilarProductTrackerListener = productCardCompactSimilarProductTrackerListener
    }

    fun setWishlistButtonListener(
        wishlistButtonListener: ProductCardCompactWishlistButtonView.TokoNowWishlistButtonListener
    ) {
        binding.wishlistButton.setListener(wishlistButtonListener)
    }
}
