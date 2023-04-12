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
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.data.Properties
import com.tokopedia.discovery2.databinding.DiscoItemMultiProductHighlightBinding
import com.tokopedia.discovery2.databinding.DiscoItemSingleProductHighlightBinding
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.media.loader.loadImage
import com.tokopedia.media.loader.loadImageWithoutPlaceholder
import com.tokopedia.mvcwidget.setMargin
import com.tokopedia.unifycomponents.ImageUnify

private const val VIEW_ID_CONSTANT = 100
private const val MARGIN_16 = 16
private const val MARGIN_4 = 4

class ProductHighlightItem(private val productHighlightData: DataItem, private val properties: Properties?,
                           private val constraintLayout: ConstraintLayout, private val constraintSet: ConstraintSet, private val viewWeight: Float? = Utils.DEFAULT_BANNER_WEIGHT,
                           private val index: Int, private val previousProductHighlightItem: ProductHighlightItem?, val context: Context, private val islastItem: Boolean, val compType: String? = null) {

    var productHighlightView = View(context)
    var singleBinding: DiscoItemSingleProductHighlightBinding? = null
    var multipleBinding: DiscoItemMultiProductHighlightBinding? = null

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


        if (viewWeight != null) {
            constraintSet.setHorizontalWeight(productHighlightView.id, viewWeight)
        } else {
            constraintSet.setHorizontalWeight(productHighlightView.id, 1.0f)
        }

        if (previousProductHighlightItem == null) {
            constraintSet.connect(productHighlightView.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START,
                MARGIN_16)
        } else {
            constraintSet.connect(previousProductHighlightItem.productHighlightView.id, ConstraintSet.END, productHighlightView.id, ConstraintSet.START,
                MARGIN_4)
            constraintSet.connect(productHighlightView.id, ConstraintSet.START, previousProductHighlightItem.productHighlightView.id, ConstraintSet.END,
                MARGIN_4)
        }

        if (islastItem) {
            constraintSet.connect(productHighlightView.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END,
                MARGIN_16)
        }
        constraintSet.connect(productHighlightView.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
        if (!productHighlightData.imageUrlDynamicMobile.isNullOrEmpty()) {
            try {
                if (context.isValidGlideContext())
                    (productHighlightView as ImageUnify).loadImage(productHighlightData.imageUrlDynamicMobile)
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
        constraintSet.applyTo(constraintLayout)
    }

    private fun setDataInCard() {

        if (compType == "single") {
            singleBinding = DiscoItemSingleProductHighlightBinding.inflate(LayoutInflater.from(context))
            productHighlightView = singleBinding?.root as ConstraintLayout
            with(singleBinding) {
                if (this != null) {

                    if (productHighlightData.isActive == false) {
                        phImageTextBottom.text = "Terjual Habis"
                    }

                    bgImage.backgroundTintList = ColorStateList.valueOf(Color.parseColor(productHighlightData.boxColor))

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
                        phProductDiscount.text = productHighlightData.discountPercentage
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
                        if (labels.position == "status") {
                            phImageTextBottom.visible()
                            phImageTextBottom.text = labels.title
                        } else if (labels.position == "promo") {
                            if (labels.type.contains("green", ignoreCase = true)) {
                                phProductDiscount.backgroundTintList = ColorStateList.valueOf(
                                    ContextCompat.getColor(
                                        context,
                                        com.tokopedia.unifyprinciples.R.color.Unify_GN100
                                    )
                                )
                                phProductDiscount.setTextColor(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_GN500))
                            }
                            phProductDiscount.visible()
                            phProductDiscount.text = labels.title
                            phDiscountedProductPrice.hide()
                        }
                    }
                }
            }
        } else {
            multipleBinding = DiscoItemMultiProductHighlightBinding.inflate(LayoutInflater.from(context))
            productHighlightView = multipleBinding?.root as ConstraintLayout
            with(multipleBinding) {
                if (this != null) {

                    bgImage.backgroundTintList = ColorStateList.valueOf(Color.parseColor(productHighlightData.boxColor))

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
                        phProductDiscount.text = productHighlightData.discountPercentage
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
                    setMargin(this)

                    productHighlightData.labelsGroupList?.forEach { labels ->
                        if (labels.position == "status") {
                            phImageTextBottom.visible()
                            phImageTextBottom.text = labels.title
                        } else if (labels.position == "promo") {
                            if (labels.type.contains("green", ignoreCase = true)) {
                                phProductDiscount.backgroundTintList = ColorStateList.valueOf(
                                    ContextCompat.getColor(
                                        context,
                                        com.tokopedia.unifyprinciples.R.color.Unify_GN100
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
                        phImageTextBottom.text = "Terjual Habis"
                        val matrix = ColorMatrix().apply {
                            setSaturation(0f)
                        }

                        val filter = ColorMatrixColorFilter(matrix)
                        dataCardParent.setBackgroundColor(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN600))
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
        }
    }

    private fun setMargin(discoItemMultiProductHighlightBinding: DiscoItemMultiProductHighlightBinding) {
        if (compType == "triple") {
            discoItemMultiProductHighlightBinding.phProductName.setMargin(context.resources.getDimensionPixelSize(R.dimen.dp_8), context.resources.getDimensionPixelSize(R.dimen.dp_40), context.resources.getDimensionPixelSize(R.dimen.dp_8), 0)
        }
    }
}
