package com.tokopedia.expresscheckout.view.variant.viewholder

import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.expresscheckout.R
import com.tokopedia.expresscheckout.view.variant.CheckoutVariantActionListener
import com.tokopedia.expresscheckout.view.variant.viewmodel.QuantityViewModel
import kotlinx.android.synthetic.main.item_quantity_detail_product_page.view.*
import rx.Observable
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * Created by Irfan Khoirul on 30/11/18.
 */

class QuantityViewHolder(view: View, listener: CheckoutVariantActionListener) : AbstractViewHolder<QuantityViewModel>(view) {

    private var actionListener: CheckoutVariantActionListener = listener
    private var quantityChangeDebounceListener: QuantityChangeDebounceListener? = null
    private lateinit var element: QuantityViewModel

    companion object {
        const val QUANTITY_PLACEHOLDER = "{{value}}"
        val LAYOUT = R.layout.item_quantity_detail_product_page
    }

    private val textWatcher by lazy {
        object : TextWatcher {
            var previousQuantity: Int = element.orderQuantity
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                var newQuantity = 0
                try {
                    newQuantity = s.toString().toInt()
                } catch (e: NumberFormatException) {
                    e.printStackTrace()
                    element.orderQuantity = 0
                }
                val quantityModel = QuantityModel(previousQuantity, newQuantity)
                // if listener is null, do without debounce
                if (quantityChangeDebounceListener == null) {
                    onNextDebounce(quantityModel)
                } else {
                    quantityChangeDebounceListener!!.onDoNext(quantityModel)
                }
            }
        }
    }

    override fun bind(element: QuantityViewModel?) {
        if (element != null) {
            this.element = element
            if (element.orderQuantity == 0) {
                itemView.et_qty.setText(element.minOrderQuantity.toString())
            } else {
                itemView.et_qty.setText(element.orderQuantity.toString())
            }
            itemView.et_qty.setSelection(itemView.et_qty.length())
            itemView.et_qty.addTextChangedListener(textWatcher)

            itemView.tv_quantity_stock_available.text = MethodChecker.fromHtml(element.stockWording)

            itemView.btn_qty_min.setOnClickListener {
                if (element.orderQuantity > element.minOrderQuantity && element.orderQuantity > 0) {
                    element.orderQuantity -= 1
                    updateQuantityText(element)
                }
            }
            setupMinButton(element)
            itemView.btn_qty_plus.setOnClickListener {
                if (element.orderQuantity < element.maxOrderQuantity) {
                    element.orderQuantity += 1
                    updateQuantityText(element)
                }
            }
            setupPlusButton(element)
            validateQuantity(element)
            actionListener.onNeedToValidateButtonBuyVisibility()
        }
    }

    fun updateQuantityText(element: QuantityViewModel) {
        itemView.et_qty.setText(element.orderQuantity.toString())
        itemView.et_qty.setSelection(itemView.et_qty.text.length)
        setupMinButton(element)
        setupPlusButton(element)
    }

    private fun setupMinButton(element: QuantityViewModel) {
        if (element.orderQuantity > element.minOrderQuantity && element.orderQuantity > 0) {
            itemView.btn_qty_min.setImageResource(R.drawable.bg_button_counter_minus_enabled)
        } else {
            itemView.btn_qty_min.setImageResource(R.drawable.bg_button_counter_minus_disabled)
        }
    }

    private fun setupPlusButton(element: QuantityViewModel) {
        if (element.orderQuantity < element.maxOrderQuantity) {
            itemView.btn_qty_plus.setImageResource(R.drawable.bg_button_counter_plus_enabled)
        } else {
            itemView.btn_qty_plus.setImageResource(R.drawable.bg_button_counter_plus_disabled)
        }
    }

    private fun commitQuantityChange(element: QuantityViewModel) {
        setupMinButton(element)
        setupPlusButton(element)
        if (validateQuantity(element) && adapterPosition != RecyclerView.NO_POSITION) {
            actionListener.onChangeQuantity(element)
        }
        actionListener.onNeedToValidateButtonBuyVisibility()
    }

    private fun validateQuantity(element: QuantityViewModel): Boolean {
        var error: String? = null
        var needToUpdateView = false

        if (element.orderQuantity <= 0 || element.orderQuantity < element.minOrderQuantity) {
            error = element.errorProductMinQuantity.replace(QUANTITY_PLACEHOLDER, "${element.minOrderQuantity}", false)
        } else if (element.orderQuantity > element.maxOrderQuantity) {
            error = element.errorProductMaxQuantity.replace(QUANTITY_PLACEHOLDER, "${element.maxOrderQuantity}", false)
        }

        if (error != null) {
            if (element.isStateError) {
                needToUpdateView = false
            }
            element.isStateError = true
            itemView.tv_error_form_validation.text = error
            itemView.tv_error_form_validation.visibility = View.VISIBLE
        } else {
            needToUpdateView = true
            element.isStateError = false
            itemView.tv_error_form_validation.visibility = View.GONE
        }

        return needToUpdateView
    }

    private fun initUpdateShippingRatesDebouncer() {
        val compositeSubscription = actionListener.onGetCompositeSubscriber()
        compositeSubscription?.run {
            add(Observable.create(Observable.OnSubscribe<QuantityModel> { subscriber ->
                quantityChangeDebounceListener = object : QuantityChangeDebounceListener {
                    override fun onDoNext(quantityModel: QuantityModel) {
                        subscriber.onNext(quantityModel)
                    }
                }
            }).debounce(500, TimeUnit.MILLISECONDS)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Subscriber<QuantityModel>() {
                        override fun onCompleted() {

                        }

                        override fun onError(e: Throwable) {
                            e.printStackTrace()
                        }

                        override fun onNext(quantityModel: QuantityModel) {
                            onNextDebounce(quantityModel)
                        }
                    }))
        }

    }

    private fun onNextDebounce(quantityModel: QuantityModel) {
        if (quantityModel.newQuantity != quantityModel.previousQuantity) {
            textWatcher.previousQuantity = quantityModel.newQuantity
            element.orderQuantity = quantityModel.newQuantity
            commitQuantityChange(element)
        }
    }

    private interface QuantityChangeDebounceListener {
        fun onDoNext(quantityModel: QuantityModel)
    }

    data class QuantityModel(
            var previousQuantity: Int,
            var newQuantity: Int
    )

    init {
        initUpdateShippingRatesDebouncer()
    }

}