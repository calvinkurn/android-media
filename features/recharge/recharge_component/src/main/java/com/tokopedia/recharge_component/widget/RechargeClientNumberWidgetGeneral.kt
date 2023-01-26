package com.tokopedia.recharge_component.widget

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.common.topupbills.data.TopupBillsEnquiryAttribute
import com.tokopedia.common.topupbills.data.TopupBillsEnquiryMainInfo
import com.tokopedia.common.topupbills.data.product.CatalogOperator
import com.tokopedia.common.topupbills.view.adapter.TopupBillsAutoCompleteAdapter
import com.tokopedia.common.topupbills.view.model.TopupBillsAutoCompleteContactModel
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.getDimens
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.recharge_component.R
import com.tokopedia.recharge_component.databinding.WidgetRechargeClientNumberGeneralBinding
import com.tokopedia.recharge_component.listener.ClientNumberAutoCompleteListener
import com.tokopedia.recharge_component.listener.ClientNumberFilterChipListener
import com.tokopedia.recharge_component.listener.ClientNumberInputFieldListener
import com.tokopedia.recharge_component.listener.ClientNumberSortFilterListener
import com.tokopedia.recharge_component.model.client_number.InputFieldType
import com.tokopedia.recharge_component.model.client_number.RechargeClientNumberAutoCompleteModel
import com.tokopedia.recharge_component.model.client_number.RechargeClientNumberChipModel
import com.tokopedia.recharge_component.presentation.adapter.InquiryRechargeClientNumberAdapter
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.ChipsUnify
import org.jetbrains.annotations.NotNull
import kotlin.math.abs
import com.tokopedia.unifyprinciples.R.dimen as unifyDimens
/**
 * @author by firman on 10/02/22
 * */

class RechargeClientNumberWidgetGeneral @JvmOverloads constructor(
    @NotNull context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    BaseCustomView(context, attrs, defStyleAttr) {

    private var binding: WidgetRechargeClientNumberGeneralBinding = WidgetRechargeClientNumberGeneralBinding.inflate(
        LayoutInflater.from(context),
        this
    )

    private var mInputFieldListener: ClientNumberInputFieldListener? = null
    private var mAutoCompleteListener: ClientNumberAutoCompleteListener? = null
    private var mFilterChipListener: ClientNumberFilterChipListener? = null
    private var mSortFilterListener: ClientNumberSortFilterListener? = null

    private var autoCompleteAdapter: TopupBillsAutoCompleteAdapter? = null

    private var textFieldStaticLabel: String = ""
    private var inputFieldType: InputFieldType? = null

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
            editText.imeOptions = EditorInfo.IME_ACTION_DONE
        }
    }

    private fun initSortFilterChip() {
        binding.clientNumberWidgetBase.clientNumberWidgetSortFilter.setMainPadding()
    }

    private fun initAutoComplete() {
        binding.clientNumberWidgetBase.clientNumberWidgetInputField.run {
            editText.run {
                threshold = AUTOCOMPLETE_THRESHOLD
                dropDownVerticalOffset = AUTOCOMPLETE_DROPDOWN_VERTICAL_OFFSET

                addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {
                    }

                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    }

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        var isManualInput = false
                        hideClearIcon()
                        clearErrorState()
                        // if manual input
                        if (abs(before - count) == 1) {
                            isManualInput = true
                        }
                        setLoading(s?.toString()?.isNotEmpty() == true)
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
                        setContactName(item.name)
                        mAutoCompleteListener?.onClickAutoComplete(item)
                    }
                }
            }
        }

        val emptyStateUnitRes = when (inputFieldType) {
            InputFieldType.Listrik -> com.tokopedia.common.topupbills.R.string.common_topup_autocomplete_unit_nomor_meter
            InputFieldType.Telco -> com.tokopedia.common.topupbills.R.string.common_topup_autocomplete_unit_nomor_hp
            else -> com.tokopedia.common.topupbills.R.string.common_topup_autocomplete_unit_nomor_meter
        }

        autoCompleteAdapter = TopupBillsAutoCompleteAdapter(
            context,
            R.layout.item_recharge_client_number_auto_complete,
            mutableListOf(),
            context.getString(emptyStateUnitRes),
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

    private fun setSortFilterChip(favnum: List<RechargeClientNumberChipModel>) {
        val sortFilter = arrayListOf<SortFilterItem>()

        // create extra chip for navigation
        val sortFilterItem = SortFilterItem(
            "",
            type = ChipsUnify.TYPE_ALTERNATE
        )
        sortFilterItem.listener = {
            mFilterChipListener?.onClickIcon(true)
        }
        sortFilter.add(sortFilterItem)

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

        binding.clientNumberWidgetBase.clientNumberWidgetSortFilter.addItem(sortFilter)

        // init navigation chip's icon & color
        val chevronRight = IconUnify(
            context,
            IconUnify.VIEW_LIST,
            ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_GN500)
        )
        chevronRight.layoutParams = ViewGroup.LayoutParams(
            getDimens(unifyDimens.layout_lvl3),
            getDimens(unifyDimens.layout_lvl3)
        )
        binding.clientNumberWidgetBase.clientNumberWidgetSortFilter.chipItems
            ?.first()?.refChipUnify?.addCustomView(chevronRight)
    }

    fun setFavoriteNumber(favNumberItems: List<RechargeClientNumberChipModel>) {
        setSortFilterChip(favNumberItems)
    }

    fun setAutoCompleteList(suggestions: List<RechargeClientNumberAutoCompleteModel>) {
        autoCompleteAdapter?.updateItems(
            suggestions.map {
                TopupBillsAutoCompleteContactModel(it.clientName, it.clientNumber)
            }.toMutableList()
        )
    }

    fun setInputFieldType(type: InputFieldType) {
        with(binding) {
            inputFieldType = type
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
        initAutoComplete()
    }

    fun setInputFieldStaticLabel(label: String) {
        textFieldStaticLabel = label
        binding.clientNumberWidgetBase.clientNumberWidgetInputField.textInputLayout.hint = textFieldStaticLabel
    }

    fun setInputNumber(inputNumber: String) {
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

    fun resetContactName() {
        binding.clientNumberWidgetBase.clientNumberWidgetInputField.textInputLayout.hint =
            textFieldStaticLabel
    }

    fun setLoading(isLoading: Boolean) {
        binding.clientNumberWidgetBase.clientNumberWidgetInputField.isLoading = isLoading
    }

    fun isInputFieldEmpty(): Boolean = binding.clientNumberWidgetBase
        .clientNumberWidgetInputField.editText.text.isEmpty()

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

    private fun onClickClearIcon() {
        clearErrorState()
        mInputFieldListener?.onClearInput()
    }

    fun clearErrorState() {
        binding.clientNumberWidgetBase.clientNumberWidgetInputField.clearErrorField()
    }

    fun setCustomInputNumberFormatter(func: (String) -> String) {
        this.customInputNumberFormatter = func
    }

    fun showClearIcon() {
        binding.clientNumberWidgetBase.clientNumberWidgetInputField.clearIconView.showClearIconUnify()
    }

    fun hideClearIcon() {
        binding.clientNumberWidgetBase.clientNumberWidgetInputField.clearIconView.hideClearIconUnify()
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
        filterChipListener: ClientNumberFilterChipListener?,
        sortFilterListener: ClientNumberSortFilterListener?
    ) {
        mInputFieldListener = inputFieldListener
        mAutoCompleteListener = autoCompleteListener
        mFilterChipListener = filterChipListener
        mSortFilterListener = sortFilterListener
    }

    fun setTitleGeneral(title: String) {
        if (!title.isNullOrEmpty()) {
            binding.clientNumberWidgetGeneralTgTitle.run {
                show()
                text = title
            }
        }
    }

    fun setChipOperators(operators: List<CatalogOperator>, selectedOperator: CatalogOperator) {
        binding.clientNumberWidgetGeneralChipOperator.run {
            show()
            val filterItems = arrayListOf<SortFilterItem>()
            operators.forEach {
                val item = SortFilterItem(it.attributes.name)
                filterItems.add(item)
            }

            filterItems.forEachIndexed { index, sortFilterItem ->
                if (selectedOperator.id == operators.get(index).id) {
                    sortFilterItem.type = ChipsUnify.TYPE_SELECTED
                }
                sortFilterItem.listener = {
                    sortFilterItem.toggle()
                    if (sortFilterItem.type == ChipsUnify.TYPE_SELECTED) {
                        mSortFilterListener?.getSelectedChipOperator(operators.get(index))
                    }
                }
            }
            addItem(filterItems)
        }
    }

    fun selectChipOperatorByPosition(position: Int) {
        binding.clientNumberWidgetGeneralChipOperator.run {
            chipItems?.forEachIndexed { index, sortFilterItem ->
                if (index == position) {
                    sortFilterItem.type = ChipsUnify.TYPE_SELECTED
                } else {
                    sortFilterItem.type = ChipsUnify.TYPE_NORMAL
                }
            }
        }
    }

    fun setInquiryList(attribute: TopupBillsEnquiryAttribute) {
        val listInquiry = attribute.mainInfoList.toMutableList()
        if (!attribute.additionalMainInfo.isNullOrEmpty()) {
            attribute.additionalMainInfo.forEach {
                listInquiry.addAll(
                    it.detail.map {
                        TopupBillsEnquiryMainInfo(it.label, it.value)
                    }
                )
            }
        }
        val adapterInquiry = InquiryRechargeClientNumberAdapter()
        adapterInquiry.setListMainInfo(listInquiry)
        binding?.clientNumberWidgetGeneralRvInquiryList.run {
            adapter = adapterInquiry
            layoutManager = LinearLayoutManager(context)
        }
    }

    fun startShakeAnimation() {
        binding.clientNumberWidgetBase.clientNumberWidgetInputField.startAnimation(
            AnimationUtils.loadAnimation(context, R.anim.client_number_widget_shake_anim)
        )
    }
}
