package com.tokopedia.productcard

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.productcard.utils.*
import com.tokopedia.productcard.utils.loadImage
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.UnifyButton
import kotlinx.android.synthetic.main.product_card_content_layout.view.*
import kotlinx.android.synthetic.main.product_card_grid_layout.view.*

class ProductCardGridView: BaseCustomView, IProductCardView {

    private var addToCartClickListener: ((View) -> Unit)? = null
    private var addToCartNonVariantClickListener: ATCNonVariantListener? = null

    constructor(context: Context): super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?): super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        View.inflate(context, R.layout.product_card_grid_layout, this)
    }

    override fun setProductModel(productCardModel: ProductCardModel) {
        imageProduct?.loadImage(productCardModel.productImageUrl)

        val isShowCampaign = productCardModel.isShowLabelCampaign()
        renderLabelCampaign(
                isShowCampaign,
                labelCampaignBackground,
                textViewLabelCampaign,
                productCardModel
        )

        val isShowBestSeller = productCardModel.isShowLabelBestSeller()
        renderLabelBestSeller(isShowBestSeller, labelBestSeller, productCardModel)

        renderOutOfStockView(productCardModel)

        labelProductStatus?.initLabelGroup(productCardModel.getLabelProductStatus())

        textTopAds?.showWithCondition(productCardModel.isTopAds)

        renderProductCardContent(productCardModel, productCardModel.isWideContent)

        renderStockBar(progressBarStock, textViewStockLabel, productCardModel)

        imageThreeDots?.showWithCondition(productCardModel.hasThreeDots)

        renderButtonAddToCart(productCardModel)

        renderQuantityEditorNonVariant(productCardModel)
        setQuantityEditorNonVariantValueChangedListener()

        renderVariant(productCardModel)

        buttonNotify?.showWithCondition(productCardModel.hasNotifyMeButton)

        constraintLayoutProductCard?.post {
            imageThreeDots?.expandTouchArea(
                getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_8),
                getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_16),
                getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_8),
                getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_16)
            )
        }
    }

    fun setImageProductViewHintListener(impressHolder: ImpressHolder, viewHintListener: ViewHintListener) {
        imageProduct?.addOnImpressionListener(impressHolder, viewHintListener)
    }

    fun setThreeDotsOnClickListener(threeDotsClickListener: (View) -> Unit) {
        imageThreeDots?.setOnClickListener(threeDotsClickListener)
    }

    fun setAddToCartOnClickListener(addToCartClickListener: (View) -> Unit) {
        this.addToCartClickListener = addToCartClickListener
    }

    fun setAddToCartNonVariantClickListener(addToCartNonVariantClickListener: ATCNonVariantListener) {
        this.addToCartNonVariantClickListener = addToCartNonVariantClickListener
    }

    fun setNotifyMeOnClickListener(notifyMeClickListener: (View) -> Unit) {
        buttonNotify?.setOnClickListener(notifyMeClickListener)
    }

    fun setButtonAddVariantClickListener(addVariantClickListener: (View) -> Unit) {
        buttonAddVariant?.setOnClickListener(addVariantClickListener)
    }

    override fun getCardMaxElevation() = cardViewProductCard?.maxCardElevation ?: 0f

    override fun getCardRadius() = cardViewProductCard?.radius ?: 0f

    fun applyCarousel() {
        setCardHeightMatchParent()
    }

    private fun setCardHeightMatchParent() {
        val layoutParams = cardViewProductCard?.layoutParams
        layoutParams?.height = MATCH_PARENT
        cardViewProductCard?.layoutParams = layoutParams
    }

    override fun recycle() {
        imageProduct?.glideClear()
        imageFreeOngkirPromo?.glideClear()
        labelCampaignBackground?.glideClear()
    }

    private fun renderOutOfStockView(productCardModel: ProductCardModel) {
        if (productCardModel.isOutOfStock) {
            textViewStockLabel?.hide()
            progressBarStock?.hide()
            outOfStockOverlay?.visible()
        } else {
            outOfStockOverlay?.gone()
        }
    }

    private fun renderButtonAddToCart(productCardModel: ProductCardModel) {
        buttonAddToCart?.shouldShowWithAction(productCardModel.hasAddToCartButton || productCardModel.shouldShowAddToCartNonVariantQuantity()) {
            buttonAddToCart?.setOnClickListener {
                if (productCardModel.shouldShowAddToCartNonVariantQuantity()) {
                    productCardModel.nonVariant?.let { nonVariant ->
                        val newValue = nonVariant.minQuantity
                        addToCartNonVariantClickListener?.onQuantityChanged(newValue)
                        quantityEditorNonVariant?.setValue(newValue)
                        quantityEditorNonVariant?.show()
                        buttonAddToCart?.gone()
                    }
                }
                else {
                    addToCartClickListener?.invoke(it)
                }
            }
        }

        if (productCardModel.shouldShowAddToCartNonVariantQuantity()) buttonAddToCart?.buttonType = UnifyButton.Type.MAIN
        else buttonAddToCart?.buttonType = productCardModel.addToCartButtonType
    }

    private fun renderQuantityEditorNonVariant(productCardModel: ProductCardModel) {
        productCardModel.nonVariant?.let {
            quantityEditorNonVariant?.setValue(it.quantity)
            quantityEditorNonVariant?.maxValue = it.maxQuantity
            quantityEditorNonVariant?.minValue = it.minQuantity
        }
        quantityEditorNonVariant?.showWithCondition(productCardModel.shouldShowQuantityEditor())
    }

    private fun setQuantityEditorNonVariantValueChangedListener() {
        quantityEditorNonVariant?.setValueChangedListener { newValue, _, _ ->
            addToCartNonVariantClickListener?.onQuantityChanged(quantity = newValue)
        }
    }

    private fun renderVariant(productCardModel: ProductCardModel) {
        buttonAddVariant?.shouldShowWithAction(productCardModel.hasVariant()) {
            renderButtonAddVariant(productCardModel)
        }

        textVariantQuantity?.shouldShowWithAction(productCardModel.hasVariantWithQuantity()) {
            productCardModel.variant?.let { renderTextVariantQuantity(it.quantity) }
        }

        dividerVariantQuantity?.showWithCondition(productCardModel.hasVariantWithQuantity())
    }

    private fun renderButtonAddVariant(productCardModel: ProductCardModel) {
        if (productCardModel.hasVariantWithQuantity()) buttonAddVariant?.text = context.getString(R.string.product_card_text_add_other_variant_grid)
        else buttonAddVariant?.text = context.getString(R.string.product_card_text_add_variant_grid)
    }

    private fun renderTextVariantQuantity(quantity: Int) {
        if (quantity > 99) textVariantQuantity?.text = context.getString(R.string.product_card_text_variant_quantity_grid)
        else textVariantQuantity?.text = "$quantity pcs"
    }

    override fun getThreeDotsButton(): View? = imageThreeDots

    override fun getNotifyMeButton(): UnifyButton? = buttonNotify

    interface ATCNonVariantListener {
        fun onQuantityChanged(quantity: Int)
    }

}