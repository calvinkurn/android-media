package com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.addressform.widget

import android.content.Context
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.clearImage
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.logisticaddaddress.databinding.FormAddressNewAkunBinding
import com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.addressform.AddressFormFragment
import com.tokopedia.logisticaddaddress.utils.TextInputUtil.setWrapperError
import com.tokopedia.logisticaddaddress.utils.TextInputUtil.setWrapperWatcher
import com.tokopedia.logisticaddaddress.utils.TextInputUtil.setWrapperWatcherPhone

class FormAccountWidget : ConstraintLayout {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private var binding: FormAddressNewAkunBinding? = null

    val phoneNumber: String
        get() = binding?.etNomorHp?.textFieldInput?.text.toString()

    val receiverName: String
        get() = binding?.etNamaPenerima?.textFieldInput?.text.toString()

    init {
        binding = FormAddressNewAkunBinding.inflate(LayoutInflater.from(context), this)
    }

    fun setPhoneNumber(
        phoneNumber: String
    ) {
        binding?.etNomorHp?.textFieldInput?.setText(phoneNumber)
    }

    fun renderView(
        receiverName: String,
        phoneNumber: String,
        onClickPhoneNumberFirstIcon: () -> Unit,
        onClickBtnInfo: () -> Unit,
        isEdit: Boolean
    ) {
        binding?.apply {
            if (isEdit) {
                etNamaPenerima.textFieldInput.setText(receiverName)
                infoNameLayout.gone()
            } else {
                if (receiverName.isNotEmpty() && !receiverName.contains(
                        AddressFormFragment.TOPPERS,
                        ignoreCase = true
                    )
                ) {
                    etNamaPenerima.textFieldInput.setText(receiverName)
                    infoNameLayout.gone()
                } else if (receiverName.contains(AddressFormFragment.TOPPERS, ignoreCase = true)) {
                    etNamaPenerima.textFieldWrapper.helperText =
                        context.getString(R.string.helper_nama_penerima)
                }
            }
            setupFormAccount(isEdit)
            etNomorHp.textFieldInput.setText(phoneNumber)
            etNomorHp.getFirstIcon().setOnClickListener {
                onClickPhoneNumberFirstIcon.invoke()
            }
            btnInfo.setOnClickListener {
                onClickBtnInfo.invoke()
            }
        }
    }

    private fun setupFormAccount(
        isEdit: Boolean
    ) {
        binding?.run {
            etNamaPenerima.textFieldInput.addTextChangedListener(
                setWrapperWatcher(
                    etNamaPenerima.textFieldWrapper,
                    null,
                    context?.getString(R.string.tv_error_field)
                )
            )
            etNomorHp.getFirstIcon().let {
                it.clearImage()
                it.setImageDrawable(
                    getIconUnifyDrawable(
                        context,
                        IconUnify.CONTACT
                    )
                )
                it.show()
            }
            etNomorHp.textFieldInput.addTextChangedListener(
                setWrapperWatcherPhone(
                    etNomorHp.textFieldWrapper,
                    context.getString(R.string.validate_no_ponsel_new),
                    isEdit,
                    context?.getString(R.string.tv_error_field)
                )
            )
        }
    }

    fun setWrapperErrorPhoneNumber() {
        binding?.etNomorHp?.apply {
            setWrapperError(textFieldWrapper, context.getString(R.string.tv_error_field))
        }
    }

    fun setWrapperErrorReceiverName() {
        binding?.etNamaPenerima?.apply {
            setWrapperError(textFieldWrapper, context.getString(R.string.tv_error_field))
        }
    }

    fun setupOnTextChangeListener(
        hasFocusInputReceiverName: () -> Unit,
        hasFocusInputPhoneNumber: () -> Unit
    ) {
        binding?.apply {
            etNomorHp.textFieldInput.apply {
                addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {
                        // no op
                    }

                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                        // no op
                    }

                    override fun onTextChanged(
                        s: CharSequence,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                        filters =
                            arrayOf(InputFilter.LengthFilter(AddressFormFragment.MAX_CHAR_PHONE_NUMBER))
                    }
                })
            }

            etNamaPenerima.textFieldInput.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    hasFocusInputReceiverName.invoke()
                }
            }

            etNomorHp.textFieldInput.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    hasFocusInputPhoneNumber.invoke()
                }
            }
        }
    }
}
