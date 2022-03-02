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
import com.tokopedia.common.topupbills.favorite.data.TopupBillsPersoFavNumberItem
import com.tokopedia.common.topupbills.favorite.util.FavoriteNumberDataMapper
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
import com.tokopedia.recharge_component.listener.ClientNumberAutoCompleteListener
import com.tokopedia.recharge_component.listener.ClientNumberFilterChipListener
import com.tokopedia.recharge_component.listener.ClientNumberInputFieldListener
import com.tokopedia.recharge_component.model.InputFieldType
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.unifycomponents.toPx
import org.jetbrains.annotations.NotNull
import kotlin.math.abs

/**
 * @author by misael on 05/01/22
 * */
class RechargeClientNumberWidget @JvmOverloads constructor(@NotNull context: Context, attrs: AttributeSet? = null,
                                                           defStyleAttr: Int = 0)
    : BaseCustomView(context, attrs, defStyleAttr) {

    private var binding: WidgetRechargeClientNumberBinding = WidgetRechargeClientNumberBinding.inflate(
        LayoutInflater.from(context), this)

    private var mInputFieldListener: ClientNumberInputFieldListener? = null
    private var mAutoCompleteListener: ClientNumberAutoCompleteListener? = null
    private var mFilterChipListener: ClientNumberFilterChipListener? = null

    private var autoCompleteAdapter: TopupBillsAutoCompleteAdapter? = null

    private var textFieldStaticLabel: String = ""

    private var isClearableState = false

    // this custom formatter will be used when user call getInputNumber & setInputNumber
    private var customInputNumberFormatter: ((String) -> String)? = null

    init {
        initInputField()
        initSortFilterChip()
        initAutoComplete()
    }

    private fun initInputField() {
        binding.clientNumberWidgetMainLayout.clientNumberWidgetBase.clientNumberWidgetInputField.run {
            clearIconView.setOnClickListener {
                this.onClickClearIconUnify(textFieldStaticLabel, { onClickClearIcon()})
            }
        }
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
                        clearFocus()
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
                    return binding.clientNumberWidgetMainLayout.clientNumberWidgetBase.clientNumberWidgetInputField.editText.text.toString()
                }
            }
        )

        binding.clientNumberWidgetMainLayout.clientNumberWidgetBase.clientNumberWidgetInputField.editText.run {
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
                    mFilterChipListener?.onClickFilterChip(false, number.trackingData.operatorId)
                } else {
                    setContactName(number.title)
                    setInputNumber(number.subtitle, true)
                    mFilterChipListener?.onClickFilterChip(true, number.trackingData.operatorId)
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

        binding.clientNumberWidgetMainLayout.clientNumberWidgetBase.clientNumberWidgetSortFilter.addItem(sortFilter)

        // init navigation chip's icon & color
        if (isMoreThanLimit) {
            val chevronRight = IconUnify(
                context, IconUnify.VIEW_LIST,
                ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_GN500))
            chevronRight.layoutParams = ViewGroup.LayoutParams(
                resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.layout_lvl3),
                resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.layout_lvl3)
            )
            binding.clientNumberWidgetMainLayout.clientNumberWidgetBase.clientNumberWidgetSortFilter.chipItems?.
                last()?.refChipUnify?.addCustomView(chevronRight)
        }
    }

    private fun onClickClearIcon(){
        clearErrorState()
        hideIndicatorIcon()
        hideOperatorIcon()
        mInputFieldListener?.onClearInput()
    }

    fun setFavoriteNumber(favNumberItems: List<TopupBillsPersoFavNumberItem>) {
        setSortFilterChip(favNumberItems)
    }

    fun setAutoCompleteList(suggestions: List<TopupBillsPersoFavNumberItem>) {
        autoCompleteAdapter?.updateItems(
            FavoriteNumberDataMapper
                .mapPersoFavNumberItemToContactDataView(suggestions).toMutableList())
    }

    fun setInputFieldType(type: InputFieldType) {
        with(binding) {
            clientNumberWidgetMainLayout.clientNumberWidgetBase.clientNumberWidgetInputField.run {
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
        binding.clientNumberWidgetMainLayout.clientNumberWidgetBase.clientNumberWidgetInputField.textInputLayout.hint = textFieldStaticLabel
    }

    fun setInputNumber(inputNumber: String, isAutoFill: Boolean = false) {
        isClearableState = isAutoFill
        val number = customInputNumberFormatter?.invoke(inputNumber) ?: inputNumber
        binding.clientNumberWidgetMainLayout.clientNumberWidgetBase.clientNumberWidgetInputField.editText.setText(number)
    }

    fun getInputNumber(): String {
        val inputNumber = binding.clientNumberWidgetMainLayout.clientNumberWidgetBase.clientNumberWidgetInputField.editText.text.toString()
        return customInputNumberFormatter?.invoke(inputNumber) ?: inputNumber
    }

    fun setContactName(contactName: String, needValidation: Boolean = true) {
        val label = if (needValidation) validateContactName(textFieldStaticLabel, contactName) else contactName
        binding.clientNumberWidgetMainLayout.clientNumberWidgetBase.clientNumberWidgetInputField.textInputLayout.hint = label
    }

    fun setLoading(isLoading: Boolean) {
        binding.clientNumberWidgetMainLayout.clientNumberWidgetBase.clientNumberWidgetInputField.isLoading = isLoading
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

    fun hideIndicatorIcon(shouldShowClearIcon: Boolean = false) {
        if (shouldShowClearIcon && isClearableState) showClearIcon() else hideClearIcon()
        hideCheckIcon()
    }

    fun setClearable() {
        binding.clientNumberWidgetMainLayout.clientNumberWidgetBase.clientNumberWidgetInputField.run {
            isClearableState = true
            if (!isLoading) {
                clearFocus()
                hideIndicatorIcon(true)
            }
        }
    }

    private fun showCheckIcon() {
        binding.clientNumberWidgetMainLayout.clientNumberWidgetBase.clientNumberWidgetInputField.icon1.show()
    }

    private fun hideCheckIcon() {
        binding.clientNumberWidgetMainLayout.clientNumberWidgetBase.clientNumberWidgetInputField.icon1.hide()
    }

    fun isErrorMessageShown(): Boolean = binding.clientNumberWidgetMainLayout.clientNumberWidgetBase.clientNumberWidgetInputField.isInputError

    fun setErrorInputField(errorMessage: String, resetProvider: Boolean = false) {
        binding.clientNumberWidgetMainLayout.clientNumberWidgetBase.clientNumberWidgetInputField.run {
            val temp = textInputLayout.helperText.toString()
            if (temp != errorMessage && getInputNumber().isNotEmpty()) {
                setMessage(errorMessage)
                isInputError = true

                if (resetProvider) {
                    binding.clientNumberWidgetOperatorGroup.hide()
                }
            }
        }
    }

    fun setFilterChipShimmer(show: Boolean, shouldHideChip: Boolean = false) {
        binding.run {
            if (show) {
                clientNumberWidgetMainLayout.clientNumberWidgetBase.clientNumberWidgetSortFilter.hide()
                clientNumberWidgetMainLayout.clientNumberWidgetBase.clientNumberWidgetSortFilterShimmer.show()
            } else {
                if (!shouldHideChip) clientNumberWidgetMainLayout.clientNumberWidgetBase.clientNumberWidgetSortFilter.show()
                clientNumberWidgetMainLayout.clientNumberWidgetBase.clientNumberWidgetSortFilterShimmer.hide()
            }
        }
    }

    fun clearErrorState() {
        binding.clientNumberWidgetMainLayout.clientNumberWidgetBase.clientNumberWidgetInputField.clearErrorField()
    }

    fun setVisibleSimplifiedLayout(show: Boolean) {
        with (binding) {
            includeLayout.clientNumberSimplifiedLabel.text =
                binding.clientNumberWidgetMainLayout.clientNumberWidgetBase.clientNumberWidgetInputField.textInputLayout.hint
            includeLayout.clientNumberSimplifiedPhoneNumber.text = getInputNumber()

            if (show) {
                includeLayout.clientNumberWidgetSimplifiedLayout.show()
                clientNumberWidgetMainLayout.clientNumberWidgetBase.root.hide()
                clientNumberWidgetOperatorGroup.hide()
            } else {
                includeLayout.clientNumberWidgetSimplifiedLayout.hide()
                clientNumberWidgetMainLayout.clientNumberWidgetBase.root.show()
                clientNumberWidgetOperatorGroup.show()
            }
        }
    }

    fun setCustomInputNumberFormatter(func: (String) -> String) {
        this.customInputNumberFormatter = func
    }

    private fun showClearIcon() {
        binding.clientNumberWidgetMainLayout.clientNumberWidgetBase.clientNumberWidgetInputField.clearIconView.showClearIconUnify()
    }

    private fun hideClearIcon() {
        binding.clientNumberWidgetMainLayout.clientNumberWidgetBase.clientNumberWidgetInputField.clearIconView.hideClearIconUnify()
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
        binding.clientNumberWidgetMainLayout.clientNumberWidgetBase.clientNumberWidgetInputField.editText.clearFocus()
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