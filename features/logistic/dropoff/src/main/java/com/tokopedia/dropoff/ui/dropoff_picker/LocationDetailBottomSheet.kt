package com.tokopedia.dropoff.ui.dropoff_picker

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.dropoff.databinding.BottomsheetDropoffDetailBinding
import com.tokopedia.dropoff.ui.dropoff_picker.model.DropoffNearbyModel

class LocationDetailBottomSheet : ConstraintLayout {

    private var mData: DropoffNearbyModel? = null

    private var binding: BottomsheetDropoffDetailBinding? = null

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        binding = BottomsheetDropoffDetailBinding.inflate(LayoutInflater.from(context), this)
    }

    fun setStore(datum: DropoffNearbyModel) {
        mData = datum
        binding?.apply {
            tvOpening.text = datum.openingHours
            tvTitle.text = datum.addrName
            tvDetail.text = datum.address1
        }
        invalidate()
        requestLayout()
    }

    fun setOnOkClickListener(listener: (view: View, data: DropoffNearbyModel?) -> Unit) {
        binding?.buttonChoose?.setOnClickListener { listener(it, mData) }
    }

    fun setOnCancelClickListener(listener: (view: View, data: DropoffNearbyModel?) -> Unit) {
        binding?.buttonCancel?.setOnClickListener { listener(it, mData) }
    }
}
