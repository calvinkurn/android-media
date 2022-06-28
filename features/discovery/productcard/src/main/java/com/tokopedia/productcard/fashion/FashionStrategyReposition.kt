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
import com.tokopedia.productcard.renderLabelVariantSize
import com.tokopedia.productcard.renderVariantColor
import com.tokopedia.productcard.utils.LABEL_VARIANT_CHAR_LIMIT
import com.tokopedia.productcard.utils.LABEL_VARIANT_WITH_LABEL_CHAR_LIMIT
import com.tokopedia.productcard.utils.SQUARE_IMAGE_RATIO
import com.tokopedia.productcard.utils.applyConstraintSet
import com.tokopedia.productcard.utils.getDimensionPixelSize
import com.tokopedia.productcard.utils.initLabelGroup
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.video_widget.VideoPlayerView
import com.tokopedia.productcard.utils.renderLabelBestSeller
import com.tokopedia.productcard.utils.renderLabelBestSellerCategorySide
import com.tokopedia.productcard.utils.renderLabelBestSellerCategoryBottom
import com.tokopedia.productcard.utils.setBottomCorners
import com.tokopedia.productcard.utils.shouldShowWithAction
import com.tokopedia.unifycomponents.Label
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

    override fun setImageRadius(imageProduct: ImageView?, videoProduct: VideoPlayerView?) {
        val cornerRadius: Int =
            imageProduct?.getDimensionPixelSize(R.dimen.product_card_image_corner_radius_fashion) ?: 0
        imageProduct?.setBottomCorners(cornerRadius)
        videoProduct?.setBottomCorners(cornerRadius)
    }

    override fun getImageHeight(imageWidth: Int): Int = imageWidth

    override fun renderOverlayImageRoundedLabel(
        labelImageBackground: ImageView?,
        labelImage: Typography?,
        productCardModel: ProductCardModel,
    ) {
        com.tokopedia.productcard.utils.renderOverlayImageRoundedLabel(
            true,
            labelImageBackground,
            labelImage,
            productCardModel,
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

    override fun renderTextETA(view: View, productCardModel: ProductCardModel) {
        val textViewETA = view.findViewById<Typography?>(R.id.textViewETA)
        textViewETA?.initLabelGroup(null)
    }

    override fun getLabelETAHeight(context: Context, productCardModel: ProductCardModel): Int = 0

    override fun getLabelCampaignHeight(
        context: Context,
        productCardModel: ProductCardModel,
    ): Int = 0

    override fun renderLabelCampaign(
        labelCampaignBackground: ImageView?,
        textViewLabelCampaign: Typography?,
        productCardModel: ProductCardModel,
    ) {
        val isShowCampaign = false

        com.tokopedia.productcard.utils.renderLabelCampaign(
            isShowCampaign,
            labelCampaignBackground,
            textViewLabelCampaign,
            productCardModel
        )
    }

    override fun moveDiscountConstraint(view: View, productCardModel: ProductCardModel) {
        val view = view.findViewById<ConstraintLayout?>(R.id.productCardContentLayout)

        view?.applyConstraintSet {
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

    override fun setDiscountMarginLeft(label: Label) {
        val margin = 0
        val marginLeft = label.context.resources.getDimensionPixelSize(
            R.dimen.product_card_label_discount_margin_left_fashion
        )
        label.setMargin(marginLeft, margin, margin, margin)
    }

    override fun renderLabelPrice(view: View, productCardModel: ProductCardModel) {
        val labelPrice = view.findViewById<Label?>(R.id.labelPrice)
        val labelPriceNextToVariant = view.findViewById<Label?>(R.id.labelPriceNextToVariant)

        labelPrice?.initLabelGroup(null)
        labelPriceNextToVariant?.initLabelGroup(productCardModel.getLabelPrice())
    }

    override fun renderVariant(
        willShowVariant: Boolean,
        view: View,
        productCardModel: ProductCardModel,
        colorSampleSize: Int,
    ) {
        val container = view.findViewById<LinearLayout?>(R.id.labelVariantWithLabelContainer)

        container?.shouldShowWithAction(willShowVariant) { labelVariantWithLabelContainer ->
            labelVariantWithLabelContainer.removeAllViews()

            val renderedLabelGroupVariantList =
                productCardModel.getRenderedLabelGroupVariantList(
                    LABEL_VARIANT_WITH_LABEL_CHAR_LIMIT,
                    productCardModel.getLabelPrice() == null,
                )
            val renderedLabelVariantSizeList = renderedLabelGroupVariantList.filter { it.isSize() }
            val renderedLabelVariantColorList = renderedLabelGroupVariantList.filter { it.isColor() }
            val labelVariantSizeList = productCardModel.labelGroupVariantList.filter { it.isSize() }
            val labelVariantColorList = productCardModel.labelGroupVariantList.filter { it.isColor() }

            val hiddenSizeCount = labelVariantSizeList.size - renderedLabelVariantSizeList.size
            val hiddenColorCount = labelVariantColorList.size - renderedLabelVariantColorList.size

            labelVariantWithLabelContainer.renderLabelVariantSize(
                renderedLabelVariantSizeList,
                hiddenSizeCount,
            )

            labelVariantWithLabelContainer.renderVariantColor(
                renderedLabelVariantColorList,
                hiddenColorCount,
                colorSampleSize,
            )
        }

        view.findViewById<LinearLayout?>(R.id.labelVariantContainer).hide()
    }
}