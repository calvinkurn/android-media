package com.tokopedia.topupbills.telco.common.widget

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.tokopedia.common.topupbills.data.TopupBillsSeamlessFavNumberItem
import com.tokopedia.common.topupbills.utils.CommonTopupBillsDataMapper
import com.tokopedia.common.topupbills.utils.CommonTopupBillsUtil
import com.tokopedia.common.topupbills.view.adapter.TopupBillsAutoCompleteAdapter
import com.tokopedia.common.topupbills.view.model.TopupBillsAutoCompleteContactModel
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isNumeric
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.sortfilter.SortFilter
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.topupbills.R
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.TextFieldUnify2
import com.tokopedia.unifycomponents.toPx
import org.jetbrains.annotations.NotNull
import com.tokopedia.common.topupbills.R as commontopupbillsR
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

/**
 * Created by nabillasabbaha on 25/04/19.
 */
open class DigitalClientNumberWidget @JvmOverloads constructor(
    @NotNull context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    FrameLayout(context, attrs, defStyleAttr) {

    protected val imgOperator: ImageView
    protected val inputNumberField: TextFieldUnify2
    protected val layoutInputNumber: ConstraintLayout
    protected val seeAllChip: ChipsUnify
    protected val sortFilterChip: SortFilter
    protected val sortFilterChipShimmer: LoaderUnify

    private val inputNumberResult: TextView
    private val imgOperatorResult: ImageView
    private val layoutResult: ConstraintLayout
    private var favoriteNumbers: List<TopupBillsSeamlessFavNumberItem> = listOf()
    private var textFieldStaticLabel: String = context.getString(R.string.digital_client_label_telco)
    protected val view: View

    private lateinit var listener: ActionListener
    private lateinit var autoCompleteAdapter: TopupBillsAutoCompleteAdapter

    init {
        view = View.inflate(context, getLayout(), this)
        imgOperator = view.findViewById(R.id.telco_img_operator)
        layoutInputNumber = view.findViewById(R.id.telco_input_number_layout)
        seeAllChip = view.findViewById(R.id.telco_chip_see_all)
        sortFilterChip = view.findViewById(R.id.telco_filter_chip)
        sortFilterChipShimmer = view.findViewById(R.id.telco_filter_chip_shimmer)

        layoutResult = view.findViewById(R.id.telco_input_number_result_layout)
        imgOperatorResult = view.findViewById(R.id.telco_img_operator_result)
        inputNumberResult = view.findViewById(R.id.telco_phone_number_result)
        inputNumberField = view.findViewById(R.id.telco_field_input_number)

        sortFilterChip.run {
            sortFilterHorizontalScrollView.setPadding(
                SORT_FILTER_PADDING_4.toPx(),
                0,
                SORT_FILTER_PADDING_8.toPx(),
                0
            )
            sortFilterHorizontalScrollView.clipToPadding = false
        }

        initListener()
    }

    open fun formatClientNumberInput(clientNumber: String): String {
        return if (clientNumber.isNumeric()) {
            CommonTopupBillsUtil.formatPrefixClientNumber(clientNumber)
        } else {
            clientNumber
        }
    }

    private fun initListener() {
        inputNumberField.icon2.run {
            setOnClickListener { listener.onNavigateToContact(false) }
            setImageDrawable(getIconUnifyDrawable(context, IconUnify.CONTACT))
            show()
        }
        inputNumberField.clearIconView.setOnClickListener {
            inputNumberField.editText.setText("")
            inputNumberField.isInputError = false
            inputNumberField.textInputLayout.hint = textFieldStaticLabel
            clearErrorState()
            imgOperator.hide()
            listener.onClickClearInput()
        }

        inputNumberField.editText.run {
            threshold = AUTOCOMPLETE_THRESHOLD
            dropDownVerticalOffset = AUTOCOMPLETE_DROPDOWN_VERTICAL_OFFSET

            addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    removeTextChangedListener(this)
                    s?.let {
                        it.replace(
                            Int.ZERO,
                            it.length,
                            formatClientNumberInput(it.toString())
                        )
                    }
                    addTextChangedListener(this)
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s?.toString()?.isEmpty() == true) {
                        listener.onClearAutoComplete()
                        imgOperator.visibility = View.GONE
                        clearErrorState()
                    }
                    val isUserManualType = !start.isZero() && count == 1
                    if (isUserManualType) {
                        listener.onUserManualType()
                    }
                    listener.onRenderOperator(isUserManualType)
                }
            })

            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    clearFocus()
                    hideSoftKeyboard()
                }
                true
            }

            setOnItemClickListener { _, _, position, _ ->
                val item = autoCompleteAdapter.getItem(position)
                if (item is TopupBillsAutoCompleteContactModel) {
                    setContactName(item.name)
                    listener.onClickAutoComplete(item.isFavoriteNumber)
                }
            }
        }

        initClientNumberAutoComplete(context)
    }

    fun setInputType(inputType: Int) {
        inputNumberField.editText.inputType = inputType
    }

    fun setFavoriteNumber(favNumberItems: List<TopupBillsSeamlessFavNumberItem>) {
        this.favoriteNumbers = favNumberItems
        initSortFilterChip(favNumberItems)
    }

    private fun initSortFilterChip(favnum: List<TopupBillsSeamlessFavNumberItem>) {
        val sortFilter = arrayListOf<SortFilterItem>()

        seeAllChip.run {
            if (favnum.isNotEmpty()) {
                chip_text.hide()
                chipType = ChipsUnify.TYPE_ALTERNATE
                chipImageResource = getIconUnifyDrawable(
                    context,
                    IconUnify.VIEW_LIST,
                    ContextCompat.getColor(context, unifyprinciplesR.color.Unify_GN500)
                )
                setOnClickListener {
                    listener.onNavigateToContact(true)
                }
                show()
            } else {
                hide()
            }
        }

        // create each chip
        for (number in favnum.take(MAX_CHIP_SIZE)) {
            if (number.clientName.isEmpty()) {
                listener.onShowFilterChip(false)
            } else {
                listener.onShowFilterChip(true)
            }
            val chipText = if (number.clientName.isEmpty()) {
                number.clientNumber
            } else {
                number.clientName
            }
            val sortFilterItem = SortFilterItem(chipText, type = ChipsUnify.TYPE_ALTERNATE)
            sortFilterItem.listener = {
                if (number.clientName.isEmpty()) {
                    setContactName("")
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

        sortFilterChip.addItem(sortFilter)
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
            val dummy = textInputLayout.helperText.toString()
            if (dummy != errorMessage) {
                setMessage(errorMessage)
                isInputError = true

                if (resetProvider) {
                    imgOperator.visibility = View.GONE
                }
            }
        }
    }

    fun isErrorMessageShown(): Boolean = inputNumberField.isInputError

    fun clearErrorState() {
        inputNumberField.run {
            if (isInputError) {
                setMessage("")
                isInputError = false
            }
        }
    }

    fun setAutoCompleteList(
        favoriteSuggestions: List<TopupBillsSeamlessFavNumberItem>,
        contactSuggestions: List<TopupBillsSeamlessFavNumberItem>
    ) {
        autoCompleteAdapter.updateItems(
            CommonTopupBillsDataMapper
                .mapSeamlessFavNumberItemToContactDataView(favoriteSuggestions, isFavoriteNumber = true).toMutableList(),
            CommonTopupBillsDataMapper
                .mapSeamlessFavNumberItemToContactDataView(contactSuggestions, isFavoriteNumber = false).toMutableList()
        )
    }

    fun setInputNumber(inputNumber: String) {
        inputNumberField.editText.setText(inputNumber)
    }

    fun getInputNumber(): String {
        return formatClientNumberInput(inputNumberField.editText.text.toString())
    }

    fun setContactName(contactName: String) {
        val validatedLabel = validateContactName(contactName)
        inputNumberField.textInputLayout.hint = validatedLabel
    }

    fun setIconOperator(url: String) {
        imgOperator.loadImage(url)
        imgOperatorResult.loadImage(url)
        imgOperator.visibility = View.VISIBLE
    }

    fun setFilterChipShimmer(show: Boolean, shouldHideChip: Boolean = false) {
        if (show) {
            sortFilterChip.hide()
            sortFilterChipShimmer.show()
        } else {
            if (!shouldHideChip) sortFilterChip.show()
            sortFilterChipShimmer.hide()
        }
    }

    fun hideContactIcon() {
        inputNumberField.icon1.hide()
    }

    fun hideContactIcon2() {
        inputNumberField.icon2.hide()
    }

    fun setTextFieldStaticLabel(label: String) {
        textFieldStaticLabel = label
        inputNumberField.textInputLayout.hint = textFieldStaticLabel
    }

    private fun initClientNumberAutoComplete(context: Context) {
        autoCompleteAdapter = TopupBillsAutoCompleteAdapter(
            context,
            commontopupbillsR.layout.item_topup_bills_autocomplete_number,
            mutableListOf(),
            mutableListOf(),
            context.getString(commontopupbillsR.string.common_topup_autocomplete_unit_nomor_hp),
            object : TopupBillsAutoCompleteAdapter.ContactArrayListener {
                override fun getFilterText(): String {
                    return inputNumberField.editText.text.toString()
                }
            }
        )

        inputNumberField.editText.run {
            setAdapter(autoCompleteAdapter)
        }
    }

    private fun validateContactName(contactName: String): String {
        return if (contactName.matches(REGEX_IS_ALPHABET_AND_SPACE_ONLY.toRegex()) && contactName.isNotEmpty()) {
            if (contactName.length > LABEL_MAX_CHAR) {
                contactName.substring(0, LABEL_MAX_CHAR).plus(ELLIPSIZE)
            } else {
                contactName
            }
        } else {
            textFieldStaticLabel
        }
    }

    fun hideSoftKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(this.windowToken, 0)
    }

    interface ActionListener {
        fun onNavigateToContact(isSwitchChecked: Boolean)
        fun onRenderOperator(isDelayed: Boolean)
        fun onClearAutoComplete()
        fun onClickClearInput()
        fun onShowFilterChip(isLabeled: Boolean)
        fun onClickFilterChip(isLabeled: Boolean)
        fun onClickAutoComplete(isFavoriteNumber: Boolean)
        fun onUserManualType()
    }

    companion object {
        private const val REGEX_IS_ALPHABET_AND_SPACE_ONLY = "^[a-zA-Z0-9\\s]*$"
        private const val SORT_FILTER_PADDING_4 = 4
        private const val SORT_FILTER_PADDING_8 = 8
        private const val LABEL_MAX_CHAR = 18
        private const val ELLIPSIZE = "..."
        private const val MAX_CHIP_SIZE = 5

        private const val AUTOCOMPLETE_THRESHOLD = 1
        private const val AUTOCOMPLETE_DROPDOWN_VERTICAL_OFFSET = 10
    }
}
