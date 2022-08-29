package com.tokopedia.vouchercreation.shop.detail.view.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.vouchercreation.databinding.ViewMvcStartEndVoucherBinding

/**
 * Created By @ilhamsuaib on 30/04/20
 */

class StartEndVoucher(context: Context, attrs: AttributeSet?) : ConstraintLayout(context, attrs) {

    private var binding: ViewMvcStartEndVoucherBinding? = null

    init {
        binding = ViewMvcStartEndVoucherBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun setStartTime(model: Model) {
        binding?.tvMvcViewStartDate?.text = model.date
        binding?.tvMvcViewStartHour?.text = model.hour
    }

    fun setEndTime(model: Model) {
        binding?.tvMvcViewEndDate?.text = model.date
        binding?.tvMvcViewEndHour?.text = model.hour
    }

    class Model(val date: String, val hour: String)
}