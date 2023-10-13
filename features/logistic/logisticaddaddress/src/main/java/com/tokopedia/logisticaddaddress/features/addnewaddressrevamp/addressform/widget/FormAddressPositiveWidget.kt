package com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.addressform.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.logisticCommon.data.entity.address.SaveAddressDataModel
import com.tokopedia.logisticaddaddress.databinding.FormAddressNewAlamatBinding
import com.tokopedia.unifycomponents.TextFieldUnify

class FormAddressPositiveWidget : ConstraintLayout, BaseFormAddressWidget {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private var binding: FormAddressNewAlamatBinding? = null
    override val addressDetailField: TextFieldUnify?
        get() = binding?.etAlamatNew
    override val addressLabelField: TextFieldUnify?
        get() = binding?.etLabel
    override val addressLabelChips: RecyclerView?
        get() = binding?.rvLabelAlamatChips
    override val courierNoteField: TextFieldUnify?
        get() = binding?.etCourierNote

    init {
        binding = FormAddressNewAlamatBinding.inflate(LayoutInflater.from(context), this)
    }

    override fun renderView(
        data: SaveAddressDataModel,
        formattedAddress: String?,
        hasFocusEtDistrict: (() -> Unit)?,
        onClickEtDistrictListener: (() -> Unit)?
    ) {
        setupDetailAddressField(data)
        setupCourierNoteField(data)
        setupAddressLabelField(data)
    }

    fun setLayoutDirection() {
        binding?.rvLabelAlamatChips?.let {
            ViewCompat.setLayoutDirection(
                it,
                ViewCompat.LAYOUT_DIRECTION_LTR
            )
        }
    }
}
