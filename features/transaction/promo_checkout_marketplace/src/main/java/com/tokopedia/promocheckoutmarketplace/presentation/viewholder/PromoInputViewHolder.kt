package com.tokopedia.promocheckoutmarketplace.presentation.viewholder

import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.promocheckoutmarketplace.R
import com.tokopedia.promocheckoutmarketplace.databinding.PromoCheckoutMarketplaceModuleItemPromoInputBinding
import com.tokopedia.promocheckoutmarketplace.presentation.listener.PromoCheckoutActionListener
import com.tokopedia.promocheckoutmarketplace.presentation.uimodel.PromoInputUiModel

class PromoInputViewHolder(private val viewBinding: PromoCheckoutMarketplaceModuleItemPromoInputBinding,
                           private val listener: PromoCheckoutActionListener
) : AbstractViewHolder<PromoInputUiModel>(viewBinding.root) {

    companion object {
        val LAYOUT = R.layout.promo_checkout_marketplace_module_item_promo_input
    }

    override fun bind(element: PromoInputUiModel) {
        with(viewBinding) {
            renderErrorState(viewBinding, element)
            renderLoadingState(viewBinding, element)
            renderCloseIcon(viewBinding, element)
            renderTextField(viewBinding, element)

            setOnClickListeners(viewBinding, element)

            element.uiState.viewHeight = itemView.height
        }
    }

    private fun renderTextField(viewBinding: PromoCheckoutMarketplaceModuleItemPromoInputBinding, element: PromoInputUiModel) {
        with(viewBinding) {
            textFieldInputPromo.editText.imeOptions = EditorInfo.IME_ACTION_DONE
            textFieldInputPromo.editText.setText(element.uiData.promoCode)
            textFieldInputPromo.editText.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (element.uiState.isValidLastSeenPromo) {
                        element.uiState.isValidLastSeenPromo = text.toString() == element.uiData.validLastSeenPromoCode
                    }
                    element.uiData.promoCode = text?.toString() ?: ""
                    if (text?.isNotEmpty() == true) {
                        element.uiState.isButtonSelectEnabled = true
                        buttonApplyPromo.show()
                        textFieldInputPromo.setFirstIcon(com.tokopedia.unifycomponents.R.drawable.unify_chips_ic_close)
                    } else {
                        element.uiState.isButtonSelectEnabled = false
                        buttonApplyPromo.gone()
                        textFieldInputPromo.setFirstIcon(com.tokopedia.unifyprinciples.R.color.Unify_N0)
                    }
                }
            })
        }
    }

    private fun setOnClickListeners(viewBinding: PromoCheckoutMarketplaceModuleItemPromoInputBinding, element: PromoInputUiModel) {
        with(viewBinding) {
            textFieldInputPromo.editText.setOnClickListener {
                listener.onClickPromoManualInputTextField()
            }

            textFieldInputPromo.icon1.setOnClickListener {
                textFieldInputPromo.editText.text.clear()
                listener.onCLickClearManualInputPromo()
            }

            buttonApplyPromo.setOnClickListener {
                val promoCode = textFieldInputPromo.editText.text.toString()
                if (promoCode.isNotEmpty()) {
                    listener.onClickApplyManualInputPromo(promoCode, element.uiState.isValidLastSeenPromo)
                }
            }
        }
    }

    private fun renderCloseIcon(viewBinding: PromoCheckoutMarketplaceModuleItemPromoInputBinding, element: PromoInputUiModel) {
        with(viewBinding) {
            if (element.uiData.promoCode.isNotBlank()) {
                buttonApplyPromo.show()
                textFieldInputPromo.setFirstIcon(com.tokopedia.unifycomponents.R.drawable.unify_chips_ic_close)
            } else {
                buttonApplyPromo.gone()
                textFieldInputPromo.setFirstIcon(com.tokopedia.unifyprinciples.R.color.Unify_N0)
            }
        }
    }

    private fun renderLoadingState(viewBinding: PromoCheckoutMarketplaceModuleItemPromoInputBinding, element: PromoInputUiModel) {
        with(viewBinding) {
            if (element.uiState.isLoading) {
                buttonApplyPromo.isLoading = true
                buttonApplyPromo.isClickable = false
            } else {
                buttonApplyPromo.isLoading = false
                buttonApplyPromo.isClickable = true
            }
        }
    }

    private fun renderErrorState(viewBinding: PromoCheckoutMarketplaceModuleItemPromoInputBinding, element: PromoInputUiModel) {
        if (element.uiState.isError) {
            renderPromoInputError(viewBinding, element)
        } else {
            renderPromoInputNotError(viewBinding)
        }
    }

    private fun renderPromoInputError(viewBinding: PromoCheckoutMarketplaceModuleItemPromoInputBinding, element: PromoInputUiModel) {
        with(viewBinding) {
            textFieldInputPromo.isInputError = true
            if (element.uiData.exception != null) {
                textFieldInputPromo.setMessage(element.uiData.exception?.message
                        ?: "Terjadi kesalahan. Ulangi beberapa saat lagi")
                setPaddingViewHasError(viewBinding)
            } else {
                setPaddingViewHasNoError(viewBinding)
            }
        }
    }

    private fun setPaddingViewHasNoError(viewBinding: PromoCheckoutMarketplaceModuleItemPromoInputBinding) {
        val padding16 = itemView.context?.resources?.getDimension(com.tokopedia.abstraction.R.dimen.dp_16)?.toInt()
                ?: 0
        viewBinding.root.setPadding(padding16, padding16, padding16, 0)
    }

    private fun setPaddingViewHasError(viewBinding: PromoCheckoutMarketplaceModuleItemPromoInputBinding) {
        val padding16 = itemView.context?.resources?.getDimension(com.tokopedia.abstraction.R.dimen.dp_16)?.toInt()
                ?: 0
        viewBinding.root.setPadding(padding16, padding16, padding16, padding16)
    }

    private fun renderPromoInputNotError(viewBinding: PromoCheckoutMarketplaceModuleItemPromoInputBinding) {
        with(viewBinding) {
            textFieldInputPromo.isInputError = false
            textFieldInputPromo.setMessage("")
            setPaddingViewHasNoError(viewBinding)
        }
    }

}