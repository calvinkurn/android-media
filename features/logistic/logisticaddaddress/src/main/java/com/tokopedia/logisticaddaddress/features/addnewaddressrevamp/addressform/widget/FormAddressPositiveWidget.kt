package com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.addressform.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.logisticCommon.data.entity.address.SaveAddressDataModel
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.logisticaddaddress.databinding.FormAddressNewAlamatBinding
import com.tokopedia.logisticaddaddress.features.addnewaddress.ChipsItemDecoration
import com.tokopedia.logisticaddaddress.features.addnewaddress.addedit.LabelAlamatChipsAdapter
import com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.addressform.AddressFormFragment
import com.tokopedia.logisticaddaddress.utils.TextInputUtil.setNotesWrapperWatcher
import com.tokopedia.logisticaddaddress.utils.TextInputUtil.setOnTouchLabelAddress
import com.tokopedia.logisticaddaddress.utils.TextInputUtil.setWrapperWatcher
import com.tokopedia.unifycomponents.TextFieldUnify

class FormAddressPositiveWidget : LinearLayout {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private var binding: FormAddressNewAlamatBinding? = null

    val isErrorCourierNote: Boolean
        get() = binding?.etCourierNote?.textFieldWrapper?.error != null

    val etAddressNew: TextFieldUnify?
        get() = binding?.etAlamatNew

    val etLabel: TextFieldUnify?
        get() = binding?.etLabel

    val rvLabelAddressChips: RecyclerView?
        get() = binding?.rvLabelAlamatChips

    val address: String
        get() = etAddressNew?.textFieldInput?.text.toString()

    val courierNote: String
        get() = binding?.etCourierNote?.textFieldInput?.text.toString()

    val label: String
        get() = etLabel?.textFieldInput?.text.toString()

    init {
        binding = FormAddressNewAlamatBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun renderView(
        data: SaveAddressDataModel
    ) {
        binding?.apply {
            etAlamatNew.run {
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
                    this.textFieldWrapper.let { wrapper ->
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
        }
    }

    fun setOnTextFocusListener(
        hasFocusEtLabel: () -> Unit,
        hasFocusEtAddressNew: () -> Unit,
        hasFocusEtCourierNote: () -> Unit
    ) {
        binding?.apply {
            etLabel.textFieldInput.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    hasFocusEtLabel.invoke()
                }
            }

            etAlamatNew.textFieldInput.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    hasFocusEtAddressNew.invoke()
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

    fun setLayoutDirection() {
        binding?.rvLabelAlamatChips?.let {
            ViewCompat.setLayoutDirection(
                it,
                ViewCompat.LAYOUT_DIRECTION_LTR
            )
        }
    }

    fun setFocusEtAddress() {
        binding?.etAlamatNew?.textFieldInput?.requestFocus()
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
