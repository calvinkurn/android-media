package com.tkpd.atcvariant.view.viewholder

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.tkpd.atcvariant.R
import com.tkpd.atcvariant.data.uidata.VariantQuantityDataModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.atc_common.AtcConstant
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
    }

    private val quantityEditor = view.findViewById<QuantityEditorUnify>(R.id.qty_variant_stock)
    private val txtMinOrder = view.findViewById<Typography>(R.id.txt_desc_quantity)
    private val container = view.findViewById<ConstraintLayout>(R.id.container_atc_variant_qty_editor)
    private var textWatcher: TextWatcher? = null
    private var quantityDebounceSubscription: Subscription? = null

    init {
        quantityEditor.autoHideKeyboard = true
    }

    override fun bind(element: VariantQuantityDataModel) {
        if (element.shouldShowView) {
            quantityEditor.minValue = element.minOrder
            quantityEditor.maxValue = element.maxOrder
            quantityEditor.setValue(element.quantity)

            removeTextChangedListener()
            initTextWatcherDebouncer(element)
            txtMinOrder.text = view.context.getString(R.string.atc_variant_min_order_builder, element.minOrder)
            showContainer()
        } else {
            hideContainer()
        }
    }

    fun removeTextChangedListener() {
        if (textWatcher != null) {
            quantityEditor.editText.removeTextChangedListener(textWatcher)
        }
        textWatcher = null

        compositeSubscription.remove(quantityDebounceSubscription)
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
                .debounce(AtcConstant.TEXTWATCHER_QUANTITY_DEBOUNCE_TIME.toLong(), TimeUnit.MILLISECONDS)
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
                element.quantity = quantityEditor.getValue()
                listener.onQuantityUpdate(quantityEditor.getValue(), element.productId)

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