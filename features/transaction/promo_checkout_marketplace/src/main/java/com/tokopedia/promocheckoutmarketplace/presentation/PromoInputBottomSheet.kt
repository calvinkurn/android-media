package com.tokopedia.promocheckoutmarketplace.presentation

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.FragmentManager
import com.tokopedia.promocheckoutmarketplace.R
import com.tokopedia.promocheckoutmarketplace.databinding.PromoCheckoutMarketplaceModuleBottomSheetInputPromoCodeBinding
import com.tokopedia.promocheckoutmarketplace.presentation.listener.PromoCheckoutActionListener
import com.tokopedia.promocheckoutmarketplace.presentation.uimodel.PromoInputUiModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class PromoInputBottomSheet() : BottomSheetUnify() {

    private var viewBinding by autoClearedNullable<PromoCheckoutMarketplaceModuleBottomSheetInputPromoCodeBinding>()
    private var promoInputUiModel: PromoInputUiModel? = null
    private var listener: PromoCheckoutActionListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = PromoCheckoutMarketplaceModuleBottomSheetInputPromoCodeBinding.inflate(inflater)
        setTitle(getString(R.string.promo_checkout_title_bottom_sheet_input_promo_code))
        setChild(viewBinding?.root)
        promoInputUiModel?.let { renderView(it) }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun renderView(inputUiModel: PromoInputUiModel) {
        renderPromoInputErrorState(inputUiModel)
        renderLoadingState(inputUiModel)
        renderCloseIcon(inputUiModel)
        renderTextField(inputUiModel)
        renderApplyPromoButton(inputUiModel)
    }

    private fun renderApplyPromoButton(element: PromoInputUiModel) {
        viewBinding?.run {
            btnApplyPromo.isEnabled = element.uiData.promoCode.isNotBlank()

            btnApplyPromo.setOnClickListener {
                val promoCode = etInputPromo.editText.text.toString()
                if (promoCode.isNotEmpty()) {
                    listener?.onClickApplyManualInputPromo(
                        promoCode,
                        element.uiState.isValidSuggestionPromo
                    )
                }
            }
        }
    }

    private fun renderTextField(element: PromoInputUiModel) {
        viewBinding?.run {
            etInputPromo.editText.imeOptions = EditorInfo.IME_ACTION_DONE
            etInputPromo.editText.setText(element.uiData.promoCode)
            etInputPromo.editText.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (element.uiState.isValidSuggestionPromo) {
                        element.uiState.isValidSuggestionPromo =
                            text.toString() == element.uiData.validSuggestionPromoCode
                    }
                    element.uiData.promoCode = text?.toString() ?: ""
                    if (text?.isNotEmpty() == true) {
                        element.uiState.isButtonSelectEnabled = true
                        btnApplyPromo.isEnabled = true
                        etInputPromo.setFirstIcon(com.tokopedia.unifycomponents.R.drawable.unify_chips_ic_close)
                    } else {
                        element.uiState.isButtonSelectEnabled = false
                        btnApplyPromo.isEnabled = false
                        etInputPromo.setFirstIcon(com.tokopedia.unifyprinciples.R.color.Unify_NN0)
                    }
                }
            })
            setTextFieldInputListener(element)
        }
    }

    private fun setTextFieldInputListener(element: PromoInputUiModel) {
        viewBinding?.run {
            etInputPromo.icon1.setOnClickListener {
                etInputPromo.editText.text.clear()
                listener?.onCLickClearManualInputPromo()
            }
        }
    }

    private fun renderCloseIcon(element: PromoInputUiModel) {
        viewBinding?.run {
            if (element.uiData.promoCode.isNotBlank()) {
                etInputPromo.setFirstIcon(com.tokopedia.unifycomponents.R.drawable.unify_chips_ic_close)
            } else {
                etInputPromo.setFirstIcon(com.tokopedia.unifyprinciples.R.color.Unify_NN0)
            }
        }
    }

    private fun renderLoadingState(element: PromoInputUiModel) {
        viewBinding?.run {
            if (element.uiState.isLoading) {
                btnApplyPromo.isLoading = true
                btnApplyPromo.isClickable = false
            } else {
                btnApplyPromo.isLoading = false
                btnApplyPromo.isClickable = true
            }
        }
    }

    private fun renderPromoInputErrorState(element: PromoInputUiModel) {
        if (element.uiState.isError) {
            renderPromoInputError(element)
        } else {
            renderPromoInputNotError(element)
        }
    }

    private fun dismissAfterSuccess(element: PromoInputUiModel) {
        if (element.uiState.needToDismissBottomsheet) {
            val input = viewBinding?.root?.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            input?.hideSoftInputFromWindow(viewBinding?.etInputPromo?.windowToken, 0)
            this.dismiss()
        }
    }

    private fun renderPromoInputError(element: PromoInputUiModel) {
        viewBinding?.run {
            etInputPromo.isInputError = true
            if (element.uiData.exception != null) {
                etInputPromo.setMessage(
                    element.uiData.exception?.message
                        ?: root.context.getString(com.tokopedia.network.R.string.default_request_error_unknown)
                )
            }
        }
    }

    private fun renderPromoInputNotError(element: PromoInputUiModel) {
        viewBinding?.run {
            etInputPromo.isInputError = false
            etInputPromo.setMessage("")
        }
    }

    fun show(
        fragmentManager: FragmentManager,
        tag: String,
        promoInputUiModel: PromoInputUiModel,
        listener: PromoCheckoutActionListener
    ) {
        this.listener = listener
        this.promoInputUiModel = promoInputUiModel
        this.show(fragmentManager, tag)
    }

    fun setInputUiModel(inputUiModel: PromoInputUiModel) {
        this.promoInputUiModel = inputUiModel
        renderView(inputUiModel)
        dismissAfterSuccess(inputUiModel)
    }
}
