package com.tokopedia.topupbills.telco.prepaid.widget

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.elyeproj.loaderviewlibrary.LoaderTextView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.common.topupbills.data.TopupBillsSeamlessFavNumberItem
import com.tokopedia.common.topupbills.widget.TopupBillsSortFilter
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.topupbills.R
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.unifycomponents.TextFieldUnify
import com.tokopedia.unifycomponents.toPx
import org.jetbrains.annotations.NotNull

/**
 * Created by nabillasabbaha on 25/04/19.
 */
open class DigitalClientNumberWidget @JvmOverloads constructor(@NotNull context: Context, attrs: AttributeSet? = null,
                                                               defStyleAttr: Int = 0)
    : FrameLayout(context, attrs, defStyleAttr) {

    protected val imgOperator: ImageView
    protected val btnClear: ImageView
    protected val inputNumberField: TextFieldUnify
    protected val btnContactPicker: ImageView
    protected val layoutInputNumber: ConstraintLayout
    protected val sortFilterChip: TopupBillsSortFilter
    protected val sortFilterChipShimmer: LoaderTextView

    private val inputNumberResult: TextView
    private val imgOperatorResult: ImageView
    private val layoutResult: ConstraintLayout
    private var favoriteNumbers: List<TopupBillsSeamlessFavNumberItem> = listOf()

    protected val view: View
    private lateinit var listener: ActionListener

    init {
        view = View.inflate(context, getLayout(), this)
        imgOperator = view.findViewById(R.id.telco_img_operator)
        btnClear = view.findViewById(R.id.telco_clear_input_number_btn)
        btnContactPicker = view.findViewById(R.id.telco_contact_picker_btn)
        layoutInputNumber = view.findViewById(R.id.telco_input_number_layout)
        sortFilterChip = view.findViewById(R.id.telco_filter_chip)
        sortFilterChipShimmer = view.findViewById(R.id.telco_filter_chip_shimmer)

        layoutResult = view.findViewById(R.id.telco_input_number_result_layout)
        imgOperatorResult = view.findViewById(R.id.telco_img_operator_result)
        inputNumberResult = view.findViewById(R.id.telco_phone_number_result)
        inputNumberField = view.findViewById(R.id.telco_field_input_number)

        btnContactPicker.setOnClickListener { listener.onNavigateToContact(false) }
        btnClear.setOnClickListener {
            inputNumberField.textFieldInput.setText("")
            inputNumberField.textFieldWrapper.hint = context.getString(R.string.digital_client_label)
            hideErrorInputNumber()
            sortFilterChip.clearFilter()
        }

        sortFilterChip.run {
            sortFilterHorizontalScrollView.setPadding(
                SORT_FILTER_PADDING_16.toPx(), 0 ,SORT_FILTER_PADDING_16.toPx() ,0)
            sortFilterHorizontalScrollView.clipToPadding = false
        }

        inputNumberField.textFieldInput.run {
            isClickable = true
            isFocusable = false
            clearFocus()
            addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {

                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (count == 0) {
                        listener.onClearAutoComplete()
                        imgOperator.visibility = View.GONE
                        btnClear.visibility = View.GONE
                    } else {
                        btnClear.visibility = View.VISIBLE
                    }
                    listener.onRenderOperator()
                }
            })

            setOnClickListener {
                it?.run {
                    listener.onClientNumberHasFocus(inputNumberField.textFieldInput.text.toString())
                }
            }
        }
    }

    fun setFavoriteNumber(favNumberItems: List<TopupBillsSeamlessFavNumberItem>) {
        this.favoriteNumbers = favNumberItems
        initSortFilterChip(favNumberItems)
    }

    private fun initSortFilterChip(favnum: List<TopupBillsSeamlessFavNumberItem>) {
        val sortFilter = arrayListOf<SortFilterItem>()
        for (number in favnum.take(5)) {
            val chipText = if (number.clientName.isEmpty())
                number.clientNumber else number.clientName
            val sortFilterItem = SortFilterItem(chipText)
            sortFilterItem.listener = {
                if (number.clientName.isEmpty()) {
                    setContactName(context.getString(R.string.digital_client_label))
                } else {
                    setContactName(number.clientName)
                }
                setInputNumber(number.clientNumber)
            }
            sortFilter.add(sortFilterItem)
        }
        val isMoreThanFive = favnum.size > 5

        if (isMoreThanFive) {
            val sortFilterItem = SortFilterItem(
                context.getString(R.string.digital_client_filter_chip_see_all),
                type = ChipsUnify.TYPE_SELECTED
            )
            sortFilterItem.listener = {
                listener.onNavigateToContact(true)
            }
            sortFilter.add(sortFilterItem)
        }
        sortFilterChip.addItems(sortFilter, isMoreThanFive)
    }

    fun clearFocusAutoComplete() {
        inputNumberField.textFieldInput.clearFocus()
    }

    open fun getLayout(): Int {
        return R.layout.view_telco_input_number
    }

    fun setListener(listener: ActionListener) {
        this.listener = listener
    }

    fun setErrorInputNumber(errorMessage: String) {
        inputNumberField.run {
            setMessage(errorMessage)
            setError(true)
        }
    }

    private fun hideErrorInputNumber() {
        inputNumberField.run {
            setMessage("")
            setError(false)
        }
    }

    fun setInputNumber(inputNumber: String) {
        inputNumberField.textFieldInput.setText(formatPrefixClientNumber(inputNumber))
    }

    fun getInputNumber(): String {
        return formatPrefixClientNumber(inputNumberField.textFieldInput.text.toString())
    }

    fun setContactName(contactName: String) {
        val validatedLabel = validateContactName(contactName)
        inputNumberField.textFieldWrapper.hint = validatedLabel
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

    fun setFilterChipShimmer(show: Boolean) {
        if (show) {
            sortFilterChip.hide()
            sortFilterChipShimmer.show()
        } else {
            sortFilterChip.show()
            sortFilterChipShimmer.hide()
        }
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

    interface ActionListener {
        fun onNavigateToContact(isSwitchChecked: Boolean)
        fun onRenderOperator()
        fun onClearAutoComplete()
        fun onClientNumberHasFocus(clientNumber: String)
    }

    companion object {
        private const val REGEX_IS_ALPHABET_AND_SPACE_ONLY = "^[a-zA-Z0-9\\s]*$"
        private const val SORT_FILTER_PADDING_16 = 16
        private const val LABEL_MAX_CHAR = 18
        private const val ELLIPSIZE = "..."
    }
}
