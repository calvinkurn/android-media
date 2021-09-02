package com.tokopedia.topupbills.telco.prepaid.widget

import android.content.Context
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.common.topupbills.data.TopupBillsSeamlessFavNumberItem
import com.tokopedia.common.topupbills.utils.CommonTopupBillsDataMapper
import com.tokopedia.common.topupbills.view.adapter.TopupBillsAutoCompleteAdapter
import com.tokopedia.common.topupbills.view.model.TopupBillsAutoCompleteContactDataView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.topupbills.R
import com.tokopedia.unifycomponents.TextFieldUnify2
import org.jetbrains.annotations.NotNull

/**
 * Created by nabillasabbaha on 25/04/19.
 */
open class DigitalClientNumberWidget @JvmOverloads constructor(@NotNull context: Context, attrs: AttributeSet? = null,
                                                               defStyleAttr: Int = 0)
    : FrameLayout(context, attrs, defStyleAttr) {

    protected val imgOperator: ImageView
    protected val inputNumberField: TextFieldUnify2
    protected val layoutInputNumber: ConstraintLayout

    private val inputNumberResult: TextView
    private val imgOperatorResult: ImageView
    private val layoutResult: ConstraintLayout
    protected val view: View

    private lateinit var listener: ActionListener
    private lateinit var autoCompleteAdapter: TopupBillsAutoCompleteAdapter

    init {
        view = View.inflate(context, getLayout(), this)
        imgOperator = view.findViewById(R.id.telco_img_operator)
        layoutInputNumber = view.findViewById(R.id.telco_input_number_layout)

        layoutResult = view.findViewById(R.id.telco_input_number_result_layout)
        imgOperatorResult = view.findViewById(R.id.telco_img_operator_result)
        inputNumberResult = view.findViewById(R.id.telco_phone_number_result)
        inputNumberField = view.findViewById(R.id.telco_field_input_number)

        inputNumberField.icon1.run {
            setOnClickListener { listener.onNavigateToContact() }
            setImageDrawable(getIconUnifyDrawable(context, IconUnify.CONTACT))
            show()
        }
        inputNumberField.clearIconView.setOnClickListener {
            inputNumberField.editText.setText("")
            inputNumberField.textInputLayout.hint = context.getString(R.string.digital_client_label)
            hideErrorInputNumber()
            it.hide()
            imgOperator.hide()
            listener.onClearAutoComplete()
        }

        inputNumberField.editText.run {
            inputType = InputType.TYPE_CLASS_TEXT
            threshold = AUTOCOMPLETE_THRESHOLD
            dropDownVerticalOffset = AUTOCOMPLETE_DROPDOWN_VERTICAL_OFFSET

            addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {

                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (count == 0) {
                        listener.onClearAutoComplete()
                        imgOperator.visibility = View.GONE
                    }

                    listener.onRenderOperator()
                }
            })

            setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    hideSoftKeyboard()
                }
            }

            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    clearFocus()
                    hideSoftKeyboard()
                }
                true
            }

            setOnItemClickListener { _, _, position, _ ->
                val item = autoCompleteAdapter.getItem(position)
                if (item is TopupBillsAutoCompleteContactDataView) {
                    setContactName(item.name)
                }
            }
        }

        initClientNumberAutoComplete(context)
    }

    fun clearFocusAutoComplete() {
        inputNumberField.editText.clearFocus()
    }

    open fun getLayout(): Int {
        return R.layout.view_telco_input_number
    }

    fun setListener(listener: ActionListener) {
        this.listener = listener
    }

    fun setErrorInputNumber(errorMessage: String, resetProvider: Boolean = false) {
        inputNumberField.run {
            setMessage(errorMessage)
            isInputError = true

            if (resetProvider) {
                imgOperator.visibility = View.GONE
            }
        }
    }

    fun setAutoCompleteList(suggestions: List<TopupBillsSeamlessFavNumberItem>) {
        autoCompleteAdapter.updateItems(
            CommonTopupBillsDataMapper
                .mapSeamlessFavNumberItemToContactDataView(suggestions).toMutableList())
    }

    private fun hideErrorInputNumber() {
        inputNumberField.run {
            setMessage("")
            isInputError = false
        }
    }

    fun setInputNumber(inputNumber: String) {
        inputNumberField.editText.setText(formatPrefixClientNumber(inputNumber))
    }

    fun getInputNumber(): String {
        return formatPrefixClientNumber(inputNumberField.editText.text.toString())
    }

    fun setContactName(contactName: String) {
        val validatedLabel = validateContactName(contactName)
        inputNumberField.textInputLayout.hint = validatedLabel
    }

    fun setIconOperator(url: String) {
        ImageHandler.LoadImage(imgOperator, url)
        ImageHandler.LoadImage(imgOperatorResult, url)
        imgOperator.visibility = View.VISIBLE
        hideErrorInputNumber()
    }

    fun setVisibleResultNumber(show: Boolean) {
        inputNumberResult.text = getInputNumber()
        if (show && getInputNumber().isNotEmpty()) {
            layoutResult.show()
            layoutInputNumber.hide()
        } else {
            layoutInputNumber.show()
            layoutResult.hide()
        }
    }

    private fun initClientNumberAutoComplete(context: Context) {
        autoCompleteAdapter = TopupBillsAutoCompleteAdapter(
            context,
            R.layout.item_topup_bills_autocomplete_number,
            mutableListOf(),
            object : TopupBillsAutoCompleteAdapter.ContactArrayListener {
                override fun getFilterText(): String {
                    return inputNumberField.editText.text.toString()
                }
            }
        )

        inputNumberField.editText.setAdapter(autoCompleteAdapter)
    }

    private fun validateContactName(contactName: String): String {
        val label = if (contactName.matches(REGEX_IS_ALPHABET_AND_SPACE_ONLY.toRegex()) && contactName.isNotEmpty()) {
            contactName
        } else {
            context.getString(R.string.digital_client_label)
        }

        val validatedName = if (label.length > LABEL_MAX_CHAR) {
            label.substring(0, LABEL_MAX_CHAR).plus(ELLIPSIZE)
        } else {
            label
        }

        return validatedName
    }

    private fun validatePrefixClientNumber(phoneNumber: String): String {
        var phoneNumber = phoneNumber
        if (phoneNumber.startsWith("62")) {
            phoneNumber = phoneNumber.replaceFirst("62".toRegex(), "0")
        }
        if (phoneNumber.startsWith("+62")) {
            phoneNumber = phoneNumber.replace("+62", "0")
        }
        phoneNumber = phoneNumber.replace(".", "")

        return phoneNumber.replace("[^0-9]+".toRegex(), "")
    }

    private fun formatPrefixClientNumber(phoneNumber: String?): String {
        phoneNumber?.run {
            if ("".equals(phoneNumber.trim { it <= ' ' }, ignoreCase = true)) {
                return phoneNumber
            }
            var phoneNumberWithPrefix = validatePrefixClientNumber(phoneNumber)
            if (!phoneNumberWithPrefix.startsWith("0")) {
                phoneNumberWithPrefix = "0$phoneNumber"
            }
            return phoneNumberWithPrefix
        }
        return ""
    }

    fun hideSoftKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(this.windowToken, 0)
    }

    interface ActionListener {
        fun onNavigateToContact()
        fun onRenderOperator()
        fun onClearAutoComplete()
    }

    companion object {
        private const val REGEX_IS_ALPHABET_AND_SPACE_ONLY = "^[a-zA-Z0-9\\s]*$"
        private const val LABEL_MAX_CHAR = 18
        private const val ELLIPSIZE = "..."

        private const val AUTOCOMPLETE_THRESHOLD = 1
        private const val AUTOCOMPLETE_DROPDOWN_VERTICAL_OFFSET = 10
    }
}
