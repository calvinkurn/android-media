package com.tokopedia.recharge_credit_card.widget

import android.content.Context
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.common.topupbills.view.adapter.TopupBillsAutoCompleteAdapter
import com.tokopedia.common.topupbills.view.model.TopupBillsAutoCompleteContactModel
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.imageassets.TokopediaImageUrl.CC_IMG_VERIFIED
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.recharge_component.listener.ClientNumberAutoCompleteListener
import com.tokopedia.recharge_component.listener.ClientNumberFilterChipListener
import com.tokopedia.recharge_component.listener.ClientNumberInputFieldListener
import com.tokopedia.recharge_component.model.client_number.InputFieldType
import com.tokopedia.recharge_component.model.client_number.RechargeClientNumberAutoCompleteModel
import com.tokopedia.recharge_component.model.client_number.RechargeClientNumberChipModel
import com.tokopedia.recharge_component.widget.AUTOCOMPLETE_DROPDOWN_VERTICAL_OFFSET
import com.tokopedia.recharge_component.widget.AUTOCOMPLETE_THRESHOLD
import com.tokopedia.recharge_component.widget.clearErrorField
import com.tokopedia.recharge_component.widget.hideClearIconUnify
import com.tokopedia.recharge_component.widget.isNumeric
import com.tokopedia.recharge_component.widget.onClickClearIconUnify
import com.tokopedia.recharge_component.widget.setMainPadding
import com.tokopedia.recharge_component.widget.showClearIconUnify
import com.tokopedia.recharge_component.widget.validateContactName
import com.tokopedia.recharge_credit_card.R
import com.tokopedia.recharge_credit_card.databinding.WidgetClientNumberWidgetCcBinding
import com.tokopedia.recharge_credit_card.toEditable
import com.tokopedia.recharge_credit_card.util.RechargeCCConst.DEFAULT_MAX_SYMBOLS_LENGTH
import com.tokopedia.recharge_credit_card.util.RechargeCCConst.DIVIDER
import com.tokopedia.recharge_credit_card.util.RechargeCCConst.DIVIDER_MODULO
import com.tokopedia.recharge_credit_card.util.RechargeCCConst.TOTAL_DIGITS
import com.tokopedia.recharge_credit_card.util.RechargeCCConst.TOTAL_SYMBOLS
import com.tokopedia.recharge_credit_card.util.RechargeCCUtil
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerData
import com.tokopedia.unifycomponents.ticker.TickerPagerAdapter
import com.tokopedia.unifycomponents.ticker.TickerPagerCallback
import org.jetbrains.annotations.NotNull
import kotlin.math.abs

/**
 * @author by misael on 14/06/22
 * */
class RechargeCCClientNumberWidget @JvmOverloads constructor(
    @NotNull context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    BaseCustomView(context, attrs, defStyleAttr) {

    private var binding: WidgetClientNumberWidgetCcBinding = WidgetClientNumberWidgetCcBinding.inflate(
        LayoutInflater.from(context),
        this
    )

    private var mInputFieldListener: ClientNumberInputFieldListener? = null
    private var mAutoCompleteListener: ClientNumberAutoCompleteListener? = null
    private var mFilterChipListener: ClientNumberFilterChipListener? = null

    private var autoCompleteAdapter: TopupBillsAutoCompleteAdapter? = null

    private var textFieldStaticLabel: String = ""
    private var inputFieldType: InputFieldType? = null

    /* Credit Card */
    private var mCreditCardActionListener: CreditCardActionListener? = null

    init {
        initInputField()
        initSortFilterChip()
        initPrimaryButton()
        initTickerView()
        initCCLogoVerified()
    }

    private fun initInputField() {
        binding.clientNumberWidgetMainLayout.clientNumberWidgetBase.clientNumberWidgetInputField.run {
            clearIconView.setOnClickListener {
                this.onClickClearIconUnify(textFieldStaticLabel, { onClickClearIcon() })
            }
            editText.imeOptions = EditorInfo.IME_ACTION_DONE
        }
        setLengthMaxTextField()
    }

    private fun initPrimaryButton() {
        binding.clientNumberWidgetMainLayout.clientNumberWidgetButton.run {
            visibility = VISIBLE
            setOnClickListener {
                mCreditCardActionListener?.onClickNextButton(getInputNumber())
            }
        }
    }

    private fun initCCLogoVerified() {
        binding.clientNumberWidgetMainLayout.clientNumberCcLogoVerified.loadImage(CC_IMG_VERIFIED)
    }

    private fun initSortFilterChip() {
        binding.clientNumberWidgetMainLayout.clientNumberWidgetBase.clientNumberWidgetSortFilter.setMainPadding()
    }

    private fun initAutoComplete() {
        binding.clientNumberWidgetMainLayout.clientNumberWidgetBase.clientNumberWidgetInputField.run {
            editText.run {
                threshold = AUTOCOMPLETE_THRESHOLD
                dropDownVerticalOffset = AUTOCOMPLETE_DROPDOWN_VERTICAL_OFFSET

                addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(input: Editable?) {
                        input?.let {
                            if (input.toString().isNumeric() && it.length <= TOTAL_SYMBOLS) {
                                if (!RechargeCCUtil.isInputCorrect(it, TOTAL_SYMBOLS, DIVIDER_MODULO, DIVIDER)) {
                                    removeTextChangedListener(this)
                                    it.replace(
                                        0,
                                        it.length,
                                        RechargeCCUtil.concatStringWith16D(
                                            RechargeCCUtil.getDigitArray(input, TOTAL_DIGITS),
                                            DIVIDER
                                        )
                                    )
                                    addTextChangedListener(this)
                                }
                            }

                            if (input.length == TOTAL_SYMBOLS) {
                                dismissDropDown()
                            }
                        }
                    }

                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                        if (s?.isEmpty() == true) hideClearIcon() else showClearIcon()
                        // if manual input
                        var isManualInput = false
                        if (abs(before - count) == 1 && mInputFieldListener?.isKeyboardShown() == true) {
                            isManualInput = true
                            mCreditCardActionListener?.onManualInput()
                        }
                        setLoading(s?.toString()?.isNotEmpty() == true)
                        disablePrimaryButton()
                        mInputFieldListener?.onRenderOperator(true, isManualInput)
                    }
                })

                setOnEditorActionListener { _, actionId, keyEvent ->
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        clearFocus()
                        hideSoftKeyboard()
                    }
                    true
                }

                setOnItemClickListener { _, _, position, _ ->
                    val item = autoCompleteAdapter?.getItem(position)
                    if (item is TopupBillsAutoCompleteContactModel) {
                        hideSoftKeyboard()
                        setContactName(item.name)
                        mAutoCompleteListener?.onClickAutoComplete(item)
                    }
                }
            }
        }

        val emptyStateUnitRes = when (inputFieldType) {
            InputFieldType.Listrik -> com.tokopedia.common.topupbills.R.string.common_topup_autocomplete_unit_nomor_meter
            InputFieldType.Telco -> com.tokopedia.common.topupbills.R.string.common_topup_autocomplete_unit_nomor_hp
            InputFieldType.CreditCard -> com.tokopedia.common.topupbills.R.string.common_topup_autocomplete_unit_nomor_kartu
            else -> com.tokopedia.common.topupbills.R.string.common_topup_autocomplete_unit_nomor_hp
        }

        autoCompleteAdapter = TopupBillsAutoCompleteAdapter(
            context,
            com.tokopedia.recharge_component.R.layout.item_recharge_client_number_auto_complete,
            mutableListOf(),
            context.getString(emptyStateUnitRes),
            object : TopupBillsAutoCompleteAdapter.ContactArrayListener {
                override fun getFilterText(): String {
                    return binding.clientNumberWidgetMainLayout.clientNumberWidgetBase
                        .clientNumberWidgetInputField.editText.text.toString()
                }
            }
        )

        binding.clientNumberWidgetMainLayout.clientNumberWidgetBase.clientNumberWidgetInputField.editText.run {
            setAdapter(autoCompleteAdapter)
        }
    }

    private fun setSortFilterChip(favnum: List<RechargeClientNumberChipModel>) {
        val sortFilter = arrayListOf<SortFilterItem>()

        binding.clientNumberWidgetMainLayout.clientNumberWidgetBase.clientNumberWidgetSeeAll.run {
            if (favnum.isNotEmpty()) {
                chip_text.hide()
                chipType = ChipsUnify.TYPE_ALTERNATE
                chipImageResource = getIconUnifyDrawable(
                    context,
                    IconUnify.VIEW_LIST,
                    ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_GN500)
                )
                setOnClickListener {
                    mFilterChipListener?.onClickIcon(true)
                }
                show()
            } else {
                hide()
            }
        }

        // create each chip
        for (number in favnum) {
            val sortFilterItem = if (number.clientName.isEmpty()) {
                mFilterChipListener?.onShowFilterChip(false)
                SortFilterItem(number.clientNumber, type = ChipsUnify.TYPE_ALTERNATE)
            } else {
                mFilterChipListener?.onShowFilterChip(true)
                SortFilterItem(number.clientName, type = ChipsUnify.TYPE_ALTERNATE)
            }

            sortFilterItem.listener = {
                clearErrorState()
                hideSoftKeyboard()
                setContactName(number.clientName)
                setInputNumber(number.clientNumber)

                if (number.clientName.isEmpty()) {
                    mFilterChipListener?.onClickFilterChip(false, number)
                } else {
                    mFilterChipListener?.onClickFilterChip(true, number)
                }
                clearFocusAutoComplete()
            }
            sortFilter.add(sortFilterItem)
        }

        binding.clientNumberWidgetMainLayout.clientNumberWidgetBase.clientNumberWidgetSortFilter.addItem(sortFilter)
    }

    private fun onClickClearIcon() {
        clearErrorState()
        hideOperatorIcon()
        mInputFieldListener?.onClearInput()
    }

    fun setFavoriteNumber(favoriteChips: List<RechargeClientNumberChipModel>) {
        setSortFilterChip(favoriteChips)
    }

    fun setAutoCompleteList(suggestions: List<RechargeClientNumberAutoCompleteModel>) {
        autoCompleteAdapter?.updateItems(
            suggestions.map {
                TopupBillsAutoCompleteContactModel(it.clientName, it.clientNumber, it.token)
            }.toMutableList()
        )
    }

    fun setInputFieldType(type: InputFieldType) {
        with(binding) {
            inputFieldType = type
            clientNumberWidgetMainLayout.clientNumberWidgetBase.clientNumberWidgetInputField.run {
                editText.inputType = type.inputType
            }
        }
        initAutoComplete()
    }

    fun setInputFieldStaticLabel(label: String) {
        textFieldStaticLabel = label
        binding.clientNumberWidgetMainLayout.clientNumberWidgetBase
            .clientNumberWidgetInputField.textInputLayout.hint = textFieldStaticLabel
    }

    fun setInputNumber(inputNumber: String) {
        val formattedInputNumber = RechargeCCUtil.concatStringWith16D(
            RechargeCCUtil.getDigitArray(inputNumber.toEditable(), TOTAL_DIGITS),
            DIVIDER
        )
        binding.clientNumberWidgetMainLayout.clientNumberWidgetBase.clientNumberWidgetInputField.editText
            .setText(formattedInputNumber)
    }

    private fun initTickerView() {
        val messages = arrayListOf(
            TickerData(
                title = context.getString(R.string.cc_ticker_title),
                description = context.getString(R.string.cc_ticker_desc),
                type = Ticker.TYPE_ANNOUNCEMENT
            ),
            TickerData(
                title = "",
                description = context.getString(R.string.cc_ticker_2_desc),
                type = Ticker.TYPE_ANNOUNCEMENT
            )
        )

        val tickerAdapter = TickerPagerAdapter(context, messages).apply {
            setPagerDescriptionClickEvent(object : TickerPagerCallback {
                override fun onPageDescriptionViewClick(linkUrl: CharSequence, itemData: Any?) {
                    RouteManager.route(context, "${ApplinkConst.WEBVIEW}?url=$linkUrl")
                    mCreditCardActionListener?.onNavigateTokoCardWebView()
                }
            })
        }
        binding.clientNumberWidgetMainLayout.clientNumberWidgetTicker.run {
            autoSlideDelay = TICKER_AUTO_SLIDE_DELAY
            addPagerView(tickerAdapter, messages)
        }
    }

    fun getInputNumber(): String {
        return binding.clientNumberWidgetMainLayout.clientNumberWidgetBase
            .clientNumberWidgetInputField.editText.text.toString().replace(" ", "")
    }

    fun getFormattedInputNumber(): String {
        return binding.clientNumberWidgetMainLayout.clientNumberWidgetBase
            .clientNumberWidgetInputField.editText.text.toString()
    }

    fun setContactName(contactName: String, needValidation: Boolean = true) {
        val label = if (needValidation) validateContactName(textFieldStaticLabel, contactName) else contactName
        binding.clientNumberWidgetMainLayout.clientNumberWidgetBase.clientNumberWidgetInputField.textInputLayout.hint = label
    }

    fun resetContactName() {
        binding.clientNumberWidgetMainLayout.clientNumberWidgetBase.clientNumberWidgetInputField.textInputLayout.hint =
            textFieldStaticLabel
    }

    fun setLoading(isLoading: Boolean) {
        binding.clientNumberWidgetMainLayout.clientNumberWidgetBase.clientNumberWidgetInputField.isLoading = isLoading
    }

    fun isErrorMessageShown(): Boolean = binding.clientNumberWidgetMainLayout.clientNumberWidgetBase
        .clientNumberWidgetInputField.isInputError

    fun isInputFieldEmpty(): Boolean = binding.clientNumberWidgetMainLayout.clientNumberWidgetBase
        .clientNumberWidgetInputField.editText.text.isEmpty()

    fun setErrorInputField(errorMessage: String) {
        binding.clientNumberWidgetMainLayout.clientNumberWidgetBase.clientNumberWidgetInputField.run {
            val temp = textInputLayout.helperText.toString()
            if (temp != errorMessage && getInputNumber().isNotEmpty()) {
                setMessage(errorMessage)
                isInputError = true
            }
        }
    }

    fun setFilterChipShimmer(show: Boolean, shouldHideChip: Boolean = false) {
        binding.clientNumberWidgetMainLayout.clientNumberWidgetBase.run {
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
        binding.clientNumberWidgetMainLayout.clientNumberWidgetBase
            .clientNumberWidgetInputField.clearErrorField()
    }

    fun showClearIcon() {
        binding.clientNumberWidgetMainLayout.clientNumberWidgetBase
            .clientNumberWidgetInputField.clearIconView.showClearIconUnify()
    }

    fun hideClearIcon() {
        binding.clientNumberWidgetMainLayout.clientNumberWidgetBase
            .clientNumberWidgetInputField.clearIconView.hideClearIconUnify()
    }

    fun showOperatorIcon(url: String) {
        with(binding) {
            clientNumberWidgetOperatorGroup.show()
            clientNumberWidgetOperatorIcon.loadImage(url)
        }
    }

    fun hideOperatorIcon() {
        with(binding) {
            clientNumberWidgetOperatorGroup.invisible()
        }
    }

    fun clearFocusAutoComplete() {
        binding.clientNumberWidgetMainLayout.clientNumberWidgetBase
            .clientNumberWidgetInputField.editText.clearFocus()
    }

    fun hideSoftKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(this.windowToken, 0)
    }

    fun setInputFieldListener(inputFieldListener: ClientNumberInputFieldListener) {
        mInputFieldListener = inputFieldListener
    }

    fun setAutoCompleteListener(autoCompleteListener: ClientNumberAutoCompleteListener) {
        mAutoCompleteListener = autoCompleteListener
    }

    fun setFilterChipListener(filterChipListener: ClientNumberFilterChipListener) {
        mFilterChipListener = filterChipListener
    }

    fun setCreditCardATCListener(creditCardActionListener: CreditCardActionListener) {
        mCreditCardActionListener = creditCardActionListener
    }

    fun enablePrimaryButton() {
        binding.clientNumberWidgetMainLayout.clientNumberWidgetButton.isEnabled = true
    }

    fun disablePrimaryButton() {
        binding.clientNumberWidgetMainLayout.clientNumberWidgetButton.isEnabled = false
    }

    private fun setLengthMaxTextField() {
        val filterArray = arrayOfNulls<InputFilter>(1)
        filterArray[Int.ZERO] = InputFilter.LengthFilter(DEFAULT_MAX_SYMBOLS_LENGTH)
        binding.clientNumberWidgetMainLayout.clientNumberWidgetBase
            .clientNumberWidgetInputField.editText.filters = filterArray
    }

    interface CreditCardActionListener {
        fun onClickNextButton(clientNumber: String)
        fun onManualInput()
        fun onNavigateTokoCardWebView()
    }

    companion object {
        private val TICKER_AUTO_SLIDE_DELAY = 5000
    }
}
