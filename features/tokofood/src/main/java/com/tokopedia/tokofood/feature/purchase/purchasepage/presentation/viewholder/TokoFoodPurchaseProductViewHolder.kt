package com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.viewholder

import android.graphics.Paint
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintSet
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.common.util.TokofoodExt
import com.tokopedia.tokofood.databinding.ItemPurchaseProductBinding
import com.tokopedia.tokofood.databinding.SubItemPurchaseAddOnBinding
import com.tokopedia.tokofood.feature.purchase.DISABLED_ALPHA
import com.tokopedia.tokofood.feature.purchase.ENABLED_ALPHA
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.TokoFoodPurchaseActionListener
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.uimodel.TokoFoodPurchaseProductTokoFoodPurchaseUiModel

class TokoFoodPurchaseProductViewHolder(private val viewBinding: ItemPurchaseProductBinding,
                                        private val listener: TokoFoodPurchaseActionListener)
    : AbstractViewHolder<TokoFoodPurchaseProductTokoFoodPurchaseUiModel>(viewBinding.root) {

    private var textWatcher: TextWatcher? = null

    override fun bind(element: TokoFoodPurchaseProductTokoFoodPurchaseUiModel) {
        renderProductBasicInformation(viewBinding, element)
        renderProductAddOn(viewBinding, element)
        renderProductPrice(viewBinding, element)
        renderProductNotes(viewBinding, element)
        renderProductQuantity(viewBinding, element)
        renderDelete(viewBinding, element)
        renderBottomDivider(viewBinding, element)
    }

    private fun View.renderAlphaProductItem(element: TokoFoodPurchaseProductTokoFoodPurchaseUiModel) {
        if (element.isAvailable && element.isEnabled) {
            alpha = ENABLED_ALPHA
            isEnabled = true
        } else {
            alpha = DISABLED_ALPHA
            isEnabled = false
        }
    }

    private fun renderBottomDivider(viewBinding: ItemPurchaseProductBinding, element: TokoFoodPurchaseProductTokoFoodPurchaseUiModel) {
        with(viewBinding) {
            val nextItem = listener.getNextItems(adapterPosition, Int.ONE).firstOrNull().takeIf {
                it is TokoFoodPurchaseProductTokoFoodPurchaseUiModel
            }
            if (nextItem == null) {
                dividerBottom.invisible()
            } else {
                dividerBottom.show()
                val constraintSet = ConstraintSet()
                constraintSet.clone(productCell)
                if (element.isAvailable) {
                    constraintSet.connect(R.id.divider_bottom, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, Int.ZERO)
                    constraintSet.connect(R.id.divider_bottom, ConstraintSet.TOP, R.id.addNotesButton, ConstraintSet.BOTTOM, SIXTEEN_MARGIN_PX.dpToPx(itemView.resources.displayMetrics))
                } else {
                    constraintSet.connect(R.id.divider_bottom, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, Int.ZERO)
                    constraintSet.connect(R.id.divider_bottom, ConstraintSet.TOP, R.id.barrier_divider, ConstraintSet.BOTTOM, SIXTEEN_MARGIN_PX.dpToPx(itemView.resources.displayMetrics))
                }
                constraintSet.applyTo(productCell)
            }
        }
    }

    private fun renderProductBasicInformation(viewBinding: ItemPurchaseProductBinding, element: TokoFoodPurchaseProductTokoFoodPurchaseUiModel) {
        with(viewBinding) {
            imageProduct.setImageUrl(element.imageUrl)
            imageProduct.renderAlphaProductItem(element)
            productName.text = element.name
            productName.renderAlphaProductItem(element)
        }
    }

    private fun renderProductAddOn(viewBinding: ItemPurchaseProductBinding, element: TokoFoodPurchaseProductTokoFoodPurchaseUiModel) {
        with(viewBinding) {
            if (element.addOns.isNotEmpty()) {
                containerAddOn.removeAllViews()
                element.addOns.forEach {
                    val productAddOnView = SubItemPurchaseAddOnBinding.inflate(LayoutInflater.from(itemView.context))
                    productAddOnView.addOns.text = it
                    containerAddOn.addView(productAddOnView.root)
                }
                containerAddOn.show()
                containerAddOn.renderAlphaProductItem(element)
            } else {
                containerAddOn.gone()
            }
        }
    }

    private fun renderProductPrice(viewBinding: ItemPurchaseProductBinding, element: TokoFoodPurchaseProductTokoFoodPurchaseUiModel) {
        with(viewBinding) {
            if (element.originalPriceFmt.isNotEmpty()) {
                productSlashPrice.run {
                    text = element.originalPriceFmt
                    paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    show()
                    renderAlphaProductItem(element)
                }
            } else {
                productSlashPrice.gone()
            }
            productPrice.text = element.priceFmt
            productPrice.renderAlphaProductItem(element)
            if (element.discountPercentage.isNotBlank()) {
                slashPriceInfo.run {
                    text = element.discountPercentage
                    show()
                    renderAlphaProductItem(element)
                }
            } else {
                slashPriceInfo.gone()
            }
        }
    }

    private fun renderProductNotes(viewBinding: ItemPurchaseProductBinding, element: TokoFoodPurchaseProductTokoFoodPurchaseUiModel) {
        with(viewBinding) {
            if (!element.isAvailable) {
                notes.gone()
                addNotesButton.gone()
                return
            }

            val isNoteBlank = element.notes.isBlank()

            if (isNoteBlank) {
                notes.gone()
            } else {
                notes.text = element.notes
                notes.show()
            }

            if (element.variants.isEmpty()) {
                addNotesButton.text =
                    if (isNoteBlank) {
                        getString(com.tokopedia.tokofood.R.string.text_purchase_add_notes)
                    } else {
                        getString(com.tokopedia.tokofood.R.string.text_purchase_change_notes)
                    }
                addNotesButton.setOnClickListener {
                    listener.onTextChangeNotesClicked(element)
                }
            } else {
                addNotesButton.text =
                    if (isNoteBlank) {
                        getString(com.tokopedia.tokofood.R.string.text_purchase_add_notes_and_variant)
                    } else {
                        getString(com.tokopedia.tokofood.R.string.text_purchase_change_notes_and_variant)
                    }
                addNotesButton.setOnClickListener {
                    listener.onTextChangeNoteAndVariantClicked(element)
                }
            }


            addNotesButton.show()
            notes.renderAlphaProductItem(element)
            addNotesButton.renderAlphaProductItem(element)

            setNotesConstraint(viewBinding, element)
        }
    }

    private fun setNotesConstraint(viewBinding: ItemPurchaseProductBinding, element: TokoFoodPurchaseProductTokoFoodPurchaseUiModel) {
        with(viewBinding) {
            val constraintSet = ConstraintSet()
            constraintSet.clone(productCell)
            if (element.addOns.isNotEmpty()) {
                constraintSet.connect(R.id.notes, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, Int.ZERO)
                constraintSet.connect(R.id.notes, ConstraintSet.TOP, R.id.container_product_price, ConstraintSet.BOTTOM, SIXTEEN_MARGIN_PX.dpToPx(itemView.resources.displayMetrics))
            } else {
                constraintSet.connect(R.id.notes, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, Int.ZERO)
                constraintSet.connect(R.id.notes, ConstraintSet.TOP, R.id.image_product, ConstraintSet.BOTTOM, SIXTEEN_MARGIN_PX.dpToPx(itemView.resources.displayMetrics))
            }
            constraintSet.applyTo(productCell)
        }
    }

    private fun renderProductQuantity(viewBinding: ItemPurchaseProductBinding, element: TokoFoodPurchaseProductTokoFoodPurchaseUiModel) {
        with(viewBinding) {
            if (!element.isAvailable) {
                qtyEditorProduct.gone()
                return
            }
            if (textWatcher != null) {
                qtyEditorProduct.editText.removeTextChangedListener(textWatcher)
                textWatcher = null
            }
            qtyEditorProduct.show()
            qtyEditorProduct.autoHideKeyboard = true
            qtyEditorProduct.minValue = element.minQuantity
            qtyEditorProduct.maxValue = element.maxQuantity
            qtyEditorProduct.setValue(element.quantity)
            textWatcher = object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    val quantity = qtyEditorProduct.getValue().orZero()
                    if (quantity != element.quantity && quantity >= Int.ONE) {
                        element.quantity = quantity
                        element.isQuantityChanged = true
                        listener.onQuantityChanged()
                    }
                }

                override fun afterTextChanged(p0: Editable?) {

                }
            }

            qtyEditorProduct.editText.adjustLengthAndAction()

            qtyEditorProduct.renderAlphaProductItem(element)
        }
    }

    private fun renderDelete(viewBinding: ItemPurchaseProductBinding, element: TokoFoodPurchaseProductTokoFoodPurchaseUiModel) {
        with(viewBinding) {
            if (!element.isAvailable) {
                deleteProductButton.gone()
                return
            }

            deleteProductButton.show()
            deleteProductButton.setOnClickListener {
                listener.onIconDeleteProductClicked(element)
            }

            deleteProductButton.renderAlphaProductItem(element)
        }
    }

    private fun EditText.adjustLengthAndAction() {
        addTextChangedListener(textWatcher)
        val maxLength = InputFilter.LengthFilter(TokofoodExt.MAXIMUM_QUANTITY_LENGTH)
        filters = arrayOf(maxLength)
        imeOptions = EditorInfo.IME_ACTION_DONE
        setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                context?.let {
                    KeyboardHandler.DropKeyboard(it, v)
                }
                clearFocus()
                true
            } else false
        }
        clearFocus()
    }

    companion object {
        val LAYOUT = R.layout.item_purchase_product

        private const val SIXTEEN_MARGIN_PX = 16
    }

}