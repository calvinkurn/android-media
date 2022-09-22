package com.tokopedia.tkpd.flashsale.presentation.manageproduct.multilocation.varian.adapter

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.campaign.components.adapter.DelegateAdapter
import com.tokopedia.campaign.databinding.LayoutCampaignManageProductDetailVariantItemBinding
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.unifycomponents.TextFieldUnify2
import com.tokopedia.seller_tokopedia_flash_sale.R
import kotlin.math.round


class ManageProductMultiLocationVariantDelegateAdapter(
    private val onToggleSwitched: (Int, Boolean) -> Unit,
    private val onDiscountAmountChanged: (Int, Long, Long) -> Unit,
    private val onDiscountPercentageChange: (Int, Long, Long) -> Unit,
    private val onValidationInputText: (Int, Boolean, (Boolean, String, String) -> Unit) -> Unit,
    private val onValidationQuantity: (Int, Long, Boolean, (Boolean, String) -> Unit) -> Unit
) :
    DelegateAdapter<ManageProductMultiLocationVariantItem, ManageProductMultiLocationVariantDelegateAdapter.ViewHolder>(
        ManageProductMultiLocationVariantItem::class.java
    ) {

    override fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val binding = LayoutCampaignManageProductDetailVariantItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun bindViewHolder(
        item: ManageProductMultiLocationVariantItem,
        viewHolder: ManageProductMultiLocationVariantDelegateAdapter.ViewHolder
    ) {
        viewHolder.bind(item)
    }

    inner class ViewHolder(private val binding: LayoutCampaignManageProductDetailVariantItemBinding) :
        RecyclerView.ViewHolder(binding.root) {


        var listenerOfDiscountInput: TextWatcher? = null
        var listenerOfPriceInput: TextWatcher? = null

        fun bind(item: ManageProductMultiLocationVariantItem) {
            binding.run {
                containerLayoutProductParent.run {
                    textParentTitle.text = item.name
                    textParentOriginalPrice.text =
                        item.priceInWarehouse.toLong().getCurrencyFormatted()
                    textParentErrorMessage.gone()
                    textParentTotalStock.text = binding.root.context.getString(
                        R.string.stfs_note_stock_on_location,
                        item.stock
                    )
                    switcherToggleParent.run {
                        isChecked = item.isToggleOn
                        setOnCheckedChangeListener { _, isChecked ->
                            onToggleSwitched(adapterPosition, isChecked)
                        }
                    }
                }
                containerProductChild.isVisible = item.isToggleOn
                containerLayoutProductInformation.run {
                    periodSection.gone()
                    textFieldPriceDiscountNominal.apply {
                        setMessage(getPriceRange(item))
                        setDiscountAmountListener(
                            textFieldPriceDiscountPercentage,
                            item
                        )
                    }
                    textFieldPriceDiscountPercentage.apply {
                        setMessage(getDiscountRage(item))
                        appendText("%")
                        setDiscountListener(
                            textFieldPriceDiscountNominal,
                            item
                        )
                    }



                    quantityEditor.setValue(item.stock.toInt())
                    quantityEditor.run {
                        fun isAllNotEmpty(): Boolean {
                            val editTextOfAmount =
                                textFieldPriceDiscountNominal.textInputLayout.editText
                            val editTextOfDiscount =
                                textFieldPriceDiscountPercentage.textInputLayout.editText
                            return editTextOfAmount?.text.isNullOrEmpty()
                                    &&
                                    editTextOfDiscount?.text.isNullOrEmpty()
                        }

                        setValueChangedListener { stock, _, _ ->
                            onValidationQuantity.invoke(
                                adapterPosition,
                                stock.toLong(),
                                isAllNotEmpty()
                            ) { isValid, message ->
                                textQuantityEditorSubTitle.setTextColor(
                                    getColorStateOfMessage(
                                        isValid
                                    )
                                )
                                textQuantityEditorSubTitle.text = message
                            }
                        }
                    }

                    textQuantityEditorSubTitle.text = binding.root.context.getString(
                        R.string.stfs_note_stock_mandatory_on_location,
                        item.productCriteria.minCustomStock,
                        item.productCriteria.maxCustomStock
                    )
                    textQuantityEditorTitle.text =
                        binding.root.context.getString(R.string.stfs_title_qty_editor_title)
                }
            }


        }

        private fun TextFieldUnify2.setDiscountAmountListener(
            discountPercentageView: TextFieldUnify2,
            item: ManageProductMultiLocationVariantItem
        ) {
            listenerOfPriceInput = afterTextChangeListener(
                this,
                discountPercentageView,
                item,
                true
            )
            listenerOfPriceInput?.let { listener ->
                addListener(this, listener)
            }
        }

        private fun TextFieldUnify2.setDiscountListener(
            textFieldPriceDiscountNominal: TextFieldUnify2,
            item: ManageProductMultiLocationVariantItem
        ) {
            listenerOfDiscountInput = afterTextChangeListener(
                this,
                textFieldPriceDiscountNominal,
                item,
                false
            )
            listenerOfDiscountInput?.let { listener ->
                addListener(this, listener)
            }
        }

        private fun afterTextChangeListener(
            itemForListen: TextFieldUnify2,
            fieldToWriteAfterCalculate: TextFieldUnify2,
            item: ManageProductMultiLocationVariantItem,
            isPriceToPercentage: Boolean = true
        ): TextWatcher {
            return object : TextWatcher {
                override fun afterTextChanged(editable: Editable?) {

                    removeListenerTextChangeTemporary(
                        isPriceToPercentage,
                        fieldToWriteAfterCalculate
                    )

                    val textInput = editable?.toString() ?: ""
                    val inputDigit = textInput.digitsOnly()
                    val originalPrice = item.priceInStore.price.toDouble()

                    val discountCalculation =
                        calculateDiscount(isPriceToPercentage, inputDigit, originalPrice)

                    if (isPriceToPercentage) {
                        onDiscountAmountChanged(adapterPosition, inputDigit, discountCalculation)
                    }
                    else {
                        onDiscountPercentageChange(adapterPosition, inputDigit, discountCalculation)
                    }

                    fieldToWriteAfterCalculate.setTextIfNotFocus(
                        isPriceToPercentage,
                        discountCalculation.toString()
                    )

                    onValidationInputText(
                        adapterPosition,
                        isPriceToPercentage
                    ) { isValid, messageForAmount, messageForDiscount ->

                        itemForListen.isInputError = !isValid

                        setMessage(
                            isPriceToPercentage,
                            messageForAmount,
                            messageForDiscount,
                            itemForListen
                        )

                    }

                    reAddListenerTextChange(isPriceToPercentage, fieldToWriteAfterCalculate)
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(char: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    itemForListen.textInputLayout.editText?.removeTextChangedListener(this)
                    val input = char.toString().digitsOnly().getNumberFormatted()
                    itemForListen.textInputLayout.editText?.setText(input)
                    itemForListen.textInputLayout.editText?.setSelection(input.length)
                    itemForListen.textInputLayout.editText?.addTextChangedListener(this)
                }
            }
        }

        private fun calculateDiscount(
            isPriceToPercentage: Boolean,
            inputDigit: Long,
            originalPrice: Double
        ): Long {
            return when {
                isPriceToPercentage -> {
                    calculatePriceToPercent(
                        inputDigit,
                        originalPrice
                    )
                }
                else -> calculatePercentToPrice(originalPrice, inputDigit)

            }
        }

        private fun getColorStateOfMessage(
            isValid: Boolean
        ): Int {
            val errorColorRes = com.tokopedia.unifyprinciples.R.color.Unify_RN500
            val normalColorRes = com.tokopedia.unifyprinciples.R.color.Unify_NN600
            return MethodChecker.getColor(
                binding.root.context,
                when {
                    isValid -> {
                        normalColorRes
                    }
                    else -> {
                        errorColorRes
                    }
                }
            )
        }

        private fun getPriceRange(item: ManageProductMultiLocationVariantItem): String {
            return "${
                item.productCriteria.minFinalPrice.getCurrencyFormatted()
            } - ${item.productCriteria.maxFinalPrice.getCurrencyFormatted()}"
        }

        private fun getDiscountRage(item: ManageProductMultiLocationVariantItem): String {
            return "${item.productCriteria.minDiscount.getPercentFormatted()} " +
                    "- ${item.productCriteria.maxDiscount.getPercentFormatted()}"
        }

        private fun removeListenerTextChangeTemporary(
            isPriceToPercentage: Boolean,
            textFieldToRemove: TextFieldUnify2
        ) {
            if (isPriceToPercentage) {
                listenerOfDiscountInput?.let {
                    removeListenerTemporary(textFieldToRemove, it)
                }

            } else {
                listenerOfPriceInput?.let {
                    removeListenerTemporary(textFieldToRemove, it)
                }
            }
        }

        private fun reAddListenerTextChange(
            isPriceToPercentage: Boolean,
            textFieldToReAdd: TextFieldUnify2
        ) {
            when {
                isPriceToPercentage -> {
                    listenerOfDiscountInput?.let {
                        addListener(textFieldToReAdd, it)
                    }

                }
                else -> {
                    listenerOfPriceInput?.let {
                        addListener(textFieldToReAdd, it)
                    }
                }
            }
        }

        private fun removeListenerTemporary(
            field: TextFieldUnify2,
            listener: TextWatcher
        ) {
            field.textInputLayout.editText?.removeTextChangedListener(listener)
        }

        private fun addListener(
            field: TextFieldUnify2,
            listener: TextWatcher
        ) {
            field.textInputLayout.editText?.addTextChangedListener(listener)
        }

        private fun TextFieldUnify2.setTextIfNotFocus(isPriceToPercentage: Boolean, text: String) {
            if (!editText.isFocused) {
                if (isPriceToPercentage) {
                    editText.setText(text)
                } else {
                    editText.setText(text.toLong().getNumberFormatted())
                }
            }
        }

        private fun setMessage(
            isPriceToPercentage: Boolean,
            messageAmount: String,
            messageDiscount: String,
            field: TextFieldUnify2,
        ) {
            when {
                isPriceToPercentage -> {
                    field.setMessage(messageAmount)
                }
                else -> {
                    field.setMessage(messageDiscount)
                }
            }
        }
    }

    fun calculatePercentToPrice(originalPrice: Double, percentInput: Long): Long {
        return (((100 - percentInput) / 100.0) * originalPrice).toLong()
    }

    fun calculatePriceToPercent(priceInput: Long, originalPrice: Double): Long {
        return (((originalPrice - priceInput.toDouble()) / originalPrice) * 100.0).toLong()
    }
}