package com.tokopedia.productcard

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.productcard.utils.MAX_VARIANT_QUANTITY
import com.tokopedia.productcard.utils.QUANTITY_EDITOR_DEBOUNCE_IN_MS
import com.tokopedia.productcard.utils.expandTouchArea
import com.tokopedia.productcard.utils.getDimensionPixelSize
import com.tokopedia.productcard.utils.glideClear
import com.tokopedia.productcard.utils.initLabelGroup
import com.tokopedia.productcard.utils.loadImage
import com.tokopedia.productcard.utils.renderLabelBestSeller
import com.tokopedia.productcard.utils.renderLabelCampaign
import com.tokopedia.productcard.utils.renderStockBar
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.QuantityEditorUnify
import com.tokopedia.unifycomponents.UnifyButton
import kotlinx.android.synthetic.main.product_card_content_layout.view.*
import kotlinx.android.synthetic.main.product_card_grid_layout.view.*
import rx.Observable
import rx.Subscriber
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class ProductCardGridView: BaseCustomView, IProductCardView {

    private var addToCartClickListener: ((View) -> Unit)? = null
    private var addToCartNonVariantClickListener: ATCNonVariantListener? = null
    private var quantityEditorDebounce: QuantityEditorDebounce? = null
    private var quantityEditorDebounceSubscription: Subscription? = null

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
        clearQuantityEditorSubscription()
    }

    private fun clearQuantityEditorSubscription() {
        if (quantityEditorDebounceSubscription?.isUnsubscribed == false)
            quantityEditorDebounceSubscription?.unsubscribe()

        quantityEditorDebounceSubscription = null
        quantityEditorDebounce = null
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

        quantityEditorDebounce?.onQuantityChanged(newValue)
    }

    private fun renderQuantityEditorNonVariant(productCardModel: ProductCardModel) {
        if (!productCardModel.canShowQuantityEditor()) return

        configureQuantityEditorDebounce()

        quantityEditorNonVariant?.showWithCondition(productCardModel.shouldShowQuantityEditor())
        quantityEditorNonVariant?.configureQuantityEditor(productCardModel)
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
                if (quantity != null && quantity != 0) {
                    addToCartNonVariantClickListener?.onQuantityChanged(quantity)
                }
            }
        }

        quantityEditorDebounceSubscription =
                Observable.unsafeCreate(onSubscribe)
                        .debounce(QUANTITY_EDITOR_DEBOUNCE_IN_MS, TimeUnit.MILLISECONDS)
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(quantityEditorSubscriber)
    }

    private fun QuantityEditorUnify.configureQuantityEditor(productCardModel: ProductCardModel) {
        val nonVariant = productCardModel.nonVariant ?: return

        clearValueChangeListener()
        configureQuantitySettings(nonVariant)

        setAddClickListener {
            editorChangeQuantity(editText.text.toString().toIntOrZero())
        }

        setSubstractListener {
            editorChangeQuantity(editText.text.toString().toIntOrZero())
        }

        editText.setOnEditorActionListener { _, _, _ ->
            onQuantityEditorActionEnter(nonVariant)
            true
        }
    }

    private fun QuantityEditorUnify.clearValueChangeListener() {
        setValueChangedListener { _, _, _ -> }
    }

    private fun QuantityEditorUnify.configureQuantitySettings(nonVariant: ProductCardModel.NonVariant) {
        val quantity = nonVariant.quantity
        if (quantity > 0)
            this.setValue(quantity)

        this.maxValue = nonVariant.maxQuantity
        this.minValue = nonVariant.minQuantity
    }

    private fun QuantityEditorUnify.onQuantityEditorActionEnter(nonVariant: ProductCardModel.NonVariant) {
        val inputQuantity = editText.text.toString().toIntOrZero()

        addButton.isEnabled = inputQuantity < nonVariant.maxQuantity
        subtractButton.isEnabled = inputQuantity > nonVariant.minQuantity

        editorChangeQuantity(inputQuantity)
    }

    private fun editorChangeQuantity(inputQuantity: Int) {
        quantityEditorDebounce?.onQuantityChanged(inputQuantity)

        dropKeyboard(this)
    }

    private fun dropKeyboard(view: View) {
        context?.let { KeyboardHandler.DropKeyboard(it, view) }
    }

    private fun renderChooseVariant(productCardModel: ProductCardModel) {
        buttonAddVariant?.showWithCondition(productCardModel.hasVariant())

        textVariantQuantity?.shouldShowWithAction(productCardModel.hasVariantWithQuantity()) {
            productCardModel.variant?.let { renderTextVariantQuantity(it.quantity) }
        }
    }

    private fun renderTextVariantQuantity(quantity: Int) {
        if (quantity > MAX_VARIANT_QUANTITY) textVariantQuantity?.text = context.getString(R.string.product_card_text_variant_quantity_grid)
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