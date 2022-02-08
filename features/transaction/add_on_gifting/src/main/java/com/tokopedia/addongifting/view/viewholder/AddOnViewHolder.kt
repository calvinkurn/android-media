package com.tokopedia.addongifting.view.viewholder

import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.addongifting.R
import com.tokopedia.addongifting.databinding.ItemAddOnBinding
import com.tokopedia.addongifting.view.AddOnActionListener
import com.tokopedia.addongifting.view.uimodel.AddOnUiModel
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.purchase_platform.common.utils.Utils
import com.tokopedia.purchase_platform.common.utils.removeDecimalSuffix
import com.tokopedia.utils.currency.CurrencyFormatUtil

class AddOnViewHolder(private val viewBinding: ItemAddOnBinding, private val listener: AddOnActionListener)
    : AbstractViewHolder<AddOnUiModel>(viewBinding.root) {

    companion object {
        val LAYOUT = R.layout.item_add_on
    }

    override fun bind(element: AddOnUiModel) {
        renderAddOnInfo(viewBinding, element)
        renderAddOnState(viewBinding, element)
    }

    private fun renderAddOnInfo(viewBinding: ItemAddOnBinding, element: AddOnUiModel) {
        with(viewBinding) {
            if (element.isTokoCabang) {
                if (element.productCount > 1) {
                    labelAddOnHeader.text = "Bungkus ${element.productCount} barang jadi satu dengan (opsional):"
                } else {
                    labelAddOnHeader.text = "Bungkus barang jadi satu dengan (opsional):"
                }
            } else {
                labelAddOnHeader.text = "Bungkus barang dengan (opsional):"
            }
            imageAddOn.setImageUrl(element.addOnSquareImageUrl)
            labelAddOnName.text = element.addOnName
            if (element.addOnDescription.isNotBlank()) {
                labelAddOnDescription.text = element.addOnDescription
                labelAddOnDescription.show()
            } else {
                labelAddOnDescription.gone()
            }
            labelAddOnPrice.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(element.addOnPrice, false).removeDecimalSuffix()
        }
    }

    private fun renderAddOnState(viewBinding: ItemAddOnBinding, element: AddOnUiModel) {
        with(viewBinding) {
            checkBoxAddOn.isSelected = element.isAddOnSelected
            setNoteVisibility(viewBinding, element)
            checkBoxAddOn.setOnCheckedChangeListener { checkBox, isSelected ->
                element.isAddOnSelected = isSelected
                setNoteVisibility(viewBinding, element)
            }
        }
    }

    private fun setNoteVisibility(viewBinding: ItemAddOnBinding, element: AddOnUiModel) {
        with(viewBinding) {
            if (element.isAddOnSelected) {
                cardAddOnNote.show()
                renderNote(viewBinding, element)
            } else {
                cardAddOnNote.gone()
            }
        }
    }

    private fun renderNote(viewBinding: ItemAddOnBinding, element: AddOnUiModel) {
        with(viewBinding) {
            textFieldAddOnNote.editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE
            textFieldAddOnNote.editText.imeOptions = EditorInfo.IME_ACTION_DONE
            textFieldAddOnNote.editText.setRawInputType(InputType.TYPE_CLASS_TEXT)
            textFieldAddOnNote.editText.setText(Utils.getHtmlFormat(element.addOnNote))
            textFieldAddOnNote.editText.setSelection(textFieldAddOnNote.editText.length())
            textFieldAddOnNote.editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    element.addOnNote = s.toString()
                    if (s?.length ?: 0 > 0) {
                        textFieldAddOnNote.setLabel("Pesan")
                    } else {
                        textFieldAddOnNote.setLabel("Tulis pesan di kartu ucapan")
                    }
                }

                override fun afterTextChanged(s: Editable?) {

                }
            })

        }
    }

}