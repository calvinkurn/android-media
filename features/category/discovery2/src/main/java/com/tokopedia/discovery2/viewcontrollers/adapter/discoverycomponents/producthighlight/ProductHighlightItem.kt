package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.producthighlight

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.discovery2.Constant.ProductHighlight.DOUBLE
import com.tokopedia.discovery2.Constant.ProductHighlight.DOUBLESINGLEEMPTY
import com.tokopedia.discovery2.Constant.ProductHighlight.PROMO
import com.tokopedia.discovery2.Constant.ProductHighlight.SINGLE
import com.tokopedia.discovery2.Constant.ProductHighlight.STATUS
import com.tokopedia.discovery2.Constant.ProductHighlight.TRIPLE
import com.tokopedia.discovery2.Constant.ProductHighlight.TRIPLEDOUBLEEMPTY
import com.tokopedia.discovery2.Constant.ProductHighlight.TRIPLESINGLEEMPTY
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.data.Properties
import com.tokopedia.discovery2.databinding.DiscoItemMultiProductHighlightBinding
import com.tokopedia.discovery2.databinding.DiscoItemSingleProductHighlightBinding
import com.tokopedia.discovery2.databinding.EmptyStateProductHighlightDoubleBinding
import com.tokopedia.discovery2.databinding.EmptyStateProductHighlightSingleBinding
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.media.loader.loadImage
import com.tokopedia.media.loader.loadImageWithoutPlaceholder
import com.tokopedia.mvcwidget.setMargin
import com.tokopedia.unifycomponents.CardUnify2

private const val VIEW_ID_CONSTANT = 100

class ProductHighlightItem(
    private val productHighlightData: DataItem,
    private val properties: Properties?,
    private val constraintLayout: ConstraintLayout,
    private val constraintSet: ConstraintSet,
    private val index: Int,
    private val previousProductHighlightItem: ProductHighlightItem?,
    val context: Context,
    private val islastItem: Boolean,
    val compType: String? = null
) {

    var productHighlightView = View(context)
    private var singleBinding: DiscoItemSingleProductHighlightBinding? = null
    private var multipleBinding: DiscoItemMultiProductHighlightBinding? = null
    private var emptySingleBinding: EmptyStateProductHighlightSingleBinding? = null
    private var emptyDoubleBinding: EmptyStateProductHighlightDoubleBinding? = null

    init {
        addItemConstrains()
    }

    private fun addItemConstrains() {
        setDataInCard()

        productHighlightView.id = VIEW_ID_CONSTANT + index
        constraintLayout.addView(productHighlightView)
        constraintSet.clear(productHighlightView.id)
        constraintSet.constrainWidth(productHighlightView.id, ConstraintSet.MATCH_CONSTRAINT)
        constraintSet.constrainHeight(productHighlightView.id, ConstraintSet.WRAP_CONTENT)

        if (productHighlightData.typeProductHighlightComponentCard == DOUBLESINGLEEMPTY || productHighlightData.typeProductHighlightComponentCard == TRIPLESINGLEEMPTY) {
            constraintSet.constrainHeight(productHighlightView.id, ConstraintSet.MATCH_CONSTRAINT)
        }

        if (productHighlightData.typeProductHighlightComponentCard == TRIPLEDOUBLEEMPTY) {
            constraintSet.constrainHeight(productHighlightView.id, ConstraintSet.MATCH_CONSTRAINT)
            constraintSet.setHorizontalWeight(productHighlightView.id, 2.0f)
        } else {
            constraintSet.setHorizontalWeight(productHighlightView.id, 1.0f)
        }

        if (previousProductHighlightItem == null) {
            constraintSet.connect(
                productHighlightView.id,
                ConstraintSet.START,
                ConstraintSet.PARENT_ID,
                ConstraintSet.START,
                context.resources.getDimensionPixelSize(R.dimen.dp_16)
            )
        } else {
            constraintSet.connect(
                previousProductHighlightItem.productHighlightView.id,
                ConstraintSet.END,
                productHighlightView.id,
                ConstraintSet.START,
                context.resources.getDimensionPixelSize(R.dimen.dp_4)
            )
            constraintSet.connect(
                productHighlightView.id,
                ConstraintSet.START,
                previousProductHighlightItem.productHighlightView.id,
                ConstraintSet.END,
                context.resources.getDimensionPixelSize(R.dimen.dp_4)
            )
        }

        if (islastItem) {
            constraintSet.connect(
                productHighlightView.id,
                ConstraintSet.END,
                ConstraintSet.PARENT_ID,
                ConstraintSet.END,
                context.resources.getDimensionPixelSize(R.dimen.dp_16)
            )
        }
        constraintSet.connect(productHighlightView.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, context.resources.getDimensionPixelSize(R.dimen.dp_16))
        constraintSet.connect(productHighlightView.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, context.resources.getDimensionPixelSize(R.dimen.dp_4))
        constraintSet.applyTo(constraintLayout)
    }

    private fun setDataInCard() {
        if (productHighlightData.typeProductHighlightComponentCard == SINGLE) {
            singleBinding = DiscoItemSingleProductHighlightBinding.inflate(LayoutInflater.from(context))
            productHighlightView = singleBinding?.root as ConstraintLayout
            with(singleBinding) {
                if (this != null) {
                    try {
                        if(!productHighlightData.boxColor.isNullOrEmpty()) {
                            bgImage.setColorFilter(Color.parseColor(productHighlightData.boxColor))
                        }
                    } catch (e: Exception) {
                        Utils.logException(e)
                    }

                    if (!properties?.supergraphicImageUrl.isNullOrEmpty()) {
                        bgImageSupergraphic.loadImageWithoutPlaceholder(properties?.supergraphicImageUrl)
                        bgImageSupergraphic.visible()
                    } else {
                        bgImageSupergraphic.hide()
                    }
                    productHighlightImage.loadImage(productHighlightData.productImage)

                    if (!productHighlightData.shopLogo.isNullOrEmpty()) {
                        phShopLogo.visible()
                        phShopLogo.loadImage(productHighlightData.shopLogo)
                    } else {
                        phShopLogo.hide()
                    }

                    phProductName.text = productHighlightData.productName

                    if (!productHighlightData.price.isNullOrEmpty()) {
                        phProductPrice.visible()
                        phProductPrice.text = productHighlightData.price
                    } else {
                        phProductPrice.invisible()
                    }

                    if (!productHighlightData.discountPercentage.isNullOrEmpty()) {
                        phProductDiscount.visible()
                        try {
                            phProductDiscount.text = if (productHighlightData.discountPercentage.toIntOrZero() > 0) "${productHighlightData.discountPercentage}%" else ""
                        } catch (e: Exception) {
                            phProductDiscount.hide()
                            Utils.logException(e)
                        }
                    } else {
                        phProductDiscount.hide()
                    }

                    if (!productHighlightData.discountedPrice.isNullOrEmpty()) {
                        phDiscountedProductPrice.visible()
                        phDiscountedProductPrice.text = productHighlightData.discountedPrice
                        phDiscountedProductPrice.strikethrough()
                    } else {
                        phDiscountedProductPrice.invisible()
                    }

                    productHighlightData.labelsGroupList?.forEach { labels ->
                        if (labels.position == STATUS) {
                            phImageTextBottom.visible()
                            phImageTextBottom.text = labels.title
                        } else if (labels.position == PROMO) {
                            if (labels.type.contains("green", ignoreCase = true)) {
                                phProductDiscount.backgroundTintList = ColorStateList.valueOf(
                                    ContextCompat.getColor(
                                        context,
                                        R.color.discovery2_dms_clr_C9FDE0
                                    )
                                )
                                phProductDiscount.setTextColor(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_GN500))
                            }
                            phProductDiscount.visible()
                            phProductDiscount.text = labels.title
                            phDiscountedProductPrice.hide()
                        }
                    }

                    if (productHighlightData.isActive == false) {
                        val matrix = ColorMatrix().apply {
                            setSaturation(0f)
                        }

                        val filter = ColorMatrixColorFilter(matrix)
                        bgImage.backgroundTintList = ColorStateList.valueOf(
                            ContextCompat.getColor(
                                context,
                                com.tokopedia.unifyprinciples.R.color.Unify_NN600
                            )
                        )
                        phProductName.setTextColor(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN900))
                        phProductPrice.setTextColor(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN900))
                        phProductDiscount.setTextColor(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN600))
                        phProductDiscount.backgroundTintList = ColorStateList.valueOf(
                            ContextCompat.getColor(
                                context,
                                com.tokopedia.unifyprinciples.R.color.Unify_NN100
                            )
                        )
                        phDiscountedProductPrice.setTextColor(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN900))
                        bgImage.colorFilter = filter
                        productHighlightImage.colorFilter = filter
                        phShopLogo.colorFilter = filter
                    }
                }
            }
        } else if (productHighlightData.typeProductHighlightComponentCard == DOUBLESINGLEEMPTY || productHighlightData.typeProductHighlightComponentCard == TRIPLESINGLEEMPTY) {
            emptySingleBinding = EmptyStateProductHighlightSingleBinding.inflate(LayoutInflater.from(context))
            productHighlightView = emptySingleBinding?.root as CardUnify2
            emptySingleBinding?.productHighlightCardImageContainer?.cardElevation = 0F
        } else if (productHighlightData.typeProductHighlightComponentCard == TRIPLEDOUBLEEMPTY) {
            emptyDoubleBinding = EmptyStateProductHighlightDoubleBinding.inflate(LayoutInflater.from(context))
            productHighlightView = emptyDoubleBinding?.root as CardUnify2
            emptyDoubleBinding?.productHighlightCardImageContainer?.cardElevation = 0F
        } else {
            multipleBinding = DiscoItemMultiProductHighlightBinding.inflate(LayoutInflater.from(context))
            productHighlightView = multipleBinding?.root as ConstraintLayout
            with(multipleBinding) {
                if (this != null) {
                    try {
                        if(!productHighlightData.boxColor.isNullOrEmpty()) {
                            bgImage.setColorFilter(Color.parseColor(productHighlightData.boxColor))
                        }
                    } catch (e: Exception) {
                        Utils.logException(e)
                    }

                    if (!properties?.supergraphicImageUrl.isNullOrEmpty()) {
                        bgImageSupergraphic.loadImageWithoutPlaceholder(properties?.supergraphicImageUrl)
                        bgImageSupergraphic.visible()
                    } else {
                        bgImageSupergraphic.hide()
                    }

                    productHighlightImage.loadImage(productHighlightData.productImage)

                    if (!productHighlightData.shopLogo.isNullOrEmpty()) {
                        phShopLogo.visible()
                        phShopLogo.loadImage(productHighlightData.shopLogo)
                    } else {
                        phShopLogo.hide()
                    }

                    phProductName.text = productHighlightData.productName

                    if (!productHighlightData.price.isNullOrEmpty()) {
                        phProductPrice.visible()
                        phProductPrice.text = productHighlightData.price
                    } else {
                        phProductPrice.invisible()
                    }

                    if (!productHighlightData.discountPercentage.isNullOrEmpty()) {
                        phProductDiscount.visible()
                        try {
                            phProductDiscount.text = if (productHighlightData.discountPercentage.toIntOrZero() > 0) "${productHighlightData.discountPercentage}%" else ""
                        } catch (e: Exception) {
                            phProductDiscount.hide()
                            Utils.logException(e)
                        }
                    } else {
                        phProductDiscount.invisible()
                    }

                    if (!productHighlightData.discountedPrice.isNullOrEmpty()) {
                        phDiscountedProductPrice.visible()
                        phDiscountedProductPrice.text = productHighlightData.discountedPrice
                        phDiscountedProductPrice.strikethrough()
                    } else {
                        phDiscountedProductPrice.invisible()
                    }

                    productHighlightData.labelsGroupList?.forEach { labels ->
                        if (labels.position == STATUS) {
                            phImageTextBottom.visible()
                            phImageTextBottom.text = labels.title
                        } else if (labels.position == PROMO) {
                            if (labels.type.contains("green", ignoreCase = true)) {
                                phProductDiscount.backgroundTintList = ColorStateList.valueOf(
                                    ContextCompat.getColor(
                                        context,
                                        R.color.discovery2_dms_clr_C9FDE0
                                    )
                                )
                                phProductDiscount.setTextColor(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_GN500))
                            }
                            phProductDiscount.visible()
                            phProductDiscount.text = labels.title
                            phDiscountedProductPrice.hide()
                        }
                    }

                    if (productHighlightData.isActive == false) {
                        val matrix = ColorMatrix().apply {
                            setSaturation(0f)
                        }

                        val filter = ColorMatrixColorFilter(matrix)
                        bgImage.backgroundTintList = ColorStateList.valueOf(
                            ContextCompat.getColor(
                                context,
                                com.tokopedia.unifyprinciples.R.color.Unify_NN600
                            )
                        )
                        phProductName.setTextColor(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN900))
                        phProductPrice.setTextColor(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN900))
                        phProductDiscount.setTextColor(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN600))
                        phProductDiscount.backgroundTintList = ColorStateList.valueOf(
                            ContextCompat.getColor(
                                context,
                                com.tokopedia.unifyprinciples.R.color.Unify_NN100
                            )
                        )
                        phDiscountedProductPrice.setTextColor(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN900))
                        bgImage.colorFilter = filter
                        productHighlightImage.colorFilter = filter
                        phShopLogo.colorFilter = filter
                    }
                    setMargin(this)
                }
            }
        }
    }

    private fun setMargin(discoItemMultiProductHighlightBinding: DiscoItemMultiProductHighlightBinding) {
        if (productHighlightData.typeProductHighlightComponentCard == DOUBLE) {
            with(discoItemMultiProductHighlightBinding) {
                phProductPrice.setType(com.tokopedia.unifyprinciples.Typography.DISPLAY_2)
                phProductPrice.setWeight(com.tokopedia.unifyprinciples.Typography.BOLD)
                productHighlightImage.layoutParams.width = getScreenWidth() / 2 - context.resources.getDimensionPixelSize(R.dimen.dp_36)
                guideline.setGuidelineBegin(productHighlightImage.layoutParams.width - context.resources.getDimensionPixelSize(R.dimen.dp_36))
                phProductName.setMargin(context.resources.getDimensionPixelSize(R.dimen.dp_8), context.resources.getDimensionPixelSize(R.dimen.dp_56), context.resources.getDimensionPixelSize(R.dimen.dp_8), 0)
            }
        } else if (productHighlightData.typeProductHighlightComponentCard == TRIPLE) {
            with(discoItemMultiProductHighlightBinding) {
                phProductPrice.setType(com.tokopedia.unifyprinciples.Typography.DISPLAY_3)
                phProductPrice.setWeight(com.tokopedia.unifyprinciples.Typography.BOLD)
                phProductDiscount.setPadding(
                    context.resources.getDimensionPixelOffset(R.dimen.dp_4),
                    context.resources.getDimensionPixelOffset(R.dimen.dp_2),
                    context.resources.getDimensionPixelOffset(R.dimen.dp_4),
                    context.resources.getDimensionPixelOffset(R.dimen.dp_2)
                )
                productHighlightImage.layoutParams.width = getScreenWidth() / 3 - context.resources.getDimensionPixelSize(R.dimen.dp_24)
                guideline.setGuidelineBegin(productHighlightImage.layoutParams.width - context.resources.getDimensionPixelSize(R.dimen.dp_22))
                phProductName.setMargin(context.resources.getDimensionPixelSize(R.dimen.dp_8), context.resources.getDimensionPixelSize(R.dimen.dp_36), context.resources.getDimensionPixelSize(R.dimen.dp_8), 0)
            }
        }
    }
}
