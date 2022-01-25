package com.tokopedia.recharge_component.widget

import android.content.Context
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import com.tokopedia.common.topupbills.data.favorite_number_perso.TopupBillsPersoFavNumberItem
import com.tokopedia.common.topupbills.utils.CommonTopupBillsDataMapper
import com.tokopedia.common.topupbills.utils.CommonTopupBillsUtil
import com.tokopedia.common.topupbills.view.adapter.TopupBillsAutoCompleteAdapter
import com.tokopedia.common.topupbills.view.model.TopupBillsAutoCompleteContactDataView
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.recharge_component.R
import com.tokopedia.recharge_component.databinding.WidgetRechargeClientNumberBinding
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.unifycomponents.toPx
import org.jetbrains.annotations.NotNull

class RechargeClientNumberWidget @JvmOverloads constructor(@NotNull context: Context, attrs: AttributeSet? = null,
                                                           defStyleAttr: Int = 0)
    : BaseCustomView(context, attrs, defStyleAttr) {

    private var binding: WidgetRechargeClientNumberBinding = WidgetRechargeClientNumberBinding.inflate(
        LayoutInflater.from(context), this)

    private var mInputFieldListener: ClientNumberInputFieldListener? = null
    private var mAutoCompleteListener: ClientNumberAutoCompleteListener? = null
    private var mFilterChipListener: ClientNumberFilterChipListener? = null

    private var inputNumberValidator: ((String) -> String)? = null

    private var autoCompleteAdapter: TopupBillsAutoCompleteAdapter? = null

    private var textFieldStaticLabel: String = ""

    init {
        initInputField()
        initSortFilterChip()
        initAutoComplete()
    }

    private fun initInputField() {
        binding.clientNumberWidgetInputField.run {
            clearIconView.setOnClickListener {
                editText.setText("")
                isInputError = false
                textInputLayout.hint = textFieldStaticLabel
                clearErrorState()
                hideCheckIcon()
                hideOperatorIcon()
                mInputFieldListener?.onClearInput()
            }
        }
    }

    private fun initSortFilterChip() {
        binding.clientNumberWidgetSortFilter.run {
            sortFilterHorizontalScrollView.setPadding(
                SORT_FILTER_PADDING_16.toPx(), 0 ,SORT_FILTER_PADDING_8.toPx() ,0)
            sortFilterHorizontalScrollView.clipToPadding = false
        }
    }

    private fun initAutoComplete() {
        binding.clientNumberWidgetInputField.run {
            editText.run {
                threshold = AUTOCOMPLETE_THRESHOLD
                dropDownVerticalOffset = AUTOCOMPLETE_DROPDOWN_VERTICAL_OFFSET

                addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {
                        // [Misael] explore showClearIcon here
                    }

                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                    }

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        hideClearIcon()
                        if (s?.toString()?.isEmpty() == true) {
                            clearErrorState()
                        } else {
                            executeValidator(s.toString())
                        }
                        mInputFieldListener?.onRenderOperator(true)
                    }
                })

                setOnEditorActionListener { _, actionId, _ ->
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        clearFocus()
                        hideSoftKeyboard()
                        hideCheckIcon()
                        showClearIcon()
                    }
                    true
                }

                setOnItemClickListener { _, _, position, _ ->
                    val item = autoCompleteAdapter?.getItem(position)
                    if (item is TopupBillsAutoCompleteContactDataView) {
                        setContactName(item.name)
                        mAutoCompleteListener?.onClickAutoComplete(item.name.isNotEmpty())
                    }
                }
            }
        }

        autoCompleteAdapter = TopupBillsAutoCompleteAdapter(
            context,
            R.layout.item_recharge_client_number_auto_complete,
            mutableListOf(),
            object : TopupBillsAutoCompleteAdapter.ContactArrayListener {
                override fun getFilterText(): String {
                    return binding.clientNumberWidgetInputField.editText.text.toString()
                }
            }
        )

        binding.clientNumberWidgetInputField.editText.run {
            setAdapter(autoCompleteAdapter)
        }
    }

    private fun setSortFilterChip(favnum: List<TopupBillsPersoFavNumberItem>) {
        val sortFilter = arrayListOf<SortFilterItem>()

        // create each chip
        for (number in favnum.take(SORT_FILTER_LIMIT)) {
            if (number.clientName.isEmpty()) {
                mFilterChipListener?.onShowFilterChip(false)
            } else {
                mFilterChipListener?.onShowFilterChip(true)
            }
            val chipText = if (number.clientName.isEmpty())
                number.clientNumber else number.clientName
            val sortFilterItem = SortFilterItem(chipText, type = ChipsUnify.TYPE_ALTERNATE)
            sortFilterItem.listener = {
                if (number.clientName.isEmpty()) {
                    setContactName("")
                    mFilterChipListener?.onClickFilterChip(false)
                } else {
                    setContactName(number.clientName)
                    mFilterChipListener?.onClickFilterChip(true)
                }
                setInputNumber(number.clientNumber)
                clearFocusAutoComplete()
            }
            sortFilter.add(sortFilterItem)
        }

        // create extra chip for navigation
        val isMoreThanLimit = favnum.size > SORT_FILTER_LIMIT
        if (isMoreThanLimit) {
            val sortFilterItem = SortFilterItem(
                "",
                type = ChipsUnify.TYPE_ALTERNATE
            )
            sortFilterItem.listener = {
                mFilterChipListener?.onClickIcon(true)
            }
            sortFilter.add(sortFilterItem)
        }

        binding.clientNumberWidgetSortFilter.addItem(sortFilter)

        // init navigation chip's icon & color
        if (isMoreThanLimit) {
            val chevronRight = IconUnify(
                context, IconUnify.VIEW_LIST,
                ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_GN500))
            chevronRight.layoutParams = ViewGroup.LayoutParams(
                resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.layout_lvl3),
                resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.layout_lvl3)
            )
            binding.clientNumberWidgetSortFilter.chipItems?.
                last()?.refChipUnify?.addCustomView(chevronRight)
        }
    }

    fun setFavoriteNumber(favNumberItems: List<TopupBillsPersoFavNumberItem>) {
        setSortFilterChip(favNumberItems)
    }

    fun setAutoCompleteList(suggestions: List<TopupBillsPersoFavNumberItem>) {
        autoCompleteAdapter?.updateItems(
            CommonTopupBillsDataMapper
                .mapPersoFavNumberItemToContactDataView(suggestions).toMutableList())
    }

    fun setInputFieldType(type: InputFieldType) {
        with(binding) {
            clientNumberWidgetInputField.run {
                editText.inputType = type.inputType
                icon1.run {
                    setImageDrawable(getIconUnifyDrawable(context, IconUnify.CHECK))
                }
                icon2.run {
                    setImageDrawable(getIconUnifyDrawable(context, type.iconUnifyId))
                    setOnClickListener { mInputFieldListener?.onClickContact() }
                    show()
                }
            }
            clientNumberWidgetOperatorGroup.show()
        }
    }

    fun setInputFieldStaticLabel(label: String) {
        textFieldStaticLabel = label
        binding.clientNumberWidgetInputField.textInputLayout.hint = textFieldStaticLabel
    }

    fun setInputNumber(inputNumber: String) {
        binding.clientNumberWidgetInputField.editText.setText(
            CommonTopupBillsUtil.formatPrefixClientNumber(inputNumber))
    }

    fun getInputNumber(): String {
        return CommonTopupBillsUtil.formatPrefixClientNumber(binding.clientNumberWidgetInputField.editText.text.toString())
    }

    fun setContactName(contactName: String, needValidation: Boolean = true) {
        val label = if (needValidation) validateContactName(contactName) else contactName
        binding.clientNumberWidgetInputField.textInputLayout.hint = label
    }

    fun setLoading(isLoading: Boolean) {
        binding.clientNumberWidgetInputField.isLoading = isLoading
    }

    private fun showCheckIcon() {
        binding.clientNumberWidgetInputField.icon1.show()
    }

    private fun hideCheckIcon() {
        binding.clientNumberWidgetInputField.icon1.hide()
    }

    fun isErrorMessageShown(): Boolean = binding.clientNumberWidgetInputField.isInputError

    fun setErrorInputField(errorMessage: String) {
        binding.clientNumberWidgetInputField.run {
            val temp = textInputLayout.helperText.toString()
            if (temp != errorMessage) {
                setMessage(errorMessage)
                isInputError = true
            }
        }
    }

    fun setFilterChipShimmer(show: Boolean, shouldHideChip: Boolean = false) {
        binding.run {
            if (show) {
                clientNumberWidgetSortFilter.hide()
                clientNumberWidgetSortFilterShimmer.show()
            } else {
                if (!shouldHideChip) clientNumberWidgetSortFilter.show()
                clientNumberWidgetSortFilterShimmer.hide()
            }
        }
    }

    fun clearErrorState() {
        binding.clientNumberWidgetInputField.run {
            if (isInputError) {
                setMessage("")
                isInputError = false
            }
        }
    }

    fun setVisibleSimplifiedLayout(show: Boolean) {
        with (binding) {
            includeLayout.clientNumberSimplifiedLabel.text =
                binding.clientNumberWidgetInputField.textInputLayout.hint
            includeLayout.clientNumberSimplifiedPhoneNumber.text = getInputNumber()

            if (show) {
                includeLayout.clientNumberWidgetSimplifiedLayout.show()
                clientNumberWidgetMainLayout.hide()
                clientNumberWidgetOperatorGroup.hide()
            } else {
                includeLayout.clientNumberWidgetSimplifiedLayout.hide()
                clientNumberWidgetMainLayout.show()
                clientNumberWidgetOperatorGroup.show()
            }
        }
    }

    private fun showClearIcon() {
        with (binding.clientNumberWidgetInputField.clearIconView) {
            if (!isVisible) show()
        }
    }

    private fun hideClearIcon() {
        with (binding.clientNumberWidgetInputField.clearIconView) {
            if (isVisible) hide()
        }
    }

    fun showOperatorIcon(url: String) {
        with (binding) {
            clientNumberWidgetOperatorGroup.show()
            clientNumberWidgetOperatorIcon.loadImage(url)
            includeLayout.clientNumberSimplifiedOperatorIcon.loadImage(url)
        }
    }

    fun hideOperatorIcon() {
        with (binding) {
            clientNumberWidgetOperatorGroup.hide()
        }
    }

    fun clearFocusAutoComplete() {
        binding.clientNumberWidgetInputField.editText.clearFocus()
    }

    private fun validateContactName(contactName: String): String {
        return if (contactName.isAlphanumeric() && contactName.isNotEmpty()) {
            if (contactName.length > LABEL_MAX_CHAR) {
                contactName.substring(0, LABEL_MAX_CHAR).plus(ELLIPSIZE)
            } else {
                contactName
            }
        } else {
            textFieldStaticLabel
        }
    }

    private fun executeValidator(s: String) {
        setLoading(true)
        if (s.isNumeric()) {
            val errorMessage = inputNumberValidator?.invoke(
                CommonTopupBillsUtil.formatPrefixClientNumber(s))

            errorMessage?.run {
                if (isEmpty()) {
                    setLoading(false)
                    showCheckIcon()
                    clearErrorState()
                } else {
                    setLoading(false)
                    setErrorInputField(this)
                }
            }
        }
    }

    fun hideSoftKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(this.windowToken, 0)
    }

    fun setListener(
        inputFieldListener: ClientNumberInputFieldListener?,
        autoCompleteListener: ClientNumberAutoCompleteListener?,
        filterChipListener: ClientNumberFilterChipListener?
    ) {
        mInputFieldListener = inputFieldListener
        mAutoCompleteListener = autoCompleteListener
        mFilterChipListener = filterChipListener
    }

    fun setInputNumberValidator(func: (inputNumber: String) -> String) {
        inputNumberValidator = func
    }

    interface ClientNumberInputFieldListener {
        fun onRenderOperator(isDelayed: Boolean)
        fun onClearInput()
        fun onClickContact()
    }

    interface ClientNumberAutoCompleteListener {
        fun onClickAutoComplete(isFavoriteContact: Boolean)
    }

    interface ClientNumberFilterChipListener {
        fun onClickIcon(isSwitchChecked: Boolean)
        fun onShowFilterChip(isLabeled: Boolean)
        fun onClickFilterChip(isLabeled: Boolean)
    }

    enum class InputFieldType(
        val inputType: Int,
        val iconUnifyId: Int,
        val hasOperatorIcon: Boolean
    ) {
        Telco(InputType.TYPE_CLASS_TEXT, IconUnify.CONTACT, true),
        Listrik(InputType.TYPE_CLASS_TEXT, IconUnify.QR_CODE, false),
        Emoney(InputType.TYPE_CLASS_NUMBER, IconUnify.CAMERA, false)
    }


    enum class InputNumberActionType {
        MANUAL, CONTACT, FAVORITE, CHIP, AUTOCOMPLETE
    }

    private fun String.isNumeric(): Boolean {
        return this.matches(REGEX_IS_NUMERIC.toRegex())
    }

    private fun String.isAlphanumeric(): Boolean {
        return this.matches(REGEX_IS_ALPHABET_AND_SPACE_ONLY.toRegex())
    }

    companion object {
        private const val REGEX_IS_ALPHABET_AND_SPACE_ONLY = "^[a-zA-Z0-9\\s]*$"
        private const val REGEX_IS_NUMERIC = "^[0-9\\s]*$"
        private const val SORT_FILTER_PADDING_8 = 8
        private const val SORT_FILTER_PADDING_16 = 16
        private const val SORT_FILTER_LIMIT = 3
        private const val LABEL_MAX_CHAR = 18
        private const val ELLIPSIZE = "..."

        private const val AUTOCOMPLETE_THRESHOLD = 1
        private const val AUTOCOMPLETE_DROPDOWN_VERTICAL_OFFSET = 10
    }
}