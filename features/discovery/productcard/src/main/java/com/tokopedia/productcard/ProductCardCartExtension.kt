package com.tokopedia.productcard

import android.view.View
import android.widget.TextView
import androidx.annotation.IdRes
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.productcard.utils.MAX_VARIANT_QUANTITY
import com.tokopedia.productcard.utils.QUANTITY_EDITOR_DEBOUNCE_IN_MS
import com.tokopedia.unifycomponents.QuantityEditorUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import rx.Observable
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import rx.Subscription

internal class ProductCardCartExtension(private val productCardView: View) {

    private fun <T: View?> findView(@IdRes id: Int): T {
        return productCardView.findViewById(id)
    }

    val buttonAddToCart by lazy { findView<UnifyButton?>(R.id.buttonAddToCart) }
    val buttonDeleteCart by lazy { findView<IconUnify?>(R.id.buttonDeleteCart) }
    val quantityEditorNonVariant by lazy {
        findView<QuantityEditorUnify?>(R.id.quantityEditorNonVariant)
    }
    val buttonAddVariant by lazy { findView<UnifyButton?>(R.id.buttonAddVariant) }
    val buttonSimilarProduct by lazy { findView<UnifyButton?>(R.id.buttonSeeSimilarProduct) }

    val textVariantQuantity by lazy { findView<Typography?>(R.id.textVariantQuantity) }

    var addToCartClickListener: ((View) -> Unit)? = null
    var similarProductClickListener: ((View) -> Unit)? = null
    var addToCartNonVariantClickListener: ATCNonVariantListener? = null

    private val context = productCardView.context
    private var quantityEditorDebounce: QuantityEditorDebounce? = null
    private var quantityEditorDebounceSubscription: Subscription? = null

    fun setProductModel(productCardModel: ProductCardModel) {
        renderButtonAddToCart(productCardModel)
        renderCartEditorNonVariant(productCardModel)
        renderChooseVariant(productCardModel)
    }

    private fun renderButtonAddToCart(productCardModel: ProductCardModel) {
        when {
            productCardModel.shouldShowAddToCartNonVariantQuantity() ->
                buttonAddToCart?.configureButtonAddToCartNonVariant(productCardModel)

            productCardModel.hasAddToCartButton ->
                buttonAddToCart?.configureButtonAddToCart()

            productCardModel.hasSimilarProductButton ->
                buttonSimilarProduct?.configureButtonSimilarProduct()

            else ->
                buttonAddToCart?.gone()
        }
    }

    private fun UnifyButton.configureButtonAddToCartNonVariant(productCardModel: ProductCardModel) {
        setOnClickListener {
            addToCartNonVariantClick(productCardModel)
        }

        show()
    }

    private fun addToCartNonVariantClick(productCardModel: ProductCardModel) {
        val nonVariant = productCardModel.nonVariant ?: return
        val newValue = nonVariant.minQuantityFinal

        quantityEditorNonVariant?.setValue(newValue)
        quantityEditorNonVariant?.show()
        buttonDeleteCart?.show()
        buttonAddToCart?.gone()

        quantityEditorDebounce?.onQuantityChanged(newValue)
    }

    private fun UnifyButton.configureButtonAddToCart() {
        setOnClickListener {
            addToCartClickListener?.invoke(it)
        }

        show()
    }

    private fun UnifyButton.configureButtonSimilarProduct() {
        setOnClickListener {
            similarProductClickListener?.invoke(it)
        }

        show()
    }

    private fun renderCartEditorNonVariant(productCardModel: ProductCardModel) {
        if (!productCardModel.canShowQuantityEditor())
            removeQuantityEditorComponents()
        else
            showQuantityEditorComponent(productCardModel)
    }

    private fun removeQuantityEditorComponents() {
        clear()

        quantityEditorNonVariant?.gone()
        buttonDeleteCart?.gone()
    }

    private fun showQuantityEditorComponent(productCardModel: ProductCardModel) {
        val shouldShowCartEditorComponent = productCardModel.shouldShowCartEditorComponent()

        configureButtonDeleteCart(shouldShowCartEditorComponent, productCardModel)
        configureQuantityEditor(shouldShowCartEditorComponent, productCardModel)
    }

    private fun configureButtonDeleteCart(
            shouldShowCartEditorComponent: Boolean,
            productCardModel: ProductCardModel,
    ) {
        buttonDeleteCart?.showWithCondition(shouldShowCartEditorComponent)
        buttonDeleteCart?.setOnClickListener {
            deleteCartClick(productCardModel)
        }
    }

    private fun deleteCartClick(productCardModel: ProductCardModel) {
        buttonAddToCart?.configureButtonAddToCartNonVariant(productCardModel)
        buttonDeleteCart?.gone()
        quantityEditorNonVariant?.gone()

        addToCartNonVariantClickListener?.onQuantityChanged(0)
    }

    private fun configureQuantityEditor(
            shouldShowCartEditorComponent: Boolean,
            productCardModel: ProductCardModel,
    ) {
        configureQuantityEditorDebounce()

        quantityEditorNonVariant?.showWithCondition(shouldShowCartEditorComponent)
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
        this.maxValue = nonVariant.maxQuantityFinal
        this.minValue = nonVariant.minQuantityFinal

        val quantity = nonVariant.quantity
        if (quantity > 0)
            this.setValue(quantity)
    }

    private fun QuantityEditorUnify.onQuantityEditorActionEnter(nonVariant: ProductCardModel.NonVariant) {
        safeguardQuantityEditorInput(nonVariant)

        val inputQuantity = editText.text.toString().toIntOrZero()

        addButton.isEnabled = inputQuantity < nonVariant.maxQuantityFinal
        subtractButton.isEnabled = inputQuantity > nonVariant.minQuantityFinal

        editorChangeQuantity(inputQuantity)
    }

    private fun QuantityEditorUnify.safeguardQuantityEditorInput(
            nonVariant: ProductCardModel.NonVariant
    ) {
        val userQuantity = editText.text.toString().replace(".", "").toIntOrZero()
        val coercedQuantity = userQuantity.coerceIn(nonVariant.quantityRange)
        val coercedQuantityString = coercedQuantity.toString()

        editText.setText(coercedQuantityString, TextView.BufferType.EDITABLE)
        editText.setSelection(coercedQuantityString.length)
    }

    private fun editorChangeQuantity(inputQuantity: Int) {
        quantityEditorDebounce?.onQuantityChanged(inputQuantity)

        dropKeyboard()
    }

    private fun dropKeyboard() {
        context?.let { KeyboardHandler.DropKeyboard(it, productCardView) }
    }

    private fun renderChooseVariant(productCardModel: ProductCardModel) {
        buttonAddVariant?.showWithCondition(productCardModel.hasVariant())

        textVariantQuantity?.shouldShowWithAction(productCardModel.hasVariantWithQuantity()) {
            productCardModel.variant?.let { renderTextVariantQuantity(it.quantity) }
        }
    }

    private fun renderTextVariantQuantity(quantity: Int) {
        if (quantity > MAX_VARIANT_QUANTITY)
            textVariantQuantity?.text =
                    context?.getString(R.string.product_card_text_variant_quantity_grid)
        else
            textVariantQuantity?.text = "$quantity pcs"
    }

    fun clear() {
        if (quantityEditorDebounceSubscription?.isUnsubscribed == false)
            quantityEditorDebounceSubscription?.unsubscribe()

        quantityEditorDebounce = null
        quantityEditorDebounceSubscription = null
    }

    private interface QuantityEditorDebounce {
        fun onQuantityChanged(quantity: Int)
    }
}