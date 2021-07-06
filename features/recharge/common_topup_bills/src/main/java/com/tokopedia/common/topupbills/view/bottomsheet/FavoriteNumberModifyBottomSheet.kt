package com.tokopedia.common.topupbills.view.bottomsheet

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.tokopedia.common.topupbills.R
import com.tokopedia.common.topupbills.data.TopupBillsSeamlessFavNumberItem
import com.tokopedia.common.topupbills.view.listener.FavoriteNumberModifyListener
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.bottom_sheet_seamless_favorite_number_modify.view.*

class FavoriteNumberModifyBottomSheet(
    private val favNumberItem: TopupBillsSeamlessFavNumberItem,
    private val listener: FavoriteNumberModifyListener
): BottomSheetUnify() {

    private lateinit var childView: View

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

        childView = View.inflate(context, R.layout.bottom_sheet_seamless_favorite_number_modify, null)
        setChild(childView)

        childView.common_topupbills_favorite_number_phone_field.textFieldWrapper.hint = favNumberItem.clientNumber
        childView.common_topupbills_favorite_number_phone_field.textFiedlLabelText.setTextColor(
                resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_N75)
        )
    }

    private fun initListener() {
        with(childView) {
            common_topupbills_favorite_number_modify_button.setOnClickListener {
                common_topupbills_favorite_number_name_field.run {
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
                if (!common_topupbills_favorite_number_name_field.isTextFieldError) {
                    listener.onChangeName(
                            common_topupbills_favorite_number_name_field.textFieldInput.text.toString(),
                            favNumberItem
                    )
                    dismiss()
                }
            }

            common_topupbills_favorite_number_phone_field.textFieldInput.run {
                isClickable = false
                isFocusable = false
            }
        }
    }

    private fun validateClientName(clientName: String): String? {
        val mClientName = clientName
        if (mClientName.length > 18) {
            return getString(R.string.common_topup_fav_number_validator_more_than_18_char)
        }
        if (mClientName.length < 3) {
            return getString(R.string.common_topup_fav_number_validator_less_than_3_char)
        }
        if (!mClientName.matches(REGEX_IS_ALPHABET_AND_SPACE_ONLY.toRegex())) {
            return getString(R.string.common_topup_fav_number_validator_alphanumeric)
        }
        return null
    }

    companion object {
        private const val REGEX_IS_ALPHABET_AND_SPACE_ONLY = "^[a-zA-Z0-9\\s]*$"

        fun newInstance(favNumberItem: TopupBillsSeamlessFavNumberItem,
                        listener: FavoriteNumberModifyListener): FavoriteNumberModifyBottomSheet {
            return FavoriteNumberModifyBottomSheet(favNumberItem, listener)
        }
    }
}