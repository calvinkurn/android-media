package com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.addressform.widget

import androidx.recyclerview.widget.RecyclerView
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.logisticCommon.data.entity.address.SaveAddressDataModel
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.logisticaddaddress.common.ChipsItemDecoration
import com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.addressform.AddressFormFragment
import com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.addressform.LabelAlamatChipsAdapter
import com.tokopedia.logisticaddaddress.utils.TextInputUtil
import com.tokopedia.logisticaddaddress.utils.TextInputUtil.setOnTouchLabelAddress
import com.tokopedia.unifycomponents.TextFieldUnify

interface BaseFormAddressWidget {

    val addressDetailField: TextFieldUnify?
    val addressLabelField: TextFieldUnify?
    val addressLabelChips: RecyclerView?
    val courierNoteField: TextFieldUnify?

    val addressDetail: String
        get() = addressDetailField?.textFieldInput?.text.toString()
    val courierNote: String
        get() = courierNoteField?.textFieldInput?.text.toString()
    val label: String
        get() = addressLabelField?.textFieldInput?.text.toString()
    val isErrorCourierNote: Boolean
        get() = courierNoteField?.textFieldWrapper?.error != null

    fun renderView(
        data: SaveAddressDataModel,
        formattedAddress: String?,
        hasFocusEtDistrict: (() -> Unit)?,
        onClickEtDistrictListener: (() -> Unit)?
    )

    fun setupDetailAddressField(data: SaveAddressDataModel) {
        addressDetailField?.run {
            textFieldInput.setText(data.address1)
            if (data.address1.length > AddressFormFragment.MAX_CHAR_ALAMAT) {
                this.textFieldWrapper.let { wrapper ->
                    wrapper.error =
                        context.getString(R.string.error_alamat_exceed_max_char)
                    wrapper.isErrorEnabled = true
                }
            }
            textFieldInput.addTextChangedListener(
                TextInputUtil.setWrapperWatcher(
                    textFieldWrapper,
                    null,
                    context?.getString(R.string.tv_error_field)
                )
            )
        }
    }

    fun setupCourierNoteField(data: SaveAddressDataModel) {
        courierNoteField?.run {
            textFieldInput.setText(data.address1Notes)
            if (data.address1Notes.length > AddressFormFragment.MAX_CHAR_NOTES) {
                this.textFieldWrapper.let { wrapper ->
                    wrapper.error =
                        context.getString(R.string.error_notes_exceed_max_char)
                    wrapper.isErrorEnabled = true
                }
                textFieldInput.addTextChangedListener(
                    TextInputUtil.setNotesWrapperWatcher(
                        textFieldWrapper
                    )
                )
            }
        }
    }

    fun setupAddressLabelField(data: SaveAddressDataModel) {
        addressLabelField?.run {
            textFieldInput.setText(data.addressName.ifEmpty { AddressFormFragment.LABEL_HOME })
            textFieldInput.addTextChangedListener(
                TextInputUtil.setWrapperWatcher(
                    textFieldWrapper,
                    null,
                    context?.getString(R.string.tv_error_field)
                )
            )
        }
        addressLabelChips?.gone()
    }

    fun setOnTextFocusListener(
        hasFocusEtLabel: () -> Unit,
        hasFocusEtAddress: () -> Unit,
        hasFocusEtCourierNote: () -> Unit
    ) {
        addressLabelField?.textFieldInput?.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                hasFocusEtLabel.invoke()
            }
        }
        addressDetailField?.textFieldInput?.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                hasFocusEtAddress.invoke()
            }
        }
        courierNoteField?.textFieldInput?.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                hasFocusEtCourierNote.invoke()
            }
        }
    }

    fun setFocusEtAddress() {
        addressDetailField?.textFieldInput?.requestFocus()
    }

    fun showLabelAddressList() {
        addressLabelChips?.visible()
    }

    fun setOnTouchLabelAddress(
        showListLabelAlamat: () -> Unit,
        onAfterTextChanged: (String, RecyclerView?) -> Unit
    ) {
        addressLabelField?.textFieldInput?.setOnTouchLabelAddress(
            showListLabelAlamat = showListLabelAlamat,
            rvChips = addressLabelChips,
            onAfterTextChanged = onAfterTextChanged
        )
    }

    fun setupRvLabelAddressChips(
        staticDimen8dp: Int?,
        labelAlamatChipsLayoutManager: ChipsLayoutManager?,
        labelAlamatChipsAdapter: LabelAlamatChipsAdapter?
    ) {
        if (labelAlamatChipsLayoutManager != null && labelAlamatChipsAdapter != null) {
            addressLabelChips?.apply {
                staticDimen8dp?.let { ChipsItemDecoration(it) }?.let { addItemDecoration(it) }
                layoutManager = labelAlamatChipsLayoutManager
                adapter = labelAlamatChipsAdapter
            }
        }
    }
}
