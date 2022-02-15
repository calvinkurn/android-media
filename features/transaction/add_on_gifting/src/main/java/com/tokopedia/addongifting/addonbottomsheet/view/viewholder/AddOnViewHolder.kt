package com.tokopedia.addongifting.addonbottomsheet.view.viewholder

import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.addongifting.R
import com.tokopedia.addongifting.addonbottomsheet.data.getaddonbyproduct.BasicInfoResponse
import com.tokopedia.addongifting.addonbottomsheet.view.AddOnActionListener
import com.tokopedia.addongifting.addonbottomsheet.view.uimodel.AddOnUiModel
import com.tokopedia.addongifting.databinding.ItemAddOnBinding
import com.tokopedia.addongifting.databinding.SubLayoutAddOnFooterMessageBinding
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.purchase_platform.common.utils.Utils
import com.tokopedia.purchase_platform.common.utils.removeDecimalSuffix
import com.tokopedia.utils.currency.CurrencyFormatUtil

class AddOnViewHolder(private val viewBinding: ItemAddOnBinding, private val listener: AddOnActionListener)
    : AbstractViewHolder<AddOnUiModel>(viewBinding.root) {

    companion object {
        val LAYOUT = R.layout.item_add_on
        const val FROM_MAX_CHAR = 25
        const val TO_MAX_CHAR = 25
        const val NOTE_MAX_CHAR = 250
        const val QUANTITY_PLACEHOLDER = "{{qty}}"
    }

    override fun bind(element: AddOnUiModel) {
        renderAddOnInfo(viewBinding, element)
        renderAddOnState(viewBinding, element)
    }

    private fun renderAddOnInfo(viewBinding: ItemAddOnBinding, element: AddOnUiModel) {
        with(viewBinding) {
            if (element.isTokoCabang) {
                if (element.productCount > 1) {
                    labelAddOnHeader.text = String.format(itemView.context.getString(R.string.add_on_label_header_toko_cabang_multiple_product), element.productCount)
                } else {
                    labelAddOnHeader.text = itemView.context.getString(R.string.add_on_label_header_toko_cabang_single_product)
                }
            } else {
                labelAddOnHeader.text = itemView.context.getString(R.string.add_on_label_header)
            }
            imageAddOn.setImageUrl(element.addOnSquareImageUrl)
            imageAddOn.setOnClickListener {
                listener.onAddOnImageClicked(element)
            }
            labelAddOnName.text = element.addOnName
            if (element.addOnDescription.isNotBlank()) {
                labelAddOnDescription.text = element.addOnDescription
                labelAddOnDescription.show()
            } else {
                labelAddOnDescription.gone()
            }
            val addOnQuantityAndPrice = "${element.addOnQty} x ${CurrencyFormatUtil.convertPriceValueToIdrFormat(element.addOnPrice, false).removeDecimalSuffix()}"
            labelAddOnQuantityAndPrice.text = addOnQuantityAndPrice
            labelAddOnName.setOnClickListener(getCheckboxClickListener())
            labelAddOnDescription.setOnClickListener(getCheckboxClickListener())
            labelAddOnQuantityAndPrice.setOnClickListener(getCheckboxClickListener())
        }
    }

    private fun getCheckboxClickListener(): View.OnClickListener {
        return View.OnClickListener {
            viewBinding.checkBoxAddOn.isChecked = !viewBinding.checkBoxAddOn.isChecked
        }
    }

    private fun renderAddOnState(viewBinding: ItemAddOnBinding, element: AddOnUiModel) {
        with(viewBinding) {
            checkBoxAddOn.setOnCheckedChangeListener { checkBox, isChecked ->
                element.isAddOnSelected = isChecked
                setNoteVisibility(viewBinding, element)
                listener.onCheckBoxCheckedChanged(element)
            }
            checkBoxAddOn.isChecked = element.isAddOnSelected
            setNoteVisibility(viewBinding, element)
        }
    }

    private fun setNoteVisibility(viewBinding: ItemAddOnBinding, element: AddOnUiModel) {
        with(viewBinding) {
            if (element.isAddOnSelected) {
                containerAddOnNote.show()
                renderTo(viewBinding, element)
                renderFrom(viewBinding, element)
                renderNote(viewBinding, element)
                renderAddOnFooterMessages(viewBinding, element)
            } else {
                containerAddOnNote.gone()
            }
        }
    }

    private fun renderTo(viewBinding: ItemAddOnBinding, element: AddOnUiModel) {
        with(viewBinding) {
            textFieldAddOnTo.editText.inputType = InputType.TYPE_TEXT_FLAG_CAP_WORDS
            textFieldAddOnTo.editText.imeOptions = EditorInfo.IME_ACTION_DONE
            textFieldAddOnTo.editText.setRawInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS)
            textFieldAddOnTo.setCounter(TO_MAX_CHAR)
            textFieldAddOnTo.editText.setText(Utils.getHtmlFormat(element.addOnNoteTo))
            textFieldAddOnTo.editText.setSelection(textFieldAddOnTo.editText.length())
            textFieldAddOnTo.editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    element.addOnNoteTo = s.toString()
                }

                override fun afterTextChanged(s: Editable?) {

                }
            })
        }
    }

    private fun renderFrom(viewBinding: ItemAddOnBinding, element: AddOnUiModel) {
        with(viewBinding) {
            textFieldAddOnFrom.editText.inputType = InputType.TYPE_TEXT_FLAG_CAP_WORDS
            textFieldAddOnFrom.editText.imeOptions = EditorInfo.IME_ACTION_DONE
            textFieldAddOnFrom.editText.setRawInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS)
            textFieldAddOnFrom.setCounter(FROM_MAX_CHAR)
            textFieldAddOnFrom.editText.setText(Utils.getHtmlFormat(element.addOnNoteFrom))
            textFieldAddOnFrom.editText.setSelection(textFieldAddOnFrom.editText.length())
            textFieldAddOnFrom.editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    element.addOnNoteFrom = s.toString()
                }

                override fun afterTextChanged(s: Editable?) {

                }
            })
        }
    }

    private fun renderNote(viewBinding: ItemAddOnBinding, element: AddOnUiModel) {
        with(viewBinding) {
            if (element.isCustomNote) {
                textFieldAddOnNote.editText.isEnabled = true
                textFieldAddOnNote.setMessage(itemView.context.getString(R.string.add_on_label_message_custom_note))
            } else {
                textFieldAddOnNote.editText.isEnabled = false
                textFieldAddOnNote.setMessage(itemView.context.getString(R.string.add_on_label_message_pre_defined_note))
            }

            textFieldAddOnNote.editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE
            textFieldAddOnNote.editText.imeOptions = EditorInfo.IME_ACTION_DONE
            textFieldAddOnNote.editText.setRawInputType(InputType.TYPE_CLASS_TEXT)
            textFieldAddOnNote.setCounter(NOTE_MAX_CHAR)
            textFieldAddOnNote.editText.setText(Utils.getHtmlFormat(element.addOnNote))
            textFieldAddOnNote.editText.setSelection(textFieldAddOnNote.editText.length())

            if (textFieldAddOnNote.editText.text.isNotEmpty()) {
                textFieldAddOnNote.setLabel(itemView.context.getString(R.string.add_on_label_note_filled))
            } else {
                textFieldAddOnNote.setLabel(itemView.context.getString(R.string.add_on_label_note_empty))
            }

            textFieldAddOnNote.editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    element.addOnNote = s.toString()
                    if (textFieldAddOnNote.editText.text.isNotEmpty()) {
                        textFieldAddOnNote.setLabel(itemView.context.getString(R.string.add_on_label_note_filled))
                    } else {
                        textFieldAddOnNote.setLabel(itemView.context.getString(R.string.add_on_label_note_empty))
                    }
                }

                override fun afterTextChanged(s: Editable?) {

                }
            })
        }
    }

    private fun renderAddOnFooterMessages(viewBinding: ItemAddOnBinding, element: AddOnUiModel) {
        with(viewBinding) {
            val informationMessages = mutableListOf<String>()
            if (element.isTokoCabang) {
                informationMessages.add(element.invoiceNotSentToRecipientInfo)
            } else {
                if (element.addOnType == BasicInfoResponse.ADD_ON_TYPE_GREETING_CARD) {
                    informationMessages.add(element.onlyGreetingCardInfo)
                } else if (element.addOnType == BasicInfoResponse.ADD_ON_TYPE_GREETING_CARD_AND_PACKAGING) {
                    informationMessages.add(element.packagingAndGreetingCardInfo)
                }
                informationMessages.add(element.invoiceNotSentToRecipientInfo)
            }
            containerFooterMessages.removeAllViews()
            informationMessages.forEach {
                val messageView = SubLayoutAddOnFooterMessageBinding.inflate(LayoutInflater.from(itemView.context))
                val message = it.replace(QUANTITY_PLACEHOLDER, element.mainProductQuantity.toString())
                messageView.labelFooterMessage.text = message
                containerFooterMessages.addView(messageView.root)
            }
        }
    }

}