package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.producthighlight

import android.content.Context
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.discovery2.Constant.ProductHighlight
import com.tokopedia.discovery2.Constant.ProductHighlight.ATC_OCS
import com.tokopedia.discovery2.Constant.ProductHighlight.Type
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.data.Properties
import com.tokopedia.discovery2.databinding.DiscoItemMultiProductHighlightRevampBinding
import com.tokopedia.discovery2.databinding.DiscoItemSingleProductHighlightRevampBinding
import com.tokopedia.discovery2.databinding.DiscoItemTripleProductHighlightRevampBinding
import com.tokopedia.discovery2.databinding.EmptyStateProductHighlightDoubleBinding
import com.tokopedia.discovery2.databinding.EmptyStateProductHighlightSingleBinding
import com.tokopedia.discovery2.viewcontrollers.customview.CashbackView
import com.tokopedia.discovery2.viewcontrollers.customview.PriceBoxView
import com.tokopedia.discovery2.viewcontrollers.customview.ProductHighlightStockShopView
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class ProductHighlightRevampItem(
    private val productHighlightData: DataItem,
    private val properties: Properties?,
    constraintLayout: ConstraintLayout,
    constraintSet: ConstraintSet,
    index: Int,
    previousProductHighlightItem: BaseProductHighlightItem?,
    override val context: Context,
    islastItem: Boolean,
    override val compType: String? = null
) : BaseProductHighlightItem(
    productHighlightData,
    constraintLayout,
    constraintSet,
    index,
    previousProductHighlightItem,
    context,
    islastItem,
    compType
) {

    override var productHighlightView = View(context)

    private var singleBinding: DiscoItemSingleProductHighlightRevampBinding? = null
    private var multipleBinding: DiscoItemMultiProductHighlightRevampBinding? = null
    private var tripleBinding: DiscoItemTripleProductHighlightRevampBinding? = null
    private var emptySingleBinding: EmptyStateProductHighlightSingleBinding? = null
    private var emptyDoubleBinding: EmptyStateProductHighlightDoubleBinding? = null

    private var ocsClickAction: () -> Unit = {}

    init {
        addItemConstrains()
    }

    override fun setDataInCard() {
        when (productHighlightData.typeProductHighlightComponentCard) {
            ProductHighlight.SINGLE -> {
                singleBinding =
                    DiscoItemSingleProductHighlightRevampBinding.inflate(LayoutInflater.from(context))
                productHighlightView = singleBinding?.root as ConstraintLayout
                with(singleBinding) {
                    if (this != null) {
                        productHighlightImage.loadImage(productHighlightData.productImage)
                        priceBox.fontType = Type.SINGLE

                        phProductName.text = productHighlightData.productName

                        renderOCSButton(checkoutBtn)
                        renderStockBarShop(progressBarStock, Type.SINGLE)
                        renderCashback(cashbackView)
                        renderPriceBox(priceBox)
                        renderStatus(phImageTextBottom)

                        if (productHighlightData.isActive == false) {
                            val matrix = ColorMatrix().apply {
                                setSaturation(0f)
                            }

                            val filter = ColorMatrixColorFilter(matrix)
                            productHighlightImage.colorFilter = filter

                            phProductName.setTextColor(
                                MethodChecker.getColor(
                                    context,
                                    unifyprinciplesR.color.Unify_NN900
                                )
                            )
                            cashbackView.changeToInactive()
                            priceBox.changeToInactive()
                        }
                    }
                }
            }

            ProductHighlight.DOUBLESINGLEEMPTY, ProductHighlight.TRIPLESINGLEEMPTY -> {
                emptySingleBinding =
                    EmptyStateProductHighlightSingleBinding.inflate(LayoutInflater.from(context))
                productHighlightView = emptySingleBinding?.root as CardUnify2
                emptySingleBinding?.productHighlightCardImageContainer?.cardElevation = 0F
            }

            ProductHighlight.TRIPLEDOUBLEEMPTY -> {
                emptyDoubleBinding =
                    EmptyStateProductHighlightDoubleBinding.inflate(LayoutInflater.from(context))
                productHighlightView = emptyDoubleBinding?.root as CardUnify2
                emptyDoubleBinding?.productHighlightCardImageContainer?.cardElevation = 0F
            }

            ProductHighlight.DOUBLE -> {
                multipleBinding =
                    DiscoItemMultiProductHighlightRevampBinding.inflate(LayoutInflater.from(context))
                productHighlightView = multipleBinding?.root as ConstraintLayout
                with(multipleBinding) {
                    if (this != null) {
                        productHighlightImage.loadImage(productHighlightData.productImage)
                        priceBox.fontType = Type.DOUBLE

                        phProductName.text = productHighlightData.productName

                        renderOCSButton(checkoutBtn)
                        renderStockBarShop(progressBarStock, Type.DOUBLE)
                        renderCashback(cashbackView)
                        renderPriceBox(priceBox)
                        renderStatus(phImageTextBottom)

                        if (productHighlightData.isActive == false) {
                            val matrix = ColorMatrix().apply {
                                setSaturation(0f)
                            }

                            val filter = ColorMatrixColorFilter(matrix)
                            productHighlightImage.colorFilter = filter

                            phProductName.setTextColor(
                                MethodChecker.getColor(
                                    context,
                                    unifyprinciplesR.color.Unify_NN900
                                )
                            )
                            cashbackView.changeToInactive()
                            priceBox.changeToInactive()
                        }
                    }
                }
            }

            ProductHighlight.TRIPLE -> {
                tripleBinding =
                    DiscoItemTripleProductHighlightRevampBinding.inflate(LayoutInflater.from(context))
                productHighlightView = tripleBinding?.root as ConstraintLayout
                with(tripleBinding) {
                    if (this != null) {
                        productHighlightImage.loadImage(productHighlightData.productImage)

                        phProductName.text = productHighlightData.productName

                        priceBox.fontType = Type.TRIPLE

                        renderOCSButton(checkoutBtn)
                        renderStockBarShop(progressBarStock, Type.TRIPLE)
                        renderCashback(cashbackView)
                        renderPriceBox(priceBox)
                        renderStatus(phImageTextBottom)

                        if (productHighlightData.isActive == false) {
                            val matrix = ColorMatrix().apply {
                                setSaturation(0f)
                            }

                            val filter = ColorMatrixColorFilter(matrix)
                            productHighlightImage.colorFilter = filter

                            phProductName.setTextColor(
                                MethodChecker.getColor(
                                    context,
                                    unifyprinciplesR.color.Unify_NN900
                                )
                            )
                            cashbackView.changeToInactive()
                            priceBox.changeToInactive()
                        }
                    }
                }
            }
        }
    }

    private fun renderOCSButton(checkoutBtn: ImageUnify) {
        checkoutBtn.isVisible = productHighlightData.atcButtonCTA == ATC_OCS

        checkoutBtn.setOnClickListener {
            ocsClickAction.invoke()
        }
    }
    fun onOCSButtonClicked(action: () -> Unit) {
        ocsClickAction = action
    }

    private fun renderPriceBox(view: PriceBoxView) {
        view.apply {
            renderProductPrice(productHighlightData.price)
            renderDiscountedPrice(productHighlightData.discountedPrice)
            renderDiscountPercentage(
                productHighlightData.discountPercentage,
                properties?.ribbon?.fontColor,
                properties?.ribbon?.backgroundColor
            )

            val benefit = productHighlightData.labelsGroupList
                ?.firstOrNull { it.position == ProductHighlight.PROMO }
                ?.title

            renderBenefit(benefit)

            setFontColor(properties?.priceBox?.fontColor)
            setBackgroundColor(
                properties?.priceBox?.backgroundColor,
                properties?.priceBox?.imageUrl
            )
        }
    }

    private fun renderCashback(view: CashbackView) {
        val cashbackLabel = productHighlightData.labelsGroupList
            ?.firstOrNull { it.position == ProductHighlight.PRICE }

        view.renderCashback(cashbackLabel?.title, cashbackLabel?.colors)
    }

    private fun renderStockBarShop(view: ProductHighlightStockShopView, type: Type) {
        view.apply {
            if (productHighlightData.stockWording?.title?.isEmpty() == true || productHighlightData.stockSoldPercentage == null) {
                renderShopInfo(
                    productHighlightData.shopBadgeImageUrl,
                    productHighlightData.shopName
                )

                fontType = type
            } else {
                renderStockBar(
                    productHighlightData.stockSoldPercentage,
                    productHighlightData.stockWording?.title
                )
            }
        }
    }

    private fun renderStatus(phImageTextBottom: Typography) {
        val status = productHighlightData.labelsGroupList
            ?.firstOrNull { it.position == ProductHighlight.STATUS }
            ?.title

        phImageTextBottom.isVisible = !status.isNullOrEmpty()
        phImageTextBottom.text = status
    }
}
