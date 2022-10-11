package com.tokopedia.productcard.fashion

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Space
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.R
import com.tokopedia.productcard.R.dimen.product_card_label_variant_reposition_color_offset
import com.tokopedia.productcard.utils.COLOR_LIMIT_REPOSITION
import com.tokopedia.productcard.utils.EXTRA_CHAR_SPACE_REPOSITION
import com.tokopedia.productcard.utils.LABEL_VARIANT_CHAR_LIMIT_REPOSITION
import com.tokopedia.productcard.utils.LABEL_VARIANT_TAG
import com.tokopedia.productcard.utils.MAX_LABEL_VARIANT_COUNT
import com.tokopedia.productcard.utils.MIN_LABEL_VARIANT_COUNT
import com.tokopedia.productcard.utils.SQUARE_IMAGE_RATIO
import com.tokopedia.productcard.utils.applyConstraintSet
import com.tokopedia.productcard.utils.createColorSampleDrawable
import com.tokopedia.productcard.utils.initLabelGroup
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.video_widget.VideoPlayerView
import com.tokopedia.productcard.utils.renderLabelBestSeller
import com.tokopedia.productcard.utils.renderLabelBestSellerCategorySide
import com.tokopedia.productcard.utils.renderLabelBestSellerCategoryBottom
import com.tokopedia.productcard.utils.shouldShowWithAction
import com.tokopedia.unifycomponents.Label
import com.tokopedia.productcard.utils.renderLabelReposition
import com.tokopedia.unifycomponents.toPx

internal open class FashionStrategyReposition: FashionStrategy {
    override fun setupImageRatio(
        constraintLayoutProductCard: ConstraintLayout?,
        imageProduct: ImageView?,
        mediaAnchorProduct: Space?,
        videoProduct: VideoPlayerView?,
    ) {
        com.tokopedia.productcard.utils.setupImageRatio(
            constraintLayoutProductCard,
            imageProduct,
            mediaAnchorProduct,
            videoProduct,
            SQUARE_IMAGE_RATIO,
        )
    }

    override fun getImageHeight(imageWidth: Int): Int = imageWidth

    override fun renderLabelReposition(
        labelRepositionBackground: ImageView?,
        labelReposition: Typography?,
        productCardModel: ProductCardModel,
    ) {
        renderLabelReposition(
            true,
            labelRepositionBackground,
            labelReposition,
            productCardModel.getLabelReposition(),
        )
    }

    override fun renderTextGimmick(view: View, productCardModel: ProductCardModel) {
        val textViewGimmick = view.findViewById<Typography?>(R.id.textViewGimmick)
        textViewGimmick?.initLabelGroup(null)
    }

    override fun getGimmickSectionHeight(
        context: Context,
        productCardModel: ProductCardModel,
    ): Int = 0

    override fun renderLabelBestSeller(
        labelBestSeller: Typography?,
        productCardModel: ProductCardModel,
    ) {
        renderLabelBestSeller(
            false,
            labelBestSeller,
            productCardModel
        )
    }

    override fun getLabelBestSellerHeight(
        context: Context,
        productCardModel: ProductCardModel,
    ): Int = 0

    override fun getGridViewContentMarginTop(
        context: Context,
        productCardModel: ProductCardModel,
    ): Int = context.resources.getDimensionPixelSize(R.dimen.product_card_content_margin)

    override fun renderLabelBestSellerCategorySide(
        textCategorySide: Typography?,
        productCardModel: ProductCardModel,
    ) {
        val isShowCategorySide = false
        renderLabelBestSellerCategorySide(
            isShowCategorySide,
            textCategorySide,
            productCardModel
        )
    }

    override fun renderLabelBestSellerCategoryBottom(
        textCategoryBottom: Typography?,
        productCardModel: ProductCardModel,
    ) {
        val isShowCategoryBottom = false

        renderLabelBestSellerCategoryBottom(
            isShowCategoryBottom,
            textCategoryBottom,
            productCardModel
        )
    }

    override fun getTextCategoryBottomHeight(
        context: Context,
        productCardModel: ProductCardModel,
    ): Int = 0

    override fun moveDiscountConstraint(view: View, productCardModel: ProductCardModel) {
        val contentLayout = view.findViewById<ConstraintLayout?>(R.id.productCardContentLayout)

        contentLayout?.applyConstraintSet {
            it.clear(R.id.labelDiscount, ConstraintSet.START)
            it.clear(R.id.labelDiscount, ConstraintSet.TOP)

            if (productCardModel.discountPercentage.isNotEmpty()) {
                it.connect(
                    R.id.labelDiscount,
                    ConstraintSet.START,
                    R.id.textViewPrice,
                    ConstraintSet.END,
                )

                it.connect(
                    R.id.labelDiscount,
                    ConstraintSet.TOP,
                    R.id.textViewPrice,
                    ConstraintSet.TOP,
                )
            } else {
                it.connect(
                    R.id.labelDiscount,
                    ConstraintSet.START,
                    ConstraintSet.PARENT_ID,
                    ConstraintSet.START,
                )

                it.connect(
                    R.id.labelDiscount,
                    ConstraintSet.TOP,
                    R.id.textViewPrice,
                    ConstraintSet.BOTTOM,
                )
            }
        }
    }

    override fun setDiscountMargin(label: Label) {
        val margin = 0
        val marginLeft = label.context.resources.getDimensionPixelSize(
            R.dimen.product_card_label_discount_margin_left_fashion
        )
        label.setMargin(marginLeft, margin, margin, margin)
    }

    override fun renderLabelPrice(view: View, productCardModel: ProductCardModel) {
        val labelPrice = view.findViewById<Label?>(R.id.labelPrice)
        val labelPriceReposition = view.findViewById<Label?>(R.id.labelPriceReposition)

        labelPrice?.initLabelGroup(null)

        if (productCardModel.isShowDiscountOrSlashPrice())
            labelPriceReposition?.initLabelGroup(null)
        else
            labelPriceReposition?.initLabelGroup(productCardModel.getLabelPrice())
    }

    override fun renderVariant(
        willShowVariant: Boolean,
        view: View,
        productCardModel: ProductCardModel,
    ) {
        val colorContainer = view.findViewById<LinearLayout?>(R.id.labelColorVariantReposition)
        val colorSampleSize = 12.toPx()
        val sizeTextView = view.findViewById<Typography?>(R.id.labelSizeVariantReposition)

        val renderedLabelGroupVariantList = productCardModel.getRenderedLabelGroupVariantList()
        val renderedLabelVariantSizeList = renderedLabelGroupVariantList.filter { it.isSize() }
        val renderedLabelVariantColorList = renderedLabelGroupVariantList.filter { it.isColor() }
        val labelVariantSizeList = productCardModel.labelGroupVariantList.filter { it.isSize() }
        val labelVariantColorList = productCardModel.labelGroupVariantList.filter { it.isColor() }

        val hiddenSizeCount = labelVariantSizeList.size - renderedLabelVariantSizeList.size
        val hiddenColorCount = labelVariantColorList.size - renderedLabelVariantColorList.size

        sizeTextView.shouldShowWithAction(
            willShowVariant
                && !productCardModel.isShowDiscountOrSlashPrice()
                && renderedLabelVariantSizeList.isNotEmpty()
        ) {
            it.renderLabelVariantSize(
                renderedLabelVariantSizeList,
                hiddenSizeCount,
            )
        }

        colorContainer.shouldShowWithAction(
            willShowVariant
            && !productCardModel.isShowDiscountOrSlashPrice()
            && renderedLabelVariantColorList.isNotEmpty()
        ) {
            it.renderVariantColor(
                renderedLabelVariantColorList,
                hiddenColorCount,
                colorSampleSize,
            )
        }

        view.findViewById<LinearLayout?>(R.id.labelVariantContainer).hide()
    }

    override val sizeCharLimit: Int
        get() = LABEL_VARIANT_CHAR_LIMIT_REPOSITION

    override val extraCharSpace: Int
        get() = EXTRA_CHAR_SPACE_REPOSITION

    override val colorLimit: Int
        get() = COLOR_LIMIT_REPOSITION

    override fun getLabelVariantSizeCount(
        productCardModel: ProductCardModel,
        colorVariantTaken: Int,
    ): Int {
        return if (productCardModel.getLabelPrice() == null
            || !productCardModel.hasLabelVariantColor()
        ) {
            MAX_LABEL_VARIANT_COUNT
        } else {
            0
        }
    }

    override fun getLabelVariantColorCount(
        colorVariant: List<ProductCardModel.LabelGroupVariant>,
    ): Int {
        return if (colorVariant.size >= MIN_LABEL_VARIANT_COUNT)
            colorLimit
        else 0
    }

    override fun isSingleLine(willShowVariant: Boolean): Boolean = false

    override fun configContentPosition(view: View) {
        val contentLayout = view.findViewById<ConstraintLayout?>(R.id.productCardContentLayout)
        val shopBadgeMarginTop = view.context.resources.getDimensionPixelSize(R.dimen.product_card_shop_badge_margin_top)
        val textViewShopLocationMarginTop = view.context.resources.getDimensionPixelSize(R.dimen.product_card_text_shop_location_margin_top)
        val imageFulfillmentnMarginTop = view.context.resources.getDimensionPixelSize(R.dimen.product_card_fulfillment_badge_margin_top)
        val linearLayoutImageRatingMarginTop = view.context.resources.getDimensionPixelSize(R.dimen.product_card_image_rating_star_margin_top)
        val textViewReviewCountMarginTop = view.context.resources.getDimensionPixelSize(R.dimen.product_card_text_review_count_margin_top)
        val imageFreeOngkirPromoMarginTop = view.context.resources.getDimensionPixelSize(R.dimen.product_card_free_ongkir_badge_margin_top)

        contentLayout?.applyConstraintSet {
            it.clear(R.id.imageShopBadge, ConstraintSet.TOP)
            it.clear(R.id.textViewShopLocation, ConstraintSet.TOP)
            it.clear(R.id.imageFulfillment, ConstraintSet.TOP)
            it.clear(R.id.linearLayoutImageRating, ConstraintSet.TOP)
            it.clear(R.id.textViewReviewCount, ConstraintSet.TOP)
            it.clear(R.id.imageFreeOngkirPromo, ConstraintSet.TOP)

            it.connect(
                R.id.imageShopBadge,
                ConstraintSet.TOP,
                R.id.imageShopRating,
                ConstraintSet.BOTTOM,
                shopBadgeMarginTop,
            )
            it.connect(
                R.id.textViewShopLocation,
                ConstraintSet.TOP,
                R.id.imageShopRating,
                ConstraintSet.BOTTOM,
                textViewShopLocationMarginTop,
            )
            it.connect(
                R.id.imageFulfillment,
                ConstraintSet.TOP,
                R.id.imageShopRating,
                ConstraintSet.BOTTOM,
                imageFulfillmentnMarginTop,
            )
            it.connect(
                R.id.linearLayoutImageRating,
                ConstraintSet.TOP,
                R.id.labelPriceBarrier,
                ConstraintSet.BOTTOM,
                linearLayoutImageRatingMarginTop,
            )
            it.connect(
                R.id.textViewReviewCount,
                ConstraintSet.TOP,
                R.id.labelPriceBarrier,
                ConstraintSet.BOTTOM,
                textViewReviewCountMarginTop,
            )
            it.connect(
                R.id.imageFreeOngkirPromo,
                ConstraintSet.TOP,
                R.id.textViewFulfillment,
                ConstraintSet.BOTTOM,
                imageFreeOngkirPromoMarginTop,
            )
        }
    }

    private fun Typography.renderLabelVariantSize(
        listLabelVariant: List<ProductCardModel.LabelGroupVariant>,
        hiddenSizeCount: Int,
    ) {
        var sizeText = listLabelVariant.joinToString(", ") { it.title }

        if (hiddenSizeCount > 0)
            sizeText += ", +$hiddenSizeCount"

        text = sizeText
    }

    private fun LinearLayout.renderVariantColor(
        listLabelVariant: List<ProductCardModel.LabelGroupVariant>,
        hiddenColorCount: Int,
        colorSampleSize: Int,
    ) {
        this.removeAllViews()

        listLabelVariant.forEachIndexed { index, labelGroupVariant ->
            val gradientDrawable = createColorSampleDrawable(context, labelGroupVariant.hexColor)
            val colorOffset = context.resources.getDimensionPixelSize(
                product_card_label_variant_reposition_color_offset
            )

            val layoutParams = LinearLayout.LayoutParams(colorSampleSize, colorSampleSize)
            layoutParams.marginStart = if (index > 0) colorOffset else 0

            val colorSampleImageView = ImageView(context)
            colorSampleImageView.setImageDrawable(gradientDrawable)
            colorSampleImageView.layoutParams = layoutParams
            colorSampleImageView.tag = LABEL_VARIANT_TAG

            addView(colorSampleImageView)
        }

        if (hiddenColorCount > 0) {
            val additionalTextView = Typography(context)
            additionalTextView.setType(Typography.SMALL)
            additionalTextView.text = " +$hiddenColorCount"
            additionalTextView.tag = LABEL_VARIANT_TAG

            addView(additionalTextView)
        }
    }
}