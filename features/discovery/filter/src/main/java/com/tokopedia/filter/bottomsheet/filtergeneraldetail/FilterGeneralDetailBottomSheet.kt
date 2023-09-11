package com.tokopedia.filter.bottomsheet.filtergeneraldetail

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.filter.R
import com.tokopedia.filter.common.data.IOption
import com.tokopedia.filter.common.data.Option
import com.tokopedia.filter.common.data.OptionHolder
import com.tokopedia.filter.common.helper.addItemDecorationIfNotExists
import com.tokopedia.filter.common.helper.createFilterDividerItemDecoration
import com.tokopedia.filter.common.helper.isTypeRadio
import com.tokopedia.filter.common.helper.setBottomSheetActionBold
import com.tokopedia.filter.common.helper.setMargin
import com.tokopedia.filter.common.helper.uniqueId
import com.tokopedia.filter.databinding.FilterGeneralDetailBottomSheetBinding
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.SearchBarUnify
import com.tokopedia.unifycomponents.toDp
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class FilterGeneralDetailBottomSheet: BottomSheetUnify(), FilterGeneralDetailAdapter.Callback {

    companion object {
        private const val FILTER_GENERAL_DETAIL_BOTTOM_SHEET_TAG = "FILTER_GENERAL_DETAIL_BOTTOM_SHEET_TAG"
    }

    private var optionHolder: OptionHolder? = null
    private var originalSelectedOptionList = mutableListOf<IOption>()
    private var callback: Callback? = null
    private var optionCallback: OptionCallback? = null
    private var buttonApplyFilterDetailText: String? = null

    private var filterGeneralDetailBottomSheetView: View? = null
    private val filterGeneralDetailAdapter = FilterGeneralDetailAdapter(this)

    private val optionSearchFilter = OptionSearchFilter {
        filterGeneralDetailAdapter.setOptionList(it)
    }

    private var enableResetButton = true
    private var isLoading = false

    private var binding: FilterGeneralDetailBottomSheetBinding? = null

    fun show(
        fragmentManager: FragmentManager,
        filter: OptionHolder?,
        callback: Callback? = null,
        optionCallback: OptionCallback? = null,
        buttonApplyFilterDetailText: String? = null,
        enableResetButton: Boolean = true,
    ) {
        this.optionHolder = filter?.copy()
        this.isLoading = filter != null
        this.originalSelectedOptionList.addAll(filter.getSelectedOptions())
        this.callback = callback
        this.optionCallback = optionCallback
        this.buttonApplyFilterDetailText = buttonApplyFilterDetailText
        this.enableResetButton = enableResetButton
        show(fragmentManager, FILTER_GENERAL_DETAIL_BOTTOM_SHEET_TAG)
    }

    fun setOptionHolder(optionHolder: OptionHolder?) {
        this.optionHolder = optionHolder
        this.isLoading = optionHolder != null
        this.originalSelectedOptionList.addAll(optionHolder.getSelectedOptions())

        filterGeneralDetailAdapter.setOptionList(this.optionHolder?.options ?: listOf())

        setTitle(optionHolder?.title ?: "")

        renderLoading(isLoading)
    }

    private fun OptionHolder?.getSelectedOptions(): List<IOption> {
        return this?.options?.filter { it.inputState.toBoolean() } ?: listOf()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
    }

    private fun initView() {
        initViewSettings()

        setTitle(optionHolder?.title ?: "")

        initButtonReset()

        val view = View.inflate(requireContext(), R.layout.filter_general_detail_bottom_sheet, null)
        binding = FilterGeneralDetailBottomSheetBinding.bind(view)
        filterGeneralDetailBottomSheetView = binding?.root
        setChild(filterGeneralDetailBottomSheetView)

        renderLoading(isLoading)

        initSearchBar()

        initRecyclerView()

        initButtonApplyDetailFilter()
    }

    private fun renderLoading(isLoading : Boolean) {
        binding?.progressBarSortBottomSheet?.showWithCondition(!isLoading)
        binding?.buttonApplyFilterDetailContainer?.showWithCondition(isLoading)
    }

    private fun initButtonReset() {
        if (optionHolder.hasActiveOptions() && enableResetButton) {
            setAction(getString(R.string.filter_button_reset_text), this::onResetFilter)
        }
        else {
            clearAction()
        }
    }

    private fun OptionHolder?.hasActiveOptions(): Boolean {
        return this.getSelectedOptions().isNotEmpty()
    }

    private fun initViewSettings() {
        if (optionHolder?.isLocationFilter == true)
            initViewSettingsLocationFilter()
        else
            initViewSettingsNonLocationFilter()
    }

    private fun initViewSettingsLocationFilter() {
        isDragable = true
        isHideable = true
        showKnob = true
        showCloseIcon = false
        customPeekHeight = (getScreenHeight() / 2).toDp()
    }

    private fun initViewSettingsNonLocationFilter() {
        isDragable = false
        isHideable = false
        showKnob = false
        showCloseIcon = true
    }

    private fun onResetFilter(view: View) {
        optionHolder?.options?.forEach { it.inputState = "" }

        filterGeneralDetailAdapter.notifyDataSetChanged()

        applyFilterViewInteractions(false)
    }

    private fun applyFilterViewInteractions(isResetVisible: Boolean) {
        setButtonApplyFilterVisibility()
        setActionResetVisibility(isResetVisible)
        expandBottomSheet()
        hideKeyboard()
    }

    private fun setButtonApplyFilterVisibility() {
        val isVisible = optionHolder.getSelectedOptions() != originalSelectedOptionList

        binding?.buttonApplyFilterDetailContainer?.showWithCondition(isVisible)
    }

    private fun setActionResetVisibility(isVisible: Boolean) {
        bottomSheetAction.shouldShowWithAction(isVisible && enableResetButton) {
            bottomSheetAction.text = getString(R.string.filter_button_reset_text)
            bottomSheetAction.setOnClickListener(this::onResetFilter)
        }
    }

    private fun expandBottomSheet() {
        bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun hideKeyboard() {
        activity?.let { activity ->
            KeyboardHandler.DropKeyboard(activity, filterGeneralDetailBottomSheetView)
        }
    }

    private fun initSearchBar() {
        binding?.searchBarFilterDetail?.let {
            it.shouldShowWithAction(optionHolder?.search?.searchable == 1) {
                initVisibleSearchBar(it)
            }
        }
    }

    private fun initVisibleSearchBar(searchBarFilterDetail: SearchBarUnify?) {
        searchBarFilterDetail?.searchBarPlaceholder = optionHolder?.search?.placeholder ?: ""
        searchBarFilterDetail?.showIcon = false
        context?.let{
            searchBarFilterDetail?.searchBarTextField?.setTextColor(
                ContextCompat.getColor(
                    it,
                    unifyprinciplesR.color.Unify_NN950_96
                )
            )
        }
        searchBarFilterDetail?.searchBarTextField?.setOnFocusChangeListener { _, _ ->
            bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED
        }
        searchBarFilterDetail?.searchBarTextField?.addTextChangedListener(createSearchBarTextWatcher())
        searchBarFilterDetail?.searchBarTextField?.setOnEditorActionListener { _, _, _ ->
            hideKeyboard()
            true
        }
    }

    private fun createSearchBarTextWatcher() = object: TextWatcher {
        override fun afterTextChanged(s: Editable?) {}

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            optionSearchFilter.setOptionList(optionHolder?.options ?: listOf())
            optionSearchFilter.filter(s)
        }
    }

    private fun initRecyclerView() {
        binding?.recyclerViewFilterDetailBottomSheet?.let {
            filterGeneralDetailAdapter.setOptionList(optionHolder?.options ?: listOf())

            val layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            val itemDecorationLeftMargin = getFilterDividerItemDecorationLeftMargin(optionHolder?.isColorFilter ?: false)
            val itemDecoration = createFilterDividerItemDecoration(it.context, layoutManager.orientation, itemDecorationLeftMargin)

            it.adapter = filterGeneralDetailAdapter
            it.layoutManager = layoutManager
            it.addItemDecorationIfNotExists(itemDecoration)
            it.addOnScrollListener(createRecyclerViewOnScrollListener())
        }
    }

    private fun getFilterDividerItemDecorationLeftMargin(isColorFilter: Boolean): Int {
        return if (!isColorFilter) 16.toPx()
        else 12.toPx() + 32.toPx() + 12.toPx() // Color sample margin left + width + margin right
    }

    private fun createRecyclerViewOnScrollListener() = object: RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            if (newState == RecyclerView.SCROLL_STATE_DRAGGING) hideKeyboard()
        }
    }

    private fun initButtonApplyDetailFilter() {
        binding?.buttonApplyFilterDetail?.setOnClickListener {
            apply()
            dismiss()
        }

        buttonApplyFilterDetailText?.let {
            binding?.buttonApplyFilterDetail?.text = it
        }
    }

    private fun apply() {
        callback?.onApplyButtonClicked(optionHolder?.options as? List<Option>)
        optionCallback?.onApplyButtonClicked(optionHolder?.options)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        bottomSheetWrapper.setPadding(0, bottomSheetWrapper.paddingTop, 0, bottomSheetWrapper.paddingBottom)

        bottomSheetAction.setMargin(marginRight = 16.toPx())

        if (showCloseIcon) bottomSheetClose.setMargin(marginLeft = 16.toPx(), marginTop = 4.toPx(), marginRight = 12.toPx())
        else bottomSheetTitle.setMargin(marginLeft = 16.toPx())

        setBottomSheetActionBold()
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    override fun onOptionClick(option: IOption, isChecked: Boolean, position: Int) {
        processOptionClick(option, isChecked)

        notifyAdapterChanges(option, position)

        applyFilterViewInteractions(getButtonResetVisibility(isChecked))

        clickOption(option, isChecked, position)
    }

    private fun clickOption(
        option: IOption,
        isChecked: Boolean,
        position: Int
    ) {
        if (option is Option)
            callback?.onOptionClick(option, isChecked, position)

        optionCallback?.onOptionClick(option, isChecked, position)
    }

    private fun processOptionClick(option: IOption, isChecked: Boolean) {
        option.inputState = isChecked.toString()

        if (option.isTypeRadio)
            optionHolder?.options?.processRadioTypeOption(option)
    }

    private fun notifyAdapterChanges(option: IOption, position: Int) {
        if (option.isTypeRadio)
            filterGeneralDetailAdapter.notifyItemRangeChanged(0, filterGeneralDetailAdapter.itemCount, FilterGeneralDetailAdapter.Payload.BIND_INPUT_STATE_ONLY)
        else
            filterGeneralDetailAdapter.notifyItemChanged(position, FilterGeneralDetailAdapter.Payload.BIND_INPUT_STATE_ONLY)
    }

    private fun getButtonResetVisibility(isChecked: Boolean) = isChecked || optionHolder.hasActiveOptions()

    private fun List<IOption>.processRadioTypeOption(selectedOption: IOption) {
        this.filter { it.isDifferentOptionAndSameInputState(selectedOption) }.map { it.inputState = "" }
    }

    private fun IOption.isDifferentOptionAndSameInputState(option: IOption): Boolean {
        return uniqueId != option.uniqueId && inputState.toBoolean() == option.inputState.toBoolean()
    }

    @Deprecated("Use IOptionCallback")
    interface Callback {
        fun onApplyButtonClicked(optionList: List<Option>?)
        fun onOptionClick(option: Option, isChecked: Boolean, position: Int) { }
    }

    interface OptionCallback {
        fun onApplyButtonClicked(optionList: List<IOption>?)

        fun onOptionClick(option: IOption, isChecked: Boolean, position: Int) { }
    }
}
