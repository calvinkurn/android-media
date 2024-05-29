package com.tokopedia.productcard.reimagine.cart

import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.productcard.ATCNonVariantListener
import com.tokopedia.productcard.R
import com.tokopedia.productcard.experiments.ProductCardColor
import com.tokopedia.productcard.reimagine.ProductCardModel
import com.tokopedia.productcard.reimagine.ProductCardType
import com.tokopedia.productcard.reimagine.lazyView
import com.tokopedia.productcard.reimagine.showView
import com.tokopedia.productcard.utils.QUANTITY_EDITOR_DEBOUNCE_IN_MS
import com.tokopedia.unifycomponents.QuantityEditorUnify
import com.tokopedia.unifycomponents.UnifyButton
import rx.Observable
import rx.Subscriber
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.concurrent.TimeUnit

internal class ProductCardCartExtension(
    private val view: View,
    private val type: ProductCardType,
) {

    private val context = view.context

    private val cardConstraintLayout by view.lazyView<ConstraintLayout?>(R.id.productCardConstraintLayout)

    private var quantityEditorDebounce: QuantityEditorDebounce? = null
    private var quantityEditorDebounceSubscription: Subscription? = null

    var addToCartClickListener: ((View) -> Unit)? = null
    var addToCartNonVariantClickListener: ATCNonVariantListener? = null

    val addToCartButton: UnifyButton?
        get() = view.findViewById(R.id.productCardAddToCart)

    val deleteCartButton: IconUnify?
        get() = view.findViewById(R.id.productCardButtonDeleteCart)

    val quantityEditor: QuantityEditorUnify?
        get() = view.findViewById(R.id.productCardQuantityEditor)

    fun render(productCardModel: ProductCardModel) {
        renderAddToCart(productCardModel)

        if (!productCardModel.useQuantityEditor()) removeQuantityEditorComponents()
        else showQuantityEditorComponents(productCardModel)
        
        overrideColor(productCardModel.colorMode)
    }

    private fun renderAddToCart(productCardModel: ProductCardModel) {
        val cardConstraintLayout = cardConstraintLayout ?: return

        view.showView(R.id.productCardAddToCart, productCardModel.showAddToCartButton()) {
            AddToCartButton(cardConstraintLayout, type.addToCartConstraints())
        }

        addToCartButton?.setOnClickListener(addToCartOnClickListener(productCardModel))
    }

    private fun addToCartOnClickListener(productCardModel: ProductCardModel): View.OnClickListener =
        if (productCardModel.useQuantityEditor())
            View.OnClickListener { addToCartNonVariantClick(productCardModel) }
        else
            View.OnClickListener { addToCartClickListener?.invoke(it) }

    private fun addToCartNonVariantClick(productCardModel: ProductCardModel) {
        val nonVariant = productCardModel.nonVariant ?: return
        val newValue = nonVariant.minQuantityFinal

        quantityEditor?.setValue(newValue)
        quantityEditor?.show()
        deleteCartButton?.show()
        addToCartButton?.hide()

        quantityEditorDebounce?.onQuantityChanged(newValue)
    }

    private fun removeQuantityEditorComponents() {
        clear()

        quantityEditor?.gone()
        deleteCartButton?.gone()
    }

    private fun showQuantityEditorComponents(productCardModel: ProductCardModel) {
        renderQuantityEditor(cardConstraintLayout, type, productCardModel)

        configureButtonDeleteCart()
        configureQuantityEditor(productCardModel)
    }

    private fun configureButtonDeleteCart() {
        deleteCartButton?.setOnClickListener {
            deleteCartClick()
        }
    }

    private fun deleteCartClick() {
        addToCartButton?.show()
        deleteCartButton?.gone()
        quantityEditor?.gone()

        addToCartNonVariantClickListener?.onQuantityChanged(0)
    }

    private fun configureQuantityEditor(productCardModel: ProductCardModel) {
        configureQuantityEditorDebounce()

        quantityEditor?.configureQuantityEditor(productCardModel)
    }

    private fun configureQuantityEditorDebounce() {
        val onSubscribe = Observable.OnSubscribe {
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
        context?.let { KeyboardHandler.DropKeyboard(it, view) }
    }

    fun clear() {
        if (quantityEditorDebounceSubscription?.isUnsubscribed == false)
            quantityEditorDebounceSubscription?.unsubscribe()

        quantityEditorDebounce = null
        quantityEditorDebounceSubscription = null
    }

    interface QuantityEditorDebounce {
        fun onQuantityChanged(quantity: Int)
    }

    private fun overrideColor(colorMode: ProductCardColor?) {
        if (colorMode == null) return

        colorMode.quantityEditorColor?.quantityTextColor?.let { quantityTextColor ->
            quantityEditor?.editText?.setTextColor(ContextCompat.getColor(context, quantityTextColor))
        }

        if (colorMode.quantityEditorColor?.buttonDeleteCartColorLight != null && colorMode.quantityEditorColor?.buttonDeleteCartColorDark != null) {
            val buttonDeleteCartLightColor = ContextCompat.getColor(context, colorMode.quantityEditorColor?.buttonDeleteCartColorLight ?: return)
            val buttonDeleteCartDarkColor = ContextCompat.getColor(context, colorMode.quantityEditorColor?.buttonDeleteCartColorDark ?: return)

            deleteCartButton?.setImage(
                newIconId = IconUnify.DELETE,
                newLightEnable = buttonDeleteCartLightColor,
                newDarkDisable = buttonDeleteCartDarkColor
            )
        }

        colorMode.buttonColorMode?.let { buttonColorMode ->
            addToCartButton?.applyColorMode(buttonColorMode)
        }
    }
}
