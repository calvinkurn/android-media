package com.tokopedia.recharge_component.widget

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.AnimationSet
import android.view.animation.AnimationUtils
import android.view.animation.DecelerateInterpolator
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import com.tokopedia.common.topupbills.favorite.data.TopupBillsPersoFavNumberItem
import com.tokopedia.common.topupbills.favorite.util.FavoriteNumberDataMapper
import com.tokopedia.common.topupbills.view.adapter.TopupBillsAutoCompleteAdapter
import com.tokopedia.common.topupbills.view.model.TopupBillsAutoCompleteContactDataView
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.getDimens
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.invisible
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
import org.jetbrains.annotations.NotNull
import kotlin.math.abs
import com.tokopedia.unifyprinciples.R.dimen as unifyDimens

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
    private var inputFieldType: InputFieldType? = null

    // this custom formatter will be used when user call getInputNumber & setInputNumber
    private var customInputNumberFormatter: ((String) -> String)? = null

    init {
        initInputField()
        initSortFilterChip()
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
                        var isManualInput = false
                        hideClearIcon()
                        clearErrorState()
                        // if manual input
                        if (abs(before - count) == 1 && mInputFieldListener?.isKeyboardShown() == true) {
                            isClearableState = false
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
                    if (item is TopupBillsAutoCompleteContactDataView) {
                        setContactName(item.name)
                        mAutoCompleteListener?.onClickAutoComplete(item.name.isNotEmpty())
                    }
                }
            }
        }

        val emptyStateUnitRes = when (inputFieldType) {
            InputFieldType.Listrik -> com.tokopedia.common.topupbills.R.string.common_topup_autocomplete_unit_nomor_meter
            InputFieldType.Telco -> com.tokopedia.common.topupbills.R.string.common_topup_autocomplete_unit_nomor_hp
            else -> com.tokopedia.common.topupbills.R.string.common_topup_autocomplete_unit_nomor_hp
        }

        autoCompleteAdapter = TopupBillsAutoCompleteAdapter(
            context,
            R.layout.item_recharge_client_number_auto_complete,
            mutableListOf(),
            context.getString(emptyStateUnitRes),
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
        for (number in favnum) {
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
        val sortFilterItem = SortFilterItem(
            "",
            type = ChipsUnify.TYPE_ALTERNATE
        )
        sortFilterItem.listener = {
            mFilterChipListener?.onClickIcon(true)
        }
        sortFilter.add(sortFilterItem)

        binding.clientNumberWidgetMainLayout.clientNumberWidgetBase.clientNumberWidgetSortFilter.addItem(sortFilter)

        // init navigation chip's icon & color
        val chevronRight = IconUnify(
            context, IconUnify.VIEW_LIST,
            ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_GN500))
        chevronRight.layoutParams = ViewGroup.LayoutParams(
            getDimens(unifyDimens.layout_lvl3),
            getDimens(unifyDimens.layout_lvl3)
        )
        binding.clientNumberWidgetMainLayout.clientNumberWidgetBase.clientNumberWidgetSortFilter.chipItems?.
            last()?.refChipUnify?.addCustomView(chevronRight)
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
            inputFieldType = type
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
        initAutoComplete()
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

    fun resetContactName() {
        binding.clientNumberWidgetMainLayout.clientNumberWidgetBase.clientNumberWidgetInputField.textInputLayout.hint =
            textFieldStaticLabel
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

    fun isInputFieldEmpty(): Boolean = binding.clientNumberWidgetMainLayout
        .clientNumberWidgetBase.clientNumberWidgetInputField.editText.text.isEmpty()

    fun setErrorInputField(errorMessage: String, resetProvider: Boolean = false) {
        binding.clientNumberWidgetMainLayout.clientNumberWidgetBase.clientNumberWidgetInputField.run {
            val temp = textInputLayout.helperText.toString()
            if (temp != errorMessage && getInputNumber().isNotEmpty()) {
                setMessage(errorMessage)
                isInputError = true

                if (resetProvider) {
                    binding.clientNumberWidgetOperatorGroup.invisible()
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
                includeLayout.clientNumberWidgetSimplifiedLayout.animateFadeInThenShow()
                clientNumberWidgetMainLayout.clientNumberWidgetBase.root.animateFadeOutThenGone()
                clientNumberWidgetOperatorGroup.animateFadeOutThenInvisible()
            } else {
                includeLayout.clientNumberWidgetSimplifiedLayout.animateFadeOutThenGone()
                clientNumberWidgetMainLayout.clientNumberWidgetBase.root.animateFadeInThenShow()
                clientNumberWidgetOperatorGroup.animateFadeInThenShow()
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
            clientNumberWidgetOperatorGroup.invisible()
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

    fun startShakeAnimation() {
        binding.clientNumberWidgetMainLayout.clientNumberWidgetBase.clientNumberWidgetInputField.startAnimation(
            AnimationUtils.loadAnimation(context, R.anim.client_number_widget_shake_anim))
    }

    private fun View.animateFadeInThenShow() {
        val fadeIn = AlphaAnimation(ALPHA_0_5, ALPHA_1_0)
        fadeIn.interpolator = DecelerateInterpolator()
        fadeIn.duration = FADE_DURATION

        val animation = AnimationSet(false)
        animation.addAnimation(fadeIn)
        this.animation = animation

        show()
    }

    private fun View.animateFadeOutThenGone() {

        val fadeOut = AlphaAnimation(ALPHA_1_0, ALPHA_0_5)
        fadeOut.interpolator = AccelerateInterpolator()
        fadeOut.duration = FADE_DURATION

        val animation = AnimationSet(false)
        animation.addAnimation(fadeOut)
        this.animation = animation

        gone()
    }

    private fun View.animateFadeOutThenInvisible() {

        val fadeOut = AlphaAnimation(ALPHA_1_0, ALPHA_0_5)
        fadeOut.interpolator = AccelerateInterpolator()
        fadeOut.duration = FADE_DURATION

        val animation = AnimationSet(false)
        animation.addAnimation(fadeOut)
        this.animation = animation

        invisible()
    }

    companion object {
        private const val FADE_DURATION = 200L
        private const val ALPHA_0_5 = 0.5f
        private const val ALPHA_1_0 = 1.0f
    }
}