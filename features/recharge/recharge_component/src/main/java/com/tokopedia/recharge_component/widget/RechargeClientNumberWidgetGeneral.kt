package com.tokopedia.recharge_component.widget

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import com.tokopedia.common.topupbills.data.favorite_number_perso.TopupBillsPersoFavNumberItem
import com.tokopedia.common.topupbills.utils.CommonTopupBillsDataMapper
import com.tokopedia.common.topupbills.view.adapter.TopupBillsAutoCompleteAdapter
import com.tokopedia.common.topupbills.view.model.TopupBillsAutoCompleteContactDataView
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.recharge_component.R
import com.tokopedia.recharge_component.databinding.WidgetRechargeClientNumberGeneralBinding
import com.tokopedia.recharge_component.listener.ClientNumberAutoCompleteListener
import com.tokopedia.recharge_component.listener.ClientNumberFilterChipListener
import com.tokopedia.recharge_component.listener.ClientNumberInputFieldListener
import com.tokopedia.recharge_component.model.InputFieldType
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.ChipsUnify
import org.jetbrains.annotations.NotNull
import kotlin.math.abs

/**
 * @author by firman on 10/02/22
 * */

class RechargeClientNumberWidgetGeneral @JvmOverloads constructor(@NotNull context: Context, attrs: AttributeSet? = null,
                                                                  defStyleAttr: Int = 0)
    : BaseCustomView(context, attrs, defStyleAttr) {

        private var binding: WidgetRechargeClientNumberGeneralBinding = WidgetRechargeClientNumberGeneralBinding.inflate(
            LayoutInflater.from(context), this)

    private var mInputFieldListener: ClientNumberInputFieldListener? = null
    private var mAutoCompleteListener: ClientNumberAutoCompleteListener? = null
    private var mFilterChipListener: ClientNumberFilterChipListener? = null

    private var autoCompleteAdapter: TopupBillsAutoCompleteAdapter? = null

    private var textFieldStaticLabel: String = ""

    private var isClearableState = false

    private var customInputNumberFormatter: ((String) -> String)? = null

    init {
        initInputField()
        initSortFilterChip()
    }

    private fun initInputField() {
        binding.clientNumberWidgetBase.clientNumberWidgetInputField.run {
            clearIconView.setOnClickListener {
                this.onClickClearIconUnify(textFieldStaticLabel, { onClickClearIcon() })
            }
        }
    }

    private fun initSortFilterChip(){
        binding.clientNumberWidgetBase.clientNumberWidgetSortFilter.setMainPadding()
    }

    private fun initAutoComplete() {
        binding.clientNumberWidgetBase.clientNumberWidgetInputField.run {
            keyImeChangeListener = object : RechargeTextFieldImeBack.KeyImeChange {
                override fun onPreKeyIme(event: KeyEvent) {
                    if (event.keyCode == KeyEvent.KEYCODE_BACK) {
                        if(isLoading){
                            isClearableState = true
                        } else {
                            clearFocus()
                            hideIndicatorIcon()
                            showClearIcon()
                        }
                    }
                }
            }
            editText.run {
                threshold = AUTOCOMPLETE_THRESHOLD
                dropDownVerticalOffset = AUTOCOMPLETE_DROPDOWN_VERTICAL_OFFSET

                addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {

                    }

                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                    }

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        hideClearIcon()
                        clearErrorState()
                        // if manual input
                        if (abs(before - count) == 1 && mInputFieldListener?.isKeyboardShown() == true) {
                            isClearableState = false
                        }
                        setLoading(s?.toString()?.isNotEmpty() == true)
                        mInputFieldListener?.onRenderOperator(true)
                    }
                })

                setOnEditorActionListener { _, actionId, keyEvent ->
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        if(isLoading){
                            isClearableState = true
                        } else {
                            clearFocus()
                            hideIndicatorIcon()
                            showClearIcon()
                        }
                        hideSoftKeyboard()
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
                    return binding.clientNumberWidgetBase.clientNumberWidgetInputField.editText.text.toString()
                }
            }
        )

        binding.clientNumberWidgetBase.clientNumberWidgetInputField.editText.run {
            setAdapter(autoCompleteAdapter)
        }
    }

    private fun setSortFilterChip(favnum: List<TopupBillsPersoFavNumberItem>) {
        val sortFilter = arrayListOf<SortFilterItem>()

        // create each chip
        for (number in favnum.take(SORT_FILTER_LIMIT)) {
            if (number.subtitle.isEmpty()) {
                mFilterChipListener?.onShowFilterChip(false)
            } else {
                mFilterChipListener?.onShowFilterChip(true)
            }
            val sortFilterItem = SortFilterItem(number.title, type = ChipsUnify.TYPE_ALTERNATE)
            sortFilterItem.listener = {
                if (number.subtitle.isEmpty()) {
                    setContactName("")
                    setInputNumber(number.title, true)
                    mFilterChipListener?.onClickFilterChip(false)
                } else {
                    setContactName(number.title)
                    setInputNumber(number.subtitle, true)
                    mFilterChipListener?.onClickFilterChip(true)
                }
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

        binding.clientNumberWidgetBase.clientNumberWidgetSortFilter.addItem(sortFilter)

        // init navigation chip's icon & color
        if (isMoreThanLimit) {
            val chevronRight = IconUnify(
                context, IconUnify.VIEW_LIST,
                ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_GN500))
            chevronRight.layoutParams = ViewGroup.LayoutParams(
                resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.layout_lvl3),
                resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.layout_lvl3)
            )
            binding.clientNumberWidgetBase.clientNumberWidgetSortFilter.chipItems?.
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
            clientNumberWidgetBase.clientNumberWidgetInputField.run {
                editText.inputType = type.inputType
                icon1.run {
                    setImageDrawable(getIconUnifyDrawable(context, IconUnify.CHECK))
                }
                icon2.run {
                    setImageDrawable(getIconUnifyDrawable(context, type.iconUnifyId))
                    setOnClickListener { mInputFieldListener?.onClickNavigationIcon() }
                    show()
                }
            }
        }
    }

    fun setInputFieldStaticLabel(label: String) {
        textFieldStaticLabel = label
        binding.clientNumberWidgetBase.clientNumberWidgetInputField.textInputLayout.hint = textFieldStaticLabel
    }

    fun setInputNumber(inputNumber: String, isAutoFill: Boolean = false) {
        isClearableState = isAutoFill
        val number = customInputNumberFormatter?.invoke(inputNumber) ?: inputNumber
        binding.clientNumberWidgetBase.clientNumberWidgetInputField.editText.setText(number)
    }

    fun getInputNumber(): String {
        val inputNumber = binding.clientNumberWidgetBase.clientNumberWidgetInputField.editText.text.toString()
        return customInputNumberFormatter?.invoke(inputNumber) ?: inputNumber
    }

    fun setContactName(contactName: String, needValidation: Boolean = true) {
        val label = if (needValidation) validateContactName(textFieldStaticLabel, contactName) else contactName
        binding.clientNumberWidgetBase.clientNumberWidgetInputField.textInputLayout.hint = label
    }

    fun setLoading(isLoading: Boolean) {
        binding.clientNumberWidgetBase.clientNumberWidgetInputField.isLoading = isLoading
    }


    fun showIndicatorIcon() {
        if (isClearableState) {
            hideCheckIcon()
            showClearIcon()
        } else {
            hideClearIcon()
            showCheckIcon()
        }
    }

    fun setErrorInputField(errorMessage: String) {
        binding.clientNumberWidgetBase.clientNumberWidgetInputField.run {
            val temp = textInputLayout.helperText.toString()
            if (temp != errorMessage && getInputNumber().isNotEmpty()) {
                setMessage(errorMessage)
                isInputError = true
            }
        }
    }

    fun setFilterChipShimmer(show: Boolean, shouldHideChip: Boolean = false) {
        binding.run {
            if (show) {
                clientNumberWidgetBase.clientNumberWidgetSortFilter.hide()
                clientNumberWidgetBase.clientNumberWidgetSortFilterShimmer.show()
            } else {
                if (!shouldHideChip) clientNumberWidgetBase.clientNumberWidgetSortFilter.show()
                clientNumberWidgetBase.clientNumberWidgetSortFilterShimmer.hide()
            }
        }
    }

    private fun onClickClearIcon(){
        clearErrorState()
        hideIndicatorIcon()
        mInputFieldListener?.onClearInput()
    }

    fun clearErrorState() {
        binding.clientNumberWidgetBase.clientNumberWidgetInputField.clearErrorField()
    }

    fun setCustomInputNumberFormatter(func: (String) -> String) {
        this.customInputNumberFormatter = func
    }

    private fun showClearIcon() {
        binding.clientNumberWidgetBase.clientNumberWidgetInputField.clearIconView.showClearIconUnify()
    }

    private fun hideClearIcon() {
        binding.clientNumberWidgetBase.clientNumberWidgetInputField.clearIconView.hideClearIconUnify()
    }

    fun hideIndicatorIcon(shouldShowClearIcon: Boolean = false) {
        if (shouldShowClearIcon && isClearableState) showClearIcon() else hideClearIcon()
        hideCheckIcon()
    }

    private fun showCheckIcon() {
        binding.clientNumberWidgetBase.clientNumberWidgetInputField.icon1.show()
    }

    private fun hideCheckIcon() {
        binding.clientNumberWidgetBase.clientNumberWidgetInputField.icon1.hide()
    }

    fun clearFocusAutoComplete() {
        binding.clientNumberWidgetBase.clientNumberWidgetInputField.editText.clearFocus()
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
}