package com.tokopedia.tokofood.purchase.purchasepage.view.viewholder

import android.graphics.Paint
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintSet
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
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
import kotlinx.coroutines.Job

class TokoFoodPurchaseProductViewHolder(private val viewBinding: ItemPurchaseProductBinding,
                                        private val listener: TokoFoodPurchaseActionListener)
    : AbstractViewHolder<TokoFoodPurchaseProductUiModel>(viewBinding.root) {

    companion object {
        val LAYOUT = R.layout.item_purchase_product
    }

    private var delayChangeQty: Job? = null

    override fun onViewRecycled() {
        super.onViewRecycled()
        delayChangeQty?.cancel()
    }

    override fun bind(element: TokoFoodPurchaseProductUiModel) {
        renderAlpha(element)
        renderTopDivider(viewBinding, element)
        renderProductBasicInformation(viewBinding, element)
        renderProductAddOn(viewBinding, element)
        renderProductPrice(viewBinding, element)
        renderProductNotes(viewBinding, element)
        renderProductQuantity(viewBinding, element)
        renderDelete(viewBinding, element)
    }

    private fun renderAlpha(element: TokoFoodPurchaseProductUiModel) {
        if (element.isDisabled) {
            itemView.alpha = 0.5f
        } else {
            itemView.alpha = 1.0f
        }
    }

    private fun renderTopDivider(viewBinding: ItemPurchaseProductBinding, element: TokoFoodPurchaseProductUiModel) {
        with(viewBinding) {
            val previousItem = listener.getPreviousItems(adapterPosition, 1).firstOrNull()
            previousItem?.let {
                if (previousItem is TokoFoodPurchaseProductUiModel) {
                    dividerTop.show()
                } else {
                    dividerTop.gone()
                }
            }
        }
    }

    private fun renderProductBasicInformation(viewBinding: ItemPurchaseProductBinding, element: TokoFoodPurchaseProductUiModel) {
        with(viewBinding) {
            imageProduct.setImageUrl(element.imageUrl)
            textProductName.text = element.name
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
            } else {
                textProductOriginalPrice.gone()
            }
            textProductPrice.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(element.price, false).removeDecimalSuffix()
            if (element.discountPercentage.isNotBlank()) {
                labelSlashPricePercentage.text = element.discountPercentage
                labelSlashPricePercentage.show()
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
                    listener.onTextChangeNotesClicked()
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
                listener.onIconDeleteProductClicked()
            }
        }
    }
}