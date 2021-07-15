package com.tkpd.atcvariant.view.viewholder

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.tkpd.atcvariant.R
import com.tkpd.atcvariant.data.uidata.VariantQuantityDataModel
import com.tkpd.atcvariant.util.PAYLOAD_UPDATE_PRODUCT_ID_ONLY
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.product.detail.common.view.AtcVariantListener
import com.tokopedia.unifycomponents.QuantityEditorUnify
import com.tokopedia.unifyprinciples.Typography
import rx.Observable
import rx.Subscriber
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import timber.log.Timber
import java.util.concurrent.TimeUnit


/**
 * Created by Yehezkiel on 11/05/21
 */
class AtcVariantQuantityViewHolder constructor(
        private val view: View,
        private val listener: AtcVariantListener,
        private val compositeSubscription: CompositeSubscription)
    : AbstractViewHolder<VariantQuantityDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.atc_variant_quantity_viewholder

        private const val QUANTITY_REGEX = "[^0-9]"
        private const val TEXTWATCHER_QUANTITY_DEBOUNCE_TIME = 500L
    }

    private val quantityEditor = view.findViewById<QuantityEditorUnify>(R.id.qty_variant_stock)
    private val txtMinOrder = view.findViewById<Typography>(R.id.txt_desc_quantity)
    private val container = view.findViewById<ConstraintLayout>(R.id.container_atc_variant_qty_editor)
    private val icDelete = view.findViewById<IconUnify>(R.id.ic_variant_delete_quantity)

    private var textWatcher: TextWatcher? = null
    private var quantityDebounceSubscription: Subscription? = null

    init {
        quantityEditor.autoHideKeyboard = true
    }

    override fun bind(element: VariantQuantityDataModel) {
        if (element.shouldShowView) {
            setupQuantityValue(element)
            setupQuantityEditor(element)
            setupDeleteButton(element.shouldShowDeleteButton, element.productId)
            setupMinOrder(element.minOrder)
            showContainer()
        } else {
            hideContainer()
        }
    }

    override fun bind(element: VariantQuantityDataModel, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            return
        }

        when (payloads[0] as? Int) {
            PAYLOAD_UPDATE_PRODUCT_ID_ONLY -> {
                //reassign the listener to update the element
                setupDeleteButton(element.shouldShowDeleteButton, element.productId)
                setupQuantityEditor(element)
            }
        }
    }

    private fun setupMinOrder(minOrder: Int) {
        txtMinOrder.shouldShowWithAction(minOrder > 1) {
            txtMinOrder.text = view.context.getString(R.string.atc_variant_min_order_builder, minOrder)
        }
    }

    private fun setupQuantityEditor(element: VariantQuantityDataModel) {
        removeTextChangedListener()
        initTextWatcherDebouncer(element)
    }

    private fun setupQuantityValue(element: VariantQuantityDataModel) {
        quantityEditor.minValue = element.minOrder
        quantityEditor.maxValue = element.maxOrder
        quantityEditor.setValue(element.quantity)

        if (element.minOrder == element.maxOrder) {
            quantityEditor.addButton.isEnabled = false
            quantityEditor.subtractButton.isEnabled = false
        }
    }

    private fun setupDeleteButton(shouldShowDeleteButton: Boolean, productId: String) {
        icDelete.shouldShowWithAction(shouldShowDeleteButton) {
            icDelete.setOnClickListener {
                listener.onDeleteQuantityClicked(productId)
            }
        }
    }

    fun removeTextChangedListener() {
        if (textWatcher != null) {
            quantityEditor.editText.removeTextChangedListener(textWatcher)
        }
        textWatcher = null

        if (quantityDebounceSubscription != null) {
            compositeSubscription.remove(quantityDebounceSubscription)
        }
        quantityDebounceSubscription = null
    }

    private fun initTextWatcherDebouncer(element: VariantQuantityDataModel) {
        quantityDebounceSubscription = Observable.create(
                Observable.OnSubscribe<Int> { subscriber ->
                    textWatcher = object : TextWatcher {
                        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                        }

                        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        }

                        override fun afterTextChanged(s: Editable) {
                            val quantityInt: Int = s.toString().replace(QUANTITY_REGEX.toRegex(), "").toIntOrZero()
                            subscriber.onNext(quantityInt)
                        }
                    }
                    quantityEditor.editText.addTextChangedListener(textWatcher)
                })
                .debounce(TEXTWATCHER_QUANTITY_DEBOUNCE_TIME, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<Int>() {
                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable) {
                        Timber.d(e)
                    }

                    override fun onNext(quantity: Int) {
                        onNextValueQuantity(quantity, element)
                    }
                })

        compositeSubscription.add(quantityDebounceSubscription)
    }

    private fun onNextValueQuantity(quantity: Int, element: VariantQuantityDataModel) {
        if (quantity < quantityEditor.minValue) {
            quantityEditor?.setValue(quantityEditor.minValue)
        } else if (quantity > quantityEditor.maxValue) {
            quantityEditor?.setValue(quantityEditor.maxValue)
        } else {
            if (element.quantity != quantity) {
                listener.onQuantityUpdate(quantityEditor.getValue(), element.productId, element.quantity)
                element.quantity = quantityEditor.getValue()

                //fire again to update + and - button
                quantityEditor.setValue(quantityEditor.getValue())
            }
        }
    }

    private fun showContainer() {
        container?.layoutParams?.height = ViewGroup.LayoutParams.WRAP_CONTENT
    }

    private fun hideContainer() {
        container?.layoutParams?.height = 0
    }
}