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
import com.tokopedia.unifycomponents.QuantityEditorUnify
import com.tokopedia.unifycomponents.UnifyButton
import kotlinx.android.synthetic.main.product_card_content_layout.view.*
import kotlinx.android.synthetic.main.product_card_grid_layout.view.*
import rx.Observable
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class ProductCardGridView: BaseCustomView, IProductCardView {

    private var addToCartClickListener: ((View) -> Unit)? = null
    private var addToCartNonVariantClickListener: ATCNonVariantListener? = null
    private var quantityEditorDebounce: QuantityEditorDebounce? = null

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

        renderChooseVariant(productCardModel)

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

    fun setAddVariantClickListener(addVariantClickListener: (View) -> Unit) {
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
        val shouldShowAddToCartButton =
                productCardModel.hasAddToCartButton
                        || productCardModel.shouldShowAddToCartNonVariantQuantity()

        buttonAddToCart?.shouldShowWithAction(shouldShowAddToCartButton) {
            buttonAddToCart?.setOnClickListener {
                if (productCardModel.shouldShowAddToCartNonVariantQuantity())
                    addToCartNonVariantClick(productCardModel)
                else
                    addToCartClickListener?.invoke(it)
            }
        }

        if (productCardModel.shouldShowAddToCartNonVariantQuantity())
            buttonAddToCart?.buttonType = UnifyButton.Type.MAIN
        else
            buttonAddToCart?.buttonType = productCardModel.addToCartButtonType
    }

    private fun addToCartNonVariantClick(productCardModel: ProductCardModel) {
        val nonVariant = productCardModel.nonVariant ?: return
        val newValue = nonVariant.minQuantity

        quantityEditorNonVariant?.setValue(newValue)
        quantityEditorNonVariant?.show()
        buttonAddToCart?.gone()
    }

    private fun renderQuantityEditorNonVariant(productCardModel: ProductCardModel) {
        val shouldShowQuantityEditor = productCardModel.shouldShowQuantityEditor()

        quantityEditorNonVariant?.showWithCondition(shouldShowQuantityEditor)
        quantityEditorNonVariant?.configureQuantityEditor(productCardModel)
    }

    private fun QuantityEditorUnify.configureQuantityEditor(productCardModel: ProductCardModel) {
        val nonVariant = productCardModel.nonVariant ?: return

        clearValueChangeListener()
        configureQuantityEditorDebounce()
        configureQuantitySettings(nonVariant)

        setValueChangedListener { newValue, _, _ ->
            quantityEditorDebounce?.onQuantityChanged(newValue)
        }
    }

    private fun QuantityEditorUnify.clearValueChangeListener() {
        setValueChangedListener { _, _, _ -> }
    }

    private fun configureQuantityEditorDebounce() {
        val onSubscribe = Observable.OnSubscribe<Int> {
            quantityEditorDebounce = object : QuantityEditorDebounce {
                override fun onQuantityChanged(quantity: Int) {
                    it.onNext(quantity)
                }
            }
        }

        val quantityEditorSubscriber = object : Subscriber<Int>() {
            override fun onCompleted() {}

            override fun onError(e: Throwable) {}

            override fun onNext(quantity: Int?) {
                if (quantity != null) {
                    addToCartNonVariantClickListener?.onQuantityChanged(quantity)
                }
            }
        }

        Observable.unsafeCreate(onSubscribe)
                .debounce(QUANTITY_EDITOR_DEBOUNCE_IN_MS, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(quantityEditorSubscriber)
    }

    private fun QuantityEditorUnify.configureQuantitySettings(nonVariant: ProductCardModel.NonVariant) {
        val quantity = nonVariant.quantity
        if (quantity > 0)
            this.setValue(quantity)

        this.maxValue = nonVariant.maxQuantity
        this.minValue = nonVariant.minQuantity
    }

    private fun renderChooseVariant(productCardModel: ProductCardModel) {
        buttonAddVariant?.shouldShowWithAction(productCardModel.hasVariant()) {
            renderButtonAddVariant(productCardModel)
        }

        textVariantQuantity?.shouldShowWithAction(productCardModel.hasVariantWithQuantity()) {
            productCardModel.variant?.let { renderTextVariantQuantity(it.quantity) }
        }
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

    override fun getShopBadgeView(): View? = imageShopBadge

    private interface QuantityEditorDebounce {
        fun onQuantityChanged(quantity: Int)
    }

    interface ATCNonVariantListener {
        fun onQuantityChanged(quantity: Int)
    }

}