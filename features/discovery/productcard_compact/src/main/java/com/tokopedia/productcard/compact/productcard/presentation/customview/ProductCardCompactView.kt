package com.tokopedia.productcard.compact.productcard.presentation.customview

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.common.utils.view.MethodChecker
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
import com.tokopedia.productcard.compact.R
import com.tokopedia.productcard.compact.common.util.ViewUtil.doOnPreDraw
import com.tokopedia.productcard.compact.common.util.ViewUtil.getDpFromDimen
import com.tokopedia.productcard.compact.common.util.ViewUtil.getHexColorFromIdColor
import com.tokopedia.productcard.compact.common.util.ViewUtil.inflateView
import com.tokopedia.productcard.compact.common.util.ViewUtil.safeParseColor
import com.tokopedia.productcard.compact.databinding.LayoutProductCardCompactViewBinding
import com.tokopedia.productcard.compact.productcard.presentation.listener.ProductCardCompactViewListener
import com.tokopedia.productcard.compact.productcard.presentation.uimodel.LIGHT_GREEN
import com.tokopedia.productcard.compact.productcard.presentation.uimodel.LIGHT_RED
import com.tokopedia.productcard.compact.productcard.presentation.uimodel.ProductCardCompactUiModel
import com.tokopedia.productcard.compact.productcard.presentation.uimodel.ProductCardCompactUiModel.LabelGroup
import com.tokopedia.productcard.compact.productcard.presentation.uimodel.TEXT_DARK_ORANGE
import com.tokopedia.productcard.compact.productcard.presentation.uimodel.TRANSPARENT_BLACK
import com.tokopedia.productcard.compact.similarproduct.presentation.listener.ProductCardCompactSimilarProductTrackerListener
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.ProgressBarUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

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
        private const val SEPARATOR_STRING = "  • "
    }

    private var listener: ProductCardCompactViewListener? = null

    private var quantityEditor: ProductCardCompactQuantityEditorView? = null
    private var wishlistButton: ProductCardCompactWishlistButtonView? = null
    private var progressBar: ProgressBarUnify? = null
    private var progressTypography: Typography? = null
    private var promoLayout: LinearLayout? = null
    private var promoLabel: Label? = null
    private var slashPriceTypography: Typography? = null

    private var binding: LayoutProductCardCompactViewBinding = LayoutProductCardCompactViewBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    private fun LayoutProductCardCompactViewBinding.setupUi(
        model: ProductCardCompactUiModel
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
            needToShowQuantityEditor = model.needToShowQuantityEditor,
            hasBlockedAddToCart = model.hasBlockedAddToCart
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
            slashPrice = model.slashPrice
        )
        initProductNameTypography(
            name = model.name,
            needToChangeMaxLinesName = model.needToChangeMaxLinesName
        )
        initRatingTypography(
            rating = model.rating,
            sold = model.sold,
            isFlashSale = model.isFlashSale(),
            isNormal = model.isNormal()
        )
        initWeight(
            labelGroup = model.getWeightLabelGroup()
        )
        initOosAndPreOrderLabel(
            labelGroupOos = model.getOosLabelGroup(),
            isOos = model.isOos(),
            labelGroupPreOrder = model.getPreOrderLabelGroup(),
            isPreOrder = model.isPreOrder
        )
        initWishlistButton(
            isOos = model.isOos(),
            isShown = model.isWishlistShown,
            hasBeenWishlist = model.hasBeenWishlist,
            productId = model.productId
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
        initNowUSPTypography(
            labelGroup = model.getNowUSPLabelGroup(),
            isFlashSale = model.isFlashSale()
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
        needToShowQuantityEditor: Boolean,
        hasBlockedAddToCart: Boolean
    ) {
        if (!isOos && needToShowQuantityEditor) {
            inflateQuantityEditor()
            quantityEditor?.apply {
                this.isVariant = isVariant
                this.minQuantity = minOrder
                this.maxQuantity = maxOrder
                this.hasBlockedAddToCart = hasBlockedAddToCart
                this.onQuantityChangedListener = listener?.onQuantityChangedListener
                this.onBlockAddToCartListener = listener?.blockAddToCartListener
                this.onClickAddVariantListener = listener?.clickAddVariantListener
                setQuantity(orderQuantity)
            }
            quantityEditor?.show()
        } else {
            quantityEditor?.hide()
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
        val isDiscountNotBlankOrZero = isDiscountNotBlankOrZero(discount, discountInt)

        if (isDiscountNotBlankOrZero || labelGroup != null) {
            inflatePromoLayout()

            promoLabel?.apply {
                if (isDiscountNotBlankOrZero) {
                    text = if (discountInt.isZero()) {
                        if (discount.last() != PERCENTAGE_CHAR) "$discount$PERCENTAGE_CHAR" else discount
                    } else {
                        context.getString(R.string.product_card_compact_product_card_percentage_format, discountInt)
                    }
                    adjustLabelType(LIGHT_RED)
                } else {
                    labelGroup?.let { labelGroup ->
                        text = labelGroup.title
                        adjustLabelType(labelGroup.type)
                    }
                }
            }
            promoLabel?.show()
            promoLayout?.show()
        } else {
            promoLabel?.hide()
            promoLayout?.hide()
        }
    }

    private fun LayoutProductCardCompactViewBinding.initSlashPriceTypography(
        slashPrice: String
    ) {
        if (slashPrice.isNotBlank()) {
            inflatePromoLayout()
            slashPriceTypography?.apply {
                text = slashPrice
                paintFlags = Paint.STRIKE_THRU_TEXT_FLAG or Paint.ANTI_ALIAS_FLAG
            }
            slashPriceTypography?.show()
            promoLayout?.show()
        } else {
            slashPriceTypography?.hide()
            promoLayout?.hide()
        }
    }

    private fun LayoutProductCardCompactViewBinding.initProductNameTypography(
        name: String,
        needToChangeMaxLinesName: Boolean
    ) {
        productNameTypography.showIfWithBlock(name.isNotBlank()) {
            text = MethodChecker.fromHtml(name)
            maxLines = if (needToChangeMaxLinesName && promoLayout?.isVisible == true) {
                MAX_LINES_NEEDED_TO_CHANGE
            } else {
                DEFAULT_MAX_LINES
            }
        }
    }

    private fun LayoutProductCardCompactViewBinding.initRatingTypography(
        rating: String,
        sold: String,
        isFlashSale: Boolean,
        isNormal: Boolean
    ) {
        ratingIcon.hide()
        ratingTypography.showIfWithBlock(
            rating.isNotBlank() || sold.isNotBlank() &&
                !isFlashSale
        ) {
            var ratingText = ""
            if (rating.isNotBlank()) {
                ratingIcon.show()
                ratingText += rating
            }
            if (sold.isNotBlank()) {
                if (ratingText.isNotBlank()) {
                    ratingText += SEPARATOR_STRING
                }
                ratingText += " $sold"
            }
            text = ratingText
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
                        unifyprinciplesR.color.Unify_GN500
                    )
                )
            )
            setOnClickListener {
                val productCardListener = listener?.productCardCompactListener
                val trackerListener = listener?.similarProductTrackerListener
                productCardListener?.onClickSimilarProduct(productId, trackerListener)
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

    private fun LayoutProductCardCompactViewBinding.initOosAndPreOrderLabel(
        labelGroupOos: LabelGroup?,
        isOos: Boolean,
        labelGroupPreOrder: LabelGroup?,
        isPreOrder: Boolean
    ) {
        when {
            (labelGroupOos != null && isOos) -> {
                oosLabel.apply {
                    show()
                    text = labelGroupOos.title
                    unlockFeature = true
                    adjustBackgroundColor(
                        colorType = labelGroupOos.type
                    )
                }
            }
            (labelGroupPreOrder != null && isPreOrder) -> {
                oosLabel.apply {
                    show()
                    text = labelGroupPreOrder.title
                    unlockFeature = true
                    adjustBackgroundColor(
                        colorType = labelGroupPreOrder.type
                    )
                }
            }
            else -> oosLabel.hide()
        }
    }

    private fun LayoutProductCardCompactViewBinding.initNowUSPTypography(
        labelGroup: LabelGroup?,
        isFlashSale: Boolean
    ) {
        nowUspTypography.showIfWithBlock(labelGroup != null) {
            labelGroup?.let { labelGroup ->
                text = labelGroup.title
                setTextColorCompat(unifyprinciplesR.color.Unify_NN600)
                if (isFlashSale) {
                    adjustNowUSPConstraint()
                }
            }
        }
    }

    private fun LayoutProductCardCompactViewBinding.initWishlistButton(
        isOos: Boolean,
        isShown: Boolean,
        hasBeenWishlist: Boolean,
        productId: String
    ) {
        if (isShown && isOos) {
            inflateWishlistButton()
            wishlistButton?.bind(
                isSelected = hasBeenWishlist,
                productId = productId
            )
            wishlistButton?.setListener(listener?.wishlistButtonListener)
            wishlistButton?.show()
        } else {
            wishlistButton?.hide()
        }
    }

    private fun LayoutProductCardCompactViewBinding.initProgressBar(
        isFlashSale: Boolean,
        progressBarLabel: String,
        progressBarPercentage: Int
    ) {
        if (isFlashSale) {
            inflateProgressBar()

            progressBar?.apply {
                setValue(progressBarPercentage, false)
                progressBarHeight = ProgressBarUnify.SIZE_SMALL
                progressBarColor = intArrayOf(
                    ContextCompat.getColor(
                        context,
                        unifyprinciplesR.color.Unify_RN600
                    ),
                    ContextCompat.getColor(
                        context,
                        unifyprinciplesR.color.Unify_RN500
                    )
                )
                adjustFireIcon(progressBarLabel)
            }

            progressTypography?.apply {
                text = progressBarLabel
                val colorRes = if (progressBarLabel.equals(WORDING_SEGERA_HABIS, ignoreCase = true)) {
                    unifyprinciplesR.color.Unify_RN500
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
            progressTypography?.show()
            progressBar?.show()
        } else {
            progressTypography?.hide()
            progressBar?.hide()
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

    private fun LayoutProductCardCompactViewBinding.adjustNowUSPConstraint() {
        val constraintSet = ConstraintSet()
        constraintSet.clone(root)

        constraintSet.connect(
            nowUspTypography.id,
            ConstraintSet.BOTTOM,
            progressBarViewStub.id,
            ConstraintSet.TOP
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
                    unifyprinciplesR.color.Unify_NN600
                )
            )
        )
    }

    private fun Typography.adjustTextColor(
        colorType: String
    ) {
        when (colorType) {
            TEXT_DARK_ORANGE -> {
                setTextColorCompat(unifyprinciplesR.color.Unify_YN500)
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

    private fun LayoutProductCardCompactViewBinding.inflateWishlistButton() {
        if (wishlistButtonViewStub.parent != null) {
            val view = wishlistButtonViewStub
                .inflateView(R.layout.layout_product_card_compact_wishlist_button)
            wishlistButton = view as? ProductCardCompactWishlistButtonView
            wishlistButtonViewStub.show()
        }
    }

    private fun LayoutProductCardCompactViewBinding.inflateQuantityEditor() {
        if (quantityEditorViewStub.parent != null) {
            val view = quantityEditorViewStub
                .inflateView(R.layout.layout_product_card_compact_quantity_editor)
            quantityEditor = view as? ProductCardCompactQuantityEditorView
            quantityEditorViewStub.show()
        }
    }

    private fun LayoutProductCardCompactViewBinding.inflateProgressBar() {
        if (progressBarViewStub.parent != null) {
            val progressBarView = progressBarViewStub
                .inflateView(R.layout.layout_product_card_compact_progress_bar)
            progressBar = progressBarView as? ProgressBarUnify
            progressBarViewStub.show()
        }

        if (progressTypographyViewStub.parent != null) {
            val progressBarTypographyView = progressTypographyViewStub
                .inflateView(R.layout.layout_product_card_compact_progress_typography)
            progressTypography = progressBarTypographyView as? Typography
            progressTypographyViewStub.show()
        }
    }

    private fun LayoutProductCardCompactViewBinding.inflatePromoLayout() {
        if (promoLayoutViewStub.parent != null) {
            val view = promoLayoutViewStub
                .inflateView(R.layout.layout_product_card_promo)
            promoLayout = view.findViewById(R.id.promo_layout)
            promoLabel = view.findViewById(R.id.promo_label)
            slashPriceTypography = view.findViewById(R.id.slash_price_typography)
            promoLayoutViewStub.show()
        }
    }

    private fun isDiscountNotBlankOrZero(discount: String, discountInt: Int) =
        (discount.isNotBlank() && discount != NO_DISCOUNT_STRING) || !discountInt.isZero()

    fun bind(
        model: ProductCardCompactUiModel,
        listener: ProductCardCompactViewListener
    ) {
        this.listener = listener

        if (model.usePreDraw) {
            binding.root.doOnPreDraw {
                binding.setupUi(model)
            }
        } else {
            binding.setupUi(model)
        }
    }

    interface ProductCardCompactListener {
        fun onClickSimilarProduct(
            productId: String,
            similarProductTrackerListener: ProductCardCompactSimilarProductTrackerListener?
        )
    }
}
