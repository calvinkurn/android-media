package com.tokopedia.common.topupbills.favoritepage.view.bottomsheet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.common.topupbills.R
import com.tokopedia.common.topupbills.databinding.BottomSheetSeamlessFavoriteNumberModifyBinding
import com.tokopedia.common.topupbills.favoritepage.view.model.TopupBillsPersoFavNumberDataView
import com.tokopedia.unifycomponents.BottomSheetUnify

class PersoFavoriteNumberModifyBottomSheet : BottomSheetUnify() {

    // reuse seamless layout
    private lateinit var binding: BottomSheetSeamlessFavoriteNumberModifyBinding

    private var listener: PersoFavoriteNumberModifyListener? = null
    private var favNumberItem: TopupBillsPersoFavNumberDataView? = null

    init {
        showCloseIcon = true
        isKeyboardOverlap = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { arguments ->
            favNumberItem = arguments.getParcelable(FAV_NUMBER_ITEM_EXTRA)
        }
        initBottomSheet()
        initListener()
    }

    private fun initBottomSheet() {
        favNumberItem?.let { favNumberItem ->
            setTitle(getString(R.string.common_topup_fav_number_modify_bottom_sheet_title))
            setCloseClickListener {
                dismiss()
                listener?.onCloseClick(favNumberItem)
            }

            binding =
                BottomSheetSeamlessFavoriteNumberModifyBinding.inflate(LayoutInflater.from(context))
            setChild(binding.root)

            if (favNumberItem.subtitle.isNotEmpty()) {
                binding.commonTopupbillsFavoriteNumberNameField.run {
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
                binding.commonTopupbillsFavoriteNumberPhoneField.textFieldWrapper.hint =
                    favNumberItem.subtitle
            } else {
                binding.commonTopupbillsFavoriteNumberPhoneField.textFieldWrapper.hint =
                    favNumberItem.title
            }

            context?.let { context ->
                binding.commonTopupbillsFavoriteNumberPhoneField.textFiedlLabelText.setTextColor(
                    context.resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_NN50)
                )
            }
        }

    }

    private fun initListener() {
        favNumberItem?.let { favNumberItem ->
            with(binding) {
                commonTopupbillsFavoriteNumberModifyButton.setOnClickListener {
                    commonTopupbillsFavoriteNumberNameField.run {
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
                    if (!commonTopupbillsFavoriteNumberNameField.isTextFieldError) {
                        listener?.onChangeName(
                            commonTopupbillsFavoriteNumberNameField.textFieldInput.text.toString(),
                            favNumberItem
                        )
                        dismiss()
                    }
                }

                commonTopupbillsFavoriteNumberPhoneField.textFieldInput.run {
                    isClickable = false
                    isFocusable = false
                }
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

    fun setListener(listener: PersoFavoriteNumberModifyListener) {
        this.listener = listener
    }

    interface PersoFavoriteNumberModifyListener {
        fun onChangeName(newName: String, favNumberItem: TopupBillsPersoFavNumberDataView)
        fun onCloseClick(favNumberItem: TopupBillsPersoFavNumberDataView)
    }

    companion object {
        private const val REGEX_IS_ALPHABET_AND_SPACE_ONLY = "^[a-zA-Z0-9\\s]*$"
        private const val MAX_CHAR = 18
        private const val MIN_CHAR = 3

        private const val FAV_NUMBER_ITEM_EXTRA = "FAV_NUMBER_ITEM_EXTRA"

        fun newInstance(favNumberItem: TopupBillsPersoFavNumberDataView): PersoFavoriteNumberModifyBottomSheet {
            val bottomSheet = PersoFavoriteNumberModifyBottomSheet()
            val bundle = Bundle()
            bundle.putParcelable(FAV_NUMBER_ITEM_EXTRA, favNumberItem)
            bottomSheet.arguments = bundle
            return bottomSheet
        }
    }
}
