package com.tokopedia.common.topupbills.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import com.tokopedia.common.topupbills.R
import com.tokopedia.common.topupbills.databinding.BottomSheetPersoFavoriteNumberModifyBinding
import com.tokopedia.common.topupbills.view.model.favoriteperso.TopupBillsPersoFavNumberDataView
import com.tokopedia.unifycomponents.BottomSheetUnify

class PersoFavoriteNumberModifyBottomSheet(
    private val favNumberItem: TopupBillsPersoFavNumberDataView,
    private val listener: PersoFavoriteNumberModifyListener
): BottomSheetUnify() {

    private lateinit var binding: BottomSheetPersoFavoriteNumberModifyBinding

    init {
        showCloseIcon = true
        isKeyboardOverlap = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBottomSheet()
        initListener()
    }

    private fun initBottomSheet() {
        setTitle(getString(R.string.common_topup_fav_number_modify_bottom_sheet_title))
        setCloseClickListener { dismiss() }

        binding = BottomSheetPersoFavoriteNumberModifyBinding.inflate(LayoutInflater.from(context))
        setChild(binding.root)

        if (favNumberItem.subtitle.isNotEmpty()) {
            binding.commonTopupbillsPersoFavoriteNumberNameField.run {
                setLabelStatic(true)
                textFieldInput.setText(favNumberItem.title)
                textFieldInput.setOnFocusChangeListener { _, hasFocus ->
                    if (hasFocus) {
                        textFieldWrapper.hint = ""
                    } else {
                        textFieldWrapper.hint = favNumberItem.title
                    }
                }
            }
            binding.commonTopupbillsPersoFavoriteNumberPhoneField.textFieldWrapper.hint = favNumberItem.subtitle
        } else {
            binding.commonTopupbillsPersoFavoriteNumberPhoneField.textFieldWrapper.hint = favNumberItem.title
        }

        binding.commonTopupbillsPersoFavoriteNumberPhoneField.textFiedlLabelText.setTextColor(
            resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_N75)
        )
    }

    private fun initListener() {
        with(binding) {
            commonTopupbillsPersoFavoriteNumberModifyButton.setOnClickListener {
                commonTopupbillsPersoFavoriteNumberNameField.run {
                    val newClientName = textFieldInput.text.toString()
                    val errMsg = validateClientName(newClientName)
                    if (errMsg.isNullOrEmpty()) {
                        setMessage("")
                        setError(false)
                    } else {
                        setMessage(errMsg)
                        setError(true)
                    }
                }
                if (!commonTopupbillsPersoFavoriteNumberNameField.isTextFieldError) {
                    listener.onChangeName(
                        commonTopupbillsPersoFavoriteNumberNameField.textFieldInput.text.toString(),
                        favNumberItem
                    )
                    dismiss()
                }
            }

            commonTopupbillsPersoFavoriteNumberPhoneField.textFieldInput.run {
                isClickable = false
                isFocusable = false
            }
        }
    }

    private fun validateClientName(clientName: String): String? {
        val mClientName = clientName
        if (mClientName.length > MAX_CHAR) {
            return getString(R.string.common_topup_fav_number_validator_more_than_18_char)
        }
        if (mClientName.length < MIN_CHAR) {
            return getString(R.string.common_topup_fav_number_validator_less_than_3_char)
        }
        if (!mClientName.matches(REGEX_IS_ALPHABET_AND_SPACE_ONLY.toRegex())) {
            return getString(R.string.common_topup_fav_number_validator_alphanumeric)
        }
        return null
    }

    interface PersoFavoriteNumberModifyListener {
        fun onChangeName(newName: String, favNumberItem: TopupBillsPersoFavNumberDataView)
    }

    companion object {
        private const val REGEX_IS_ALPHABET_AND_SPACE_ONLY = "^[a-zA-Z0-9\\s]*$"
        private const val MAX_CHAR = 18
        private const val MIN_CHAR = 3

        fun newInstance(favNumberItem: TopupBillsPersoFavNumberDataView,
                        listener: PersoFavoriteNumberModifyListener
        ): PersoFavoriteNumberModifyBottomSheet {
            return PersoFavoriteNumberModifyBottomSheet(favNumberItem, listener)
        }
    }
}