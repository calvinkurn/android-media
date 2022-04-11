package com.tokopedia.tokofood.purchase.purchasepage.presentation.viewholder

import android.graphics.Paint
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.constraintlayout.widget.ConstraintSet
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.databinding.ItemPurchaseProductBinding
import com.tokopedia.tokofood.databinding.SubItemPurchaseAddOnBinding
import com.tokopedia.tokofood.purchase.purchasepage.presentation.TokoFoodPurchaseActionListener
import com.tokopedia.tokofood.purchase.purchasepage.presentation.uimodel.TokoFoodPurchaseProductTokoFoodPurchaseUiModel
import com.tokopedia.tokofood.purchase.removeDecimalSuffix
import com.tokopedia.utils.currency.CurrencyFormatUtil

class TokoFoodPurchaseProductViewHolder(private val viewBinding: ItemPurchaseProductBinding,
                                        private val listener: TokoFoodPurchaseActionListener)
    : AbstractViewHolder<TokoFoodPurchaseProductTokoFoodPurchaseUiModel>(viewBinding.root) {

    companion object {
        val LAYOUT = R.layout.item_purchase_product
    }

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
        if (element.isUnavailable || element.isDisabled) {
            alpha = 0.5f
        } else {
            alpha = 1.0f
        }
    }

    private fun renderBottomDivider(viewBinding: ItemPurchaseProductBinding, element: TokoFoodPurchaseProductTokoFoodPurchaseUiModel) {
        with(viewBinding) {
            val nextItem = listener.getNextItems(adapterPosition, 1).firstOrNull()
            nextItem?.let {
                val constraintSet = ConstraintSet()
                constraintSet.clone(productCell)
                if (element.isUnavailable) {
                    constraintSet.connect(R.id.divider_bottom, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0)
                    constraintSet.connect(R.id.divider_bottom, ConstraintSet.TOP, R.id.container_product_price, ConstraintSet.BOTTOM, 16.dpToPx(itemView.resources.displayMetrics))
                } else {
                    constraintSet.connect(R.id.divider_bottom, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0)
                    constraintSet.connect(R.id.divider_bottom, ConstraintSet.TOP, R.id.addNotesButton, ConstraintSet.BOTTOM, 16.dpToPx(itemView.resources.displayMetrics))
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
            if (element.originalPrice > 0) {
                productSlashPrice.run {
                    text = CurrencyFormatUtil.convertPriceValueToIdrFormat(element.originalPrice, false).removeDecimalSuffix()
                    paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    show()
                    renderAlphaProductItem(element)
                }
            } else {
                productSlashPrice.gone()
            }
            productPrice.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(element.price, false).removeDecimalSuffix()
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
            if (element.isUnavailable) {
                notes.gone()
                addNotesButton.gone()
                return
            }

            if (element.notes.isNotBlank()) {
                notes.text = element.notes
                notes.show()
            } else {
                notes.gone()
            }

            if (element.hasAddOnsOption) {
                addNotesButton.text = getString(R.string.text_purchase_change_notes_and_variant)
                addNotesButton.setOnClickListener {
                    listener.onTextChangeNoteAndVariantClicked()
                }
            } else {
                if (element.notes.isNotBlank()) {
                    addNotesButton.text = getString(R.string.text_purchase_change_notes)
                } else {
                    addNotesButton.text = getString(R.string.text_purchase_add_notes)
                }
                addNotesButton.setOnClickListener {
                    listener.onTextChangeNotesClicked(element)
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
                constraintSet.connect(R.id.notes, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0)
                constraintSet.connect(R.id.notes, ConstraintSet.TOP, R.id.container_product_price, ConstraintSet.BOTTOM, 16.dpToPx(itemView.resources.displayMetrics))
            } else {
                constraintSet.connect(R.id.notes, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0)
                constraintSet.connect(R.id.notes, ConstraintSet.TOP, R.id.image_product, ConstraintSet.BOTTOM, 16.dpToPx(itemView.resources.displayMetrics))
            }
            constraintSet.applyTo(productCell)
        }
    }

    private fun renderProductQuantity(viewBinding: ItemPurchaseProductBinding, element: TokoFoodPurchaseProductTokoFoodPurchaseUiModel) {
        with(viewBinding) {
            if (element.isUnavailable) {
                qtyEditorProduct.gone()
                return
            }

            qtyEditorProduct.show()
            qtyEditorProduct.autoHideKeyboard = true
            qtyEditorProduct.minValue = element.minQuantity
            qtyEditorProduct.maxValue = element.maxQuantity
            qtyEditorProduct.setValue(element.quantity)
            qtyEditorProduct.editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    val quantity = p0.toString().toIntOrZero()
                    if (quantity != element.quantity) {
                        element.quantity = quantity
                        listener.onQuantityChanged()
                    }
                }

                override fun afterTextChanged(p0: Editable?) {

                }
            })
            qtyEditorProduct.editText.imeOptions = EditorInfo.IME_ACTION_DONE
            qtyEditorProduct.editText.setOnEditorActionListener { v, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    qtyEditorProduct.editText.context?.let {
                        KeyboardHandler.DropKeyboard(it, v)
                    }
                    qtyEditorProduct.editText.clearFocus()
                    true
                } else false
            }
            qtyEditorProduct.editText.clearFocus()

            qtyEditorProduct.renderAlphaProductItem(element)
        }
    }

    private fun renderDelete(viewBinding: ItemPurchaseProductBinding, element: TokoFoodPurchaseProductTokoFoodPurchaseUiModel) {
        with(viewBinding) {
            if (element.isUnavailable) {
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
}