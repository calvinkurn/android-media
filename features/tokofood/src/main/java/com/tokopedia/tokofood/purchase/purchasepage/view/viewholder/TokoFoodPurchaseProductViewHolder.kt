package com.tokopedia.tokofood.purchase.purchasepage.view.viewholder

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
import com.tokopedia.tokofood.purchase.purchasepage.view.TokoFoodPurchaseActionListener
import com.tokopedia.tokofood.purchase.purchasepage.view.uimodel.TokoFoodPurchaseProductUiModel
import com.tokopedia.tokofood.purchase.removeDecimalSuffix
import com.tokopedia.utils.currency.CurrencyFormatUtil

class TokoFoodPurchaseProductViewHolder(private val viewBinding: ItemPurchaseProductBinding,
                                        private val listener: TokoFoodPurchaseActionListener)
    : AbstractViewHolder<TokoFoodPurchaseProductUiModel>(viewBinding.root) {

    companion object {
        val LAYOUT = R.layout.item_purchase_product
    }

    override fun bind(element: TokoFoodPurchaseProductUiModel) {
        renderProductBasicInformation(viewBinding, element)
        renderProductAddOn(viewBinding, element)
        renderProductPrice(viewBinding, element)
        renderProductNotes(viewBinding, element)
        renderProductQuantity(viewBinding, element)
        renderDelete(viewBinding, element)
        renderBottomDivider(viewBinding, element)
    }

    private fun View.renderAlpha(element: TokoFoodPurchaseProductUiModel) {
        if (element.isDisabled) {
            alpha = 0.5f
        } else {
            alpha = 1.0f
        }
    }

    private fun renderBottomDivider(viewBinding: ItemPurchaseProductBinding, element: TokoFoodPurchaseProductUiModel) {
        with(viewBinding) {
            val nextItem = listener.getNextItems(adapterPosition, 1).firstOrNull()
            nextItem?.let {
                val constraintSet = ConstraintSet()
                constraintSet.clone(containerPurchaseProduct)
                if (element.isDisabled) {
                    constraintSet.connect(R.id.divider_bottom, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0)
                    constraintSet.connect(R.id.divider_bottom, ConstraintSet.TOP, R.id.container_product_price, ConstraintSet.BOTTOM, 16.dpToPx(itemView.resources.displayMetrics))
                } else {
                    constraintSet.connect(R.id.divider_bottom, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0)
                    constraintSet.connect(R.id.divider_bottom, ConstraintSet.TOP, R.id.text_notes_or_variant_action, ConstraintSet.BOTTOM, 16.dpToPx(itemView.resources.displayMetrics))
                }
                constraintSet.applyTo(containerPurchaseProduct)
            }
        }
    }

    private fun renderProductBasicInformation(viewBinding: ItemPurchaseProductBinding, element: TokoFoodPurchaseProductUiModel) {
        with(viewBinding) {
            imageProduct.setImageUrl(element.imageUrl)
            imageProduct.renderAlpha(element)
            textProductName.text = element.name
            textProductName.renderAlpha(element)
        }
    }

    private fun renderProductAddOn(viewBinding: ItemPurchaseProductBinding, element: TokoFoodPurchaseProductUiModel) {
        with(viewBinding) {
            if (element.addOns.isNotEmpty()) {
                containerAddOn.removeAllViews()
                element.addOns.forEach {
                    val productAddOnView = SubItemPurchaseAddOnBinding.inflate(LayoutInflater.from(itemView.context))
                    productAddOnView.textAddOnDetail.text = it
                    containerAddOn.addView(productAddOnView.root)
                }
                containerAddOn.show()
                containerAddOn.renderAlpha(element)
            } else {
                containerAddOn.gone()
            }
        }
    }

    private fun renderProductPrice(viewBinding: ItemPurchaseProductBinding, element: TokoFoodPurchaseProductUiModel) {
        with(viewBinding) {
            if (element.originalPrice > 0) {
                textProductOriginalPrice.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(element.originalPrice, false).removeDecimalSuffix()
                textProductOriginalPrice.paintFlags = textProductOriginalPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                textProductOriginalPrice.show()
                textProductOriginalPrice.renderAlpha(element)
            } else {
                textProductOriginalPrice.gone()
            }
            textProductPrice.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(element.price, false).removeDecimalSuffix()
            textProductPrice.renderAlpha(element)
            if (element.discountPercentage.isNotBlank()) {
                labelSlashPricePercentage.text = element.discountPercentage
                labelSlashPricePercentage.show()
                labelSlashPricePercentage.renderAlpha(element)
            } else {
                labelSlashPricePercentage.gone()
            }
        }
    }

    private fun renderProductNotes(viewBinding: ItemPurchaseProductBinding, element: TokoFoodPurchaseProductUiModel) {
        with(viewBinding) {
            if (element.isDisabled) {
                textNotes.gone()
                textNotesOrVariantAction.gone()
                return
            }

            if (element.notes.isNotBlank()) {
                textNotes.text = element.notes
                textNotes.show()
            } else {
                textNotes.gone()
            }

            if (element.hasAddOnsOption) {
                textNotesOrVariantAction.text = getString(R.string.text_purchase_change_notes_and_variant)
                textNotesOrVariantAction.setOnClickListener {
                    listener.onTextChangeNoteAndVariantClicked()
                }
            } else {
                if (element.notes.isNotBlank()) {
                    textNotesOrVariantAction.text = getString(R.string.text_purchase_change_notes)
                } else {
                    textNotesOrVariantAction.text = getString(R.string.text_purchase_add_notes)
                }
                textNotesOrVariantAction.setOnClickListener {
                    listener.onTextChangeNotesClicked(element)
                }
            }
            textNotesOrVariantAction.show()

            setNotesConstraint(viewBinding, element)
        }
    }

    private fun setNotesConstraint(viewBinding: ItemPurchaseProductBinding, element: TokoFoodPurchaseProductUiModel) {
        with(viewBinding) {
            val constraintSet = ConstraintSet()
            constraintSet.clone(containerPurchaseProduct)
            if (element.addOns.isNotEmpty()) {
                constraintSet.connect(R.id.text_notes, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0)
                constraintSet.connect(R.id.text_notes, ConstraintSet.TOP, R.id.container_product_price, ConstraintSet.BOTTOM, 16.dpToPx(itemView.resources.displayMetrics))
            } else {
                constraintSet.connect(R.id.text_notes, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0)
                constraintSet.connect(R.id.text_notes, ConstraintSet.TOP, R.id.image_product, ConstraintSet.BOTTOM, 16.dpToPx(itemView.resources.displayMetrics))
            }
            constraintSet.applyTo(containerPurchaseProduct)
        }
    }

    private fun renderProductQuantity(viewBinding: ItemPurchaseProductBinding, element: TokoFoodPurchaseProductUiModel) {
        with(viewBinding) {
            if (element.isDisabled) {
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
                    listener.onQuantityChanged(quantity)
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
        }
    }

    private fun renderDelete(viewBinding: ItemPurchaseProductBinding, element: TokoFoodPurchaseProductUiModel) {
        with(viewBinding) {
            if (element.isDisabled) {
                buttonDeleteProduct.gone()
                return
            }

            buttonDeleteProduct.show()
            buttonDeleteProduct.setOnClickListener {
                listener.onIconDeleteProductClicked(element)
            }
        }
    }
}