package com.tokopedia.expresscheckout.view.variant.viewholder

import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
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

class QuantityViewHolder : AbstractViewHolder<QuantityViewModel> {

    private var actionListener: CheckoutVariantActionListener
    private lateinit var quantityChangeDebounceListener: QuantityChangeDebounceListener
    private lateinit var element: QuantityViewModel

    constructor(view: View, listener: CheckoutVariantActionListener) : super(view) {
        this.actionListener = listener
        initUpdateShippingRatesDebouncer()
    }

    companion object {
        const val QUANTITY_PLACEHOLDER = "{{value}}"
        val LAYOUT = R.layout.item_quantity_detail_product_page
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
            itemView.et_qty.addTextChangedListener(object : TextWatcher {
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
                    quantityChangeDebounceListener.onDoNext(quantityModel)
                }
            })

            itemView.tv_quantity_stock_available.text = element.stockWording

            setupMinButton(element)
            setupPlusButton(element)
            validateQuantity(element)
            actionListener.onNeedToValidateButtonBuyVisibility()
        }
    }

    private fun setupMinButton(element: QuantityViewModel) {
        if (element.orderQuantity > element.minOrderQuantity && element.orderQuantity > 0) {
            itemView.btn_qty_min.setImageResource(R.drawable.bg_button_counter_minus_enabled)
            itemView.btn_qty_min.setOnClickListener {
                element.orderQuantity -= 1
                itemView.et_qty.setText(element.orderQuantity.toString())
            }
        } else {
            itemView.btn_qty_min.setImageResource(R.drawable.bg_button_counter_minus_disabled)
            itemView.btn_qty_min.setOnClickListener { }
        }
    }

    private fun setupPlusButton(element: QuantityViewModel) {
        if (element.orderQuantity < element.maxOrderQuantity) {
            itemView.btn_qty_plus.setImageResource(R.drawable.bg_button_counter_plus_enabled)
            itemView.btn_qty_plus.setOnClickListener {
                element.orderQuantity += 1
                itemView.et_qty.setText(element.orderQuantity.toString())
            }
        } else {
            itemView.btn_qty_plus.setImageResource(R.drawable.bg_button_counter_plus_disabled)
            itemView.btn_qty_plus.setOnClickListener { }
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
        actionListener.onGetCompositeSubscriber().add(Observable.create(Observable.OnSubscribe<QuantityModel> { subscriber ->
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
                        if (quantityModel.newQuantity != quantityModel.previousQuantity) {
                            itemView.et_qty.setSelection(itemView.et_qty.length())
                            quantityModel.previousQuantity = quantityModel.newQuantity
                            element.orderQuantity = quantityModel.newQuantity
                            commitQuantityChange(element)
                        }
                    }
                }))
    }

    private interface QuantityChangeDebounceListener {
        fun onDoNext(quantityModel: QuantityModel)
    }

    data class QuantityModel(
            var previousQuantity: Int,
            var newQuantity: Int
    )

}