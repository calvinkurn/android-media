package com.tokopedia.topupbills.telco.prepaid.widget

import android.content.Context
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.elyeproj.loaderviewlibrary.LoaderTextView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.common.topupbills.data.TopupBillsSeamlessFavNumberItem
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.common.topupbills.utils.CommonTopupBillsDataMapper
import com.tokopedia.common.topupbills.view.adapter.TopupBillsAutoCompleteAdapter
import com.tokopedia.common.topupbills.view.model.TopupBillsAutoCompleteContactDataView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.sortfilter.SortFilter
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.topupbills.R
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.unifycomponents.toPx
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
    protected val sortFilterChip: SortFilter
    protected val sortFilterChipShimmer: LoaderTextView

    private val inputNumberResult: TextView
    private val imgOperatorResult: ImageView
    private val layoutResult: ConstraintLayout
    private var favoriteNumbers: List<TopupBillsSeamlessFavNumberItem> = listOf()
    protected val view: View

    private lateinit var listener: ActionListener
    private lateinit var autoCompleteAdapter: TopupBillsAutoCompleteAdapter

    init {
        view = View.inflate(context, getLayout(), this)
        imgOperator = view.findViewById(R.id.telco_img_operator)
        layoutInputNumber = view.findViewById(R.id.telco_input_number_layout)
        sortFilterChip = view.findViewById(R.id.telco_filter_chip)
        sortFilterChipShimmer = view.findViewById(R.id.telco_filter_chip_shimmer)

        layoutResult = view.findViewById(R.id.telco_input_number_result_layout)
        imgOperatorResult = view.findViewById(R.id.telco_img_operator_result)
        inputNumberResult = view.findViewById(R.id.telco_phone_number_result)
        inputNumberField = view.findViewById(R.id.telco_field_input_number)

        sortFilterChip.run {
            sortFilterHorizontalScrollView.setPadding(
                SORT_FILTER_PADDING_16.toPx(), 0 ,SORT_FILTER_PADDING_8.toPx() ,0)
            sortFilterHorizontalScrollView.clipToPadding = false
        }

        initListener()
    }

    private fun initListener() {
        inputNumberField.icon1.run {
            setOnClickListener { listener.onNavigateToContact(false) }
            setImageDrawable(getIconUnifyDrawable(context, IconUnify.CONTACT))
            show()
        }
        inputNumberField.clearIconView.setOnClickListener {
            inputNumberField.editText.setText("")
            inputNumberField.isInputError = false
            inputNumberField.textInputLayout.hint = context.getString(R.string.digital_client_label)
            hideErrorInputNumber()
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
                    if (s?.toString()?.isEmpty() == true) {
                        listener.onClearAutoComplete()
                        imgOperator.visibility = View.GONE
                    }

                    if (inputNumberField.isInputError) clearErrorState()

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

    fun setFavoriteNumber(favNumberItems: List<TopupBillsSeamlessFavNumberItem>) {
        this.favoriteNumbers = favNumberItems
        initSortFilterChip(favNumberItems)
    }

    private fun initSortFilterChip(favnum: List<TopupBillsSeamlessFavNumberItem>) {
        val sortFilter = arrayListOf<SortFilterItem>()
        for (number in favnum.take(5)) {
            if (number.clientName.isEmpty()) {
                listener.onShowFilterChip(false)
            } else {
                listener.onShowFilterChip(true)
            }
            val chipText = if (number.clientName.isEmpty())
                number.clientNumber else number.clientName
            val sortFilterItem = SortFilterItem(chipText, type = ChipsUnify.TYPE_ALTERNATE)
            sortFilterItem.listener = {
                if (number.clientName.isEmpty()) {
                    setContactName(context.getString(R.string.digital_client_label))
                    listener.onClickFilterChip(false)
                } else {
                    setContactName(number.clientName)
                    listener.onClickFilterChip(true)
                }
                setInputNumber(number.clientNumber)
                clearFocusAutoComplete()
            }
            sortFilter.add(sortFilterItem)
        }

        val isMoreThanFive = favnum.size > 5
        if (isMoreThanFive) {
            val sortFilterItem = SortFilterItem(
                "",
                type = ChipsUnify.TYPE_ALTERNATE
            )
            sortFilterItem.listener = {
                listener.onNavigateToContact(true)
            }
            sortFilter.add(sortFilterItem)
        }

        sortFilterChip.addItem(sortFilter)

        if (isMoreThanFive) {
            val chevronRight = IconUnify(
                context, IconUnify.CHEVRON_RIGHT,
                ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_GN500))
            chevronRight.layoutParams = ViewGroup.LayoutParams(
                resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.layout_lvl3),
                resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.layout_lvl3)
            )
            sortFilterChip.chipItems?.last()?.refChipUnify?.addCustomView(chevronRight)
        }
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

    fun clearErrorState() {
        inputNumberField.run {
            setMessage("")
            isInputError = false
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

    fun setFilterChipShimmer(show: Boolean) {
        if (show) {
            sortFilterChip.hide()
            sortFilterChipShimmer.show()
        } else {
            sortFilterChip.show()
            sortFilterChipShimmer.hide()
        }
    }

    private fun initClientNumberAutoComplete(context: Context) {
        autoCompleteAdapter = TopupBillsAutoCompleteAdapter(
            context,
            com.tokopedia.common.topupbills.R.layout.item_topup_bills_autocomplete_number,
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
        fun onNavigateToContact(isSwitchChecked: Boolean)
        fun onRenderOperator()
        fun onClearAutoComplete()
        fun onShowFilterChip(isLabeled: Boolean)
        fun onClickFilterChip(isLabeled: Boolean)
    }

    companion object {
        private const val REGEX_IS_ALPHABET_AND_SPACE_ONLY = "^[a-zA-Z0-9\\s]*$"
        private const val SORT_FILTER_PADDING_8 = 8
        private const val SORT_FILTER_PADDING_16 = 16
        private const val LABEL_MAX_CHAR = 18
        private const val ELLIPSIZE = "..."

        private const val AUTOCOMPLETE_THRESHOLD = 1
        private const val AUTOCOMPLETE_DROPDOWN_VERTICAL_OFFSET = 10
    }
}
