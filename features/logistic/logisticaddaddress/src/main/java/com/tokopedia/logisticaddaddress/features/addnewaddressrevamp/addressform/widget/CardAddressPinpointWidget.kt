package com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.addressform.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.logisticaddaddress.databinding.CardAddressNewLocBinding

class CardAddressPinpointWidget : ConstraintLayout {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private var binding: CardAddressNewLocBinding? = null

    init {
        binding = CardAddressNewLocBinding.inflate(LayoutInflater.from(context), this)
    }

    fun setAddressDistrict(
        formattedAddress: String?
    ) {
        binding?.addressDistrict?.text = formattedAddress.orEmpty()
    }

    fun renderView(
        formattedAddress: String?,
        onBtnChangeClickListener: () -> Unit
    ) {
        binding?.apply {
            context?.let {
                btnChange.visible()
                btnChange.setOnClickListener {
                    onBtnChangeClickListener.invoke()
                }
            }
            addressDistrict.text = formattedAddress.orEmpty()
            tvPinpointTitle.visible()
        }
    }
}
