package com.tokopedia.vouchercreation.common.view.voucherticker

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.vouchercreation.R
import kotlinx.android.synthetic.main.view_mvc_voucher_ticker.view.*

/**
 * Created By @ilhamsuaib on 10/05/20
 */

class VoucherTickerView(context: Context, attrs: AttributeSet?) : ConstraintLayout(context, attrs) {

    init {
        View.inflate(context, R.layout.view_mvc_voucher_ticker, this)
    }

    var title: String = ""
        set(value) {
            tvMvcTickerTitle.text = value
            field = value
        }

    var description: String = ""
        set(value) {
            tvMvcTickerDescription.text = value
            field = value
        }

    var nominal: String = ""
        set(value) {
            tvMvcExpense.text = value
            field = value
        }

    fun setOnTooltipClick(callback: () -> Unit) {
        mvcImgInfo.visible()
        mvcImgInfo.setOnClickListener {
            callback()
        }
    }
}