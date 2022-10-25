package com.tokopedia.tokopedianow.common.view

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import com.tokopedia.home_component.util.getHexColorFromIdColor
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.setTextColorCompat
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showIfWithBlock
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.model.LIGHT_GREEN
import com.tokopedia.tokopedianow.common.model.LIGHT_RED
import com.tokopedia.tokopedianow.common.model.LabelGroup
import com.tokopedia.tokopedianow.common.model.TEXT_DARK_ORANGE
import com.tokopedia.tokopedianow.common.model.TRANSPARENT_BLACK
import com.tokopedia.tokopedianow.common.model.TokoNowProductCardViewUiModel
import com.tokopedia.tokopedianow.common.util.ViewUtil.getDpFromDimen
import com.tokopedia.tokopedianow.common.util.ViewUtil.safeParseColor
import com.tokopedia.tokopedianow.databinding.LayoutTokopedianowProductCardViewBinding
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.ProgressBarUnify
import com.tokopedia.unifyprinciples.Typography

class TokoNowProductCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : CardView(context, attrs) {

    companion object {
        private const val VERTICAL_BIAS_RATING_TYPOGRAPHY = 1.0f
        private const val WORDING_SEGERA_HABIS = "Segera Habis"
        private const val BOUND_DEFAULT_VALUE = 0
    }

    private var binding: LayoutTokopedianowProductCardViewBinding

    init {
        binding = LayoutTokopedianowProductCardViewBinding.inflate(
            LayoutInflater.from(context),
            this,
            true
        )
    }

    private fun setupUi(
        model: TokoNowProductCardViewUiModel
    ) {
        binding.apply {
            initImageFilterView(
                imageUrl = model.imageUrl,
                brightness = model.getImageBrightness()
            )
            initQuantityEditor(
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
                labelGroup = model.getPriceLabelGroup()
            )
            initSlashPriceTypography(
                slashPrice = model.slashPrice,
            )
            initProductNameTypography(
                model.name
            )
            initRatingTypography(
                rating = model.rating,
                isFlashSale = model.isFlashSale(),
                isNormal = model.isNormal()
            )
            initOosLabel(
                labelGroup = model.getOosLabelGroup(),
                isOos = model.isOos(),
                hasBeenWishlist = model.hasBeenWishlist
            )
            initWishlistButton(
                isOos = model.isOos(),
                hasBeenWishlist = model.hasBeenWishlist
            )
            initSimilarProductTypography(
                isOos = model.isOos()
            )
            initProgressBar(
                isFlashSale = model.isFlashSale(),
                progressBarLabel = model.progressBarLabel,
                progressBarLabelColor = model.progressBarLabelColor,
                progressBarPercentage = model.progressBarPercentage
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

    private fun LayoutTokopedianowProductCardViewBinding.initQuantityEditor(
        minOrder: Int,
        maxOrder: Int,
        orderQuantity: Int,
        isOos: Boolean,
        needToShowQuantityEditor: Boolean
    ) {
        quantityEditor.showIfWithBlock(!isOos && needToShowQuantityEditor) {
            quantityEditor.minQuantity = minOrder
            quantityEditor.maxQuantity = maxOrder
            quantityEditor.setQuantity(
                quantity = orderQuantity
            )
        }
    }

    private fun LayoutTokopedianowProductCardViewBinding.initAssignedValueTypography(
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

    private fun LayoutTokopedianowProductCardViewBinding.initMainPriceTypography(
        price: String
    ) {
        mainPriceTypography.showIfWithBlock(price.isNotBlank()) {
            text = price
        }
    }

    private fun LayoutTokopedianowProductCardViewBinding.initPromoLabel(
        discount: String,
        labelGroup: LabelGroup?
    ) {
        val isDiscountNotBlank = discount.isNotBlank()
        promoLabel.showIfWithBlock(isDiscountNotBlank || labelGroup != null) {
            if (isDiscountNotBlank) {
                text = discount
                adjustLabelType(LIGHT_RED)
            } else {
                labelGroup?.let { labelGroup ->
                    text = labelGroup.title
                    adjustLabelType(labelGroup.type)
                }
            }
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
        ratingIcon.hide()
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
        }
    }

    private fun LayoutTokopedianowProductCardViewBinding.initOosLabel(
        labelGroup: LabelGroup?,
        isOos: Boolean,
        hasBeenWishlist: Boolean
    ) {
        oosLabel.showIfWithBlock( labelGroup != null && isOos) {
            labelGroup?.let { labelGroup ->
                text = labelGroup.title
                unlockFeature = true
                adjustBackgroundColor(
                    colorType = labelGroup.type
                )
            }
        }
    }

    private fun LayoutTokopedianowProductCardViewBinding.initWishlistButton(
        isOos: Boolean,
        hasBeenWishlist: Boolean
    ) {
        wishlistButton.showIfWithBlock(isOos) {
            wishlistButton.setValue(hasBeenWishlist)
        }
    }

    private fun LayoutTokopedianowProductCardViewBinding.initProgressBar(
        isFlashSale: Boolean,
        progressBarLabel: String,
        progressBarLabelColor: String,
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
            progressTypography.showIfWithBlock(isFlashSale) {
                text = progressBarLabel
                if (progressBarLabelColor.isNotBlank()) {
                    setTextColorCompat(
                        resourceId = com.tokopedia.unifycomponents.R.color.Unify_RN500
                    )
                }
            }
            adjustFireIcon(progressBarLabel)
        }
    }

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
            getDpFromDimen(
                context = context,
                id = R.dimen.tokopedianow_product_card_rating_typography_start_margin_normal_state
            ).toIntSafely()
        )

        constraintSet.connect(
            ratingIcon.id,
            ConstraintSet.START,
            ConstraintSet.PARENT_ID,
            ConstraintSet.START,
            getDpFromDimen(
                context = context,
                id = R.dimen.tokopedianow_product_card_rating_icon_start_margin_normal_state
            ).toIntSafely()
        )

        constraintSet.connect(
            ratingTypography.id,
            ConstraintSet.BOTTOM,
            ConstraintSet.PARENT_ID,
            ConstraintSet.BOTTOM,
            getDpFromDimen(
                context = context,
                id = R.dimen.tokopedianow_product_card_rating_typography_bottom_margin_normal_state
            ).toIntSafely()
        )

        constraintSet.applyTo(root)
    }

    private fun ProgressBarUnify.adjustFireIcon(
        progressBarLabel: String
    ) {
        if (progressBarLabel.equals(WORDING_SEGERA_HABIS, ignoreCase = true)) {
            setProgressIcon(
                icon = ContextCompat.getDrawable(
                    context,
                    R.drawable.tokopedianow_ic_product_card_fire_filled
                ),
                width = getDpFromDimen(
                    context = context,
                    id = R.dimen.tokopedianow_product_card_progress_bar_width
                ).toIntSafely(),
                height = getDpFromDimen(
                    context = context,
                    id = R.dimen.tokopedianow_product_card_progress_bar_height
                ).toIntSafely()
            )

            setMargin(
                getDpFromDimen(
                    context = context,
                    id = R.dimen.tokopedianow_product_card_progress_bar_margin_start
                ).toIntSafely(),
                getDpFromDimen(
                    context = context,
                    id = R.dimen.tokopedianow_product_card_progress_bar_margin_top
                ).toIntSafely(),
                getDpFromDimen(
                    context = context,
                    id = R.dimen.tokopedianow_product_card_progress_bar_margin_end
                ).toIntSafely(),
                getDpFromDimen(
                    context = context,
                    id = R.dimen.tokopedianow_product_card_progress_bar_margin_bottom
                ).toIntSafely()
            )
        }
    }

    private fun Typography.adjustChevronIcon(
        drawable: Drawable?
    ) {
        val widthRight = getDpFromDimen(
            context = context,
            id = R.dimen.tokopedianow_product_card_similar_product_typography_width_right
        ).toIntSafely()

        val heightBottom = getDpFromDimen(
            context = context,
            id = R.dimen.tokopedianow_product_card_similar_product_typography_height_bottom
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
            R.drawable.tokopedianow_bg_product_card_best_seller
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
                        idColor = R.color.tokopedianow_product_card_dms_status_label_background
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
        setupUi(model)
    }

    fun setOnClickQuantityEditorListener(
        onClickListener: (Int) -> Unit,
        onClickVariantListener: () -> Unit
    ) {
        binding.quantityEditor.onClickListener = onClickListener
        binding.quantityEditor.onClickVariantListener = onClickVariantListener
    }
}
