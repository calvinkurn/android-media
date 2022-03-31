package com.tokopedia.vouchercreation.shop.detail.view.component

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.vouchercreation.R
import kotlinx.android.synthetic.main.view_mvc_start_end_voucher.view.*

/**
 * Created By @ilhamsuaib on 30/04/20
 */

class StartEndVoucher(context: Context, attrs: AttributeSet?) : ConstraintLayout(context, attrs) {

    init {
        View.inflate(context, R.layout.view_mvc_start_end_voucher, this)
    }

    fun setStartTime(model: Model) {
        tvMvcViewStartDate.text = model.date
        tvMvcViewStartHour.text = model.hour
    }

    fun setEndTime(model: Model) {
        tvMvcViewEndDate.text = model.date
        tvMvcViewEndHour.text = model.hour
    }

    class Model(val date: String, val hour: String)
}