package com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.addressform.widget

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.logisticCommon.data.entity.address.SaveAddressDataModel
import com.tokopedia.logisticaddaddress.databinding.FormAddressNewAlamatNegativeBinding
import com.tokopedia.unifycomponents.TextFieldUnify

class FormAddressNegativeWidget : ConstraintLayout, BaseFormAddressWidget {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private var binding: FormAddressNewAlamatNegativeBinding? = null
    override val addressDetailField: TextFieldUnify?
        get() = binding?.etAlamat
    override val addressLabelField: TextFieldUnify?
        get() = binding?.etLabel
    override val addressLabelChips: RecyclerView?
        get() = binding?.rvLabelAlamatChips
    override val courierNoteField: TextFieldUnify?
        get() = binding?.etCourierNote
    private val districtField: TextFieldUnify?
        get() = binding?.etKotaKecamatan

    val district: String
        get() = districtField?.textFieldInput?.text.toString()
    val isEmptyDistrict: Boolean
        get() = district.isEmpty()

    init {
        binding =
            FormAddressNewAlamatNegativeBinding.inflate(LayoutInflater.from(context), this)
    }

    fun setDistrict(
        formattedAddress: String?
    ) {
        binding?.etKotaKecamatan?.textFieldInput?.setText(
            formattedAddress.orEmpty()
        )
    }

    private fun setupDistrictField(
        formattedAddress: String?,
        hasFocusEtDistrict: (() -> Unit)?,
        onClickEtDistrictListener: (() -> Unit)?
    ) {
        districtField?.textFieldInput?.apply {
            setText(formattedAddress.orEmpty())
            inputType = InputType.TYPE_NULL
            setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    hasFocusEtDistrict?.invoke()
                }
            }
            setOnClickListener {
                onClickEtDistrictListener?.invoke()
            }
        }
    }

    override fun renderView(
        data: SaveAddressDataModel,
        formattedAddress: String?,
        hasFocusEtDistrict: (() -> Unit)?,
        onClickEtDistrictListener: (() -> Unit)?
    ) {
        setupDistrictField(formattedAddress, hasFocusEtDistrict, onClickEtDistrictListener)
        setupAddressLabelField(data)
        setupDetailAddressField(data)
        setupCourierNoteField(data)
    }
}
