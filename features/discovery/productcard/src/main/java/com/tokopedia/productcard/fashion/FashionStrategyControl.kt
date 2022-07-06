package com.tokopedia.productcard.fashion

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Space
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.media.loader.loadIcon
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.R
import com.tokopedia.productcard.utils.EXTRA_CHAR_SPACE
import com.tokopedia.productcard.utils.LABEL_VARIANT_CHAR_LIMIT
import com.tokopedia.productcard.utils.LABEL_VARIANT_TAG
import com.tokopedia.productcard.utils.MAX_LABEL_VARIANT_COUNT
import com.tokopedia.productcard.utils.MIN_LABEL_VARIANT_COUNT
import com.tokopedia.productcard.utils.SQUARE_IMAGE_RATIO
import com.tokopedia.productcard.utils.applyConstraintSet
import com.tokopedia.productcard.utils.createColorSampleDrawable
import com.tokopedia.productcard.utils.initLabelGroup
import com.tokopedia.productcard.utils.setupImageRatio
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.video_widget.VideoPlayerView
import com.tokopedia.productcard.utils.renderLabelReposition
import com.tokopedia.productcard.utils.renderLabelBestSeller
import com.tokopedia.productcard.utils.renderLabelBestSellerCategorySide
import com.tokopedia.productcard.utils.renderLabelBestSellerCategoryBottom
import com.tokopedia.productcard.utils.shouldShowWithAction
import com.tokopedia.productcard.utils.toUnifyLabelType
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.utils.contentdescription.TextAndContentDescriptionUtil

internal open class FashionStrategyControl: FashionStrategy {
    override fun setupImageRatio(
        constraintLayoutProductCard: ConstraintLayout?,
        imageProduct: ImageView?,
        mediaAnchorProduct: Space?,
        videoProduct: VideoPlayerView?,
    ) {
        setupImageRatio(
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
            false,
            labelRepositionBackground,
            labelReposition,
            productCardModel.getLabelReposition(),
        )
    }

    override fun renderTextGimmick(view: View, productCardModel: ProductCardModel) {
        val textViewGimmick = view.findViewById<Typography?>(R.id.textViewGimmick)
        if (productCardModel.isShowLabelGimmick())
            textViewGimmick?.initLabelGroup(productCardModel.getLabelGimmick())
        else
            textViewGimmick?.initLabelGroup(null)
    }

    override fun getGimmickSectionHeight(
        context: Context,
        productCardModel: ProductCardModel,
    ): Int {
        return if (productCardModel.isShowLabelGimmick()) {
            val labelGimmick = productCardModel.getLabelGimmick()

            if (labelGimmick != null && labelGimmick.title.isNotEmpty())
                context.resources.getDimensionPixelSize(R.dimen.product_card_text_view_gimmick_height)
            else 0
        }
        else 0
    }

    override fun renderLabelBestSeller(
        labelBestSeller: Typography?,
        productCardModel: ProductCardModel,
    ) {
        val isShowBestSeller = productCardModel.isShowLabelBestSeller()
        renderLabelBestSeller(
            isShowBestSeller,
            labelBestSeller,
            productCardModel
        )
    }

    override fun getLabelBestSellerHeight(
        context: Context,
        productCardModel: ProductCardModel,
    ): Int {
        val hasLabelBestSeller = productCardModel.isShowLabelBestSeller()

        return if (hasLabelBestSeller)
            context.resources.getDimensionPixelSize(R.dimen.product_card_label_best_seller_height) +
                context.resources.getDimensionPixelSize(R.dimen.product_card_label_best_seller_margintop)
        else 0
    }

    override fun getGridViewContentMarginTop(
        context: Context,
        productCardModel: ProductCardModel,
    ): Int {
        val hasLabelBestSeller = productCardModel.isShowLabelBestSeller()

        return if (hasLabelBestSeller)
            context.resources.getDimensionPixelSize(R.dimen.product_card_content_margin_top)
        else context.resources.getDimensionPixelSize(R.dimen.product_card_content_margin)
    }

    override fun renderLabelBestSellerCategorySide(
        textCategorySide: Typography?,
        productCardModel: ProductCardModel,
    ) {
        val isShowCategorySide = productCardModel.isShowLabelCategorySide()

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
        val isShowCategoryBottom = productCardModel.isShowLabelCategoryBottom()

        renderLabelBestSellerCategoryBottom(
            isShowCategoryBottom,
            textCategoryBottom,
            productCardModel
        )
    }

    override fun getTextCategoryBottomHeight(
        context: Context,
        productCardModel: ProductCardModel,
    ): Int {
        val hasLabelCategoryBottom = productCardModel.isShowLabelCategoryBottom()

        return if (hasLabelCategoryBottom)
            context.resources.getDimensionPixelSize(R.dimen.product_card_label_best_seller_category_bottom_height)
        else 0
    }

    override fun moveDiscountConstraint(view: View, productCardModel: ProductCardModel) {
        val constraintLayout = view.findViewById<ConstraintLayout?>(R.id.productCardContentLayout)

        constraintLayout?.applyConstraintSet {
            it.clear(R.id.labelDiscount, ConstraintSet.START)
            it.clear(R.id.labelDiscount, ConstraintSet.TOP)

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

    override fun setDiscountMargin(label: Label) {
        val margin = 0
        label.setMargin(margin, margin, margin, margin)
    }

    override fun renderLabelPrice(view: View, productCardModel: ProductCardModel) {
        val labelPrice = view.findViewById<Label?>(R.id.labelPrice)
        val labelPriceReposition = view.findViewById<Label?>(R.id.labelPriceReposition)
        view.moveLabelPriceConstraint(productCardModel)

        if (productCardModel.isShowDiscountOrSlashPrice())
            labelPrice?.initLabelGroup(null)
        else
            labelPrice?.initLabelGroup(productCardModel.getLabelPrice())

        labelPriceReposition?.initLabelGroup(null)
    }

    override fun renderVariant(
        willShowVariant: Boolean,
        view: View,
        productCardModel: ProductCardModel,
    ) {
        val container = view.findViewById<LinearLayout?>(R.id.labelVariantContainer)
        val colorSampleSize = 14.toPx()

        container?.shouldShowWithAction(willShowVariant) { labelVariantContainer ->
            labelVariantContainer.removeAllViews()

            val marginStart = 4.toPx()

            productCardModel.getRenderedLabelGroupVariantList()
                .forEachIndexed { index, labelVariant ->
                    val hasMarginStart = index > 0

                    when {
                        labelVariant.isColor() -> {
                            labelVariantContainer.addLabelVariantColor(
                                labelVariant,
                                hasMarginStart,
                                colorSampleSize,
                                marginStart,
                            )
                        }
                        labelVariant.isSize() -> {
                            labelVariantContainer.addLabelVariantSize(
                                labelVariant,
                                hasMarginStart,
                                marginStart,
                            )
                        }
                        labelVariant.isCustom() -> {
                            labelVariantContainer.addLabelVariantCustom(labelVariant, marginStart)
                        }
                    }
                }
        }

        view.findViewById<LinearLayout?>(R.id.labelColorVariantReposition).hide()
        view.findViewById<Typography?>(R.id.labelSizeVariantReposition).hide()
    }

    override fun renderShopBadge(view: View, productCardModel: ProductCardModel) {
        val imageShopBadge = view.findViewById<ImageView?>(R.id.imageShopBadge)
        val shopBadge = productCardModel.shopBadgeList.find { it.isShown && it.imageUrl.isNotEmpty() }
        imageShopBadge?.shouldShowWithAction(productCardModel.isShowShopBadge()) {
            it.loadIcon(shopBadge?.imageUrl ?: "")
        }

        val imageShopBadgeReposition = view.findViewById<ImageView?>(R.id.imageShopBadgeReposition)
        imageShopBadgeReposition.hide()
    }

    override fun renderTextShopLocation(view: View, productCardModel: ProductCardModel) {
        val textViewShopLocation = view.findViewById<Typography?>(R.id.textViewShopLocation)
        textViewShopLocation?.shouldShowWithAction(
            productCardModel.isShowShopLocation()
        ) {
            TextAndContentDescriptionUtil.setTextAndContentDescription(
                it,
                productCardModel.shopLocation,
                view.context.getString(R.string.content_desc_textViewShopLocation),
            )
        }

        val textViewShopLocationReposition =
            view.findViewById<Typography?>(R.id.textViewShopLocationReposition)

        textViewShopLocationReposition.hide()
    }

    override val sizeCharLimit: Int
        get() = LABEL_VARIANT_CHAR_LIMIT

    override val extraCharSpace: Int
        get() = EXTRA_CHAR_SPACE

    override val colorLimit: Int
        get() = MAX_LABEL_VARIANT_COUNT

    override fun getLabelVariantSizeCount(
        productCardModel: ProductCardModel,
        colorVariantTaken: Int,
    ): Int {
        val hasLabelVariantColor = colorVariantTaken > 0

        return if (hasLabelVariantColor) 0 else MAX_LABEL_VARIANT_COUNT
    }

    override fun getLabelVariantColorCount(
        colorVariant: List<ProductCardModel.LabelGroupVariant>,
    ): Int {
        return if (colorVariant.size >= MIN_LABEL_VARIANT_COUNT)
            colorLimit
        else 0
    }

    override fun isSingleLine(willShowVariant: Boolean): Boolean = willShowVariant

    private fun LinearLayout.addLabelVariantColor(
        labelVariant: ProductCardModel.LabelGroupVariant,
        hasMarginStart: Boolean,
        colorSampleSize: Int,
        marginStart: Int
    ) {
        val gradientDrawable = createColorSampleDrawable(context, labelVariant.hexColor)

        val layoutParams = LinearLayout.LayoutParams(colorSampleSize, colorSampleSize)
        layoutParams.marginStart = if (hasMarginStart) marginStart else 0

        val colorSampleImageView = ImageView(context)
        colorSampleImageView.setImageDrawable(gradientDrawable)
        colorSampleImageView.layoutParams = layoutParams
        colorSampleImageView.tag = LABEL_VARIANT_TAG

        addView(colorSampleImageView)
    }

    private fun LinearLayout.addLabelVariantSize(
        labelVariant: ProductCardModel.LabelGroupVariant,
        hasMarginStart: Boolean,
        marginStart: Int
    ) {
        val layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        layoutParams.marginStart = if (hasMarginStart) marginStart else 0

        val unifyLabel = Label(context)
        unifyLabel.setLabelType(labelVariant.type.toUnifyLabelType())
        unifyLabel.text = labelVariant.title
        unifyLabel.layoutParams = layoutParams
        unifyLabel.tag = LABEL_VARIANT_TAG

        addView(unifyLabel)
    }

    private fun LinearLayout.addLabelVariantCustom(labelVariant: ProductCardModel.LabelGroupVariant, marginStart: Int) {
        val layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        layoutParams.topMargin = 1.toPx() // Small hack to make custom label center
        layoutParams.marginStart = marginStart

        val typography = Typography(context)
        typography.weightType = Typography.BOLD
        typography.setType(Typography.SMALL)
        typography.text = "+${labelVariant.title}"
        typography.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_68))
        typography.layoutParams = layoutParams
        typography.tag = LABEL_VARIANT_TAG

        addView(typography)
    }

    private fun View.moveLabelPriceConstraint(productCardModel: ProductCardModel) {
        val targetConstraint = if (productCardModel.discountPercentage.isNotEmpty()) R.id.labelDiscount else R.id.textViewSlashedPrice
        val view = findViewById<ConstraintLayout?>(R.id.productCardContentLayout)

        view?.applyConstraintSet {
            it.connect(R.id.labelPrice, ConstraintSet.TOP, targetConstraint, ConstraintSet.BOTTOM, 2.toPx())
        }
    }
}