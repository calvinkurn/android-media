package com.tokopedia.purchase_platform.features.one_click_checkout.order.view.card

import android.text.InputFilter
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.imagepreview.ImagePreviewActivity
import com.tokopedia.purchase_platform.common.utils.QuantityTextWatcher
import com.tokopedia.purchase_platform.features.one_click_checkout.common.MAX_QUANTITY
import com.tokopedia.purchase_platform.features.one_click_checkout.order.view.OrderProduct
import com.tokopedia.purchase_platform.features.one_click_checkout.order.view.OrderProductChild
import com.tokopedia.purchase_platform.features.one_click_checkout.order.view.card.OptionVariantUiModel.Companion.STATE_NOT_AVAILABLE
import com.tokopedia.purchase_platform.features.one_click_checkout.order.view.card.OptionVariantUiModel.Companion.STATE_NOT_SELECTED
import com.tokopedia.purchase_platform.features.one_click_checkout.order.view.card.OptionVariantUiModel.Companion.STATE_SELECTED
import kotlinx.android.synthetic.main.card_order_product.view.*

class OrderProductCard(private val view: View, private val listener: OrderProductCardListener) {

    private lateinit var product: OrderProduct

    private val etQty: EditText
    private val rvProductVariant: RecyclerView

    private lateinit var adapter: OrderProductVariantAdapter

    init {
        etQty = view.et_qty
        rvProductVariant = view.rv_product_variant
    }

    fun setProduct(product: OrderProduct) {
        this.product = product
    }

    fun initView() {
        if (::product.isInitialized) {
            view.tv_shop.text = "WOW QREN SKLI 2"
            view.et_note.filters = arrayOf(InputFilter.LengthFilter(100))
            view.btn_qty_plus.setOnClickListener {
                if (product.quantity!!.orderQuantity < product.quantity!!.maxOrderQuantity) {
                    product.quantity!!.orderQuantity++
                    listener.onProductChange(product)
                }
            }
            view.btn_qty_min.setOnClickListener {
                if (product.quantity!!.orderQuantity > product.quantity!!.minOrderQuantity) {
                    product.quantity!!.orderQuantity--
                    listener.onProductChange(product)
                }
            }
            etQty.addTextChangedListener(QuantityTextWatcher(QuantityTextWatcher.QuantityTextwatcherListener { quantity ->
                if (quantity.editable.isNotEmpty()) {
                    var zeroCount = 0
                    for (element in quantity.editable) {
                        if (element == '0') {
                            zeroCount++
                        } else {
                            break
                        }
                    }
                    if (zeroCount == quantity.editable.length) {
                        product.quantity!!.orderQuantity = 0
                        listener.onProductChange(product)
                        return@QuantityTextwatcherListener
                    } else if (quantity.editable[0] == '0') {
                        etQty.setText(quantity.editable.toString()
                                .substring(zeroCount, quantity.editable.toString().length))
                        etQty.setSelection(etQty.length())
                    }
                } else if (TextUtils.isEmpty(etQty.text)) {
                    product.quantity!!.orderQuantity = 0
                    listener.onProductChange(product)
                    return@QuantityTextwatcherListener
                }

                var qty = 0
                try {
                    qty = Integer.parseInt(quantity.editable.toString())
                } catch (e: NumberFormatException) {
                    e.printStackTrace()
                }

                product.quantity!!.orderQuantity = qty
                listener.onProductChange(product)
            }))
            rvProductVariant.layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
            adapter = OrderProductVariantAdapter(object : CheckoutVariantActionListener {
                override fun onBindVariantGetProductViewModel(): OrderProduct {
                    return product
                }

                override fun onChangeVariant(selectedOptionUiModel: OptionVariantUiModel) {
//                    val productViewModel = fragmentUiModel.getProductViewModel()
//                    val summaryViewModel = fragmentUiModel.getSummaryViewModel()
//                    val quantityViewModel = fragmentUiModel.getQuantityViewModel()
//
                    if (product.productChildrenList.isNotEmpty()) {
                        var selectedKey = 0
                        for ((key, value) in product.selectedVariantOptionsIdMap) {
                            if (key == selectedOptionUiModel.variantId && value != selectedOptionUiModel.optionId) {
                                selectedKey = key
                            }
                        }
                        if (selectedKey != 0) {
                            product.selectedVariantOptionsIdMap[selectedKey] = selectedOptionUiModel.optionId
                        }

                        // Check is product child for selected variant is available
                        var newSelectedProductChild: OrderProductChild? = null
                        for (productChild: OrderProductChild in product.productChildrenList) {
                            var matchOptionId = 0
                            for ((_, value) in product.selectedVariantOptionsIdMap) {
                                if (value in productChild.optionsId) {
                                    matchOptionId++
                                }
                            }
                            if (matchOptionId == product.selectedVariantOptionsIdMap.size) {
                                newSelectedProductChild = productChild
                                break
                            }
                        }

                        if (newSelectedProductChild != null) {
                            for (productChild: OrderProductChild in product.productChildrenList) {
                                productChild.isSelected = productChild.productId == newSelectedProductChild.productId
                            }
//                            onNeedToNotifySingleItem(fragmentUiModel.getIndex(productViewModel))
//
//                            if (summaryViewModel != null) {
//                                summaryViewModel.itemPrice = quantityViewModel?.orderQuantity?.times(newSelectedProductChild.productPrice.toLong())
//                                        ?: 0
//                                onNeedToNotifySingleItem(fragmentUiModel.getIndex(summaryViewModel))
//                            }
//
                            val variantTypeViewModels = product.typeVariantList
                            if (variantTypeViewModels != null) {
                                variantTypeViewModels.removeAt(variantTypeViewModels.lastIndex)
                                val selectedTypeVariantUiModel = SelectedTypeVariantUiModel()
                                for (variantTypeUiModel: VariantUiModel in variantTypeViewModels) {
                                    if (variantTypeUiModel is TypeVariantUiModel) {
                                        if (variantTypeUiModel.variantId == selectedOptionUiModel.variantId) {
                                            variantTypeUiModel.variantSelectedValue = selectedOptionUiModel.variantName
//                                    onNeedToNotifySingleItem(fragmentUiModel.getIndex(variantTypeUiModel))
//                                            break
                                        }
                                        selectedTypeVariantUiModel.selectedList.add(variantTypeUiModel)
                                    }
                                }
                                variantTypeViewModels.add(selectedTypeVariantUiModel)

                                for (variantTypeUiModel: VariantUiModel in variantTypeViewModels) {
                                    if (variantTypeUiModel is TypeVariantUiModel) {
                                        if (variantTypeUiModel.variantId != selectedOptionUiModel.variantId) {
                                            for (optionUiModel: OptionVariantUiModel in variantTypeUiModel.variantOptions) {
                                                // Get other variant type selected option id
                                                val otherVariantSelectedOptionIds = ArrayList<Int>()
                                                for (otherVariantUiModel: VariantUiModel in variantTypeViewModels) {
                                                    if (otherVariantUiModel is TypeVariantUiModel) {
                                                        if (otherVariantUiModel.variantId != variantTypeUiModel.variantId &&
                                                                otherVariantUiModel.variantId != selectedOptionUiModel.variantId) {
                                                            for (otherVariantTypeOption: OptionVariantUiModel in otherVariantUiModel.variantOptions) {
                                                                if (otherVariantTypeOption.currentState == STATE_SELECTED) {
                                                                    otherVariantSelectedOptionIds.add(otherVariantTypeOption.optionId)
                                                                    break
                                                                }
                                                            }
                                                        }
                                                    }
                                                }

                                                // Look for available child
                                                var hasAvailableChild = false
                                                for (productChild: OrderProductChild in product.productChildrenList) {
                                                    hasAvailableChild = checkChildAvailable(productChild, optionUiModel.optionId, selectedOptionUiModel.optionId, otherVariantSelectedOptionIds)
                                                    if (hasAvailableChild) break
                                                }

                                                // Set option id state with checking result
                                                if (!hasAvailableChild) {
                                                    optionUiModel.hasAvailableChild = false
                                                    optionUiModel.currentState = STATE_NOT_AVAILABLE
                                                } else if (optionUiModel.currentState != STATE_SELECTED) {
                                                    optionUiModel.hasAvailableChild = true
                                                    optionUiModel.currentState = STATE_NOT_SELECTED
                                                }
                                            }
//                                        onNeedToNotifySingleItem(fragmentUiModel.getIndex(variantTypeUiModel))
                                        }
                                    }
                                }
                            }
//
//
                            if (product.quantity != null) {
                                if (newSelectedProductChild.isAvailable && newSelectedProductChild.stock == 0) {
                                    product.quantity!!.maxOrderQuantity = MAX_QUANTITY
                                } else {
                                    product.quantity!!.maxOrderQuantity = newSelectedProductChild.stock
                                }
//                                onNeedToNotifySingleItem(fragmentUiModel.getIndex(quantityViewModel))
                            }
                        }
//
//                        reloadRatesDebounceListener.onNeedToRecalculateRates(false)
//                        fragmentUiModel.isStateChanged = true
                        adapter.setList(product.typeVariantList!!)
//                        adapter.notifyDataSetChanged()
                    }
                }

                private fun checkChildAvailable(productChild: OrderProductChild,
                                                optionViewModelId: Int,
                                                currentChangedOptionId: Int,
                                                otherVariantSelectedOptionIds: ArrayList<Int>): Boolean {

                    // Check is child with newly selected option id, other variant selected option ids,
                    // and current looping variant option id is available
                    var otherSelectedOptionIdCount = 0
                    for (optionId: Int in otherVariantSelectedOptionIds) {
                        if (optionId in productChild.optionsId) {
                            otherSelectedOptionIdCount++
                        }
                    }

                    val otherSelectedOptionIdCountEqual = otherSelectedOptionIdCount == otherVariantSelectedOptionIds.size
                    val currentChangedOptionIdAvailable = currentChangedOptionId in productChild.optionsId
                    val optionViewModelIdAvailable = optionViewModelId in productChild.optionsId

                    return productChild.isAvailable && currentChangedOptionIdAvailable && optionViewModelIdAvailable && otherSelectedOptionIdCountEqual
                }

                override fun onVariantGuidelineClick(variantGuideline: String) {
                    view.context?.run {
                        startActivity(ImagePreviewActivity.getCallingIntent(view.context!!,
                                arrayListOf(variantGuideline),
                                null, 0))
                    }
                }
            })
            rvProductVariant.adapter = adapter
            adapter.setList(product.typeVariantList!!)

//            adapter.setList(product.typeVariantList)
        }
    }

    interface OrderProductCardListener {

        fun onProductChange(product: OrderProduct)
    }
}