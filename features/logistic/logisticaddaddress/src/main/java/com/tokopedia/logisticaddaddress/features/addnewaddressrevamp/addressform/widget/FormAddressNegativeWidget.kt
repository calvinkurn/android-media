package com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.addressform.widget

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.logisticCommon.data.entity.address.SaveAddressDataModel
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.logisticaddaddress.databinding.FormAddressNewAlamatNegativeBinding
import com.tokopedia.logisticaddaddress.features.addnewaddress.ChipsItemDecoration
import com.tokopedia.logisticaddaddress.features.addnewaddress.addedit.LabelAlamatChipsAdapter
import com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.addressform.AddressFormFragment
import com.tokopedia.logisticaddaddress.utils.TextInputUtil.setNotesWrapperWatcher
import com.tokopedia.logisticaddaddress.utils.TextInputUtil.setOnTouchLabelAddress
import com.tokopedia.logisticaddaddress.utils.TextInputUtil.setWrapperWatcher
import com.tokopedia.unifycomponents.TextFieldUnify

class FormAddressNegativeWidget : ConstraintLayout {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private var binding: FormAddressNewAlamatNegativeBinding? = null

    val isErrorCourierNote: Boolean
        get() = binding?.etCourierNote?.textFieldWrapper?.error != null

    val etAddress: TextFieldUnify?
        get() = binding?.etAlamat

    val etLabel: TextFieldUnify?
        get() = binding?.etLabel

    val rvLabelAddressChips: RecyclerView?
        get() = binding?.rvLabelAlamatChips

    val isEmptyDistrict: Boolean?
        get() = binding?.etKotaKecamatan?.textFieldInput?.text?.toString()?.isEmpty()

    val address: String
        get() = etAddress?.textFieldInput?.text.toString()

    val courierNote: String
        get() = binding?.etCourierNote?.textFieldInput?.text.toString()

    val label: String
        get() = etLabel?.textFieldInput?.text.toString()

    init {
        binding =
            FormAddressNewAlamatNegativeBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun setDistrict(
        formattedAddress: String?
    ) {
        binding?.etKotaKecamatan?.textFieldInput?.setText(
            formattedAddress.orEmpty()
        )
    }

    fun setFocusEtAddress() {
        binding?.etAlamat?.textFieldInput?.requestFocus()
    }

    fun bindView(
        formattedAddress: String?,
        data: SaveAddressDataModel,
        hasFocusEtDistrict: () -> Unit,
        onClickEtDistrictListener: () -> Unit
    ) {
        binding?.apply {
            etKotaKecamatan.textFieldInput.apply {
                setText(formattedAddress.orEmpty())
                inputType = InputType.TYPE_NULL
                setOnFocusChangeListener { _, hasFocus ->
                    if (hasFocus) {
                        hasFocusEtDistrict.invoke()
                    }
                }
                setOnClickListener {
                    onClickEtDistrictListener.invoke()
                }
            }
            etLabel.run {
                textFieldInput.setText(data.addressName.ifEmpty { AddressFormFragment.LABEL_HOME })
                textFieldInput.addTextChangedListener(
                    setWrapperWatcher(
                        textFieldWrapper,
                        null,
                        context?.getString(R.string.tv_error_field)
                    )
                )
            }
            rvLabelAlamatChips.gone()
            etAlamat.run {
                textFieldInput.setText(data.address1)
                if (data.address1.length > AddressFormFragment.MAX_CHAR_ALAMAT) {
                    this.textFieldWrapper.let { wrapper ->
                        wrapper.error =
                            context.getString(R.string.error_alamat_exceed_max_char)
                        wrapper.isErrorEnabled = true
                    }
                }
                textFieldInput.addTextChangedListener(
                    setWrapperWatcher(
                        textFieldWrapper,
                        null,
                        context?.getString(R.string.tv_error_field)
                    )
                )
            }
            etCourierNote.run {
                textFieldInput.setText(data.address1Notes)
                if (data.address1Notes.length > AddressFormFragment.MAX_CHAR_NOTES) {
                    textFieldWrapper.let { wrapper ->
                        wrapper.error =
                            context.getString(R.string.error_notes_exceed_max_char)
                        wrapper.isErrorEnabled = true
                    }
                    textFieldInput.addTextChangedListener(
                        setNotesWrapperWatcher(
                            textFieldWrapper
                        )
                    )
                }
            }
        }
    }

    fun setOnTextFocusListener(
        hasFocusEtLabel: () -> Unit,
        hasFocusEtAddress: () -> Unit,
        hasFocusEtCourierNote: () -> Unit
    ) {
        binding?.apply {
            etLabel.textFieldInput.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    hasFocusEtLabel.invoke()
                }
            }

            etAlamat.textFieldInput.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    hasFocusEtAddress.invoke()
                }
            }

            etCourierNote.textFieldInput.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    hasFocusEtCourierNote.invoke()
                }
            }
        }
    }

    fun showLabelAddressList() {
        binding?.rvLabelAlamatChips?.visible()
    }

    fun setOnTouchLabelAddress(
        showListLabelAlamat: () -> Unit,
        onAfterTextChanged: (String, RecyclerView?) -> Unit
    ) {
        etLabel?.textFieldInput?.setOnTouchLabelAddress(
            showListLabelAlamat = showListLabelAlamat,
            rvChips = rvLabelAddressChips,
            onAfterTextChanged = onAfterTextChanged
        )
    }

    fun setupRvLabelAddressChips(
        staticDimen8dp: Int?,
        labelAlamatChipsLayoutManager: ChipsLayoutManager,
        labelAlamatChipsAdapter: LabelAlamatChipsAdapter
    ) {
        rvLabelAddressChips?.apply {
            staticDimen8dp?.let { ChipsItemDecoration(it) }?.let { addItemDecoration(it) }
            layoutManager = labelAlamatChipsLayoutManager
            adapter = labelAlamatChipsAdapter
        }
    }
}
